package com.kimseongwooo.pawming.feature.shelterdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kimseongwooo.pawming.designsystem.theme.PawmingColors

private val Gray100 = Color(0xFFF5F5F5)
private val Gray900 = Color(0xFF1A1A1A)
private val OrangeLight = Color(0xFFFFF0EB)

@Composable
internal fun DetailSection(
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
            color = Gray900,
            modifier = Modifier.padding(bottom = 14.dp)
        )
        content()
    }
}

@Composable
internal fun InfoRow(
    label: String,
    value: String,
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
            color = Color(0xFF999999),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value.ifEmpty { "-" },
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Gray900,
            modifier = Modifier
                .weight(2f)
                .padding(start = 8.dp),
            textAlign = TextAlign.End
        )
    }
    if (showDivider) {
        HorizontalDivider(color = Gray100, thickness = 1.dp, modifier = Modifier.padding(bottom = 10.dp))
    }
}

@Composable
internal fun PhoneButton(
    tel: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(OrangeLight)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "📞", fontSize = 16.sp)
        Text(
            text = "  $tel",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = PawmingColors.Primary500
        )
    }
}
