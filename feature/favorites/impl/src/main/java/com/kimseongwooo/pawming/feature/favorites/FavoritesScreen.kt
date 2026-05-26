package com.kimseongwooo.pawming.feature.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kimseongwooo.pawming.designsystem.component.PawmingLoadingIndicator
import com.kimseongwooo.pawming.model.FavoriteAnimal

@Composable
internal fun FavoritesScreen(
    uiState: FavoritesUiState,
    onIntent: (FavoritesIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        FavoritesHeader()
        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    PawmingLoadingIndicator()
                }
            }
            uiState.favorites.isEmpty() -> FavoritesEmptyState()
            else -> FavoritesList(favorites = uiState.favorites, onIntent = onIntent)
        }
    }
}

@Composable
private fun FavoritesHeader() {
    Row(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "즐겨찾기",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A1A1A),
            letterSpacing = (-0.5).sp
        )
        Text(text = " 🐾", fontSize = 20.sp)
    }
}

@Composable
private fun FavoritesEmptyState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "🐾", fontSize = 48.sp)
            Text(
                text = "저장된 동물이 없어요",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF888888)
            )
            Text(
                text = "홈에서 마음에 드는 동물을 저장해 보세요",
                fontSize = 13.sp,
                color = Color(0xFFAAAAAA)
            )
        }
    }
}

@Composable
private fun FavoritesList(
    favorites: List<FavoriteAnimal>,
    onIntent: (FavoritesIntent) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(items = favorites, key = { it.desertionNo }) { animal ->
            FavoriteAnimalItem(
                animal = animal,
                onClick = { onIntent(FavoritesIntent.ClickAnimal(animal.desertionNo)) },
                onRemove = { onIntent(FavoritesIntent.RemoveFavorite(animal.desertionNo)) }
            )
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

@Composable
private fun FavoriteAnimalItem(
    animal: FavoriteAnimal,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .aspectRatio(1f)
            ) {
                if (animal.imageUrl.isNotEmpty()) {
                    AsyncImage(
                        model = animal.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🐾", fontSize = 24.sp)
                    }
                }
                ProcessStateBadge(
                    state = animal.processState,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = animal.kindNm,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = animal.sexCd.toGenderIcon(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = animal.sexCd.toGenderColor()
                    )
                    Text(
                        text = animal.age.replace("(년생)", "년생"),
                        fontSize = 12.sp,
                        color = Color(0xFF888888)
                    )
                }
                Text(
                    text = "📍 ${animal.happenPlace.toShortRegion()}",
                    fontSize = 11.sp,
                    color = Color(0xFFAAAAAA),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onRemove, modifier = Modifier.size(36.dp)) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "즐겨찾기 해제",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ProcessStateBadge(state: String, modifier: Modifier = Modifier) {
    val bgColor = if (state == "공고중") MaterialTheme.colorScheme.primary else Color(0xFF1D9E75)
    Box(
        modifier = modifier
            .background(bgColor, RoundedCornerShape(20.dp))
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(
            text = state,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
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

private fun String.toShortRegion(): String = split(" ").take(2).joinToString(" ")
