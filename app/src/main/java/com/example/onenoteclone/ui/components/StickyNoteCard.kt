package com.example.onenoteclone.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.onenoteclone.data.StickyNoteColor
import com.example.onenoteclone.ui.theme.*

/**
 * Sticky note card component with color variants
 */
@Composable
fun StickyNoteCard(
    title: String,
    content: String,
    color: StickyNoteColor,
    dateText: String,
    photoUrl: String? = null,
    hasReminder: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (color) {
        StickyNoteColor.YELLOW -> StickyYellow
        StickyNoteColor.PURPLE -> StickyPurple
        StickyNoteColor.GREEN -> StickyGreen
        StickyNoteColor.BLUE -> StickyBlue
        StickyNoteColor.PINK -> StickyPink
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color.Black.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            if (photoUrl != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (photoUrl == "sample_photo") {
                        // Placeholder for photo
                        Icon(
                            imageVector = Icons.Default.Photo,
                            contentDescription = "Photo",
                            modifier = Modifier.size(32.dp),
                            tint = Color.Black.copy(alpha = 0.4f)
                        )
                    } else {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(photoUrl)
                                .build(),
                            contentDescription = "Note photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black.copy(alpha = 0.7f),
                maxLines = if (photoUrl != null) 3 else 6,
                overflow = TextOverflow.Ellipsis
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StickyNoteCardPreview() {
    OneNoteCloneTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StickyNoteCard(
                title = "Remember",
                content = "Remember to give the keys to Jason",
                color = StickyNoteColor.YELLOW,
                dateText = "08/26"
            )
            
            StickyNoteCard(
                title = "Grocery List",
                content = "Toothpaste, Harissa paste, Milk",
                color = StickyNoteColor.PURPLE,
                dateText = "08/25"
            )
            
            StickyNoteCard(
                title = "Vacation Plans",
                content = "Book flights for December trip",
                color = StickyNoteColor.BLUE,
                dateText = "08/23",
                photoUrl = "sample_photo"
            )
        }
    }
}