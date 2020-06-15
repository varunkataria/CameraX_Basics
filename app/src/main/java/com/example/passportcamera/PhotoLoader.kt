package com.example.passportcamera

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * A SharedViewModel class which provides the ability to store and pass the photo between the
 * CameraFragment and the PreviewFragment
 */
class PhotoLoader : ViewModel() {

    private val photoUri = MutableLiveData<Uri>()

    fun getPhoto(): MutableLiveData<Uri> {
        return photoUri
    }

    fun setPhoto(uri: Uri) {
        photoUri.postValue(uri)
    }



}