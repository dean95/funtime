package com.raywenderlich.funtime.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.raywenderlich.funtime.R
import com.raywenderlich.funtime.data.network.MovieService
import com.raywenderlich.funtime.data.network.model.ApiMoviesResult
import com.raywenderlich.funtime.ui.trailer.TrailerActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var moviesRecyclerView: RecyclerView
    private val mainAdapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moviesRecyclerView = findViewById(R.id.rv_main_movies_container)

        initializeRecyclerView()

//        fetchMovies()
    }

    fun onRefreshButtonClick(view: View) {
        val intent = Intent(this, TrailerActivity::class.java)
        intent.putExtra(TrailerActivity.MOVIE_ID_EXTRA, 1)
        startActivity(intent)
    }

    private fun fetchMovies() {
        MovieService.getMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::moviesFetchedSuccessfully,
                        this::moviesFetchFailed)
    }

    private fun moviesFetchedSuccessfully(movies: ApiMoviesResult) {
        mainAdapter.onMoviesUpdate(movies)
    }

    private fun moviesFetchFailed(throwable: Throwable) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }

    private fun initializeRecyclerView() {
        moviesRecyclerView.layoutManager = LinearLayoutManager(this)
        moviesRecyclerView.adapter = mainAdapter
    }
}
