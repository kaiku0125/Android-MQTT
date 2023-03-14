@file:OptIn(ExperimentalPagerApi::class)

package org.amobile.mqtt_k.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@Composable
fun MainView() {
    val pagerState = rememberPagerState(
        pageCount = TabPage.values().size,
        initialPage = TabPage.HOME.ordinal
    )
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = { MyTopBar() },
        bottomBar = {
            TabHome(
                selectedTabIndex = pagerState.currentPage,
                onSelectedTab = {
                    scope.launch {
                        pagerState.animateScrollToPage(it.ordinal)
                    }
                }
            )
        }) {
        HorizontalPager(state = pagerState) { index ->
            Column(modifier = Modifier.fillMaxSize()) {
//                                InitUI(TabPage.values()[index].name)
                when (index) {
                    TabPage.SETTINGS.ordinal -> InitUI(TabPage.values()[index].name)
                    1 -> {
                        HomeUI()
                    }
                    2 -> InitUI(TabPage.values()[index].name)
                }
            }
        }

    }
}


@Composable
fun MyTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Tim048推播",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun InitUI(name: String) {
    Text(text = name)
}

