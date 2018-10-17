package com.tjohnn.popularmovieskotlin.ui.moviedetail

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.tjohnn.popularmovieskotlin.R
import com.tjohnn.popularmovieskotlin.data.dto.Review

class ReviewsAdapter(
        private var items: List<Review>
): RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int)
            = ReviewViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_reviews, parent, false))

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bindReview(items.get(position))
    }

    fun updateItems(trailers: List<Review>){
        items = trailers
        notifyDataSetChanged()
    }


    inner class ReviewViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {

        @BindView(R.id.tv_content)
        lateinit var contentTextView: TextView

        @BindView(R.id.tv_author)
        lateinit var authorTextView: TextView

        init {
            ButterKnife.bind(this, mView)

        }

        @SuppressLint("SetTextI18n")
        fun bindReview(review: Review){
            with(review){
                contentTextView.text = content
                authorTextView.text = "- $author"
            }
        }

    }
}