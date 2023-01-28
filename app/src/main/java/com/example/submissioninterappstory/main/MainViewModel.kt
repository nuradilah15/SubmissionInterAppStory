package com.example.submissioninterappstory.main

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.example.submissioninterappstory.api.ApiAkunConfig
import com.example.submissioninterappstory.api.ListAllStoriesItem
import com.example.submissioninterappstory.api.StoryAllRespon
import com.example.submissioninterappstory.model.UserModelStory
import com.example.submissioninterappstory.model.UserPref
import com.example.submissioninterappstory.paging.RepostStoriesst
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(
    private val pref: UserPref
) : ViewModel() {
    val listStoryakun = MutableLiveData<List<ListAllStoriesItem>>()

    fun setStoriesakun(tokenAuth: String){

        Log.d(this@MainViewModel::class.java.simpleName, tokenAuth)
        ApiAkunConfig().getApiUserInter().getAllStoriesakun(token = "Bearer $tokenAuth ")
            .enqueue(object : Callback<StoryAllRespon> {
                override fun onFailure(call: Call<StoryAllRespon>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<StoryAllRespon>,
                    response: Response<StoryAllRespon>
                ) {

                    if (response.isSuccessful){
                        listStoryakun.postValue(response.body()?.listStory)
                        Log.d(this@MainViewModel::class.java.simpleName, response.body()?.listStory.toString())

                    }

                }
            })
    }

//    fun getStoryPaging(token: String): LiveData<PagingData<ListAllStoriesItem>>{
//        return RepostStoriesst().getStories(token).cachedIn(viewModelScope)
//    }

    fun getUserakun(): LiveData<UserModelStory>{
        return pref.getUser().asLiveData()
    }

    fun logOut(){
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getStoriesak(): MutableLiveData<List<ListAllStoriesItem>>{

        return listStoryakun

    }
}