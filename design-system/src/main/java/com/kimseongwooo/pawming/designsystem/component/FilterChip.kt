package com.kimseongwooo.pawming.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimseongwooo.pawming.designsystem.theme.PawmingTheme

private val ChipShape = RoundedCornerShape(16.dp)
private val UnselectedBorder = Color(0xFFE0E0E0)
private val UnselectedLabel = Color(0xFF555555)

@Composable
fun PawmingFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = label,
                fontSize = 12.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        },
        modifier = modifier,
        shape = ChipShape,
        border = if (selected) null else FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = false,
            borderColor = UnselectedBorder,
            borderWidth = 1.5.dp
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = UnselectedLabel,
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun <T> PawmingFilterChipRow(
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    labelOf: (T) -> String = { it.toString() }
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        options.forEach { option ->
            PawmingFilterChip(
                label = labelOf(option),
                selected = option == selected,
                onClick = { onSelect(option) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PawmingFilterChipRowPreview() {
    val options = remember { listOf("전체", "개", "고양이", "기타") }
    var selected by rememberSaveable { mutableStateOf("전체") }
    PawmingTheme {
        PawmingFilterChipRow(
            options = options,
            selected = selected,
            onSelect = { selected = it }
        )
    }
}
