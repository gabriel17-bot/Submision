package com.example.storyappfinal.ui.dashboard.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.storyappfinal.utils.Helper
import com.example.storyappfinal.R
import com.example.storyappfinal.databinding.FragmentHomeBinding
import com.example.storyappfinal.ui.dashboard.MainActivity
import java.util.*
import kotlin.concurrent.schedule


class HomeFragment : Fragment() {

    private val rvAdapter = StoryAdapter()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* activate options menu in fragments */
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val mainViewModel = (activity as MainActivity).getStoryViewModel()
        mainViewModel.story.observe(viewLifecycleOwner) {
            rvAdapter.submitData(
                lifecycle,
                it
            )
//            Helper.updateWidgetData(requireContext())
        }

        binding.rvStory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter =
                rvAdapter.withLoadStateFooter(footer = StoryLoadingStateAdapter { rvAdapter.retry() })
        }
        return binding.root
    }


}