package com.example.storyappfinal.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.storyappfinal.data.repository.remote.ApiConfig
import com.example.storyappfinal.ui.auth.AuthActivity
import com.example.storyappfinal.ui.main.explore.ExploreFragment
import com.example.storyappfinal.ui.main.home.HomeFragment
import com.example.storyappfinal.ui.main.setting.ProfileFragment
import com.example.storyappfinal.ui.main.story.CameraActivity
import com.example.storyappfinal.R
import com.example.storyappfinal.data.viewmodel.*
import com.example.storyappfinal.databinding.ActivityMainBinding
import com.example.storyappfinal.utils.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val pref = SettingPreferences.getInstance(dataStore)
    private val settingViewModel: SettingViewModel by viewModels { ModelSettingFactory(pref) }
    private var token = ""
    private var fragmentHome: HomeFragment? = null
    private lateinit var startNewStory: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentProfile = ProfileFragment()
        fragmentHome = HomeFragment()
        val fragmentExplore = ExploreFragment()

        startNewStory =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    fragmentHome
                }
            }

        settingViewModel.getUserPreferences(Constanta.UserPreferences.UserToken.name)
            .observe(this) {
                token = "Bearer $it"
                switchFragment(fragmentHome!!)
            }

        binding.bottomNavigationView.background = null

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    switchFragment(fragmentHome!!)
                    true
                }
                R.id.navigation_story -> {
                    routeToStory()
                    true
                }
                R.id.navigation_explore -> {
                    if (Helper.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    ) {
                        switchFragment(fragmentExplore)
                        true
                    } else {
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ),
                            Constanta.LOCATION_PERMISSION_CODE
                        )
                        false
                    }
                }
                R.id.navigation_setting -> {
                    switchFragment(fragmentProfile)
                    true
                }
                else -> false
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            Constanta.CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Helper.notifyGivePermission(this, "Berikan aplikasi izin mengakses kamera  ")
                }
            }
            Constanta.LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    Helper.notifyGivePermission(
                        this,
                        "Berikan aplikasi izin lokasi untuk membaca lokasi  "
                    )
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            REQUIRED_PERMISSIONS,
            REQUEST_CODE_PERMISSIONS
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private fun routeToStory() {
        if (!allPermissionsGranted()) {
            requestPermission()
        } else {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

    fun getUserToken() = token

    fun getStoryViewModel(): StoryPagerViewModel {
        val viewModel: StoryPagerViewModel by viewModels {
            ModelStoryFactory(
                this,
                ApiConfig.getApiService(),
                getUserToken()
            )
        }
        return viewModel
    }

    private fun switchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    fun routeToAuth() = startActivity(Intent(this, AuthActivity::class.java))

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}