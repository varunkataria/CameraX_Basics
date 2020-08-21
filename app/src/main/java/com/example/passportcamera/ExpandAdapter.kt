package com.example.passportcamera

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.passportcamera.models.MediaPiece

class ExpandAdapter(
    private val piece: MediaPiece,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_FILM_FIRST = -1
    private val TYPE_FILM = 0
    private val TYPE_BOOK = 1
    private val TYPE_RICK_MORTY = 2


    class ViewHolderFilmExpanded(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) :
        RecyclerView.ViewHolder(
            inflater.inflate
                (R.layout.home_film_expanded, parent, false)
        ) {

        private var mImageView: ImageView? = null

        init {
            mImageView = itemView.findViewById(R.id.iv_film_expanded)
        }

        fun bind(filmImage: String) {
            Glide.with(mImageView!!).load(filmImage).into(mImageView!!)

        }
    }

    class ViewHolderFilmExpandedFirst(
        inflater: LayoutInflater,
        parent: ViewGroup,
        private var piece: MediaPiece.Film
    ) :
        RecyclerView.ViewHolder(
            inflater.inflate
                (R.layout.home_first_film_expanded, parent, false)
        ) {

        private var mImageView: ImageView? = null
        private var mTitleYearTextView: TextView? = null
        private var mRuntimeTextView: TextView? = null
        private var mPlotTextView: TextView? = null
        private var mActorsTextView: TextView? = null
        private var mRatedTextView: TextView? = null


        init {
            mTitleYearTextView = itemView.findViewById(R.id.tv_film_title_year)
            mRuntimeTextView = itemView.findViewById(R.id.tv_film_runtime)
            mPlotTextView = itemView.findViewById(R.id.tv_plot)
            mActorsTextView = itemView.findViewById(R.id.tv_actors)
            mRatedTextView = itemView.findViewById(R.id.tv_film_rated)
            mImageView = itemView.findViewById(R.id.iv_first_film_expanded)
        }

        fun bind(filmImage: String) {
            val titleYear = "${piece.Title} (${piece.Year})"
            mTitleYearTextView?.text = titleYear
            mRuntimeTextView?.text = piece.Runtime
            mPlotTextView?.text = piece.Plot
            mActorsTextView?.text = piece.Actors
            mRatedTextView?.text = piece.Rated
            println("Film Image to Bind: $filmImage")
            Glide.with(mImageView!!).load(filmImage).into(mImageView!!)
        }
    }

    class ViewHolderBookExpanded(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) :
        RecyclerView.ViewHolder(
            inflater.inflate
                (R.layout.home_book_expanded, parent, false)
        ) {

        private var mImageView: ImageView? = null
        private var mTitleTextView: TextView? = null
        private var mPageCountTextView: TextView? = null
        private var mDescriptionTextView: TextView? = null
        private var mAuthorsTextView: TextView? = null


        init {
            mTitleTextView = itemView.findViewById(R.id.tv_book_title)
            mPageCountTextView = itemView.findViewById(R.id.tv_book_page_count)
            mDescriptionTextView = itemView.findViewById(R.id.tv_book_description)
            mAuthorsTextView = itemView.findViewById(R.id.tv_book_authors)
            mImageView = itemView.findViewById(R.id.iv_expanded_book)
        }

        fun bind(piece: MediaPiece.Book) {
            mTitleTextView?.text = piece.title
            mPageCountTextView?.text = piece.pageCount.toString()
            mDescriptionTextView?.text = piece.shortDescription
            var authors = ""
            for (author in piece.authors.withIndex()) {
                authors += if (author.index == piece.authors.size - 1) {
                    author.value
                } else {
                    "${author.value}, "
                }
            }
            mAuthorsTextView?.text = authors
            Glide.with(mImageView!!).load(piece.thumbnailUrl).into(mImageView!!)
        }
    }

    class ViewHolderRickMortyExpanded(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) :
        RecyclerView.ViewHolder(
            inflater.inflate
                (R.layout.home_rick_and_morty_expanded, parent, false)
        ) {

        private var mImageView: ImageView? = null
        private var mNameTextView: TextView? = null
        private var mSpeciesTextView: TextView? = null
        private var mGenderTextView: TextView? = null
        private var mOriginTextView: TextView? = null


        init {
            mNameTextView = itemView.findViewById(R.id.tv_rick_morty_name)
            mSpeciesTextView = itemView.findViewById(R.id.tv_rick_morty_species)
            mGenderTextView = itemView.findViewById(R.id.tv_rick_morty_gender)
            mOriginTextView = itemView.findViewById(R.id.tv_rick_morty_origin)
            mImageView = itemView.findViewById(R.id.iv_expanded_rick_morty)
        }

        fun bind(piece: MediaPiece.RickMorty) {
            mNameTextView?.text = piece.name
            mSpeciesTextView?.text = piece.species
            println("RickMorty Gender: ${piece.gender}")
            mGenderTextView?.text = piece.gender
            var originName = ""
            for (name in piece.origin.keys) {
                if (name == "name") {
                    originName = piece.origin[name] as String
                }
            }
            println(originName)
            mOriginTextView?.text = originName

            Glide.with(mImageView!!).load(piece.image).into(mImageView!!)
        }
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (piece is MediaPiece.Film) {
            if (viewType == TYPE_FILM_FIRST) {
                ViewHolderFilmExpandedFirst(LayoutInflater.from(context), parent, piece)
            } else {
                ViewHolderFilmExpanded(LayoutInflater.from(context), parent)
            }
        } else if (piece is MediaPiece.Book) {
            ViewHolderBookExpanded(LayoutInflater.from(context), parent)
        } else {
            ViewHolderRickMortyExpanded(LayoutInflater.from(context), parent)
        }
    }

    override fun getItemCount(): Int {
        return if (piece is MediaPiece.Film) {
            piece.Images.size
        } else {
            1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (piece is MediaPiece.Film) {
            if (position == 0) {
                TYPE_FILM_FIRST
            } else
                TYPE_FILM
        } else if (piece is MediaPiece.Book) {
            TYPE_BOOK
        } else {
            TYPE_RICK_MORTY
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (piece is MediaPiece.Film) {
            val filmImage: String = piece.Images[position]
            println("Film Image: $filmImage")
            if (holder is ViewHolderFilmExpandedFirst) {
                val filmExpandedHolder: ViewHolderFilmExpandedFirst = holder
                filmExpandedHolder.bind(filmImage)
            } else {
                val filmExpandedHolder: ViewHolderFilmExpanded = holder as ViewHolderFilmExpanded
                filmExpandedHolder.bind(filmImage)
            }
        }
        if (piece is MediaPiece.Book) {
            val bookExpandedHolder: ViewHolderBookExpanded = holder as ViewHolderBookExpanded
            bookExpandedHolder.bind(piece)
        }
        if (piece is MediaPiece.RickMorty) {
            val rickMortyExpandedHolder: ViewHolderRickMortyExpanded =
                holder as ViewHolderRickMortyExpanded
            rickMortyExpandedHolder.bind(piece)
        }
    }

}
