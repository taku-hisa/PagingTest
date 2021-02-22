package com.example.pagingtest

import androidx.paging.PagingSource
import kotlinx.coroutines.delay
import kotlin.random.Random


class DataSource : PagingSource<Int, String>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        return try {
            if (Random.nextInt(3) == 0) throw IllegalStateException()

            val pageNumber = params.key ?: 0
            val items = (0 until 25).map { it + pageNumber * 25 }.map { it.toString() }
            val prev: Int? = if (pageNumber > 0) pageNumber - 1 else null
            val next = if (pageNumber < 10) pageNumber + 1 else null

            delay(1000)

            LoadResult.Page(items, prev, next)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}