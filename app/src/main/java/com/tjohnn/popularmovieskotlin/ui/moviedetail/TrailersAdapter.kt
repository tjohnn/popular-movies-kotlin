package com.tjohnn.popularmovieskotlin.ui.moviedetail

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.tjohnn.popularmovieskotlin.R
import com.tjohnn.popularmovieskotlin.data.dto.Trailer

class TrailersAdapter(
        private var items: List<Trailer>,
        private val mListener: OnTrailerItemListener
): RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int)
            = TrailerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_trailers, parent, false))

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        holder.bindTrailer(items.get(position))
    }

    fun updateItems(trailers: List<Trailer>){
        items = trailers
        notifyDataSetChanged()
    }


    inner class TrailerViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {

        @BindView(R.id.tv_trailer)
        lateinit var trailerName: TextView

        @BindView(R.id.tv_trailer_size)
        lateinit var trailerSize: TextView

        init {
            ButterKnife.bind(this, mView)

        }

        fun bindTrailer(trailer: Trailer){
            with(trailer){
                trailerName.text = name
                trailerSize.text = size
            }

            mView.setOnClickListener{
                mListener.onTrailerItemClicked(trailer)
            }
        }

    }

    interface OnTrailerItemListener {
        fun onTrailerItemClicked(trailer: Trailer)
    }
}