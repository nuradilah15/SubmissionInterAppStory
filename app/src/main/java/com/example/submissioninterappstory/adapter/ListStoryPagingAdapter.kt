package com.example.submissioninterappstory.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.submissioninterappstory.api.ListAllStoriesItem
import com.example.submissioninterappstory.databinding.ItemStoryBinding
import com.example.submissioninterappstory.story.DetailStoryakunn

class ListStoryPagingAdapter : PagingDataAdapter<ListAllStoriesItem, ListStoryPagingAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(var Binding:ItemStoryBinding) : RecyclerView.ViewHolder(Binding.root){
        fun bind(dataStoriesst: ListAllStoriesItem){
            Binding.nameTvitem.text = dataStoriesst.name
            Binding.deskTv.text = dataStoriesst.description
            Binding.creadTimeakun.text = dataStoriesst.createdAt
            Glide.with(itemView.context)
                .load(dataStoriesst.photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(Binding.imgItemstoriess)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context,DetailStoryakunn::class.java)
                intent.putExtra(DetailStoryakunn.EXTRA_URL, dataStoriesst.photoUrl)
                intent.putExtra(DetailStoryakunn.EXTRA_NAME, dataStoriesst.name)
                intent.putExtra(DetailStoryakunn.EXTRA_DESC, dataStoriesst.description)
                intent.putExtra(DetailStoryakunn.EXTRA_AT, dataStoriesst.createdAt)
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val Binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(Binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userStoryStories = getItem(position)
        if (userStoryStories != null){
            holder.bind(userStoryStories)
        }
    }

    companion object{
        val DIFF_CALLBACK = object  : DiffUtil.ItemCallback<ListAllStoriesItem>(){
            override fun areItemsTheSame(
                oldItem: ListAllStoriesItem,
                newItem: ListAllStoriesItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListAllStoriesItem,
                newItem: ListAllStoriesItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}