package com.example.storyappfinal.ui.dashboard.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.storyappfinal.data.model.Story
import com.example.storyappfinal.ui.detail.DetailActivity
import com.example.storyappfinal.utils.Constanta
import com.example.storyappfinal.utils.Helper
import com.bumptech.glide.Glide
import com.example.storyappfinal.R
import com.example.storyappfinal.databinding.RvStoryBinding

class StoryAdapter : PagingDataAdapter<Story, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

    inner class ViewHolder(private val binding: RvStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(story: Story) {
            with(binding) {
                storyName.text = story.name
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .into(storyImage)
                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(Constanta.StoryDetail.UserName.name, story.name)
                    intent.putExtra(Constanta.StoryDetail.ImageURL.name, story.photoUrl)
                    intent.putExtra(
                        Constanta.StoryDetail.ContentDescription.name,
                        story.description
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            androidx.core.util.Pair(storyImage, "story_image"),
                            androidx.core.util.Pair(storyName, "user_name"),
                        )
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }
}