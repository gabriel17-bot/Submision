package com.example.storyappfinal.ui.dashboard.story

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.storyappfinal.data.viewmodel.SettingViewModel
import com.example.storyappfinal.data.viewmodel.StoryViewModel
import com.example.storyappfinal.data.viewmodel.ViewModelSettingFactory
import com.example.storyappfinal.utils.Constanta
import com.example.storyappfinal.utils.Helper
import com.example.storyappfinal.utils.SettingPreferences
import com.example.storyappfinal.utils.dataStore
import com.example.storyappfinal.R
import com.example.storyappfinal.data.repository.remote.ApiConfig
import com.example.storyappfinal.databinding.ActivityNewStoryBinding
import com.google.android.gms.maps.model.LatLng
import java.io.File

class NewStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewStoryBinding

    private var userToken: String? = null
    private var isPicked: Boolean? = false
    private var getResult: ActivityResultLauncher<Intent>? = null

    val viewModel: StoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelSettingFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getUserPreferences(Constanta.UserPreferences.UserToken.name)
            .observe(this) { token ->
                userToken = StringBuilder("Bearer ").append(token).toString()
            }

        val myFile = intent?.getSerializableExtra(EXTRA_PHOTO_RESULT) as File
        val isBackCamera = intent?.getBooleanExtra(EXTRA_CAMERA_MODE, true) as Boolean
        val rotatedBitmap = Helper.rotateBitmap(
            BitmapFactory.decodeFile(myFile.path),
            isBackCamera
        )
        binding.storyImage.setImageBitmap(rotatedBitmap)
        binding.btnUpload.setOnClickListener {
            if (binding.storyDescription.text.isNotEmpty()) {
                uploadImage(myFile, binding.storyDescription.text.toString())
            } else {
                Helper.showDialogInfo(
                    this,
                    getString(R.string.description_story)
                )
            }
        }

        viewModel.let { vm ->
            vm.isSuccessUploadStory.observe(this) {
                if (it) {
                    val dialog = Helper.dialogInfoBuilder(
                        this,
                        getString(R.string.success_upload)
                    )
                    val btnOk = dialog.findViewById<Button>(R.id.button_ok)
                    btnOk.setOnClickListener {
                        setResult(RESULT_OK)
                        finish()
                    }
                    dialog.show()
                }
            }
            vm.loading.observe(this) {
                binding.loading.root.visibility = it
            }
            vm.error.observe(this) {
                if (it.isNotEmpty()) {
                    Helper.showDialogInfo(this, it)
                }
            }
        }
    }

    private fun uploadImage(image: File, description: String) {
        if (userToken != null) {
            if (viewModel.isLocationPicked.value != true) {
                viewModel.uploadNewStory(
                    this,
                    userToken!!,
                    image,
                    description,
                    ApiConfig.getApiService()
                )
            } else {
                viewModel.uploadNewStory(
                    this,
                    userToken!!,
                    image,
                    description,
                    ApiConfig.getApiService()
                )
            }
        } else {
            Helper.showDialogInfo(
                this,
                getString(R.string.header_token)
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constanta.LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Helper.notifyGivePermission(
                        this,
                        getString(R.string.permission_location)
                    )
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    companion object {
        const val EXTRA_PHOTO_RESULT = "PHOTO_RESULT"
        const val EXTRA_CAMERA_MODE = "CAMERA_MODE"
    }
}