package com.kimseongwooo.pawming.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

internal val PawmingShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),   // Badge
    small      = RoundedCornerShape(8.dp),   // Button
    medium     = RoundedCornerShape(12.dp),  // Card
    large      = RoundedCornerShape(16.dp),  // Modal
    extraLarge = RoundedCornerShape(24.dp)   // Bottom Sheet
)
