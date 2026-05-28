package com.kimseongwooo.pawming.feature.animaldetail.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimseongwooo.pawming.model.Animal

@Composable
internal fun NameSection(animal: Animal) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = animal.kindNm,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.5).sp
                )
                if (animal.kindFullNm.isNotEmpty()) {
                    Text(
                        text = animal.kindFullNm,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val (genderText, genderColor) = when (animal.sexCd) {
                    "M" -> "수컷 ♂" to Color(0xFF5B8DEF)
                    "F" -> "암컷 ♀" to Color(0xFFF06292)
                    else -> "미상" to MaterialTheme.colorScheme.onSurfaceVariant
                }
                Text(
                    text = genderText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = genderColor
                )
                val neuterText = when (animal.neuterYn) {
                    "Y" -> "완료"
                    "N" -> "미완료"
                    else -> "미상"
                }
                Text(
                    text = "중성화 $neuterText",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                animal.age.replace("(년생)", "년생"),
                animal.weight,
                animal.colorCd
            ).filter { it.isNotEmpty() }.forEach { label ->
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
internal fun NoticeSection(animal: Animal) {
    DetailSection(title = "📢 공고 정보") {
        InfoRow(label = "공고번호", value = animal.noticeNo)
        val period = buildString {
            if (animal.noticeSdt.isNotEmpty()) append(animal.noticeSdt.toDisplayDate())
            append(" ~ ")
            if (animal.noticeEdt.isNotEmpty()) append(animal.noticeEdt.toDisplayDate())
        }
        InfoRow(label = "공고기간", value = period)
        val dDay = animal.noticeEdt.toDDay()
        if (dDay != null) {
            InfoRow(
                label = "잔여일",
                value = dDay,
                valueColor = if (dDay == "만료") MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
                showDivider = false
            )
        }
    }
}

@Composable
internal fun BasicInfoSection(animal: Animal) {
    DetailSection(title = "📋 기본 정보") {
        InfoRow(label = "접수일", value = animal.happenDt.toDisplayDate())
        InfoRow(label = "발견장소", value = animal.happenPlace)
        InfoRow(label = "구조번호", value = animal.desertionNo)
        if (animal.rfidCd.isNotEmpty()) {
            InfoRow(label = "동물등록번호", value = animal.rfidCd, showDivider = false)
        }
    }
}

@Composable
internal fun HealthSection(animal: Animal) {
    DetailSection(title = "💊 건강 및 특징") {
        if (animal.specialMark.isNotEmpty()) {
            TextBlock(label = "특징", text = animal.specialMark)
            Spacer(Modifier.height(10.dp))
        }
        if (animal.sfeSoci.isNotEmpty()) {
            TextBlock(label = "사회성", text = animal.sfeSoci)
            Spacer(Modifier.height(10.dp))
        }
        if (animal.sfeHealth.isNotEmpty()) {
            TextBlock(label = "건강", text = animal.sfeHealth)
            Spacer(Modifier.height(10.dp))
        }
        InfoRow(label = "예방접종", value = animal.vaccinationChk)
        InfoRow(label = "건강검사", value = animal.healthChk, showDivider = false)
    }
}

@Composable
internal fun AdoptionSection(animal: Animal) {
    DetailSection(title = "🏠 입양 절차") {
        Text(
            text = animal.adptnTitle,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val period = "${animal.adptnSDate.toDisplayDate()} ~ ${animal.adptnEDate.toDisplayDate()}"
        InfoRow(label = "기간", value = period)
        if (animal.adptnConditionLimitTxt.isNotEmpty()) {
            InfoRow(label = "조건/제한", value = animal.adptnConditionLimitTxt)
        }
        if (animal.adptnTxt.isNotEmpty()) {
            TextBlock(
                label = "신청방법",
                text = animal.adptnTxt,
                backgroundColor = Color(0xFFFFF8F6),
                borderColor = Color(0xFFFFE5DE),
                showDivider = false
            )
        }
    }
}

@Composable
internal fun ShelterSection(
    animal: Animal,
    shelterLat: Double?,
    shelterLng: Double?,
    isShelterLoading: Boolean = false
) {
    val context = LocalContext.current
    DetailSection(title = "🏥 보호소 정보") {
        InfoRow(label = "보호소명", value = animal.careNm)
        InfoRow(label = "관할기관", value = animal.orgNm)
        InfoRow(label = "주소", value = animal.careAddr)
        InfoRow(label = "대표자", value = animal.careOwnerNm, showDivider = animal.careTel.isNotEmpty())
        if (animal.careTel.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "전화",
                    fontSize = 13.sp,
                    color = Color(0xFF999999),
                    modifier = Modifier.weight(1f)
                )
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${animal.careTel}"))
                            context.startActivity(intent)
                        }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = animal.careTel,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1A1A1A)
                    )
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
        val mapModifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(8.dp))
        when {
            shelterLat != null && shelterLng != null -> {
                Spacer(Modifier.height(12.dp))
                ShelterLocationMap(lat = shelterLat, lng = shelterLng, modifier = mapModifier)
            }
            isShelterLoading -> {
                Spacer(Modifier.height(12.dp))
                Box(modifier = mapModifier.background(Color(0xFFEEEEEE)))
            }
        }
    }
}
