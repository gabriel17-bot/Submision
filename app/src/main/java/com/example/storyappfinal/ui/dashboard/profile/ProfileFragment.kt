package com.example.storyappfinal.ui.dashboard.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.storyappfinal.data.viewmodel.SettingViewModel
import com.example.storyappfinal.data.viewmodel.ViewModelSettingFactory
import com.example.storyappfinal.utils.Constanta
import com.example.storyappfinal.utils.Helper
import com.example.storyappfinal.utils.SettingPreferences
import com.example.storyappfinal.utils.dataStore
import com.example.storyappfinal.R
import com.example.storyappfinal.databinding.FragmentProfileBinding
import com.example.storyappfinal.ui.dashboard.MainActivity


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance((activity as MainActivity).dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelSettingFactory(pref))[SettingViewModel::class.java]
        settingViewModel.getUserPreferences(Constanta.UserPreferences.UserToken.name)
            .observe(viewLifecycleOwner) {
                if (it == Constanta.preferenceDefaultValue) {
                    (activity as MainActivity).routeToAuth()
                }
            }
        binding.btnLogout.setOnClickListener {
            settingViewModel.clearUserPreferences()
        }
    }
}