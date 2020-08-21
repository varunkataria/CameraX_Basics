package com.example.passportcamera.models

import java.io.Serializable

abstract class MediaPiece: Serializable {

    abstract val text: String
    abstract val photo: String

    data class RickMorty(
        val name: String,
        val image: String,
        val species: String,
        val gender: String,
        val origin: Map<String, String>
    ) : MediaPiece() {
        override val text: String
            get() = name
        override val photo: String
            get() = image
    }

    data class Book(
        val title: String,
        val thumbnailUrl: String,
        val shortDescription: String,
        val authors: List<String>,
        val pageCount: Int
    ) : MediaPiece() {
        override val text: String
            get() = title
        override val photo: String
            get() = thumbnailUrl
    }

    data class Film(
        val Title: String,
        val Images: List<String>,
        val Plot: String,
        val Actors: String,
        val Runtime: String,
        val Year: String,
        val Rated: String
    ) : MediaPiece() {
        override val text: String
            get() = Title
        override val photo: String
            get() = Images[0]
    }

}