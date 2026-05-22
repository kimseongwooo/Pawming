package com.kimseongwooo.pawming.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kimseongwooo.pawming.designsystem.theme.PawmingTheme

internal val InactiveTabColor = Color(0xFFC0C0C0)

@Composable
fun PawmingHomeIcon(active: Boolean, modifier: Modifier = Modifier) {
    val color = if (active) MaterialTheme.colorScheme.primary else InactiveTabColor
    val vector = remember(color, active) { buildHomeVector(color, active) }
    Image(imageVector = vector, contentDescription = null, modifier = modifier)
}

@Composable
fun PawmingHeartIcon(active: Boolean, modifier: Modifier = Modifier) {
    val color = if (active) MaterialTheme.colorScheme.primary else InactiveTabColor
    val vector = remember(color, active) { buildHeartVector(color, active) }
    Image(imageVector = vector, contentDescription = null, modifier = modifier)
}

@Composable
fun PawmingShelterIcon(active: Boolean, modifier: Modifier = Modifier) {
    val color = if (active) MaterialTheme.colorScheme.primary else InactiveTabColor
    val vector = remember(color, active) { buildShelterVector(color, active) }
    Image(imageVector = vector, contentDescription = null, modifier = modifier)
}

private fun buildHomeVector(color: Color, active: Boolean): ImageVector {
    val stroke = SolidColor(color)
    val fill = if (active) SolidColor(color.copy(alpha = 0.13f)) else null
    return ImageVector.Builder(
        name = "PawmingHome",
        defaultWidth = 22.dp,
        defaultHeight = 22.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = fill,
            stroke = stroke,
            strokeLineWidth = 2f,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(3f, 9.5f)
            lineTo(12f, 3f)
            lineToRelative(9f, 6.5f)
            verticalLineTo(20f)
            arcToRelative(1f, 1f, 0f, false, true, -1f, 1f)
            horizontalLineTo(5f)
            arcToRelative(1f, 1f, 0f, false, true, -1f, -1f)
            verticalLineTo(9.5f)
            close()
        }
        path(
            stroke = stroke,
            strokeLineWidth = 2f,
            strokeLineCap = StrokeCap.Round
        ) {
            moveTo(9f, 21f)
            verticalLineTo(12f)
            horizontalLineToRelative(6f)
            verticalLineToRelative(9f)
        }
    }.build()
}

private fun buildHeartVector(color: Color, active: Boolean): ImageVector {
    val stroke = SolidColor(color)
    val fill = if (active) SolidColor(color.copy(alpha = 0.2f)) else null
    return ImageVector.Builder(
        name = "PawmingHeart",
        defaultWidth = 22.dp,
        defaultHeight = 22.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = fill,
            stroke = stroke,
            strokeLineWidth = 2f
        ) {
            moveTo(12f, 21f)
            curveTo(12f, 21f, 3f, 14.5f, 3f, 8.5f)
            curveTo(3f, 5.42f, 5.42f, 3f, 8.5f, 3f)
            curveTo(10.24f, 3f, 11.91f, 3.81f, 13f, 5.08f)
            curveTo(14.09f, 3.81f, 15.76f, 3f, 17.5f, 3f)
            curveTo(20.58f, 3f, 23f, 5.42f, 23f, 8.5f)
            curveTo(23f, 14.5f, 14f, 21f, 12f, 21f)
            close()
        }
    }.build()
}

private fun buildShelterVector(color: Color, active: Boolean): ImageVector {
    val stroke = SolidColor(color)
    val outerFill = if (active) SolidColor(color.copy(alpha = 0.13f)) else null
    val windowFill = if (active) SolidColor(color.copy(alpha = 0.2f)) else null
    return ImageVector.Builder(
        name = "PawmingShelter",
        defaultWidth = 22.dp,
        defaultHeight = 22.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = outerFill,
            stroke = stroke,
            strokeLineWidth = 2f,
            strokeLineJoin = StrokeJoin.Round
        ) {
            moveTo(12f, 2f)
            lineTo(2f, 7f)
            verticalLineToRelative(15f)
            horizontalLineToRelative(20f)
            verticalLineTo(7f)
            lineTo(12f, 2f)
            close()
        }
        path(
            stroke = stroke,
            strokeLineWidth = 2f
        ) {
            moveTo(10f, 13f)
            horizontalLineTo(14f)
            arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
            verticalLineTo(22f)
            horizontalLineTo(9f)
            verticalLineTo(14f)
            arcToRelative(1f, 1f, 0f, false, true, 1f, -1f)
            close()
        }
        path(
            fill = windowFill,
            stroke = stroke,
            strokeLineWidth = 1.5f
        ) {
            moveTo(9f, 9f)
            horizontalLineToRelative(6f)
            verticalLineToRelative(3f)
            horizontalLineTo(9f)
            close()
        }
    }.build()
}

@Preview(showBackground = true)
@Composable
private fun NavIconsPreview() {
    PawmingTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            PawmingHomeIcon(active = true)
            PawmingHomeIcon(active = false)
            PawmingHeartIcon(active = true)
            PawmingHeartIcon(active = false)
            PawmingShelterIcon(active = true)
            PawmingShelterIcon(active = false)
        }
    }
}
