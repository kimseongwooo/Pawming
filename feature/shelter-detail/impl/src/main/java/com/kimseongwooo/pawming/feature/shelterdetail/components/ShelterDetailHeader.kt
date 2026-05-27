package com.kimseongwooo.pawming.feature.shelterdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimseongwooo.pawming.designsystem.theme.PawmingColors
import com.kimseongwooo.pawming.model.ShelterDetail

private val Gray100 = Color(0xFFF5F5F5)
private val Gray200 = Color(0xFFF0F0F0)
private val Gray600 = Color(0xFF666666)
private val Gray900 = Color(0xFF1A1A1A)
private val OrangeLight = Color(0xFFFFF0EB)

@Composable
internal fun ShelterDetailHeader(
    shelter: ShelterDetail,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp, bottom = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Gray100)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "뒤로가기",
                modifier = Modifier.size(20.dp),
                tint = Gray900
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = shelter.careNm,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Gray900
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = shelter.orgNm,
            fontSize = 13.sp,
            color = Color(0xFF999999)
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (shelter.divisionNm.isNotEmpty()) {
                TagChip(text = shelter.divisionNm, isOrange = true)
            }
            if (shelter.saveTrgtAnimal.isNotEmpty()) {
                TagChip(text = shelter.saveTrgtAnimal, isOrange = false)
            }
        }
    }
    HorizontalDivider(color = Gray200, thickness = 1.dp)
}

@Composable
private fun TagChip(text: String, isOrange: Boolean) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = if (isOrange) PawmingColors.Primary500 else Gray600,
        modifier = Modifier
            .background(
                if (isOrange) OrangeLight else Gray100,
                RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 10.dp, vertical = 3.dp)
    )
}
