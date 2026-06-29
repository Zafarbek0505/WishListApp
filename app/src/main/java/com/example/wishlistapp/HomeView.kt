package com.example.wishlistapp

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wishlistapp.data.Wish
import com.example.wishlistapp.data.WishPriority
import com.example.wishlistapp.ui.theme.Screen

private const val AllCategories = "All"

@Composable
fun HomeView(
    wishes: List<Wish>,
    onAddWish: () -> Unit,
    onOpenWish: (Long) -> Unit,
    currentRoute: String = Screen.HomeScreen.route,
    onNavigate: (String) -> Unit = {}
) {
    var query by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf(AllCategories) }
    val categories = remember(wishes) {
        listOf(AllCategories) + wishes.map { it.category }.distinct().sorted()
    }
    val filteredWishes = remember(wishes, query, selectedCategory) {
        val cleanQuery = query.trim()
        wishes.filter { wish ->
            val matchesQuery = cleanQuery.isBlank() ||
                wish.title.contains(cleanQuery, ignoreCase = true) ||
                wish.description.contains(cleanQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == AllCategories || wish.category == selectedCategory
            matchesQuery && matchesCategory
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddWish,
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor   = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add wish")
            }
        },
        bottomBar = { WishBottomBar(currentRoute = currentRoute, onNavigate = onNavigate) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 18.dp, top = 22.dp, end = 18.dp, bottom = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { HomeHeader(wishes = wishes) }
            item {
                SearchField(query = query, onQueryChange = { query = it })
            }
            item {
                CategoryFilters(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Wishlist",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "${filteredWishes.size} items",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (filteredWishes.isEmpty()) {
                item {
                    EmptyWishState(
                        hasSearchQuery = query.isNotBlank() || selectedCategory != AllCategories,
                        onAddWish = onAddWish
                    )
                }
            } else {
                items(items = filteredWishes, key = { it.id }) { wish ->
                    WishItem(wish = wish, onClick = { onOpenWish(wish.id) })
                }
            }

            item { Spacer(modifier = Modifier.height(74.dp)) }
        }
    }
}

@Composable
private fun HomeHeader(wishes: List<Wish>) {
    val totalSaved  = wishes.sumOf { it.savedAmount }
    val totalTarget = wishes.sumOf { it.targetPrice }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = "WishList",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = if (totalTarget > 0)
                    "\$${totalSaved.toInt()} saved · \$${totalTarget.toInt()} total"
                else
                    "${wishes.size} wishes tracked",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                text = "${wishes.size}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(28.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        placeholder = { Text(text = "Search your wishes...") },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor   = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedBorderColor      = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor    = MaterialTheme.colorScheme.outlineVariant
        )
    )
}

@Composable
private fun CategoryFilters(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(categories) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick  = { onCategorySelected(category) },
                label    = { Text(text = category) },
                colors   = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor     = MaterialTheme.colorScheme.onPrimary,
                    containerColor         = MaterialTheme.colorScheme.surface
                )
            )
        }
    }
}

@Composable
fun WishItem(wish: Wish, onClick: () -> Unit) {
    val progress = if (wish.targetPrice > 0.0)
        (wish.savedAmount / wish.targetPrice).toFloat().coerceIn(0f, 1f)
    else 0f

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape  = RoundedCornerShape(16.dp),
        color  = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            WishImageTile(wish = wish, modifier = Modifier.size(86.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = wish.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    )
                    PriorityPill(priority = wish.priority)
                }

                Text(
                    text = wish.category,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                if (wish.description.isNotBlank()) {
                    Text(
                        text = wish.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "\$${wish.targetPrice.toInt()}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${(progress * 100).toInt()}% saved",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color      = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
fun WishImageTile(wish: Wish, modifier: Modifier = Modifier) {
    if (wish.imageUri.isNotBlank()) {
        AsyncImage(
            model = wish.imageUri,
            contentDescription = wish.title,
            modifier = modifier.clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )
    } else {
        val gradient = when (wish.imageSeed % 5) {
            1    -> listOf(Color(0xFF001A80), Color(0xFF0066FF))
            2    -> listOf(Color(0xFF004060), Color(0xFF00CCFF))
            3    -> listOf(Color(0xFF2D0066), Color(0xFF9933FF))
            4    -> listOf(Color(0xFF005533), Color(0xFF00CC96))
            else -> listOf(Color(0xFF660022), Color(0xFFFF3355))
        }
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.linearGradient(gradient)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Inventory2,
                contentDescription = null,
                modifier = Modifier.size(38.dp),
                tint = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun PriorityPill(priority: WishPriority) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = priorityColor(priority)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            text = priority.name,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun WishBottomBar(currentRoute: String, onNavigate: (String) -> Unit) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
        bottomItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick  = { if (currentRoute != item.route) onNavigate(item.route) },
                icon     = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label    = { Text(text = item.label) }
            )
        }
    }
}

private data class BottomItem(val label: String, val icon: ImageVector, val route: String)

private val bottomItems = listOf(
    BottomItem("Home",       Icons.Default.Home,              Screen.HomeScreen.route),
    BottomItem("Categories", Icons.Default.GridView,          Screen.CategoriesScreen.route),
    BottomItem("Reminders",  Icons.Default.NotificationsNone, Screen.RemindersScreen.route),
    BottomItem("Profile",    Icons.Default.PersonOutline,     Screen.ProfileScreen.route)
)

@Composable
private fun EmptyWishState(hasSearchQuery: Boolean, onAddWish: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
                text = if (hasSearchQuery) "Try another search or category."
                       else "Add something you would love to remember.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (!hasSearchQuery) {
                AssistChip(
                    onClick = onAddWish,
                    label   = { Text(text = "Create first wish") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                )
            }
        }
    }
}

fun priorityColor(priority: WishPriority): Color = when (priority) {
    WishPriority.Low    -> Color(0xFF00CC96)
    WishPriority.Medium -> Color(0xFFFFAA00)
    WishPriority.High   -> Color(0xFFFF3355)
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    HomeView(
        wishes     = com.example.wishlistapp.data.DummyWish.wishList,
        onAddWish  = {},
        onOpenWish = {}
    )
}
