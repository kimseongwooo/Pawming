package com.kimseongwooo.pawming.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimseongwooo.pawming.designsystem.theme.PawmingTheme

@Composable
fun PawmingEmptyContent(
    message: String,
    modifier: Modifier = Modifier,
    emoji: String = "🤍",
    description: String? = null,
    action: (@Composable () -> Unit)? = null
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
                text = emoji,
                fontSize = 56.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFAAAAAA),
                textAlign = TextAlign.Center
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFBBBBBB),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
            if (action != null) {
                Spacer(modifier = Modifier.height(24.dp))
                action()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PawmingEmptyContentPreview() {
    PawmingTheme {
        PawmingEmptyContent(
            message = "저장된 동물이 없습니다",
            description = "관심 있는 동물의 하트를 눌러\n즐겨찾기에 추가해보세요"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PawmingEmptyContentWithActionPreview() {
    PawmingTheme {
        PawmingEmptyContent(
            message = "검색 결과가 없습니다",
            description = "다른 검색어를 입력하거나\n필터를 변경해보세요",
            action = {
                Button(onClick = {}) {
                    Text("필터 초기화")
                }
            }
        )
    }
}
