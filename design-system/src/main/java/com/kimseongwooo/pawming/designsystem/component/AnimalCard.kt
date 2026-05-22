package com.kimseongwooo.pawming.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimseongwooo.pawming.designsystem.theme.PawmingColors
import com.kimseongwooo.pawming.designsystem.theme.PawmingTheme

@Composable
fun AnimalCard(
    processState: String,
    kindNm: String,
    sexCd: String,
    age: String,
    happenPlace: String,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    thumbnail: @Composable BoxScope.() -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                thumbnail()
                ProcessStateBadge(
                    state = processState,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )
                FavoriteButton(
                    isFavorite = isFavorite,
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = kindNm,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF1A1A1A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sexCd.toGenderIcon(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = sexCd.toGenderColor()
                    )
                    Text(
                        text = age.replace("(년생)", "년생"),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF888888)
                    )
                }
                Text(
                    text = "📍 ${happenPlace.toShortRegion()}",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = Color(0xFFAAAAAA),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun ProcessStateBadge(state: String, modifier: Modifier = Modifier) {
    val bgColor = if (state == "공고중") MaterialTheme.colorScheme.primary else PawmingColors.Success
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = bgColor
    ) {
        Text(
            text = state,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = PawmingColors.White
        )
    }
}

@Composable
private fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isFavorite) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
    } else {
        Color.Black.copy(alpha = 0.3f)
    }
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(28.dp)
            .background(bgColor, CircleShape)
    ) {
        Text(text = "♥", fontSize = 14.sp, color = Color.White)
    }
}

private fun String.toGenderIcon(): String = when (this) {
    "M" -> "♂"
    "F" -> "♀"
    else -> "?"
}

private fun String.toGenderColor(): Color = when (this) {
    "M" -> Color(0xFF5B8DEF)
    "F" -> Color(0xFFF06292)
    else -> Color(0xFF999999)
}

private fun String.toShortRegion(): String =
    split(" ").take(2).joinToString(" ")

@Preview(showBackground = true)
@Composable
private fun AnimalCardPreview() {
    PawmingTheme {
        AnimalCard(
            processState = "공고중",
            kindNm = "[개] 골든 리트리버",
            sexCd = "M",
            age = "2023(년생)",
            happenPlace = "서울특별시 강남구 어딘가",
            isFavorite = false,
            onClick = {},
            onFavoriteClick = {}
        ) {
            Surface(
                modifier = Modifier.matchParentSize(),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {}
        }
    }
}
