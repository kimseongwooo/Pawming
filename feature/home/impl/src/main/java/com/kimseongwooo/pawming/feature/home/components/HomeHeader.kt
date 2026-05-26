package com.kimseongwooo.pawming.feature.home.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.kimseongwooo.pawming.designsystem.component.PawmingFilterChip
import com.kimseongwooo.pawming.designsystem.theme.PawmingTheme
import com.kimseongwooo.pawming.feature.home.HomeIntent
import com.kimseongwooo.pawming.feature.home.HomeUiState

private val UpkindOptions = listOf(
    "417000" to "🐶 개",
    "422400" to "🐱 고양이",
    "429900" to "🐾 기타"
)
private val NeuterOptions = listOf("Y" to "완료", "N" to "미완료")
private val StateOptions = listOf("공고중" to "공고중", "보호중" to "보호중")

@Composable
internal fun HomeHeader(
    uiState: HomeUiState,
    onIntent: (HomeIntent) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Column {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pawming",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF1A1A1A),
                        letterSpacing = (-0.5).sp
                    )
                    Text(text = "🐾", fontSize = 20.sp)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(visible = uiState.hasFilter) {
                        TextButton(onClick = { onIntent(HomeIntent.ResetFilters) }) {
                            Text(
                                text = "초기화",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    FilterToggleButton(
                        isActive = uiState.isFilterOpen || uiState.hasFilter,
                        activeCount = uiState.activeFilterCount,
                        onClick = { onIntent(HomeIntent.ToggleFilter) }
                    )
                }
            }

            AnimatedVisibility(
                visible = uiState.isFilterOpen,
                enter = expandVertically(animationSpec = tween(300, easing = FastOutSlowInEasing)),
                exit = shrinkVertically(animationSpec = tween(300, easing = FastOutSlowInEasing))
            ) {
                HorizontalDivider(color = Color(0xFFF5F5F5))
                FilterPanel(uiState = uiState, onIntent = onIntent)
            }

            AnimatedVisibility(visible = uiState.filterCareRegNo.isNotEmpty() && !uiState.isFilterOpen) {
                Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    ActiveShelterPill(
                        careNm = uiState.filterCareNm,
                        onClear = { onIntent(HomeIntent.ClearShelter) }
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterToggleButton(
    isActive: Boolean,
    activeCount: Int,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        border = if (isActive) null else androidx.compose.foundation.BorderStroke(
            1.5.dp,
            Color(0xFFE0E0E0)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "≡",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isActive) Color.White else Color(0xFF666666)
            )
            Text(
                text = if (activeCount > 0) "필터 ($activeCount)" else "필터",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isActive) Color.White else Color(0xFF666666)
            )
        }
    }
}

// ── Previews ───────────────────────────────────────────────────────────────

@Preview(name = "기본 (필터 닫힘)", showBackground = true, backgroundColor = 0xFFF8F8F8)
@Composable
private fun HomeHeaderDefaultPreview() {
    PawmingTheme {
        HomeHeader(uiState = HomeUiState(), onIntent = {})
    }
}

@Preview(name = "필터 열림", showBackground = true, backgroundColor = 0xFFF8F8F8)
@Composable
private fun HomeHeaderFilterOpenPreview() {
    PawmingTheme {
        HomeHeader(
            uiState = HomeUiState(
                isFilterOpen = true,
                filterUpkind = "417000",
                filterNeuter = "Y"
            ),
            onIntent = {}
        )
    }
}

@Preview(name = "보호소 선택됨 (필터 닫힘)", showBackground = true, backgroundColor = 0xFFF8F8F8)
@Composable
private fun HomeHeaderShelterSelectedPreview() {
    PawmingTheme {
        HomeHeader(
            uiState = HomeUiState(
                filterCareRegNo = "111210000096",
                filterCareNm = "서울특별시동물복지지원센터"
            ),
            onIntent = {}
        )
    }
}

@Preview(name = "보호소 선택됨 + 필터 열림", showBackground = true, backgroundColor = 0xFFF8F8F8)
@Composable
private fun HomeHeaderShelterSelectedFilterOpenPreview() {
    PawmingTheme {
        HomeHeader(
            uiState = HomeUiState(
                isFilterOpen = true,
                filterCareRegNo = "111210000096",
                filterCareNm = "서울특별시동물복지지원센터",
                filterUpkind = "417000"
            ),
            onIntent = {}
        )
    }
}

@Composable
private fun FilterPanel(
    uiState: HomeUiState,
    onIntent: (HomeIntent) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
        FilterSectionLabel("보호소")
        if (uiState.filterCareRegNo.isNotEmpty()) {
            ActiveShelterPill(
                careNm = uiState.filterCareNm,
                onClear = { onIntent(HomeIntent.ClearShelter) }
            )
        } else {
            Surface(
                onClick = { onIntent(HomeIntent.ShowShelterPicker) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFF5F5F5)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "전체 보호소", fontSize = 13.sp, color = Color(0xFFAAAAAA))
                    Text(text = "›", fontSize = 18.sp, color = Color(0xFFCCCCCC))
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        FilterSectionLabel("축종")
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            UpkindOptions.forEach { (code, label) ->
                PawmingFilterChip(
                    label = label,
                    selected = uiState.filterUpkind == code,
                    onClick = { onIntent(HomeIntent.SelectUpkind(code)) }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                FilterSectionLabel("중성화")
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    NeuterOptions.forEach { (value, label) ->
                        PawmingFilterChip(
                            label = label,
                            selected = uiState.filterNeuter == value,
                            onClick = { onIntent(HomeIntent.SelectNeuter(value)) }
                        )
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                FilterSectionLabel("상태")
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StateOptions.forEach { (value, label) ->
                        PawmingFilterChip(
                            label = label,
                            selected = uiState.filterState == value,
                            onClick = { onIntent(HomeIntent.SelectState(value)) }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
private fun FilterSectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color(0xFFAAAAAA),
        letterSpacing = 0.5.sp,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun ActiveShelterPill(careNm: String, onClear: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFFFF0EB),
        border = BorderStroke(0.5.dp, Color(0xFFFFCAB8))
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, top = 5.dp, bottom = 5.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = careNm,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable(onClick = onClear)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null
                )
            }
        }
    }
}
