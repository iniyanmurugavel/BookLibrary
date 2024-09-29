package com.example.book.library.data.dto

import com.example.book.library.domain.model.BookListDataItem
import com.example.book.library.domain.model.Country

object DtoMapper {
    fun List<CountryDto>?.toCountryListDomain() = this?.let { countryDto ->
        countryDto.map { Country(it.country.orEmpty(), it.region.orEmpty()) }
    } ?: run {
        emptyList()
    }

    fun CountryDto?.toCountryDomain() = this?.let {
        Country(it.country.orEmpty(), it.region.orEmpty())
    } ?: kotlin.run {
        null
    }

    fun List<BookListDataItemDto>.toBookListItemDomain() = this?.let { bookListItemDto ->
        bookListItemDto.map {
            BookListDataItem(
                id = it.id,
                image = it.image,
                popularity = it.popularity,
                publishedChapterDate = it.publishedChapterDate,
                score = it.score,
                title = it.title
            )

        }
    }
}