package com.example.submissioninterappstory.main

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissioninterappstory.R
import com.example.submissioninterappstory.adapter.ListStoryPagingAdapter
import com.example.submissioninterappstory.adapter.LoadingStateAdapter
import com.example.submissioninterappstory.adapter.StoriesAdapterakun
import com.example.submissioninterappstory.api.ListAllStoriesItem
import com.example.submissioninterappstory.databinding.ActivityMainBinding
import com.example.submissioninterappstory.location.MapsActivity
import com.example.submissioninterappstory.model.ModelFactory
import com.example.submissioninterappstory.model.UserPref
import com.example.submissioninterappstory.story.AddStoryApps
import com.example.submissioninterappstory.story.DetailStoryakunn
import com.example.submissioninterappstory.welcome.WelcomeScreen

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainModelAkun: MainViewModel
    private lateinit var listPagingViewModel: ListPagingViewModel
//    private lateinit var adapter: StoriesAdapterakun
    private lateinit var listStoryPagingAdapter: ListStoryPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listStoryPagingAdapter = ListStoryPagingAdapter()
        binding.listStoryRv.layoutManager = LinearLayoutManager(this)
        binding.listStoryRv.adapter = listStoryPagingAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                listStoryPagingAdapter.retry()
            }
        )

//        initRvView()
        setModelakun()

        binding.addStoryImgs.setOnClickListener {
            val intent = Intent(this, AddStoryApps::class.java)
            startActivity(intent)
        }
        binding.btnMapsloc.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

//    @SuppressLint("NotifyDataSetChanged")
//    private fun initRvView(){
//        adapter = StoriesAdapterakun()
////        adapter.notifyDataSetChanged()
//        val recylerv = binding.listStoryRv
//        recylerv.layoutManager = LinearLayoutManager(this@MainActivity)
//        recylerv.adapter = adapter
//        recylerv.setHasFixedSize(false)
//    }

    private fun setModelakun(){
        mainModelAkun = ViewModelProvider(
            this, ModelFactory(UserPref.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainModelAkun.getUserakun().observe(this){ user ->
            if (!user.isLogin){
                val intent = Intent(this@MainActivity, WelcomeScreen::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            listPagingViewModel = ViewModelProvider(
                this,
                ViewModelFactory(user.token)
            )[ListPagingViewModel::class.java]
            listPagingViewModel.list.observe(this, {
                listStoryPagingAdapter.submitData(lifecycle, it)
            })
            listStoryPagingAdapter.refresh()
//            mainModelAkun.setStoriesakun(tokenAuth = user.token)
        }

//        mainModelAkun.getStoriesak().observe(this){
//            adapter.setListStory(it)
//
//        }

//        adapter.setOnItemClickCallback(object : StoriesAdapterakun.OnItemClickCallback{
//            override fun onItemClicked(story: ListAllStoriesItem) {
//                Toast.makeText(this@MainActivity, story.name, Toast.LENGTH_SHORT).show()
////                DetailStoryakunn.start(this@MainActivity, story)
//                val intent = Intent(this@MainActivity, DetailStoryakunn::class.java)
//                intent.putExtra(DetailStoryakunn.EXTRA_URL, story.photoUrl)
//                intent.putExtra(DetailStoryakunn.EXTRA_AT, story.createdAt)
//                intent.putExtra(DetailStoryakunn.EXTRA_DESC, story.description)
//                intent.putExtra(DetailStoryakunn.EXTRA_NAME, story.name)
//                startActivity(intent)
//            }
//        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu_akun, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu0 -> {
                mainModelAkun.logOut()
                return true
            }
            R.id.menuset -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            else -> return true
        }
    }


}