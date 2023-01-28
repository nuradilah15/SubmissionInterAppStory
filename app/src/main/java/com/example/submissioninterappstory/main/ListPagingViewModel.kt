package com.example.submissioninterappstory.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submissioninterappstory.api.ListAllStoriesItem
import com.example.submissioninterappstory.data.Injection
import com.example.submissioninterappstory.paging.RepostStoriesst

class ListPagingViewModel(repostStoriesst: RepostStoriesst) : ViewModel() {

    val list: LiveData<PagingData<ListAllStoriesItem>> =
        repostStoriesst.getStories().cachedIn(viewModelScope)

}

class ViewModelFactory(private val token : String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListPagingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListPagingViewModel(Injection.provideRepository(token)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}