package com.example.book.library.data.dto

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
}