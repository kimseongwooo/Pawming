package com.kimseongwooo.pawming.feature.shelter.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kimseongwooo.pawming.designsystem.theme.PawmingColors
import com.kimseongwooo.pawming.model.Sido
import com.kimseongwooo.pawming.model.Sigungu
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ShelterHeader(
    sidoList: ImmutableList<Sido>,
    sigunguList: ImmutableList<Sigungu>,
    selectedSido: Sido?,
    selectedSigungu: Sigungu?,
    searchQuery: String,
    isInitialLoading: Boolean,
    onSelectSido: (Sido) -> Unit,
    onSelectSigungu: (Sigungu) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // 시군구가 없는 시도(세종 등)는 드롭다운 자체를 숨김
    val showSigunguDropdown = sigunguList.isNotEmpty() || (selectedSido != null && selectedSigungu == null)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "보호센터",
            style = MaterialTheme.typography.headlineMedium,
            color = PawmingColors.Neutral900
        )

        if (!isInitialLoading) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RegionDropdown(
                    placeholder = "시/도",
                    selectedLabel = selectedSido?.orgdownNm,
                    options = sidoList,
                    optionLabel = { it.orgdownNm },
                    onSelect = onSelectSido,
                    modifier = Modifier.weight(1f)
                )
                if (showSigunguDropdown) {
                    RegionDropdown(
                        placeholder = "시/군/구",
                        selectedLabel = selectedSigungu?.orgdownNm,
                        options = sigunguList,
                        optionLabel = { it.orgdownNm },
                        onSelect = onSelectSigungu,
                        enabled = sigunguList.isNotEmpty(),
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        if (selectedSigungu != null) {
            ShelterSearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange
            )
        }
    }
}

@Composable
private fun <T> RegionDropdown(
    placeholder: String,
    selectedLabel: String?,
    options: ImmutableList<T>,
    optionLabel: (T) -> String,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    var triggerWidthPx by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val isSelected = selectedLabel != null
    val shape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier.onGloballyPositioned { triggerWidthPx = it.size.width }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(
                    if (isSelected) PawmingColors.Primary500.copy(alpha = 0.08f)
                    else PawmingColors.Neutral100
                )
                .then(
                    if (isSelected) Modifier.border(1.dp, PawmingColors.Primary500, shape)
                    else Modifier
                )
                .then(
                    if (enabled && options.isNotEmpty()) Modifier.clickable { expanded = true }
                    else Modifier
                )
                .padding(horizontal = 12.dp, vertical = 11.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedLabel ?: placeholder,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                ),
                color = when {
                    isSelected -> PawmingColors.Primary600
                    enabled -> PawmingColors.Neutral500
                    else -> PawmingColors.Neutral400
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (isSelected) PawmingColors.Primary500 else PawmingColors.Neutral400
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(density) { triggerWidthPx.toDp() })
                .background(PawmingColors.Neutral50)
                .heightIn(max = 320.dp)
        ) {
            options.forEachIndexed { index, option ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSelect(option)
                            expanded = false
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = optionLabel(option),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = PawmingColors.Neutral700,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (index < options.lastIndex) {
                    HorizontalDivider(
                        color = PawmingColors.Neutral200,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun ShelterSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(PawmingColors.Neutral100, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = PawmingColors.Neutral400
        )
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = "보호센터 이름 또는 주소로 검색",
                    style = MaterialTheme.typography.bodyMedium,
                    color = PawmingColors.Neutral400
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = PawmingColors.Neutral900
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                singleLine = true
            )
        }
    }
}
