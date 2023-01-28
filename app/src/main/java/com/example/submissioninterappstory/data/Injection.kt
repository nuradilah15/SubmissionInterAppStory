package com.example.submissioninterappstory.data

import android.content.Context
import com.example.submissioninterappstory.api.ApiAkunConfig
import com.example.submissioninterappstory.api.ApiUserInter
import com.example.submissioninterappstory.paging.RepostStoriesst

object Injection {
    fun provideRepository(token : String): RepostStoriesst {
        val apiUserInter= ApiAkunConfig().getApiUserInter()
        return RepostStoriesst(apiUserInter, token)
    }

}