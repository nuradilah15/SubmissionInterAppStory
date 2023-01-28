package com.example.submissioninterappstory.location

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submissioninterappstory.api.ApiAkunConfig
import com.example.submissioninterappstory.api.ListAllStoriesItem
import com.example.submissioninterappstory.api.StoryAllRespon
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel : ViewModel (){

    val listLocation = MutableLiveData<List<ListAllStoriesItem>>()


    fun setLocation(tokenAuth: String){

        Log.d(this@MapsViewModel::class.java.simpleName, tokenAuth)
        ApiAkunConfig().getApiUserInter().getAllStoryLocation(token = "Bearer $tokenAuth ")
            .enqueue(object : Callback<StoryAllRespon> {
                override fun onFailure(call: Call<StoryAllRespon>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<StoryAllRespon>,
                    response: Response<StoryAllRespon>
                ) {

                    if (response.isSuccessful){
                        listLocation.postValue(response.body()?.listStory)
                        Log.d(this@MapsViewModel::class.java.simpleName, response.body()?.listStory.toString())

                    }

                }
            })
    }

    fun getStoryMaps(): MutableLiveData<List<ListAllStoriesItem>>{

        return listLocation

    }
}