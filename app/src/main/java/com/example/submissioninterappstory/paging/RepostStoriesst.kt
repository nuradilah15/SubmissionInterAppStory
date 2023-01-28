package com.example.submissioninterappstory.paging

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.submissioninterappstory.api.ApiUserInter
import com.example.submissioninterappstory.api.ListAllStoriesItem

class RepostStoriesst (private val apiUserInter: ApiUserInter, private val token: String) {

    fun getStories(): LiveData<PagingData<ListAllStoriesItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
//            remoteMediator = StoriesStRemoteMediator(token, storiesStDb, apiUserInter),
            pagingSourceFactory = {
//                storiesStDb.storiesDao().getAllStoriesLocalStory()
                PagingSourceStories(apiUserInter, token)
            }
        ).liveData
    }
}