package org.amobile.mqtt_k

import android.os.Bundle
import android.util.Log
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.amobile.mqtt_k.ui.theme.MQTT_KTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MQTT_KTheme(darkTheme = true) {
                // A surface container using the 'background' color from the theme

//                var tabPage by remember { mutableStateOf(TabPage.Home) }
                var pagerState = rememberPagerState(
                    pageCount = TabPage.values().size,
                    initialPage = TabPage.HOME.ordinal
                )
                val scope = rememberCoroutineScope()

                //surface start
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(bottomBar = {
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
                                    0 -> InitUI(TabPage.values()[index].name)
                                    1 -> Screen()
                                    2 -> InitUI(TabPage.values()[index].name)
                                }
                            }
                        }
                    }
                }
                //surface end

            }
        }
    }

    override fun onResume() {
        Log.e("777", "ddd")
        super.onResume()
    }

    @Composable
    fun HomeUI() {

    }

    @Composable
    fun InitUI(name: String) {
        Text(text = name)
    }

    @Composable
    fun Screen() {
        Surface(modifier = Modifier.size(100.dp), color = Color.Red) {

        }
    }
}



