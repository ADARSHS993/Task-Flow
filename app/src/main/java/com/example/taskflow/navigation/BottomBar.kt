package com.example.taskflow.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomBar(
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit
){

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Tasks,
        BottomNavItem.Calendar,
        BottomNavItem.Stats,
        BottomNavItem.Profile
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 8.dp,
                    vertical = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            items.forEach { item ->

                val selected =
                    currentRoute == item.route

                val backgroundColor by animateColorAsState(
                    targetValue =
                        if (selected)
                            Color(0xFF4DD7F2)
                        else
                            Color.Transparent,
                    label = "navigationBackground"
                )

                val iconColor by animateColorAsState(
                    targetValue =
                        if (selected)
                            Color(0xFF17384A)
                        else
                            Color(0xFF555555),
                    label = "navigationIcon"
                )

                val scale by animateFloatAsState(
                    targetValue =
                        if (selected) 1.08f else 1f,
                    label = "navigationScale"
                )

                val horizontalPadding by animateDpAsState(
                    targetValue =
                        if (selected) 16.dp else 10.dp,
                    label = "navigationPadding"
                )

                Box(
                    modifier = Modifier
                        .scale(scale)
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(24.dp)
                        )
                        .clickable{
                            onItemClick(item)
                        }
                        .padding(
                            horizontal = horizontalPadding,
                            vertical = 8.dp
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = iconColor,
                            modifier = Modifier.size(21.dp)
                        )

                        AnimatedVisibility(
                            visible = selected,
                            enter =
                                fadeIn() + scaleIn(),
                            exit =
                                fadeOut() + scaleOut()
                        ) {

                            Row {

                                Spacer(
                                    modifier =
                                        Modifier.size(5.dp)
                                )

                                Text(
                                    text = item.title,
                                    color = iconColor,
                                    fontSize = 11.sp
                                )

                            }

                        }

                    }

                }

            }

        }

    }
}