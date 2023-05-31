package com.example.filemanagerkt

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.filemanagerkt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var  binding: ActivityMainBinding

    companion object {
        var ourViewType = 0
        var ourSpanCount = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (checkPermisstion()){

            val path = Environment.getExternalStorageDirectory().path
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.frame_layout , MyFragment(path))
            transaction.commit()

        }else { requestPermisstion()}


    }


    private fun checkPermisstion(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return if (result == PackageManager.PERMISSION_GRANTED) {
            true

        } else {

            false

        }
    }

    private fun requestPermisstion() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(this@MainActivity, "Storage", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), 111
            )
        }
    }


}