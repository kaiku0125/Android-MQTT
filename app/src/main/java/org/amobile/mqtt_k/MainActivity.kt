package org.amobile.mqtt_k

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.amobile.mqtt_k.ui.theme.MQTT_KTheme

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

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
                    }, topBar = {
                        MyTopBar()
                    }) {
                        HorizontalPager(state = pagerState) { index ->
                            Column(modifier = Modifier.fillMaxSize()) {
//                                InitUI(TabPage.values()[index].name)
                                when (index) {
                                    0 -> InitUI(TabPage.values()[index].name)
                                    1 -> HomeUI()
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
    fun HomeUI() {
        var hideKeyboard by remember { mutableStateOf(false) }
        val interactionSource = remember { MutableInteractionSource() }
        ConstraintLayout(modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { hideKeyboard = true }) {
            val (userNameTextField, clientIDTextField, companyNameTextField) = createRefs()


//            val text = remember { mutableStateOf(TextFieldValue("TEXT")) }
//            val focusManager = LocalFocusManager.current
            MyTextField(
                hideKeyboard = hideKeyboard,
                onFocusClear = { hideKeyboard = false },
                modifier = Modifier.constrainAs(userNameTextField) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            MyTextField(
                hideKeyboard = hideKeyboard,
                onFocusClear = { hideKeyboard = false },
                modifier = Modifier.constrainAs(clientIDTextField) {
                    top.linkTo(userNameTextField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

//            OutlinedTextField(
//                value = text.value,
//                onValueChange = { text.value = it },
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//                keyboardActions = KeyboardActions(onSearch = {
//                    focusManager.clearFocus()
//                }),
//                maxLines = 1,
//                singleLine = true,
//                label = { Text(text = "UserName") },
//                modifier = Modifier
//                    .constrainAs(userNameTextField) {
//                        top.linkTo(parent.top)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                    }
//                    .onFocusChanged {
//                        if (!it.isFocused) {
//                            Log.e(TAG, "not focus")
//                        }
//                    }
//            )

        }
        //Constraint end
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

    @Composable
    fun MyTextField(
        hideKeyboard: Boolean,
        onFocusClear: () -> Unit = {},
        @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
    ) {
        val text = remember { mutableStateOf(TextFieldValue("TEXT")) }
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = text.value,
            onValueChange = { text.value = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
            }),
            maxLines = 1,
            singleLine = true,
            label = { Text(text = "UserName") },
            modifier = modifier
                .onFocusChanged {
                    if (!it.isFocused) {
                        Log.e(TAG, "not focus")
                    }
                }
        )
        if (hideKeyboard) {
            focusManager.clearFocus()
            onFocusClear()
        }

    }


}



