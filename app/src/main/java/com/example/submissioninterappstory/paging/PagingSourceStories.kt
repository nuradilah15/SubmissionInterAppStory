package com.example.submissioninterappstory.paging

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.submissioninterappstory.api.ApiUserInter
import com.example.submissioninterappstory.api.ListAllStoriesItem
import com.example.submissioninterappstory.login.LogInActivity
import com.example.submissioninterappstory.main.MainActivity

//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class PagingSourceStories (private val apiUserInter: ApiUserInter, private val token: String): PagingSource<Int, ListAllStoriesItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override fun getRefreshKey(state: PagingState<Int, ListAllStoriesItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListAllStoriesItem> {
        return try {
            val position = params.key?: INITIAL_PAGE_INDEX
            val resposeData = apiUserInter.getStories(
                "Bearer ${token}",
                position,
                params.loadSize
            )
            val dataList = resposeData.listStory

//            MainActivity.listStoryst.addAll(resposeData.listStory as List<ListAllStoriesItem>)

            LoadResult.Page(
                data =  dataList,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (dataList.isEmpty()) null else position + 1
            )
        }catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

}