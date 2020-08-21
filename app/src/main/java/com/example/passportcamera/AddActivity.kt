package com.example.passportcamera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.passportcamera.constants.Constants
import com.example.passportcamera.models.MediaPiece
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.add_fragment.*

class AddActivity: AppCompatActivity() {

//    private val galleryRequestCode: Int = 123
//    private val updateRequestCode: Int = 234
    private var newCardTitle: String = ""
    private var newCardImage: Uri = Uri.EMPTY
    private lateinit var newPiece: MediaPiece
    private lateinit var homeViewModel: HomeViewModel
    private val getContent = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            if (uri != null) {
                newCardImage = uri
                Glide.with(iv_chosen_thumbnail).load(uri).into(iv_chosen_thumbnail)
            }
        }




    override fun onCreate(savedInstanceState: Bundle?) {
        println("Add Activity")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val result = intent.getStringExtra("title")
        tv_piece_add_title.text = result
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)


        setButtons()
    }

    private fun setButtons() {
        btn_close_fab.setOnClickListener {
            onBackPressed()
        }
        btn_choose_image_add.setOnClickListener{
            getContent.launch(arrayOf("image/*"))
        }
        btn_submit.setOnClickListener {
            val categoryTitle = tv_piece_add_title.text.toString()
            newCardTitle = editText_piece_add.text.toString()
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


            val intent = Intent().apply {
                val bundle = Bundle()
                bundle.putSerializable("MediaPiece", newPiece)
                putExtras(bundle)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()

//            println("Category Title: $categoryTitle")
            newCardTitle = editText_piece_add.text.toString()
//            println(newPiece.text + newPiece.photo)
//            homeViewModel.addNewPiece(newPiece)
        }
    }


}