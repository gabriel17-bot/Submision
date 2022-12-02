package com.example.storyappfinal.ui.auth

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.storyappfinal.data.viewmodel.AuthViewModel
import com.example.storyappfinal.data.viewmodel.SettingViewModel
import com.example.storyappfinal.data.viewmodel.ViewModelSettingFactory
import com.example.storyappfinal.utils.Constanta
import com.example.storyappfinal.utils.Helper
import com.example.storyappfinal.utils.SettingPreferences
import com.example.storyappfinal.utils.dataStore
import com.example.storyappfinal.R
import com.example.storyappfinal.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: AuthViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = SettingPreferences.getInstance((activity as AuthActivity).dataStore)
        val settingViewModel =
            ViewModelProvider(this, ViewModelSettingFactory(pref))[SettingViewModel::class.java]

        viewModel.let { vm ->
            vm.loginResult.observe(viewLifecycleOwner) { login ->
                // success login process triggered -> save preferences
                settingViewModel.setUserPreferences(
                    login.loginResult.token,
                    login.loginResult.userId,
                    login.loginResult.name,
                    viewModel.tempEmail.value ?: Constanta.preferenceDefaultValue
                )
            }
            vm.error.observe(viewLifecycleOwner) { error ->
                error?.let {
                    if (it.isNotEmpty()) {
                        Helper.showDialogInfo(requireContext(), it)
                    }
                }
            }
            vm.loading.observe(viewLifecycleOwner) { state ->
                binding.loading.root.visibility = state
            }
        }
        settingViewModel.getUserPreferences(Constanta.UserPreferences.UserToken.name)
            .observe(viewLifecycleOwner) { token ->
                // if token triggered change -> redirect to Main Activity
                if (token != Constanta.preferenceDefaultValue) (activity as AuthActivity).routeToMainActivity()
            }
        binding.btnAction.setOnClickListener {
            if ((binding.edEmail.text?.length ?: 0) <= 0) {
                binding.edEmail.error = getString(R.string.empty_email_password)
                binding.edEmail.requestFocus()
            } else if ((binding.edPassword.text?.length ?: 0) <= 0) {
                binding.edPassword.error = getString(R.string.empty_email_password)
                binding.edPassword.requestFocus()
            }
            /* input not empty -> check contains error */
            else if ((binding.edEmail.error?.length ?: 0) > 0) {
                binding.edEmail.requestFocus()
            } else if ((binding.edPassword.error?.length ?: 0) > 0) {
                binding.edPassword.requestFocus()
            }
            /* not contain error */
            else {
                val email = binding.edEmail.text.toString()
                val password = binding.edPassword.text.toString()
                viewModel.login(email, password)
            }
        }
        binding.btnRegister.setOnClickListener {
            viewModel.error.postValue("")

            parentFragmentManager.beginTransaction().apply {
                replace(R.id.container, RegisterFragment(), RegisterFragment::class.java.simpleName)
                addSharedElement(binding.labelAuth, "auth")
                addSharedElement(binding.edEmail, "email")
                addSharedElement(binding.edPassword, "password")
                addSharedElement(binding.containerMisc, "misc")
                commit()
            }
        }
    }

}