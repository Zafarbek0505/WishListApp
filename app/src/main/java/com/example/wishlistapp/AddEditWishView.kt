package com.example.wishlistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wishlistapp.data.Wish
import com.example.wishlistapp.data.WishPriority

@Composable
fun AddEditWishView(
    wish: Wish?,
    onBack: () -> Unit,
    onSave: (
        title: String,
        description: String,
        priority: WishPriority,
        category: String,
        targetPrice: Double,
        savedAmount: Double,
        dueDate: String,
        reminder: String
    ) -> Unit
) {
    var title by rememberSaveable(wish?.id) { mutableStateOf(wish?.title.orEmpty()) }
    var description by rememberSaveable(wish?.id) { mutableStateOf(wish?.description.orEmpty()) }
    var category by rememberSaveable(wish?.id) { mutableStateOf(wish?.category ?: "Fashion") }
    var targetPrice by rememberSaveable(wish?.id) {
        mutableStateOf(wish?.targetPrice?.takeIf { it > 0.0 }?.toString().orEmpty())
    }
    var savedAmount by rememberSaveable(wish?.id) {
        mutableStateOf(wish?.savedAmount?.takeIf { it > 0.0 }?.toString().orEmpty())
    }
    var dueDate by rememberSaveable(wish?.id) { mutableStateOf(wish?.dueDate.orEmpty()) }
    var reminder by rememberSaveable(wish?.id) { mutableStateOf(wish?.reminder ?: "1 week before") }
    var priorityName by rememberSaveable(wish?.id) {
        mutableStateOf((wish?.priority ?: WishPriority.Medium).name)
    }
    val selectedPriority = WishPriority.valueOf(priorityName)

    Scaffold(
        topBar = {
            AppBarView(
                title = if (wish == null) "Add New Wish" else "Edit Wish",
                onBackNavClickable = onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(PaddingValues(horizontal = 18.dp, vertical = 18.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            UploadImageCard()

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    WishTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = "Item Name",
                        placeholder = "e.g. Apple Watch Series 9",
                        singleLine = true
                    )
                    WishTextField(
                        value = description,
                        onValueChange = { if (it.length <= 200) description = it },
                        label = "Description",
                        placeholder = "Add a short description...",
                        minLines = 3,
                        supportingText = "${description.length}/200"
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        WishTextField(
                            value = targetPrice,
                            onValueChange = { targetPrice = it.filterPriceInput() },
                            label = "Target Price",
                            placeholder = "$ 0.00",
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        WishTextField(
                            value = category,
                            onValueChange = { category = it },
                            label = "Category",
                            placeholder = "Fashion",
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        WishTextField(
                            value = savedAmount,
                            onValueChange = { savedAmount = it.filterPriceInput() },
                            label = "Saved",
                            placeholder = "$ 0.00",
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        WishTextField(
                            value = dueDate,
                            onValueChange = { dueDate = it },
                            label = "Due Date",
                            placeholder = "Dec 25, 2026",
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            trailingIcon = {
                                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null)
                            }
                        )
                    }
                    WishTextField(
                        value = reminder,
                        onValueChange = { reminder = it },
                        label = "Reminder",
                        placeholder = "1 week before",
                        singleLine = true
                    )
                    PriorityPanel(
                        selectedPriority = selectedPriority,
                        onPrioritySelected = { priorityName = it.name }
                    )
                }
            }

            Button(
                onClick = {
                    onSave(
                        title,
                        description,
                        selectedPriority,
                        category,
                        targetPrice.toDoubleOrNull() ?: 0.0,
                        savedAmount.toDoubleOrNull() ?: 0.0,
                        dueDate,
                        reminder
                    )
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = if (wish == null) "Save Wish" else "Save Changes")
            }
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color.Transparent)
            ) {
                Text(text = "Cancel")
            }
        }
    }
}

@Composable
private fun UploadImageCard() {
    Surface(
        modifier = Modifier.size(164.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Image,
                contentDescription = null,
                modifier = Modifier.size(34.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Upload Image",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Tap to upload",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun WishTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = false,
    minLines: Int = 1,
    supportingText: String? = null,
    trailingIcon: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            minLines = minLines,
            placeholder = { Text(text = placeholder) },
            trailingIcon = trailingIcon,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
        if (supportingText != null) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = supportingText,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PriorityPanel(
    selectedPriority: WishPriority,
    onPrioritySelected: (WishPriority) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Priority",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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

private fun String.filterPriceInput(): String {
    return filterIndexed { index, char ->
        char.isDigit() || (char == '.' && indexOf('.') == index)
    }
}
