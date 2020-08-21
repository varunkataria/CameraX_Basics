package com.example.passportcamera

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.passportcamera.models.MediaPiece
import kotlinx.android.synthetic.main.add_fragment.*

/**
 * A hovering view fragment, which allows you to add a new card to the home feed lists.
 */

class AddFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val galleryRequestCode: Int = 123
    private var newCardTitle: String = ""
    private var newCardImage: Uri = Uri.EMPTY
    private lateinit var newPiece: MediaPiece


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        btn_add_close.setOnClickListener {
            hideCard()
        }
        btn_choose_image.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Pick an Image"),
                galleryRequestCode
            )
        }
        btn_add_card.setOnClickListener {
            val categoryTitle =
                view?.findViewById<View>(R.id.add_fragment_container)?.tag as String
            newCardTitle = editText_card_name.text.toString()
            if (categoryTitle == "Films") {
                val imageList = ArrayList<String>()
                imageList.add(newCardImage.toString())
                newPiece = MediaPiece.Film(newCardTitle, imageList, "", "", "", "", "")
            }
            if (categoryTitle == "Books") {
                newPiece = MediaPiece.Book(
                    newCardTitle, newCardImage.toString(), "", ArrayList(), 0
                )
            }
            if (categoryTitle == "Rick and Morty Characters") {
                newPiece = MediaPiece.RickMorty(
                    newCardTitle, newCardImage.toString(), "", "", HashMap()
                )
            }
            println(newPiece.text + newPiece.photo)
            homeViewModel.addNewPiece(newPiece)
            hideCard()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == galleryRequestCode && resultCode == RESULT_OK && data != null) {
            val imageData: Uri? = data.data
            println(imageData)
            if (imageData != null) {
                newCardImage = imageData
                checkBox.isChecked = true
            }
        }
    }

    private fun hideCard() {
        view?.findViewById<View>(R.id.add_fragment_container)?.isVisible = false
        newCardTitle = ""
        newCardImage = Uri.EMPTY
        editText_card_name.text.clear()
        checkBox.isChecked = false
    }


}
