package com.example.submissioninterappstory.model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.submissioninterappstory.login.LogInModel
import com.example.submissioninterappstory.main.MainViewModel
import com.example.submissioninterappstory.regis.RegisModelStory

class ModelFactory (private val pref: UserPref) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T{
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(RegisModelStory::class.java) -> {
                RegisModelStory(pref) as T
            }
            modelClass.isAssignableFrom(LogInModel::class.java) -> {
                LogInModel(pref) as T
            }
//            modelClass.isAssignableFrom(MainViewModel::class.java)->{
//                MainViewModel(pref) as T
//            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

    }

}