@file:OptIn(ExperimentalMaterial3Api::class)

package com.app.geofence.utils

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

var showPermissionAlertDialog = mutableStateOf(false)
var openDialog = mutableStateOf(false)

@Preview
@Composable
fun AlertView() {
    AlertDialog(
        onDismissRequest = {
            openDialog.value = false
        }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Geofence Detail",
                    color = Color.Black,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Entered Time: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("${convertMillisToTime(geofenceEnterTime)}")
                        }
                    },
                    color = Color.Black,
                    fontSize = 16.sp,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Exited Time: ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("${convertMillisToTime(geofenceExitTime)}")
                        }
                    },
                    color = Color.Black,
                    fontSize = 16.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("You stayed for ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("${covertTimeToText()} ")
                        }
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("in the geofence area")
                        }
                    },
                    color = Color.Black,
                    fontSize = 16.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { openDialog.value = false }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PermissionAlertView() {

    val startForResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = {
            showPermissionAlertDialog.value = false
        }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "Need Permissions",
                    color = Color.Black,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "This app needs permission to use this feature. You can grant them in app settings.",
                    color = Color.Black,
                    fontSize = 16.sp,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(0.5f)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .height(40.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = {
                                    showPermissionAlertDialog.value = false

                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri: Uri =
                                        Uri.fromParts("package", context.packageName, null)
                                    intent.data = uri
                                    startForResult.launch(intent)
                                }
                            )
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text = "Open settings",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .weight(0.5f)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(24.dp)
                            )
                            .height(40.dp)
                            .clickable(
                                interactionSource = MutableInteractionSource(),
                                indication = null,
                                onClick = {
                                    showPermissionAlertDialog.value = false
                                }
                            )
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text = "Dismiss",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                }
            }
        }
    }
}