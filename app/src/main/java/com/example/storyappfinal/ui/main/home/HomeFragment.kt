package com.example.storyappfinal.ui.main.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.storyappfinal.databinding.FragmentHomeBinding
import com.example.storyappfinal.ui.main.MainActivity
import java.util.*
import kotlin.concurrent.schedule

class HomeFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val rvAdapter = StoryAdapter()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val mainViewModel = (activity as MainActivity).getStoryViewModel()
        mainViewModel.story().observe(viewLifecycleOwner) {
            rvAdapter.submitData(
                lifecycle,
                it,
            )
        }
        binding.swipeRefresh.setOnRefreshListener {
            onRefresh()
        }
        binding.rvStory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
            adapter =
                rvAdapter.withLoadStateFooter(footer = StoryLoadingState { rvAdapter.retry() })
        }
        onRefresh()
        return binding.root
    }

    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = true
        rvAdapter.refresh()
        Timer().schedule(2000) {
            binding.swipeRefresh.isRefreshing = false
            binding.rvStory.smoothScrollToPosition(0)
        }
    }

}