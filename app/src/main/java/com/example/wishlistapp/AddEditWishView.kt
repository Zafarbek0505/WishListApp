package com.example.wishlistapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wishlistapp.data.Wish
import com.example.wishlistapp.data.WishPriority

@Composable
fun AddEditWishView(
    wish: Wish?,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onBack: () -> Unit,
    onSave: (title: String, description: String, priority: WishPriority) -> Unit
) {
    var title by rememberSaveable(wish?.id) { mutableStateOf(wish?.title.orEmpty()) }
    var description by rememberSaveable(wish?.id) { mutableStateOf(wish?.description.orEmpty()) }
    var priorityName by rememberSaveable(wish?.id) {
        mutableStateOf((wish?.priority ?: WishPriority.Medium).name)
    }
    val selectedPriority = WishPriority.valueOf(priorityName)
    val titleError = title.isBlank()

    Scaffold(
        topBar = {
            AppBarView(
                title = if (wish == null) "New wish" else "Edit wish",
                onBackNavClickable = onBack,
                actions = {
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isDarkMode) "Switch to day mode" else "Switch to night mode"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(PaddingValues(horizontal = 18.dp, vertical = 16.dp)),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EditorHeader(wish = wish)

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text(text = "Title") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Title, contentDescription = null)
                        },
                        isError = titleError,
                        supportingText = {
                            if (titleError) {
                                Text(text = "Title is required")
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 5,
                        label = { Text(text = "Details") },
                        leadingIcon = {
                            Icon(imageVector = Icons.AutoMirrored.Filled.Notes, contentDescription = null)
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }

            PriorityPanel(
                selectedPriority = selectedPriority,
                onPrioritySelected = { priorityName = it.name }
            )

            Button(
                onClick = {
                    onSave(title, description, selectedPriority)
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(vertical = 14.dp)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null)
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = if (wish == null) "Add wish" else "Save changes"
                )
            }
        }
    }
}

@Composable
private fun EditorHeader(wish: Wish?) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Flag,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (wish == null) "Capture a new idea" else "Refine this wish",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "Add enough detail to compare, remember, and decide later.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.72f)
                )
            }
        }
    }
}

@Composable
private fun PriorityPanel(
    selectedPriority: WishPriority,
    onPrioritySelected: (WishPriority) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(priorityColor(selectedPriority))
                )
                Text(
                    text = "Priority",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WishPriority.entries.forEach { priority ->
                    FilterChip(
                        selected = selectedPriority == priority,
                        onClick = { onPrioritySelected(priority) },
                        label = { Text(text = priority.name) },
                        leadingIcon = if (selectedPriority == priority) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
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
