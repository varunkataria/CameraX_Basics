package com.example.passportcamera

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.passportcamera.models.MediaPiece

class PieceAdapter(
    private val context: Context,
    private val pieces: ArrayList<MediaPiece>,
    private val photoLoader: PhotoLoader,
    private val homeViewModel: HomeViewModel
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TYPE_FILM = 0
    private val TYPE_BOOK = 1
    private val TYPE_RICK_MORTY = 2

    class ViewHolderFilm(
        inflater: LayoutInflater,
        parent: ViewGroup,
        private var homeViewModel: HomeViewModel
    ) :
        RecyclerView.ViewHolder(
            inflater.inflate
                (R.layout.home_media_piece, parent, false)
        ) {

        private var mTitleView: TextView? = null
        private var mImageView: ImageView? = null
        var mBtn: ImageButton? = null


        init {
            mTitleView = itemView.findViewById(R.id.tv_media_piece)
            mImageView = itemView.findViewById(R.id.iv_media_piece)
            mBtn = itemView.findViewById(R.id.btn_piece_close)

        }

        fun bind(film: MediaPiece.Film, context: Context) {
            mTitleView?.text = film.Title
            mTitleView?.setTextColor(Color.parseColor("#ffffff"))
            Glide.with(mImageView!!).load(film.Images[0]).into(mImageView!!)
            mImageView?.setOnClickListener {
                val intent = Intent(context, ExpandActivity::class.java).apply {
                    val bundle = Bundle()
                    bundle.putSerializable("RickMorty", film)
                    putExtras(bundle)
                }
                context.startActivity(intent)
            }


        }
    }


    class ViewHolderBook(
        inflater: LayoutInflater,
        parent: ViewGroup,
        private var homeViewModel: HomeViewModel
    ) :
        RecyclerView.ViewHolder(
            inflater.inflate
                (R.layout.home_media_piece, parent, false)
        ) {

        private var mTitleView: TextView? = null
        private var mImageView: ImageView? = null
        var mBtn: ImageButton? = null


        init {
            mTitleView = itemView.findViewById(R.id.tv_media_piece)
            mImageView = itemView.findViewById(R.id.iv_media_piece)
            mBtn = itemView.findViewById(R.id.btn_piece_close)
        }

        fun bind(book: MediaPiece.Book, context: Context) {
            mTitleView?.text = book.title
            Glide.with(mImageView!!).load(book.thumbnailUrl).into(mImageView!!)
            mImageView?.setOnClickListener {
                val intent = Intent(context, ExpandActivity::class.java).apply {
                    val bundle = Bundle()
                    bundle.putSerializable("RickMorty", book)
                    putExtras(bundle)
                }
                println(intent)
                println(context)
                context.startActivity(intent)
            }

        }
    }

    class ViewHolderRickMorty(
        inflater: LayoutInflater,
        parent: ViewGroup,
        private var photoLoader: PhotoLoader,
        private var homeViewModel: HomeViewModel
    ) :
        RecyclerView.ViewHolder(
            inflater.inflate
                (R.layout.home_rick_and_morty, parent, false)
        ) {

        private var mTitleView: TextView? = null
        private var mImageView: ImageView? = null
        private var mButton: ImageButton? = null
        var mButtonClose: ImageButton? = null


        init {
            mTitleView = itemView.findViewById(R.id.tv_rick_and_morty)
            mImageView = itemView.findViewById(R.id.iv_rick_and_morty)
            mButton = itemView.findViewById(R.id.btn_rick_and_morty)
            mButtonClose = itemView.findViewById(R.id.btn_rick_and_morty_close)

        }

        fun bind(rickMorty: MediaPiece.RickMorty, context: Context) {
            mTitleView?.text = rickMorty.name
            mTitleView?.setTextColor(Color.parseColor("#000000"))
            Glide.with(mImageView!!).load(rickMorty.image).into(mImageView!!)
            mButton?.setOnClickListener {
                photoLoader.setPhoto(rickMorty.image.toUri())
                it.findNavController().navigate(HomeFragmentDirections.actionHomeToPreview())
            }
            mImageView?.setOnClickListener {
                val intent = Intent(context, ExpandActivity::class.java).apply {
                    val bundle = Bundle()
                    bundle.putSerializable("RickMorty", rickMorty)
                    putExtras(bundle)
                }
                println(intent)
                println(context)
                context.startActivity(intent)
            }

        }

    }


    override fun getItemViewType(position: Int): Int {
        return when {
            pieces[position] is MediaPiece.Film -> {
                TYPE_FILM
            }
            pieces[position] is MediaPiece.RickMorty -> {
                TYPE_RICK_MORTY
            }
            else -> {
                TYPE_BOOK
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_FILM -> {
                ViewHolderFilm(LayoutInflater.from(context), parent, homeViewModel)
            }
            TYPE_RICK_MORTY -> {
                ViewHolderRickMorty(
                    LayoutInflater.from(context),
                    parent,
                    photoLoader,
                    homeViewModel
                )
            }
            else -> {
                ViewHolderBook(LayoutInflater.from(context), parent, homeViewModel)
            }
        }
    }

    override fun getItemCount(): Int {
        return pieces.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val piece: MediaPiece = pieces[position]
        if (piece is MediaPiece.Book) {
            val bookHolder: ViewHolderBook = holder as ViewHolderBook
            bookHolder.bind(piece, context)
            bookHolder.mBtn?.setOnClickListener {
                pieces.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, pieces.size)
                // MAKE A HELPER FUNCTION! pass the button in the method.
            }

        }
        if (piece is MediaPiece.Film) {
            val filmHolder: ViewHolderFilm = holder as ViewHolderFilm
            filmHolder.bind(piece, context)
            filmHolder.mBtn?.setOnClickListener {
                pieces.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, pieces.size)
            }
        }
        if (piece is MediaPiece.RickMorty) {
            val rickMortyHolder: ViewHolderRickMorty = holder as ViewHolderRickMorty
            rickMortyHolder.bind(piece, context)
            rickMortyHolder.mButtonClose?.setOnClickListener {
                pieces.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, pieces.size)
            }

        }

    }

}