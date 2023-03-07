@file:OptIn(ExperimentalPagerApi::class)

package org.amobile.mqtt_k.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.amobile.mqtt_k.prefs.Prefs
import org.amobile.mqtt_k.prefs.Shared
import kotlin.math.log


@Composable
fun MainView(ctx: Context) {
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
                        HomeUI(ctx)
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

