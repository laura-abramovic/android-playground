package com.abramoviclaura.playground.ui.screen.googleplayreview

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.abramoviclaura.playground.R
import com.abramoviclaura.playground.ui.theme.PlaygroundTheme
import com.google.android.play.core.review.ReviewManagerFactory

@Composable
fun GooglePlayReviewScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val manager = ReviewManagerFactory.create(context)
    val requestFlow = remember { mutableStateOf(false) }

    LaunchedEffect(requestFlow.value) {
        if (requestFlow.value) {
            val request = manager.requestReviewFlow()

            request.addOnSuccessListener {
                println("mojjj reivew info: $it")
                manager.launchReviewFlow(context.findActivity(), it)
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Button(
            onClick = { requestFlow.value = true },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = stringResource(id = R.string.trigger_a_google_play_review_button_label))
        }
    }
}

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Permissions should be called in the context of an Activity")
}

@Preview
@Composable
private fun GooglePlayReviewScreenPreview() = PlaygroundTheme {
    GooglePlayReviewScreen()
}
