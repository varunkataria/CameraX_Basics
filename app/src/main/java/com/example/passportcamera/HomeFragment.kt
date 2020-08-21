package com.example.passportcamera

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.passportcamera.models.MediaPiece
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_fragment.*


/**
 * The home screen, which allows you to access the navigation drawer.
 */

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val photoLoader: PhotoLoader by activityViewModels()
    private var categoryAdapter: CategoryAdapter? = null
    private val openAddActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val newPiece: MediaPiece =
                    result.data?.extras?.getSerializable("MediaPiece") as MediaPiece
                homeViewModel.addNewPiece(newPiece)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as CameraActivity).supportActionBar?.show()
    }


    override fun onStart() {
        super.onStart()
        val categoriesMap = homeViewModel.getJsonDataFromAssets(requireContext())
        homeViewModel.getCategoriesLiveData().observe(viewLifecycleOwner, Observer {
            println("Live Data Posted")
            categoryAdapter = CategoryAdapter(requireContext(), it, photoLoader, homeViewModel, openAddActivity)
            rv_home.adapter = categoryAdapter
            rv_home.layoutManager = LinearLayoutManager(requireContext())
        })
        homeViewModel.getCategories(categoriesMap)
        val swipeContainer: SwipeRefreshLayout = swiperefresh
        swipeContainer.setOnRefreshListener {
            homeViewModel.refresh(swipeContainer)
        }
        homeViewModel.getExpandCard().observe(viewLifecycleOwner, Observer {
            expandCard(it)
            println("Expanding Card")
        })
        btn_expanded_close.setOnClickListener {
            rl_background.visibility = View.INVISIBLE
            rl_background.setBackgroundColor(Color.parseColor("#ffffff"))
            (activity as CameraActivity).toolbar.setBackgroundColor(Color.parseColor("#ffffff"))
        }


    }

    private fun expandCard(pieceToExpand: MediaPiece) {
        rv_expanded_cards.adapter = ExpandAdapter(pieceToExpand, requireContext())
        rv_expanded_cards.layoutManager = LinearLayoutManager(requireContext())
        rl_background.visibility = View.VISIBLE
        rl_background.setBackgroundColor(Color.parseColor("#80000000"))
        (activity as CameraActivity).toolbar.setBackgroundColor(Color.parseColor("#80000000"))
        rl_background.bringToFront()
        btn_expanded_close.bringToFront()
    }
}