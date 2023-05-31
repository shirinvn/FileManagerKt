package com.example.filemanagerkt

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.filemanagerkt.databinding.FragmentBinding
import java.io.File

class MyFragment (val path: String) : Fragment(), MyAdapter.FileEvent {
    lateinit var binding: FragmentBinding
    lateinit var adapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBinding.inflate(layoutInflater)
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ourFile = File(path)

        if (ourFile.isDirectory) {

            val listOfFiles = arrayListOf<File>()
            listOfFiles.addAll(ourFile.listFiles()!!)
            listOfFiles.sort()

            adapter = MyAdapter(listOfFiles, this)
            binding.recyclerMain.adapter = adapter
            binding.recyclerMain.layoutManager = GridLayoutManager(
                context,
                MainActivity.ourSpanCount,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter.changeViewType(MainActivity.ourViewType)

            if (listOfFiles.size > 0) {

                binding.recyclerMain.visibility = View.VISIBLE
                binding.imgNoData.visibility = View.GONE


            } else {

                binding.recyclerMain.visibility = View.GONE
                binding.imgNoData.visibility = View.VISIBLE

            }

        }


    }

    override fun onFileClicked(file: File, type: String) {

        val intent = Intent(Intent.ACTION_VIEW)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            val fileProvider = FileProvider.getUriForFile(
                requireContext(),
                requireActivity().packageName + ".provider",
                file
            )
            intent.setDataAndType(fileProvider, type)

        } else {

            intent.setDataAndType(Uri.fromFile(file), type)

        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)

    }

    override fun onFolderClicked(path: String) {

        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, MyFragment(path))
        transaction.addToBackStack(null)
        transaction.commit()

    }


}