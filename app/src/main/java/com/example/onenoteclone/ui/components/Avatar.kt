package com.example.onenoteclone.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.onenoteclone.data.User
import com.example.onenoteclone.ui.theme.OneNoteCloneTheme

/**
 * User avatar component
 */
@Composable
fun Avatar(
    user: User,
    size: Int = 32,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        if (user.avatarUrl != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.avatarUrl)
                    .build(),
                contentDescription = "${user.name} avatar",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = user.initials,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = (size / 3).sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color.White
            )
        }
    }
}

/**
 * Group of avatars with overlap and max count
 */
@Composable
fun AvatarGroup(
    users: List<User>,
    maxVisible: Int = 3,
    size: Int = 32,
    modifier: Modifier = Modifier
) {
    if (users.isEmpty()) return
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy((-8).dp)
    ) {
        val visibleUsers = users.take(maxVisible)
        val remainingCount = users.size - maxVisible
        
        visibleUsers.forEach { user ->
            Avatar(
                user = user,
                size = size,
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = CircleShape
                    )
                    .padding(1.dp)
            )
        }
        
        if (remainingCount > 0) {
            Box(
                modifier = Modifier
                    .size(size.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outline),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$remainingCount",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = (size / 3).sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AvatarPreview() {
    OneNoteCloneTheme {
        val sampleUsers = listOf(
            User("1", "John Doe", "JD"),
            User("2", "Sarah Wilson", "SW"),
            User("3", "Mike Chen", "MC"),
            User("4", "Emily Brown", "EB"),
            User("5", "Alex Johnson", "AJ")
        )
        
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Avatar(user = sampleUsers[0])
            
            AvatarGroup(users = sampleUsers.take(3))
            
            AvatarGroup(users = sampleUsers)
        }
    }
}