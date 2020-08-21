package com.example.passportcamera

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide

/**
 * This fragment allows the user to view and possibly interact with the photo they just took.
 */
class PreviewFragment internal constructor() : Fragment() {

    private lateinit var container: RelativeLayout
    private val photoLoader: PhotoLoader by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        println("On Create View")
        return inflater.inflate(R.layout.photo_display_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("On View Created")

        container = view as RelativeLayout
        val photoDisplay: ImageView = container.findViewById<ImageButton>(R.id.photoDisplay)
        // Get the photo Uri from the Shared View Model
        val photo: Uri? = photoLoader.getPhoto().value
        println("Uri: ${photoLoader.getPhoto().value}")

        // Load the recently captured photo into the ImageView
        Glide.with(photoDisplay).load(photo).into(photoDisplay)
    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        (requireActivity() as CameraActivity).supportActionBar?.show()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.search).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }


}