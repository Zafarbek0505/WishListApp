package com.example.wishlistapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wishlistapp.data.Wish
import com.example.wishlistapp.data.WishPriority

private const val AllPriorities = "All"

@Composable
fun HomeView(
    wishes: List<Wish>,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onAddWish: () -> Unit,
    onEditWish: (Long) -> Unit,
    onDeleteWish: (Long) -> Unit
) {
    var query by rememberSaveable { mutableStateOf("") }
    var selectedFilter by rememberSaveable { mutableStateOf(AllPriorities) }
    val highPriorityCount = remember(wishes) {
        wishes.count { it.priority == WishPriority.High }
    }
    val filteredWishes = remember(wishes, query, selectedFilter) {
        val cleanQuery = query.trim()
        wishes.filter { wish ->
            val matchesQuery = cleanQuery.isBlank() ||
                wish.title.contains(cleanQuery, ignoreCase = true) ||
                wish.description.contains(cleanQuery, ignoreCase = true)
            val matchesPriority = selectedFilter == AllPriorities || wish.priority.name == selectedFilter
            matchesQuery && matchesPriority
        }
    }

    Scaffold(
        topBar = {
            AppBarView(
                title = "WishList",
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkMode) "Switch to day mode" else "Switch to night mode"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddWish,
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                text = { Text(text = "New wish") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                HomeDashboard(
                    totalWishes = wishes.size,
                    highPriorityCount = highPriorityCount
                )
            }

            item {
                SearchField(
                    query = query,
                    onQueryChange = { query = it }
                )
            }

            item {
                PriorityFilters(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it }
                )
            }

            item {
                Text(
                    text = "${filteredWishes.size} ${if (filteredWishes.size == 1) "wish" else "wishes"}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            if (filteredWishes.isEmpty()) {
                item {
                    EmptyWishState(
                        hasSearchQuery = query.isNotBlank() || selectedFilter != AllPriorities,
                        onAddWish = onAddWish
                    )
                }
            } else {
                items(
                    items = filteredWishes,
                    key = { it.id }
                ) { wish ->
                    WishItem(
                        wish = wish,
                        onClick = { onEditWish(wish.id) },
                        onDelete = { onDeleteWish(wish.id) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(78.dp))
            }
        }
    }
}

@Composable
private fun HomeDashboard(
    totalWishes: Int,
    highPriorityCount: Int
) {
    val progress = if (totalWishes == 0) 0f else highPriorityCount.toFloat() / totalWishes.toFloat()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ShoppingBag,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Plan what matters",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "$totalWishes saved, $highPriorityCount high priority",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.74f)
                    )
                }
            }

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = MaterialTheme.colorScheme.tertiary,
                trackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.55f)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                StatPill(label = "Total", value = totalWishes.toString())
                StatPill(label = "Focus", value = highPriorityCount.toString())
            }
        }
    }
}

@Composable
private fun StatPill(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.62f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        placeholder = { Text(text = "Search title or details") },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
private fun PriorityFilters(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf(AllPriorities, WishPriority.High.name, WishPriority.Medium.name, WishPriority.Low.name).forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = { Text(text = filter) }
            )
        }
    }
}

@Composable
fun WishItem(
    wish: Wish,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(priorityColor(wish.priority))
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = wish.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = wish.description.ifBlank { "No description yet" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                AssistChip(
                    onClick = onClick,
                    label = { Text(text = "${wish.priority.name} priority") }
                )
            }

            Column {
                IconButton(onClick = onClick) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit wish")
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete wish",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyWishState(
    hasSearchQuery: Boolean,
    onAddWish: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(42.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = if (hasSearchQuery) "No matching wishes" else "No wishes yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (hasSearchQuery) "Try another search or filter." else "Add something you would love to remember.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (!hasSearchQuery) {
                AssistChip(
                    onClick = onAddWish,
                    label = { Text(text = "Create first wish") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                )
            }
        }
    }
}

private fun priorityColor(priority: WishPriority): Color {
    return when (priority) {
        WishPriority.Low -> Color(0xFF16885F)
        WishPriority.Medium -> Color(0xFFE18A00)
        WishPriority.High -> Color(0xFFD84034)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    HomeView(
        wishes = listOf(
            Wish(
                id = 1L,
                title = "Apple Watch Series 11",
                description = "Track workouts, sleep, and health goals from the wrist.",
                priority = WishPriority.High
            )
        ),
        isDarkMode = false,
        onToggleTheme = {},
        onAddWish = {},
        onEditWish = {},
        onDeleteWish = {}
    )
}
