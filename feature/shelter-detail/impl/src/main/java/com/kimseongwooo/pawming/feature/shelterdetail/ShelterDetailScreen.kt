package com.kimseongwooo.pawming.feature.shelterdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimseongwooo.pawming.designsystem.component.PawmingErrorContent
import com.kimseongwooo.pawming.designsystem.component.PawmingLoadingIndicator
import com.kimseongwooo.pawming.designsystem.theme.PawmingColors
import com.kimseongwooo.pawming.feature.shelterdetail.components.DetailSection
import com.kimseongwooo.pawming.feature.shelterdetail.components.FacilityStatGrid
import com.kimseongwooo.pawming.feature.shelterdetail.components.InfoRow
import com.kimseongwooo.pawming.feature.shelterdetail.components.OperatingHoursTable
import com.kimseongwooo.pawming.feature.shelterdetail.components.PhoneButton
import com.kimseongwooo.pawming.feature.shelterdetail.components.ShelterDetailHeader
import com.kimseongwooo.pawming.feature.shelterdetail.components.ShelterLocationMap
import com.kimseongwooo.pawming.model.ShelterDetail

private val PageBackground = Color(0xFFF7F7F7)

@Composable
internal fun ShelterDetailScreen(
    uiState: ShelterDetailUiState,
    onIntent: (ShelterDetailIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    PawmingLoadingIndicator()
                }
            }
            uiState.error != null -> {
                PawmingErrorContent(
                    message = uiState.error,
                    onRetry = { onIntent(ShelterDetailIntent.Retry) },
                    modifier = Modifier.fillMaxSize()
                )
            }
            uiState.shelterDetail != null -> {
                ShelterDetailContent(
                    shelter = uiState.shelterDetail,
                    onBack = { onIntent(ShelterDetailIntent.NavigateBack) },
                    onCallPhone = { onIntent(ShelterDetailIntent.CallPhone) },
                    onViewAnimals = { onIntent(ShelterDetailIntent.ViewAnimals) }
                )
            }
        }
    }
}

@Composable
private fun ShelterDetailContent(
    shelter: ShelterDetail,
    onBack: () -> Unit,
    onCallPhone: () -> Unit,
    onViewAnimals: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = (64 + 34).dp)
        ) {
            item {
                ShelterDetailHeader(shelter = shelter, onBack = onBack)
            }
            item { Spacer(Modifier.height(8.dp)) }

            // 기본 정보
            item {
                DetailSection(title = "📋 기본 정보") {
                    InfoRow(label = "보호소번호", value = shelter.careRegNo)
                    InfoRow(label = "센터유형", value = shelter.divisionNm)
                    InfoRow(label = "구조대상", value = shelter.saveTrgtAnimal, showDivider = false)
                    Spacer(Modifier.height(12.dp))
                    if (shelter.careTel.isNotEmpty()) {
                        PhoneButton(tel = shelter.careTel, onClick = onCallPhone)
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }

            // 위치
            item {
                DetailSection(title = "📍 위치") {
                    InfoRow(label = "도로명", value = shelter.careAddr)
                    InfoRow(label = "지번", value = shelter.jibunAddr, showDivider = false)
                    val lat = shelter.lat
                    val lng = shelter.lng
                    if (lat != null && lng != null) {
                        Spacer(Modifier.height(12.dp))
                        ShelterLocationMap(
                            lat = lat,
                            lng = lng,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }

            // 운영시간
            item {
                DetailSection(title = "🕐 운영시간") {
                    OperatingHoursTable(shelter)
                }
            }
            item { Spacer(Modifier.height(8.dp)) }

            // 시설 규모
            item {
                DetailSection(title = "🏗️ 시설 규모") {
                    FacilityStatGrid(shelter)
                }
            }
            item { Spacer(Modifier.height(8.dp)) }

        }

        // 보호 동물 보기 버튼
        ViewAnimalsButton(
            onClick = onViewAnimals,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ViewAnimalsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = PawmingColors.Primary500),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = "🐾  이 보호소의 동물 보기",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}
