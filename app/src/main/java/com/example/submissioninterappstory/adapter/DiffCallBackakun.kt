package com.example.submissioninterappstory.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.submissioninterappstory.api.ListAllStoriesItem

class DiffCallBackakun (
    private val mOldFavList: List<ListAllStoriesItem>,
    private val mNewFavList: List<ListAllStoriesItem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = mOldFavList.size

    override fun getNewListSize() = mNewFavList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        mOldFavList[oldItemPosition].id == mNewFavList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldFavList[oldItemPosition]
        val newEmployee = mNewFavList[newItemPosition]
        return oldEmployee.id == newEmployee.id
    }
}