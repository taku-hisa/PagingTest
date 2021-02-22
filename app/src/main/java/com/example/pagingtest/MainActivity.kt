package com.example.pagingtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val adapter = SampleDataAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView.adapter = adapter
        lifecycleScope.launch {
            getStream().collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                progressBar.isVisible =
                    (it.refresh is LoadState.Loading) or (it.append is LoadState.Loading)
                retryButton.isVisible =
                    (it.refresh is LoadState.Error) or (it.append is LoadState.Error)
            }
        }

        retryButton.setOnClickListener {
            adapter.retry()
        }
    }

    private fun getStream(): Flow<PagingData<String>> {

        return Pager(
            config = PagingConfig(pageSize = 10),
            initialKey = 0
        ) {
            DataSource()
        }.flow
    }
}