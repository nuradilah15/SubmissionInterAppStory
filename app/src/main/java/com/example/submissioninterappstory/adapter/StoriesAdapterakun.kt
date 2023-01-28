package com.example.submissioninterappstory.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submissioninterappstory.api.ListAllStoriesItem
import com.example.submissioninterappstory.databinding.ItemStoryBinding

class StoriesAdapterakun: RecyclerView.Adapter<StoriesAdapterakun.ViewHolder>() {

    private val listStories = ArrayList<ListAllStoriesItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setListStory(itemStory: List<ListAllStoriesItem>) {
        val diffCallback = DiffCallBackakun(listStories, itemStory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        listStories.clear()
        listStories.addAll(itemStory)
        diffResult.dispatchUpdatesTo(this)
//        this.notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStories[position])
        holder.itemView.setOnClickListener {onItemClickCallback?. onItemClicked(listStories[position])}
    }

    class ViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(stories: ListAllStoriesItem){
            with(binding){
                Glide.with(itemView.context)
                    .load(stories.photoUrl)
                    .into(imgItemstoriess)
                nameTvitem.text = stories.name
                deskTv.text = stories.description
                creadTimeakun.text = stories.createdAt
            }
        }
    }

    override fun getItemCount(): Int = listStories.size

    interface OnItemClickCallback{
        fun onItemClicked(story: ListAllStoriesItem)
    }

}