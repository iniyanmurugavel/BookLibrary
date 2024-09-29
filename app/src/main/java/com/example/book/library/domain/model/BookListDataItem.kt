package com.example.book.library.domain.model


data class BookListDataItem(
    val id: String,
    val image: String,
    val popularity: Int,
    val publishedChapterDate: Int,
    val score: Double,
    val title: String
)
