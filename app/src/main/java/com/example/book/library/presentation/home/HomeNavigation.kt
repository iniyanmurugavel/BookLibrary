package com.example.book.library.presentation.home

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.book.library.domain.model.BookListDataItem
import com.example.book.library.presentation.authentication.AuthenticationActivity
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@OptIn(ExperimentalPagerApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNavHost(onLogoutClick: () -> Unit) {

    val navController = rememberNavController()

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Screen.BookDashboard.toString()
    ) {
        composable(Screen.BookDashboard.toString()) {


            val viewModel = hiltViewModel<HomeViewModel>()

            LaunchedEffect(Unit) {
                viewModel.uiEffect.onEach { effect ->
                    when (effect) {
                        is HomeViewModel.HomeEffect.NavigateToLogin -> {
                            context.startActivity(
                                Intent(
                                    context,
                                    AuthenticationActivity::class.java
                                )
                            )
                            onLogoutClick()
                        }
                    }
                }.collect()
            }

            LaunchedEffect(
                key1 = ""
            ) {
                viewModel.getBookList()
            }
            val coroutineScope = rememberCoroutineScope()

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                uiState.booksList?.let { bookList ->
                    val tabs = bookList.map { it.key }
                    val pagerState = rememberPagerState(pageCount = tabs.count(), initialPage = 0)

                    Column(
                        modifier = Modifier
                            .fillMaxSize(), verticalArrangement = Arrangement.Top
                    ) {
                        Toolbar("Book Library") {
                            viewModel.logout()
                        }

                        ScrollableTabRow(
                            selectedTabIndex = pagerState.currentPage,
                            backgroundColor = Color.White,
                            contentColor = Color.Gray,
                            divider = {
                                Spacer(modifier = Modifier.height(5.dp))
                            },
                            edgePadding = 0.dp,
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    modifier =
                                    Modifier
                                        .pagerTabIndicatorOffset(pagerState, tabPositions)
                                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)),
                                    height = 5.dp,
                                    color = Color.Blue,
                                )
                            },
                            modifier = Modifier,
                        ) {
                            tabs.forEachIndexed { index, tabTitle ->
                                Tab(
                                    selected = pagerState.currentPage == index,
                                    onClick = {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    },
                                    text = {
                                        Text(
                                            text = tabTitle.toString(),
                                            style = TextStyle.Default,
                                            color = Color.Black,
                                        )
                                    },
                                    selectedContentColor = Color.White,
                                    unselectedContentColor = Color.White,
                                )
                            }
                        }
                        HorizontalPager(
                            state = pagerState,
                            modifier =
                            Modifier.fillMaxHeight(),
                            verticalAlignment = Alignment.Top,
                            dragEnabled = true,
                        ) { index ->
                            BookList(bookList[tabs[index]])
                        }
                    }
                }
            }
        }


    }

}

@Composable
fun BookList(dataItem: List<BookListDataItem>?) {
    LazyColumn {
        items(dataItem?.size ?: 0) {
            IndividualBookDetailCard(dataItem?.get(it))
        }
    }
}

@Composable
fun IndividualBookDetailCard(bookListDataItem: BookListDataItem?) {
    var isHeartSelected by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
               isHeartSelected=!isHeartSelected
            }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = bookListDataItem?.image,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(20.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = bookListDataItem?.title.orEmpty(),
                    style = MaterialTheme.typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Score: ${bookListDataItem?.score ?: ""}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
            }

            IconButton(onClick = {
                isHeartSelected = !isHeartSelected
            }) {
                Icon(
                    imageVector = if (isHeartSelected) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isHeartSelected) Color.Red else Color.Gray
                )
            }
        }
    }
}




sealed class Screen{

    @Serializable
    data object BookDashboard : Screen()
}


