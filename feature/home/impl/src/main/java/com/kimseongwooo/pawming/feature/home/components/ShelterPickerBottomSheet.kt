package com.kimseongwooo.pawming.feature.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimseongwooo.pawming.feature.home.HomeIntent
import com.kimseongwooo.pawming.feature.home.HomeUiState
import com.kimseongwooo.pawming.feature.home.ShelterPickerStep
import com.kimseongwooo.pawming.model.Shelter
import com.kimseongwooo.pawming.model.Sido
import com.kimseongwooo.pawming.model.Sigungu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShelterPickerBottomSheet(
    state: HomeUiState,
    onIntent: (HomeIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = sheetState,
        containerColor = Color.White,
        onDismissRequest = { onIntent(HomeIntent.HideShelterPicker) }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            PickerHeader(
                step = state.shelterPickerStep,
                selectedSidoNm = state.selectedSido?.orgdownNm,
                onBack = { onIntent(HomeIntent.ShelterPickerBack) }
            )

            when (state.shelterPickerStep) {
                ShelterPickerStep.SIDO -> SidoList(
                    items = state.sidoList,
                    isLoading = state.isLoadingPickerItems,
                    onSelect = { onIntent(HomeIntent.SelectSido(it)) }
                )
                ShelterPickerStep.SIGUNGU -> SigunguList(
                    items = state.sigunguList,
                    isLoading = state.isLoadingPickerItems,
                    onSelect = { onIntent(HomeIntent.SelectSigungu(it)) }
                )
                ShelterPickerStep.SHELTER -> ShelterList(
                    state = state,
                    onIntent = onIntent
                )
            }
        }
    }
}

@Composable
private fun PickerHeader(
    step: ShelterPickerStep,
    selectedSidoNm: String?,
    onBack: () -> Unit
) {
    val title = when (step) {
        ShelterPickerStep.SIDO -> "시도 선택"
        ShelterPickerStep.SIGUNGU -> "${selectedSidoNm ?: ""} › 시군구 선택"
        ShelterPickerStep.SHELTER -> "보호소 선택"
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 20.dp, top = 4.dp, bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (step != ShelterPickerStep.SIDO) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로",
                    tint = Color(0xFF444444)
                )
            }
        }
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A1A1A),
            modifier = if (step == ShelterPickerStep.SIDO) Modifier.padding(start = 16.dp) else Modifier
        )
    }
}

@Composable
private fun SidoList(
    items: List<Sido>,
    isLoading: Boolean,
    onSelect: (Sido) -> Unit
) {
    PickerContent(isLoading = isLoading, isEmpty = items.isEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(items.size) { idx ->
                val sido = items[idx]
                PickerItem(
                    label = sido.orgdownNm,
                    onClick = { onSelect(sido) }
                )
            }
        }
    }
}

@Composable
private fun SigunguList(
    items: List<Sigungu>,
    isLoading: Boolean,
    onSelect: (Sigungu) -> Unit
) {
    PickerContent(isLoading = isLoading, isEmpty = items.isEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(items.size) { idx ->
                val sigungu = items[idx]
                PickerItem(
                    label = sigungu.orgdownNm,
                    onClick = { onSelect(sigungu) }
                )
            }
        }
    }
}

@Composable
private fun ShelterList(
    state: HomeUiState,
    onIntent: (HomeIntent) -> Unit
) {
    OutlinedTextField(
        value = state.shelterPickerQuery,
        onValueChange = { onIntent(HomeIntent.UpdateShelterQuery(it)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("보호소명 검색") },
        singleLine = true,
        shape = RoundedCornerShape(10.dp)
    )
    PickerContent(
        isLoading = state.isLoadingPickerItems,
        isEmpty = state.filteredShelters.isEmpty()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(state.filteredShelters.size) { idx ->
                val shelter = state.filteredShelters[idx]
                PickerItem(
                    label = shelter.careNm,
                    onClick = { onIntent(HomeIntent.ViewShelterDetail(shelter.careRegNo)) }
                )
            }
        }
    }
}

@Composable
private fun PickerContent(
    isLoading: Boolean,
    isEmpty: Boolean,
    content: @Composable () -> Unit
) {
    when {
        isLoading -> Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        isEmpty -> Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("목록이 없습니다", color = Color(0xFFCCCCCC), fontSize = 13.sp)
        }
        else -> content()
    }
}

@Composable
private fun PickerItem(label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Text(text = "›", fontSize = 18.sp, color = Color(0xFFDDDDDD))
        }
        HorizontalDivider(color = Color(0xFFF5F5F5))
    }
}
