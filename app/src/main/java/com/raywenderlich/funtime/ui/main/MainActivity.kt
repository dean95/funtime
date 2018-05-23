package com.raywenderlich.funtime.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.raywenderlich.funtime.R
import com.raywenderlich.funtime.data.network.MovieService
import com.raywenderlich.funtime.data.network.model.ApiMoviesResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var moviesRecyclerView: RecyclerView
    private val mainAdapter = MainAdapter()

    private val movieService = MovieService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moviesRecyclerView = findViewById(R.id.rv_main_movies_container)

        initializeRecyclerView()

        fetchMovies()
    }

    private fun fetchMovies() {
        movieService.getMovies()
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
