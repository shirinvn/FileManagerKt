package com.example.filemanagerkt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.net.URLConnection

class MyAdapter (private val data: ArrayList<File>, private val fileEvent: FileEvent) : RecyclerView.Adapter<MyAdapter.FileViewHolder>() {

    private var ourViewType = 0

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img= itemView.findViewById<ImageView>(R.id.imageView)
        val txt= itemView.findViewById<TextView>(R.id.textview)
        fun bindViews(file: File) {

            var fileType = ""

            txt.text = file.name

            if (file.isDirectory) {
                img.setImageResource(R.drawable.foldericon)
            } else {

                when {

                    isImage(file.path) -> {
                        img.setImageResource(R.drawable.image)
                        fileType = "image/*"
                    }

                    isVideo(file.path) -> {
                        img.setImageResource(R.drawable.video)
                        fileType = "video/*"
                    }

                    isZip(file.name) -> {
                        img.setImageResource(R.drawable.zip)
                        fileType = "application/zip"
                    }
                    isMusic(file.name) -> {
                        img.setImageResource(R.drawable.music)
                        fileType= "audio/*"
                    }

                    else -> {
                        img.setImageResource(R.drawable.file)
                        fileType = "text/plain"
                    }

                }

            }

            itemView.setOnClickListener {

                if (file.isDirectory) {
                    fileEvent.onFolderClicked(file.path)
                } else {
                    fileEvent.onFileClicked(file, fileType)
                }

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view: View

        if (viewType == 0) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent, false)
        }


        return FileViewHolder(view)


    }



    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bindViews(data[position])

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return ourViewType
    }


    private fun isImage(path: String): Boolean {
        val mimeType: String = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("image")
    }
    private fun isVideo(path: String): Boolean {
        val mimeType = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("video")
    }
    private fun isZip(name: String): Boolean {
        return name.contains(".zip") || name.contains(".rar")
    }
    private fun isMusic(path: String): Boolean {
        val mimeType = URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("audio")

    }

    fun removeFile(oldFile: File, position: Int) {
        data.remove(oldFile)
        notifyItemRemoved(position)
    }


    fun changeViewType(newViewType: Int) {
        ourViewType = newViewType
        notifyDataSetChanged()
    }

    interface FileEvent {

        fun onFileClicked(file: File, type: String)
        fun onFolderClicked(path: String)

    }

}