package com.kimseongwooo.pawming.feature.shelterdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimseongwooo.pawming.model.ShelterDetail

private val Gray100 = Color(0xFFF5F5F5)
private val Gray200 = Color(0xFFF0F0F0)

@Composable
internal fun OperatingHoursTable(shelter: ShelterDetail) {
    val headers = listOf("구분", "운영시간", "분양시간")
    val rows = listOf(
        listOf(
            "평일",
            formatTimeRange(shelter.weekOprStime, shelter.weekOprEtime),
            formatTimeRange(shelter.weekCellStime, shelter.weekCellEtime)
        ),
        listOf(
            "주말",
            formatTimeRange(shelter.weekendOprStime, shelter.weekendOprEtime),
            formatTimeRange(shelter.weekendCellStime, shelter.weekendCellEtime)
        )
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Gray100)
        ) {
            headers.forEach { header ->
                Text(
                    text = header,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF888888),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp, horizontal = 6.dp)
                )
            }
        }
        rows.forEachIndexed { index, row ->
            HorizontalDivider(color = Gray200, thickness = 1.dp)
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEachIndexed { colIdx, cell ->
                    Text(
                        text = cell,
                        fontSize = 12.sp,
                        fontWeight = if (colIdx == 0) FontWeight.Bold else FontWeight.Normal,
                        color = if (colIdx == 0) Color(0xFF555555) else Color(0xFF333333),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp, horizontal = 6.dp)
                    )
                }
            }
            if (index < rows.lastIndex) {
                HorizontalDivider(color = Gray200, thickness = 1.dp)
            }
        }
        Spacer(Modifier.height(12.dp))
        InfoRow(label = "휴무일", value = shelter.closeDay, showDivider = false)
    }
}

private fun formatTimeRange(start: String, end: String): String {
    if (start.isEmpty() && end.isEmpty()) return "-"
    if (start == "없음" || end == "없음") return "휴무"
    val s = formatTime(start)
    val e = formatTime(end)
    return if (s.isEmpty() || e.isEmpty()) (s + e).ifEmpty { "-" } else "$s~$e"
}

private fun formatTime(raw: String): String {
    if (raw.length != 4) return raw
    return "${raw.substring(0, 2)}:${raw.substring(2, 4)}"
}
