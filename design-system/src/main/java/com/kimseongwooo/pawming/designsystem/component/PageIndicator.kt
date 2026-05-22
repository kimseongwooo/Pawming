package com.kimseongwooo.pawming.designsystem.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kimseongwooo.pawming.designsystem.theme.PawmingTheme

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = Color.White,
    inactiveColor: Color = Color.White.copy(alpha = 0.5f)
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        repeat(pageCount) { index ->
            val isActive = index == currentPage
            val width by animateDpAsState(
                targetValue = if (isActive) 16.dp else 6.dp,
                label = "indicator_width_$index"
            )
            Box(
                modifier = Modifier
                    .width(width)
                    .height(6.dp)
                    .background(
                        color = if (isActive) activeColor else inactiveColor,
                        shape = RoundedCornerShape(3.dp)
                    )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1A1A1A)
@Composable
private fun PageIndicatorPreview() {
    PawmingTheme {
        PageIndicator(pageCount = 5, currentPage = 2)
    }
}
