package com.kimseongwooo.pawming.feature.animaldetail

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kimseongwooo.pawming.designsystem.component.PawmingErrorContent
import com.kimseongwooo.pawming.designsystem.component.PawmingLoadingIndicator
import com.kimseongwooo.pawming.model.Animal
import java.util.Calendar
import java.util.concurrent.TimeUnit

@Composable
internal fun AnimalDetailScreen(
    uiState: AnimalDetailUiState,
    onIntent: (AnimalDetailIntent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    PawmingLoadingIndicator()
                }
                BackButton(onBack = onBack)
            }
            uiState.error != null -> {
                PawmingErrorContent(
                    message = uiState.error,
                    onRetry = { onIntent(AnimalDetailIntent.Retry) },
                    modifier = Modifier.fillMaxSize()
                )
                BackButton(onBack = onBack)
            }
            uiState.animal != null -> {
                AnimalDetailContent(
                    animal = uiState.animal,
                    isFavorite = uiState.isFavorite,
                    onToggleFavorite = { onIntent(AnimalDetailIntent.ToggleFavorite) },
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun AnimalDetailContent(
    animal: Animal,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onBack: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                ImageGallery(
                    images = animal.images,
                    processState = animal.processState,
                    onBack = onBack
                )
            }
            item { Spacer(Modifier.height(8.dp)) }
            item { NameSection(animal) }
            item { Spacer(Modifier.height(8.dp)) }
            item { NoticeSection(animal) }
            item { Spacer(Modifier.height(8.dp)) }
            item { BasicInfoSection(animal) }
            item { Spacer(Modifier.height(8.dp)) }
            item { HealthSection(animal) }
            if (animal.adptnTitle.isNotEmpty()) {
                item { Spacer(Modifier.height(8.dp)) }
                item { AdoptionSection(animal) }
            }
            item { Spacer(Modifier.height(8.dp)) }
            item { ShelterSection(animal) }
        }

        FavoriteBottomBar(
            isFavorite = isFavorite,
            onClick = onToggleFavorite,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun ImageGallery(
    images: List<String>,
    processState: String,
    onBack: () -> Unit
) {
    val pageCount = images.size.coerceAtLeast(1)
    val pagerState = rememberPagerState { pageCount }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(4f / 3f)
            .background(Color.Black)
    ) {
        if (images.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐾", fontSize = 48.sp)
            }
        } else {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = images[page],
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(Color(0x66000000), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "뒤로가기",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            val badgeColor = if (processState == "공고중") MaterialTheme.colorScheme.primary else Color(0xFF1D9E75)
            Box(
                modifier = Modifier
                    .background(badgeColor, RoundedCornerShape(16.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = processState,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(images.size) { index ->
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .width(if (index == pagerState.currentPage) 16.dp else 6.dp)
                            .background(
                                if (index == pagerState.currentPage) Color.White else Color.White.copy(alpha = 0.5f),
                                RoundedCornerShape(3.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun BackButton(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .padding(start = 16.dp, top = 10.dp)
            .size(36.dp)
            .background(Color(0x66000000), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = onBack, modifier = Modifier.size(36.dp)) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "뒤로가기",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun NameSection(animal: Animal) {
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
private fun NoticeSection(animal: Animal) {
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
private fun BasicInfoSection(animal: Animal) {
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
private fun HealthSection(animal: Animal) {
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
private fun AdoptionSection(animal: Animal) {
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
private fun ShelterSection(animal: Animal) {
    val context = LocalContext.current
    DetailSection(title = "🏥 보호소 정보") {
        InfoRow(label = "보호소명", value = animal.careNm)
        InfoRow(label = "관할기관", value = animal.orgNm)
        InfoRow(label = "주소", value = animal.careAddr)
        InfoRow(label = "대표자", value = animal.careOwnerNm, showDivider = false)
        if (animal.careTel.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                    .clickable {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${animal.careTel}"))
                        context.startActivity(intent)
                    }
                    .padding(12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "📞", fontSize = 16.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = animal.careTel,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun FavoriteBottomBar(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = if (isFavorite) Alignment.Center else Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = isFavorite,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Color.White.copy(alpha = 0.95f),
                        RoundedCornerShape(999.dp)
                    )
                    .clickable(onClick = onClick)
                    .padding(horizontal = 16.dp, vertical = 9.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = "♥", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    Text(
                        text = "즐겨찾기 해제",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !isFavorite,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Text(text = "♡", fontSize = 18.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "즐겨찾기에 추가",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 14.dp)
        )
        content()
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    showDivider: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value.ifEmpty { "-" },
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = valueColor,
            modifier = Modifier
                .weight(2f)
                .padding(start = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
    if (showDivider) {
        HorizontalDivider(
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(bottom = 10.dp)
        )
    }
}

@Composable
private fun TextBlock(
    label: String,
    text: String,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    borderColor: Color = Color.Transparent,
    showDivider: Boolean = true
) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, RoundedCornerShape(8.dp))
                .then(
                    if (borderColor != Color.Transparent)
                        Modifier.padding(1.dp).background(backgroundColor, RoundedCornerShape(8.dp))
                    else Modifier
                )
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = text,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )
        }
    }
    if (showDivider) {
        Spacer(Modifier.height(10.dp))
    }
}

private fun String.toDisplayDate(): String {
    if (length != 8) return this
    return "${substring(0, 4)}.${substring(4, 6)}.${substring(6, 8)}"
}

private fun String.toDDay(): String? {
    if (length != 8) return null
    return try {
        val endCal = Calendar.getInstance().apply {
            set(substring(0, 4).toInt(), substring(4, 6).toInt() - 1, substring(6, 8).toInt())
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val diffDays = TimeUnit.MILLISECONDS.toDays(endCal.timeInMillis - todayCal.timeInMillis)
        if (diffDays >= 0) "D-$diffDays" else "만료"
    } catch (e: Exception) {
        null
    }
}
