package com.example.passportcamera

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * A SharedViewModel class which provides the ability to store and pass the photo between the
 * CameraFragment and the PreviewFragment
 */
class PhotoLoader : ViewModel() {

    enum class CameraState {
        FRONT_CAMERA,
        REAR_CAMERA
    }

    private var cameraState: CameraState = CameraState.REAR_CAMERA

    fun getCameraState(): CameraState {
        return cameraState
    }

    fun setCameraState(state: CameraState) {
        cameraState = state
    }


    private val photoUri = MutableLiveData<Uri>()


    fun getPhoto(): MutableLiveData<Uri> {
        return photoUri
    }

    fun setPhoto(uri: Uri) {
        photoUri.postValue(uri)
    }



}