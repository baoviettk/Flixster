package com.example.flixster

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
const val MOVIE_EXTRA = "MOVIE_EXTRA"
class MovieAdapter(private val context: Context, private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    val movieName: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }


    override fun getItemCount() = movies.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val orientation = context.resources.configuration.orientation
        val ivPoster = itemView.findViewById<ImageView>(R.id.ivPoster)
        val tvTile = itemView.findViewById<TextView>(R.id.tvTitle)
        val tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)
        init {
            itemView.setOnClickListener(this)
        }
        fun bind(movie: Movie) {
            tvTile.text = movie.title
            tvOverview.text = movie.overview
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
                Glide.with(context).load(movie.posterFullUrl).into(ivPoster)
            if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                Glide.with(context).load(movie.backdropPathUrl).into(ivPoster)
        }

        override fun onClick(v: View?) {
            val movie= movies[adapterPosition]
            Toast.makeText(context,movie.title,Toast.LENGTH_SHORT).show()
            val intent = Intent(context,DetailActivity::class.java)
            intent.putExtra(MOVIE_EXTRA,movie)
            context.startActivity(intent)
        }

    }

}
