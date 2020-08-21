package com.example.passportcamera

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passportcamera.models.MediaPiece
import kotlinx.android.synthetic.main.activity_expand.*
import kotlinx.android.synthetic.main.home_fragment.*

class ExpandActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expand)
    }

    override fun onResume() {
        super.onResume()
        val piece = intent.extras?.getSerializable("RickMorty") as MediaPiece
        rv_activity_expand.adapter = ExpandAdapter(piece, applicationContext)
        rv_activity_expand.layoutManager = LinearLayoutManager(applicationContext)
        btn_close_activity_expand.setOnClickListener{onBackPressed()}
    }

}