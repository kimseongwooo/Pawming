package com.kimseongwooo.pawming.feature.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimseongwooo.pawming.designsystem.theme.PawmingTheme
import com.kimseongwooo.pawming.feature.home.HomeIntent
import com.kimseongwooo.pawming.feature.home.HomeUiState
import com.kimseongwooo.pawming.model.ShelterDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ShelterDetailBottomSheet(
    uiState: HomeUiState,
    onIntent: (HomeIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = sheetState,
        containerColor = Color.White,
        onDismissRequest = { onIntent(HomeIntent.DismissShelterDetail) }
    ) {
        when {
            uiState.isLoadingShelterDetail -> {
                Box(
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
            }

            uiState.shelterDetail != null -> {
                ShelterDetailContent(
                    detail = uiState.shelterDetail,
                    onSelect = {
                        onIntent(
                            HomeIntent.SelectShelter(
                                uiState.shelterDetail.careRegNo,
                                uiState.shelterDetail.careNm
                            )
                        )
                        onIntent(HomeIntent.DismissShelterDetail)
                    }
                )
            }
        }
    }
}

@Composable
private fun ShelterDetailContent(
    detail: ShelterDetail,
    onSelect: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = detail.careNm,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A1A1A)
        )
        if (detail.orgNm.isNotEmpty()) {
            Text(
                text = detail.orgNm,
                fontSize = 13.sp,
                color = Color(0xFF888888),
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        Spacer(Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFFF5F5F5))
        Spacer(Modifier.height(16.dp))

        if (detail.careAddr.isNotEmpty()) {
            InfoRow(icon = "📍", label = "주소", value = detail.careAddr)
        }
        if (detail.careTel.isNotEmpty()) {
            InfoRow(icon = "📞", label = "전화", value = detail.careTel)
        }
        if (detail.saveTrgtAnimal.isNotEmpty()) {
            InfoRow(icon = "🐾", label = "보호 동물", value = detail.saveTrgtAnimal.replace("+", ", "))
        }

        val hasWeekHours = detail.weekOprStime.isNotEmpty() && detail.weekOprEtime.isNotEmpty()
        val hasWeekendHours =
            detail.weekendOprStime.isNotEmpty() && detail.weekendOprEtime.isNotEmpty()

        if (hasWeekHours || hasWeekendHours || detail.closeDay.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Text(
                text = "운영 시간",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFAAAAAA),
                letterSpacing = 0.5.sp
            )
            Spacer(Modifier.height(6.dp))
            if (hasWeekHours) {
                InfoRow(
                    icon = "🗓️",
                    label = "평일",
                    value = "${detail.weekOprStime.toTimeFormat()} ~ ${detail.weekOprEtime.toTimeFormat()}"
                )
            }
            if (hasWeekendHours) {
                InfoRow(
                    icon = "🗓️",
                    label = "주말",
                    value = "${detail.weekendOprStime.toTimeFormat()} ~ ${detail.weekendOprEtime.toTimeFormat()}"
                )
            }
            if (detail.closeDay.isNotEmpty()) {
                InfoRow(icon = "🔒", label = "휴무일", value = detail.closeDay)
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onSelect,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "이 보호소 선택하기",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
private fun InfoRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = icon, fontSize = 14.sp)
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF555555),
            modifier = Modifier.weight(0.25f)
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = Color(0xFF333333),
            modifier = Modifier.weight(0.75f)
        )
    }
}

private fun String.toTimeFormat(): String {
    val digits = filter { it.isDigit() }
    if (digits.length < 3) return this
    val padded = digits.padStart(4, '0')
    return "${padded.take(2)}:${padded.drop(2).take(2)}"
}

// ── Previews ───────────────────────────────────────────────────────────────

private val previewShelterDetail = ShelterDetail(
    careRegNo = "111210000096",
    careNm = "서울특별시동물복지지원센터",
    orgNm = "서울특별시",
    divisionNm = "공공",
    saveTrgtAnimal = "개+고양이",
    careAddr = "서울특별시 마포구 상암동 481",
    jibunAddr = "서울특별시 마포구 상암동 481",
    lat = 37.5665,
    lng = 126.9780,
    dsignationDate = "20150101",
    careTel = "02-1234-5678",
    dataStdDt = "20240101",
    weekOprStime = "1000",
    weekOprEtime = "1700",
    weekCellStime = "",
    weekCellEtime = "",
    weekendOprStime = "1000",
    weekendOprEtime = "1500",
    weekendCellStime = "",
    weekendCellEtime = "",
    closeDay = "법정공휴일",
    vetPersonCnt = 2,
    specsPersonCnt = 3,
    medicalCnt = 1,
    breedCnt = 0,
    quarantineCnt = 2,
    feedCnt = 5,
    transCarCnt = 1
)

@Preview(name = "보호소 상세 - 데이터", showBackground = true)
@Composable
private fun ShelterDetailContentPreview() {
    PawmingTheme {
        ShelterDetailContent(detail = previewShelterDetail, onSelect = {})
    }
}

@Preview(name = "보호소 상세 - 로딩", showBackground = true)
@Composable
private fun ShelterDetailLoadingPreview() {
    PawmingTheme {
        ShelterDetailBottomSheet(
            uiState = HomeUiState(isLoadingShelterDetail = true),
            onIntent = {}
        )
    }
}
