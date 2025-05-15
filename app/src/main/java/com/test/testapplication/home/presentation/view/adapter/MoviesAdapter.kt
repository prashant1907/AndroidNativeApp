package com.test.testapplication.home.presentation.view.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.test.testapplication.home.data.Search
import com.test.testapplication.R
import com.test.testapplication.core.AppConstants
import com.test.testapplication.core.util.ImageUtil
import com.test.testapplication.databinding.ItemMovieBinding

class MoviesAdapter(private val movies : ArrayList<Search>) :
    RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {


    class MyViewHolder(itemMovieBinding: ItemMovieBinding) :
        RecyclerView.ViewHolder(itemMovieBinding.root) {
        val binding: ItemMovieBinding = itemMovieBinding

        fun bindData(movie: Search) {
            ImageUtil.setImage(binding.image,movie.Poster)
            binding.title.text = movie.Title
            binding.year.text = movie.Year
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemMovieBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_movie,
            parent,
            false
        )

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(movies[position])
        holder.itemView.setOnClickListener {
            val navController = Navigation.findNavController(holder.itemView)
            val bundle = Bundle()
            bundle.putParcelable(AppConstants.MOVIE_DATA,movies[position])
            navController.navigate(R.id.movieDetailFragment,bundle)
        }
    }

    override fun getItemCount(): Int {
        return movies.size
    }

}