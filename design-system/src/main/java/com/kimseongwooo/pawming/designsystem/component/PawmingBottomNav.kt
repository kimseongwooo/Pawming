package com.kimseongwooo.pawming.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimseongwooo.pawming.designsystem.theme.PawmingTheme

private val BottomNavContentHeight = 62.dp
private val BottomNavDividerColor = Color(0xFFF0F0F0)

/**
 * 화면 하단에 고정되는 탭바. iOS 디자인의 1px 상단 보더 + 반투명 흰색 배경을 안드로이드에
 * 맞게 [MaterialTheme.colorScheme.surface] 솔리드 + [HorizontalDivider]로 표현합니다.
 * 시스템 제스처 인셋은 [Modifier.navigationBarsPadding]으로 처리합니다.
 */
@Composable
fun PawmingBottomNav(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        Column {
            HorizontalDivider(thickness = 1.dp, color = BottomNavDividerColor)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .height(BottomNavContentHeight),
                content = content
            )
        }
    }
}

@Composable
fun RowScope.PawmingBottomNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    icon: @Composable (active: Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val labelColor = if (selected) MaterialTheme.colorScheme.primary else InactiveTabColor
    Column(
        modifier = modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(22.dp)) {
            icon(selected)
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = labelColor
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PawmingBottomNavPreview() {
    var selected by rememberSaveable { mutableStateOf("home") }
    PawmingTheme {
        PawmingBottomNav {
            PawmingBottomNavItem(
                selected = selected == "home",
                onClick = { selected = "home" },
                label = "홈",
                icon = { active -> PawmingHomeIcon(active = active) }
            )
            PawmingBottomNavItem(
                selected = selected == "favs",
                onClick = { selected = "favs" },
                label = "즐겨찾기",
                icon = { active -> PawmingHeartIcon(active = active) }
            )
            PawmingBottomNavItem(
                selected = selected == "shelters",
                onClick = { selected = "shelters" },
                label = "보호센터",
                icon = { active -> PawmingShelterIcon(active = active) }
            )
        }
    }
}
