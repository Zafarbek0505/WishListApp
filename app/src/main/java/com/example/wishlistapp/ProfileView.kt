package com.example.wishlistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wishlistapp.data.Wish
import com.example.wishlistapp.data.WishPriority
import com.example.wishlistapp.data.WishStatus

@Composable
fun ProfileView(
    wishes: List<Wish>,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    val totalTarget = wishes.sumOf { it.targetPrice }
    val totalSaved = wishes.sumOf { it.savedAmount }
    val overallProgress = if (totalTarget > 0) (totalSaved / totalTarget).toFloat().coerceIn(0f, 1f) else 0f
    val boughtCount = wishes.count { it.status == WishStatus.Bought }
    val inProgressCount = wishes.count { it.status == WishStatus.InProgress }
    val plannedCount = wishes.count { it.status == WishStatus.Planned }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { WishBottomBar(currentRoute = currentRoute, onNavigate = onNavigate) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 18.dp, top = 22.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            item { SavingsSummaryCard(totalTarget, totalSaved, overallProgress) }

            item {
                StatsCard(title = "By Status") {
                    StatusRow(label = WishStatus.Bought.label, count = boughtCount, color = priorityColor(WishPriority.Low))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    StatusRow(label = WishStatus.InProgress.label, count = inProgressCount, color = priorityColor(WishPriority.Medium))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    StatusRow(label = WishStatus.Planned.label, count = plannedCount, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            item {
                StatsCard(title = "By Priority") {
                    StatusRow(label = "High", count = wishes.count { it.priority == WishPriority.High }, color = priorityColor(WishPriority.High))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    StatusRow(label = "Medium", count = wishes.count { it.priority == WishPriority.Medium }, color = priorityColor(WishPriority.Medium))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    StatusRow(label = "Low", count = wishes.count { it.priority == WishPriority.Low }, color = priorityColor(WishPriority.Low))
                }
            }

            if (wishes.isEmpty()) {
                item { EmptyTabState(message = "Add wishes to see your stats here.") }
            }
        }
    }
}

@Composable
private fun SavingsSummaryCard(
    totalTarget: Double,
    totalSaved: Double,
    progress: Float
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Savings Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatValue(label = "Target", value = "$${"%.0f".format(totalTarget)}", onContainer = true)
                StatValue(label = "Saved", value = "$${"%.0f".format(totalSaved)}", onContainer = true)
                StatValue(label = "Progress", value = "${(progress * 100).toInt()}%", onContainer = true)
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.25f)
            )
        }
    }
}

@Composable
private fun StatsCard(title: String, content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            content()
        }
    }
}

@Composable
private fun StatusRow(label: String, count: Int, color: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = color
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                text = count.toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = androidx.compose.ui.graphics.Color.White
            )
        }
    }
}

@Composable
private fun StatValue(label: String, value: String, onContainer: Boolean) {
    val textColor = if (onContainer) MaterialTheme.colorScheme.onPrimaryContainer
    else MaterialTheme.colorScheme.onSurface

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = textColor.copy(alpha = 0.7f)
        )
    }
}
