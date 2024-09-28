package com.example.book.library.domain

import com.example.book.library.domain.model.BookListDataItem
import java.util.Calendar
import javax.inject.Inject

class FetchBooksListUsecase @Inject constructor(private val iRemoteRepository: IRemoteRepository) {


    suspend operator fun invoke(): Map<Int, List<BookListDataItem>> {
        val data = iRemoteRepository.getBookList()
        val mappedData = mappedDataYearWise(data)
        return mappedData
    }

    private fun mappedDataYearWise(data: List<BookListDataItem>): Map<Int, List<BookListDataItem>> {
        return data.groupBy { item ->
            getYearFromEpoch(item.publishedChapterDate)
        }.toSortedMap(compareByDescending { it })
    }

    private fun getYearFromEpoch(publishedChapterDate: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = publishedChapterDate.toLong() * 1000
        return calendar.get(Calendar.YEAR)
    }


}