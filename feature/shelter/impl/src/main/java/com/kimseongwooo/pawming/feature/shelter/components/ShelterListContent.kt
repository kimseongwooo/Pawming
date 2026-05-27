package com.kimseongwooo.pawming.feature.shelter.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kimseongwooo.pawming.designsystem.component.ShelterCard
import com.kimseongwooo.pawming.designsystem.theme.PawmingColors
import com.kimseongwooo.pawming.model.Shelter
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun ShelterListContent(
    shelters: ImmutableList<Shelter>,
    totalCount: Int,
    onClickShelter: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                text = "보호센터 ${totalCount}곳",
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                color = PawmingColors.Neutral500
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
        items(shelters, key = { it.careRegNo }) { shelter ->
            ShelterCard(
                careNm = shelter.careNm,
                divisionNm = shelter.divisionNm.takeIf { it.isNotEmpty() },
                address = shelter.careAddr.takeIf { it.isNotEmpty() },
                tel = shelter.careTel.takeIf { it.isNotEmpty() },
                weekOprStime = shelter.weekOprStime.takeIf { it.isNotEmpty() },
                weekOprEtime = shelter.weekOprEtime.takeIf { it.isNotEmpty() },
                onClick = { onClickShelter(shelter.careRegNo) }
            )
        }
    }
}
