package com.kimseongwooo.pawming.feature.shelterdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimseongwooo.pawming.model.ShelterDetail

private val Gray50 = Color(0xFFF8F8F8)
private val Gray400 = Color(0xFFAAAAAA)
private val Gray900 = Color(0xFF1A1A1A)

@Composable
internal fun FacilityStatGrid(shelter: ShelterDetail) {
    val row1 = listOf(
        Triple("👩‍⚕️", "${shelter.vetPersonCnt}명", "수의사"),
        Triple("👨‍🔧", "${shelter.specsPersonCnt}명", "사양관리사"),
        Triple("🚐", "${shelter.transCarCnt}대", "운반차량")
    )
    val row2 = listOf(
        Triple("🏠", "${shelter.breedCnt}개", "사육실"),
        Triple("🔬", "${shelter.medicalCnt}개", "진료실"),
        Triple("🔒", "${shelter.quarantineCnt}개", "격리실")
    )

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        StatRow(row1)
        StatRow(row2)
    }
}

@Composable
private fun StatRow(stats: List<Triple<String, String, String>>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        stats.forEach { (icon, value, label) ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Gray50, RoundedCornerShape(10.dp))
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = icon, fontSize = 18.sp)
                Spacer(Modifier.height(2.dp))
                Text(
                    text = value,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Gray900
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = Gray400
                )
            }
        }
    }
}
