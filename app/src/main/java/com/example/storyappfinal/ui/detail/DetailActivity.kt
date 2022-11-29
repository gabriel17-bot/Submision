package com.example.storyappfinal.ui.detail

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.storyappfinal.utils.Constanta
import com.example.storyappfinal.utils.Helper
import com.bumptech.glide.Glide
import com.example.storyappfinal.databinding.ActivityDetailBinding
import kotlinx.coroutines.DelicateCoroutinesApi


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.storyName.text =
            intent.getData(Constanta.StoryDetail.UserName.name, "Name")
        Glide.with(binding.root)
            .load(intent.getData(Constanta.StoryDetail.ImageURL.name, ""))
            .into(binding.storyImage)
        binding.storyDescription.text =
            intent.getData(Constanta.StoryDetail.ContentDescription.name, "Caption")

        try {
            val lat = intent.getData(Constanta.StoryDetail.Latitude.name)
            val lon = intent.getData(Constanta.StoryDetail.Longitude.name)
            binding.labelLocation.text =
                Helper.parseAddressLocation(this, lat.toDouble(), lon.toDouble())
            binding.labelLocation.isVisible = true
        } catch (e: Exception) {
            binding.labelLocation.isVisible = false
        }
    }

    private fun Intent.getData(key: String, defaultValue: String = "None"): String {
        return getStringExtra(key) ?: defaultValue
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constanta.STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Helper.notifyGivePermission(
                        this,
                        "Berikan aplikasi izin storage untuk menyimpan story"
                    )
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

    }


}