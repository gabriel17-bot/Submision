package com.example.storyappfinal.ui.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.storyappfinal.utils.Constanta
import com.example.storyappfinal.utils.Helper
import com.bumptech.glide.Glide
import com.example.storyappfinal.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

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
}