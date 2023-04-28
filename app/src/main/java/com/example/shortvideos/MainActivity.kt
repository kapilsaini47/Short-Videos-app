package com.example.shortvideos

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.shortvideos.adapter.PagerAdapter
import com.example.shortvideos.network.NetworkManager
import com.example.shortvideos.viewModel.VideoViewModel
import com.example.shortvideos.viewModel.VideoViewModelFactory
import com.example.shortvideos.repository.Repository
import com.example.shortvideos.utils.Resource

class MainActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var videoViewModel: VideoViewModel
    private lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.pager)
        pagerAdapter = PagerAdapter(this)
        progressBar = findViewById(R.id.pbActivity)
        viewPager.adapter = pagerAdapter

        val repository = Repository()
        val networkManager = NetworkManager()
        val factory = VideoViewModelFactory(repository,application,networkManager)
        videoViewModel = ViewModelProvider(this,factory).get(VideoViewModel::class.java)

        //Observing response from view-model with it resource state modification
        videoViewModel.response.observe(this, Observer { response->
           when(response){
               is Resource.Success-> {
                   hideProgressBar()
                   pagerAdapter.diff.submitList(response.data?.hits)
               }
               is Resource.Loading-> {
                   showProgressBar()
               }
               is Resource.Error-> {
                   hideProgressBar()
                   Toast.makeText(this,response.message.toString(),Toast.LENGTH_LONG).show()
               }
               else -> {
                   Toast.makeText(this,response.message.toString(),Toast.LENGTH_LONG).show()
               }
           }
        })

    }

    private fun hideProgressBar(){
        progressBar.visibility = View.GONE
    }

    private fun showProgressBar(){
        progressBar.visibility = View.VISIBLE
    }




}