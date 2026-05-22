package com.kimseongwooo.pawming.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kimseongwooo.pawming.designsystem.theme.PawmingTheme

@Composable
fun PawmingErrorContent(
    message: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    retryLabel: String = "다시 시도",
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
            if (onRetry != null) {
                Spacer(modifier = Modifier.height(24.dp))
                PawmingOutlinedButton(text = retryLabel, onClick = onRetry)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PawmingErrorContentPreview() {
    PawmingTheme {
        PawmingErrorContent(
            message = "정보를 불러오지 못했어요",
            description = "네트워크 연결을 확인하고 다시 시도해주세요",
            onRetry = {}
        )
    }
}
