package com.kimseongwooo.pawming.feature.shelter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kimseongwooo.pawming.designsystem.component.ShelterCard
import com.kimseongwooo.pawming.designsystem.theme.PawmingColors
import com.kimseongwooo.pawming.model.Shelter
import com.kimseongwooo.pawming.model.Sido
import com.kimseongwooo.pawming.model.Sigungu
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ShelterScreen(
    uiState: ShelterUiState,
    onIntent: (ShelterIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        ShelterHeader(
            sidoList = uiState.sidoList,
            sigunguList = uiState.sigunguList,
            selectedSido = uiState.selectedSido,
            selectedSigungu = uiState.selectedSigungu,
            searchQuery = uiState.searchQuery,
            onSelectSido = { onIntent(ShelterIntent.SelectSido(it)) },
            onSelectSigungu = { onIntent(ShelterIntent.SelectSigungu(it)) },
            onSearchQueryChange = { onIntent(ShelterIntent.UpdateSearchQuery(it)) }
        )

        when {
            uiState.selectedSigungu == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "시/도와 시/군/구를 선택하면\n보호센터 목록을 볼 수 있어요",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PawmingColors.Neutral400,
                        textAlign = TextAlign.Center
                    )
                }
            }
            uiState.isShelterLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            uiState.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = uiState.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = PawmingColors.Neutral500
                    )
                }
            }
            else -> {
                val filtered = remember(uiState.shelters, uiState.searchQuery) {
                    if (uiState.searchQuery.isBlank()) uiState.shelters
                    else uiState.shelters.filter { shelter ->
                        shelter.careNm.contains(uiState.searchQuery, ignoreCase = true) ||
                            shelter.careAddr.contains(uiState.searchQuery, ignoreCase = true)
                    }
                }
                ShelterList(
                    shelters = filtered,
                    totalCount = uiState.shelters.size,
                    onClickShelter = { careRegNo -> onIntent(ShelterIntent.ClickShelter(careRegNo)) }
                )
            }
        }
    }
}

@Composable
private fun ShelterHeader(
    sidoList: ImmutableList<Sido>,
    sigunguList: ImmutableList<Sigungu>,
    selectedSido: Sido?,
    selectedSigungu: Sigungu?,
    searchQuery: String,
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
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "보호센터",
            style = MaterialTheme.typography.headlineMedium,
            color = PawmingColors.Neutral900
        )

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
                .background(MaterialTheme.colorScheme.surface)
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
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = optionLabel(option),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = PawmingColors.Neutral900,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (index < options.lastIndex) {
                    HorizontalDivider(
                        color = PawmingColors.Neutral100,
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

@Composable
private fun ShelterList(
    shelters: List<Shelter>,
    totalCount: Int,
    onClickShelter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "보호센터 ${totalCount}곳",
                style = MaterialTheme.typography.bodySmall,
                color = PawmingColors.Neutral500
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
        items(shelters, key = { it.careRegNo }) { shelter ->
            ShelterCard(
                careNm = shelter.careNm,
                divisionNm = shelter.divisionNm.takeIf { it.isNotEmpty() },
                address = shelter.careAddr.takeIf { it.isNotEmpty() },
                tel = shelter.careTel.takeIf { it.isNotEmpty() },
                weekOprStime = shelter.weekOprStime.takeIf { it.isNotEmpty() },
                weekOprEtime = shelter.weekOprEtime.takeIf { it.isNotEmpty() },
                onClick = { onClickShelter(shelter.careRegNo) }
            )
        }
    }
}
