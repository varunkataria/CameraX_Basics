package com.example.passportcamera

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.passportcamera.models.MediaCategory
import kotlinx.android.synthetic.main.add_fragment.view.*


class CategoryAdapter(
    private val context: Context,
    private val categories: ArrayList<MediaCategory>,
    private val photoLoader: PhotoLoader,
    private val homeViewModel: HomeViewModel,
    private val openAddActivity: ActivityResultLauncher<Intent>
) : RecyclerView.Adapter<ViewHolder>() {
    private var categoryHolder: ViewHolderCategory? = null
    private val addFragmentContainer = homeViewModel.addFragmentContainer


    class ViewHolderCategory(inflater: LayoutInflater, parent: ViewGroup) :
        ViewHolder(
            inflater.inflate
                (R.layout.home_media_category, parent, false)
        ) {

        private var mTitleView: TextView? = null
        var mRecyclerView: RecyclerView? = null
        var mBtn: ImageButton? = null
        var mBtnAdd: ImageButton? = null
        var mBtnAddTwo: ImageButton? = null


        init {
            mTitleView = itemView.findViewById(R.id.tv_media_category)
            mRecyclerView = itemView.findViewById(R.id.rv_media_category)
            mBtn = itemView.findViewById(R.id.btn_category_close)
            mBtnAdd = itemView.findViewById(R.id.btn_piece_add)
            mBtnAddTwo = itemView.findViewById(R.id.btn_piece_add_two)

        }

        fun bind(category: MediaCategory) {
            mTitleView?.text = category.title

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolderCategory(LayoutInflater.from(context), parent)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category: MediaCategory = categories[position]
        categoryHolder = holder as ViewHolderCategory
        categoryHolder?.bind(category)
        categoryHolder?.mRecyclerView?.adapter =
            PieceAdapter(context, category.pieces, photoLoader, homeViewModel)
        categoryHolder?.mRecyclerView?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        categoryHolder?.mRecyclerView?.setHasFixedSize(true)
        categoryHolder?.mBtn?.setOnClickListener {
            categories.removeAt(getItemViewType(position))
            notifyItemRemoved(getItemViewType(position))
        }
        categoryHolder?.mBtnAdd?.setOnClickListener {
            addFragmentContainer.tv_card_title.text = category.title
            addFragmentContainer.tag = category.title
            addFragmentContainer.isVisible = true
        }
        categoryHolder?.mBtnAddTwo?.setOnClickListener {
            val message: String = category.title
            val intent = Intent(context, AddActivity::class.java)
            intent.putExtra("title", message)
            openAddActivity.launch(intent)
//            context.startActivity(intent)
        }


    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}

