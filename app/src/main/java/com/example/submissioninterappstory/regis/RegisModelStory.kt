package com.example.submissioninterappstory.regis

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissioninterappstory.api.ApiAkunConfig
import com.example.submissioninterappstory.api.ApiInterResponse
import com.example.submissioninterappstory.model.UserModelStory
import com.example.submissioninterappstory.model.UserPref
import com.example.submissioninterappstory.story.utils.Helper
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisModelStory(private val pref: UserPref) : ViewModel() {

    fun saveUser(user: UserModelStory){
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun register(name: String, email: String, password: String, callback: Helper.ApiCallbackString){
        val service = ApiAkunConfig().getApiUserInter().regisakunAppstories(name, email, password)
        service.enqueue(object : Callback<ApiInterResponse>{
            override fun onResponse(
                call: Call<ApiInterResponse>,
                response: Response<ApiInterResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error)
                        callback.onResponse(response.body() != null, SUCCESS)
                } else {
                    Log.e(TAG, "onFailure1: ${response.message()}")
                    val jsonObject = JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<ApiInterResponse>, t: Throwable) {
                Log.e(TAG, "onFailure2: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
        private const val SUCCESS = "success"
    }

}
