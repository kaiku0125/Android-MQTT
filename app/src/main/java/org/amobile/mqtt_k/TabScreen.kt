package org.amobile.mqtt_k

import android.content.res.Resources
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

enum class TabPage(val icon: ImageVector, val tabName : String) {
//    Resources.getSystem().getString(R.string.setting)


    SETTINGS(Icons.Default.Settings, "設定"),
    HOME(Icons.Default.Home, "主頁"),
    MESSAGES(Icons.Default.Email, "訊息"),

}

@Composable
fun TabHome(selectedTabIndex: Int, onSelectedTab: (TabPage) -> Unit) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = { TabIndicator(tabPosition = it, index = selectedTabIndex) }
    ) {
        TabPage.values().forEachIndexed { index, tabPage ->

            Tab(
                selected = index == selectedTabIndex,
                onClick = { onSelectedTab(tabPage) },
                text = { Text(text = tabPage.tabName) },
                icon = { Icon(imageVector = tabPage.icon, contentDescription = null) },
                selectedContentColor = Color.Blue,
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
            )
        }
    }
}


@Composable
fun TabIndicator(tabPosition: List<TabPosition>, index: Int) {
    val transition = updateTransition(targetState = index, label = "")
    val leftIndicator by transition.animateDp(label = "", transitionSpec = {
        spring(stiffness = Spring.StiffnessMedium)
    }) {
        tabPosition[it].left
    }

    val rightIndicator by transition.animateDp(label = "", transitionSpec = {
        spring(stiffness = Spring.StiffnessMedium)
    }) {
        tabPosition[it].right
    }


//    val width = tabPosition[index].width
//    var offsetX = tabPosition[index].left
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = leftIndicator)
            .width(rightIndicator - leftIndicator)
            .padding(4.dp)
            .fillMaxSize()
            .border(
                BorderStroke(2.dp, Color.Blue),
                RoundedCornerShape(4.dp)
            )
    )
}