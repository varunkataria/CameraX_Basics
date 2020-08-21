package com.example.passportcamera

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.passportcamera.constants.Constants
import com.example.passportcamera.models.MediaCategory
import com.example.passportcamera.models.MediaPiece
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class HomeViewModel : ViewModel() {

    private var categoriesLiveData: MutableLiveData<ArrayList<MediaCategory>> =
        MutableLiveData<ArrayList<MediaCategory>>()
    private var expandCard: MutableLiveData<MediaPiece> =
        MutableLiveData<MediaPiece>()

    private var categoriesLiveDataCopy: MutableLiveData<ArrayList<MediaCategory>> =
        MutableLiveData<ArrayList<MediaCategory>>()
    lateinit var addFragmentContainer: View


    fun getCategoriesLiveData(): MutableLiveData<ArrayList<MediaCategory>> {
        return categoriesLiveData
    }

    fun getJsonDataFromAssets(context: Context): HashMap<Constants, String> {
        val categoriesMap: HashMap<Constants, String> = HashMap()
        var filmsString = ""
        var booksString = ""
        var rickMortyString = ""
        try {
            filmsString = context.assets.open("films.json").bufferedReader().use { it.readText() }
            booksString = context.assets.open("books.json").bufferedReader().use { it.readText() }
            rickMortyString =
                context.assets.open("rick_and_morty.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
        categoriesMap[Constants.TYPE_FILM] = filmsString
        categoriesMap[Constants.TYPE_BOOK] = booksString
        categoriesMap[Constants.TYPE_RICK_MORTY] = rickMortyString
        return categoriesMap
    }

    fun getCategories(categoriesMap: HashMap<Constants, String>) {
        val categoriesList: ArrayList<MediaCategory> = ArrayList()
        val categoriesListCopy: ArrayList<MediaCategory> = ArrayList()
        val gson = Gson()
        for ((type, piece) in categoriesMap) {
            if (type == Constants.TYPE_FILM) {
                val listPieceType = object : TypeToken<List<MediaPiece.Film>>() {}.type
                val mediaList: ArrayList<MediaPiece> = gson.fromJson(piece, listPieceType)
                val mediaListCopy: ArrayList<MediaPiece> = gson.fromJson(piece, listPieceType)
                categoriesList.add(MediaCategory("Films", mediaList))
                categoriesListCopy.add(MediaCategory("Films", mediaListCopy))
            }
            if (type == Constants.TYPE_BOOK) {
                val listPieceType = object : TypeToken<List<MediaPiece.Book>>() {}.type
                val mediaList: ArrayList<MediaPiece> = gson.fromJson(piece, listPieceType)
                val mediaListCopy: ArrayList<MediaPiece> = gson.fromJson(piece, listPieceType)
                categoriesList.add(MediaCategory("Books", mediaList))
                categoriesListCopy.add(MediaCategory("Books", mediaListCopy))
            }
            if (type == Constants.TYPE_RICK_MORTY) {
                val listPieceType = object : TypeToken<List<MediaPiece.RickMorty>>() {}.type
                val mediaList: ArrayList<MediaPiece> = gson.fromJson(piece, listPieceType)
                val mediaListCopy: ArrayList<MediaPiece> = gson.fromJson(piece, listPieceType)
                categoriesList.add(MediaCategory("Rick and Morty Characters", mediaList))
                categoriesListCopy.add(MediaCategory("Rick and Morty Characters", mediaListCopy))
                println("Rick and Morty")
            }

        }
        categoriesLiveData.postValue(categoriesList)
        categoriesLiveDataCopy.value = categoriesListCopy
    }

    fun filter(text: String) {
//        val categoriesList: ArrayList<MediaCategory> = categoriesLiveData.value ?: ArrayList()
        val categoriesFilteredList: ArrayList<MediaCategory>? = filterCategories(text)
        categoriesLiveData.postValue(categoriesFilteredList!!)
    }

    private fun filterCategories(text: String): ArrayList<MediaCategory>? {
        val categoriesList: ArrayList<MediaCategory> = ArrayList()
        categoriesLiveDataCopy.value?.let { categoriesList.addAll(it) }
        for (category in categoriesList.withIndex()) {
            val pieces: ArrayList<MediaPiece> = categoriesList[category.index].pieces.filter {
                it.text.toLowerCase(Locale.getDefault())
                    .contains(text.toLowerCase(Locale.getDefault()))
            } as ArrayList<MediaPiece>
            categoriesList[category.index] = MediaCategory(category.value.title, pieces)
        }
        return categoriesList
    }

    fun refresh(swipeContainer: SwipeRefreshLayout) {
        println("Refreshing")
        val categoriesList: ArrayList<MediaCategory> = ArrayList()
        for (category in categoriesLiveDataCopy.value!!) {
            val pieces: ArrayList<MediaPiece> = ArrayList()
            for (piece in category.pieces) {
                pieces.add(piece)
            }
            categoriesList.add(MediaCategory(category.title, pieces))
        }
        categoriesLiveData.postValue(categoriesList)
        swipeContainer.isRefreshing = false
    }

    fun setAddContainer(addContainer: View?) {
        if (addContainer != null) {
            addFragmentContainer = addContainer
        }
    }

    fun addNewPiece(newPiece: MediaPiece) {
        val categoriesList: ArrayList<MediaCategory> = ArrayList()
        categoriesList.addAll(categoriesLiveData.value!!)
        for (category in categoriesList) {
            if (category.pieces[0] is MediaPiece.Film && newPiece is MediaPiece.Film) {
                category.pieces.add(newPiece)
            }
            if (category.pieces[0] is MediaPiece.Book && newPiece is MediaPiece.Book) {
                category.pieces.add(newPiece)
            }
            if (category.pieces[0] is MediaPiece.RickMorty && newPiece is MediaPiece.RickMorty) {
                category.pieces.add(newPiece)
            }
        }
        categoriesLiveData.postValue(categoriesList)
    }

    fun setExpandCard(piece: MediaPiece) {
        expandCard.postValue(piece)
    }

    fun getExpandCard(): MutableLiveData<MediaPiece> {
        return expandCard
    }


}
