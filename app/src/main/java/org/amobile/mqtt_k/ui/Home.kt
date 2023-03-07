package org.amobile.mqtt_k.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import org.amobile.mqtt_k.MQTTViewModel
import org.amobile.mqtt_k.MQTTViewModelFactory

import org.amobile.mqtt_k.prefs.Prefs

private const val TAG = "Home"

@Composable
fun HomeUI() {
    val ctx = LocalContext.current
    val viewModel : MQTTViewModel = viewModel(factory = MQTTViewModelFactory(ctx))
    val checkingState by viewModel.isMQTTRunning.observeAsState(false)
    val stateDescription by viewModel.mqttStatusDescription.observeAsState("...")
    var hideKeyboard by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {
            Log.e("TAG", "HomeUI: 7777")
            hideKeyboard = true
        }) {
        val (userNameTextField, clientIDTextField, companyNameTextField, columnState, toggleBtn) = createRefs()

        MyTextField(
            labelHint = "UserName",
            defText = Prefs.userName,
            hideKeyboard = hideKeyboard,
            onFocusClear = { hideKeyboard = false },
            mSaveListener = { mValue ->
//                Log.e("TAG", "saved : $mValue")
                Prefs.userName = mValue.trim()
                Prefs.save(ctx)
            },
            modifier = Modifier
                .constrainAs(userNameTextField) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 30.dp)
                .width(220.dp)
        )

        MyTextField(
            labelHint = "ClientID",
            defText = Prefs.clientID,
            hideKeyboard = hideKeyboard,
            onFocusClear = { hideKeyboard = false },
            mSaveListener = { mValue ->
                Prefs.clientID = mValue.trim()
                Prefs.save(ctx)
            },
            modifier = Modifier
                .constrainAs(clientIDTextField) {
                    top.linkTo(userNameTextField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 10.dp)
                .width(220.dp)
        )

        MyTextField(
            labelHint = "CompanyName",
            defText = Prefs.companyName,
            hideKeyboard = hideKeyboard,
            onFocusClear = { hideKeyboard = false },
            mSaveListener = { mValue ->
                Prefs.companyName = mValue.trim()
                Prefs.save(ctx)
            },
            modifier = Modifier
                .constrainAs(companyNameTextField) {
                    top.linkTo(clientIDTextField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(top = 10.dp)
                .width(220.dp)
        )

        Row(modifier = Modifier
            .constrainAs(columnState) {
                top.linkTo(companyNameTextField.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .padding(top = 10.dp)
            .width(220.dp)) {
            Text(text = "狀態 : ", fontStyle = Italic)
            Text(text = stateDescription, fontStyle = Italic)
        }


        MyConnectToggleButton(
            isConnected = checkingState,
            onClick = {
                viewModel.btnClick()

//                checkedState.value = !checkedState.value
//                Log.e(TAG, "HomeUI: ${checkedState.value}")
//                if(checkedState.value)
//                    presenter.doMQTTConnection()
//                else
//                    presenter.endConnection()
            },
            modifier = Modifier.constrainAs(toggleBtn) {
                top.linkTo(columnState.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

    }
    //Constraint end
}


@Composable
fun MyConnectToggleButton(
    isConnected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, if(isConnected) Color.Red else Color.Green),
//        colors = ButtonDefaults.buttonColors(backgroundColor = if (isConnected) Color.Red else Color.Green),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp,
            disabledElevation = 0.dp
        ),
        modifier = modifier
            .padding(top = 40.dp)
            .width(80.dp)
            .height(50.dp)
    ) {
        var mText = "連接"
        if (isConnected)
            mText = "停止"

        Text(text = mText, color = if (isConnected) Color.Red else Color.Green, fontWeight = Bold)
//        Text(text = mText, color = if (isConnected) Color.White else Color.Black)

    }
}


@Composable
fun MyTextField(
    labelHint: String,
    defText: String,
    hideKeyboard: Boolean,
    onFocusClear: () -> Unit = {},
    mSaveListener: (mValue: String) -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val mTextField = remember { mutableStateOf(TextFieldValue(defText)) }
    val focusManager = LocalFocusManager.current
    var isError by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = mTextField.value,
        onValueChange = { newValue ->
            if (newValue.text.matches("^[a-zA-Z0-9]*$".toRegex())) {
                mTextField.value = newValue
                isError = false
            } else
                isError = true

        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        maxLines = 1,
        isError = isError,
        singleLine = true,
        label = { Text(text = labelHint) },
        placeholder = { Text(text = "請輸入內容") },
        modifier = modifier
            .onFocusChanged {
                if (!it.isFocused && mTextField.value.text != defText)
                    mSaveListener(mTextField.value.text)

            }
    )
    if (hideKeyboard) {
        focusManager.clearFocus()
        onFocusClear()
    }

}