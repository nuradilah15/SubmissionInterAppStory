package com.example.submissioninterappstory.story

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.submissioninterappstory.R
import com.example.submissioninterappstory.api.ListAllStoriesItem
import com.example.submissioninterappstory.databinding.ActivityDetailStoryakunnBinding
import com.example.submissioninterappstory.location.MapsActivity

class DetailStoryakunn : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryakunnBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryakunnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_NAME)
        val createAt = intent.getStringExtra(EXTRA_AT)
        val description = intent.getStringExtra(EXTRA_DESC)
        val photoUrl = intent.getStringExtra(EXTRA_URL)

        Toast.makeText(this, "$name, $createAt, $description", Toast.LENGTH_SHORT).show()
//
        Glide.with(this)
            .load(photoUrl)
            .into(binding.detailImgStories)
        binding.detailTvNamee.text = name
        binding.detailTvcrea.text = createAt
        binding.detailTvdesk.text = description


    }

    companion object {
        const val EXTRA_URL = "url"
        const val EXTRA_AT = "at"
        const val EXTRA_DESC = "desc"
        const val EXTRA_NAME = "name"
    }
}