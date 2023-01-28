package com.example.submissioninterappstory.login

import android.util.Log
import androidx.lifecycle.*
import com.example.submissioninterappstory.api.ApiAkunConfig
import com.example.submissioninterappstory.api.LoginResponseStoryapps
import com.example.submissioninterappstory.model.UserModelStory
import com.example.submissioninterappstory.model.UserPref
import com.example.submissioninterappstory.story.utils.Helper
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInModel (private val pref: UserPref) : ViewModel() {


    private val isLoading = MutableLiveData<Boolean>()

    fun getUser(): LiveData<UserModelStory> {
        return pref.getUser().asLiveData()
    }

    fun logIn() {
        viewModelScope.launch {
            pref.logIn()
        }
    }

    fun loginUserstory(email: String, password: String, callback: Helper.ApiCallbackString) {

        val service = ApiAkunConfig().getApiUserInter().logInakunAppstories(email, password)
        service.enqueue(object : Callback<LoginResponseStoryapps> {
            override fun onResponse(
                call: Call<LoginResponseStoryapps>,
                response: Response<LoginResponseStoryapps>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        callback.onResponse(response.body() != null, SUCCESS)
                        val model = UserModelStory(
                            responseBody.logInakun.name,
                            email,
                            password,
                            responseBody.logInakun.userId,
                            responseBody.logInakun.token,
                            true
                        )
                        saveUser(model)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    val jsonObject =
                        JSONTokener(response.errorBody()!!.string()).nextValue() as JSONObject
                    val message = jsonObject.getString("message")
                    callback.onResponse(false, message)
                }
            }

            override fun onFailure(call: Call<LoginResponseStoryapps>, t: Throwable) {
                isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                callback.onResponse(false, t.message.toString())
            }
        })
    }

    fun saveUser(user: UserModelStory) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    companion object {
        private const val TAG = "LogInViewModel"
        private const val SUCCESS = "success"
    }
}