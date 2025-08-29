package com.example.onenoteclone.data

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import android.os.Parcelable

@Parcelize
data class User(
    val id: String,
    val name: String,
    val initials: String,
    val avatarUrl: String? = null
) : Parcelable

@Parcelize
data class Notebook(
    val id: String,
    val title: String,
    val isLocked: Boolean = false,
    val isShared: Boolean = false,
    val groupCount: Int = 0,
    val lastModified: @RawValue Instant = Clock.System.now(),
    val thumbnailUrl: String? = null,
    val collaborators: List<User> = emptyList(),
    val isFavorite: Boolean = false,
    val section: String = "My Notebook"
) : Parcelable

@Parcelize
data class NotebookPage(
    val id: String,
    val title: String,
    val notebookId: String,
    val section: String,
    val breadcrumb: String,
    val previewText: String = "",
    val thumbnailUrl: String? = null,
    val lastModified: @RawValue Instant = Clock.System.now(),
    val pageNumber: Int = 1
) : Parcelable

@Parcelize
data class Suggestion(
    val id: String,
    val title: String,
    val subtitle: String,
    val referencesCount: Int,
    val thumbnailUrl: String? = null
) : Parcelable

enum class StickyNoteColor(val color: Long) {
    YELLOW(0xFFFFF59D),
    PURPLE(0xFFE1BEE7),
    GREEN(0xFFC8E6C9),
    BLUE(0xFFBBDEFB),
    PINK(0xFFF8BBD9)
}

@Parcelize
data class StickyNote(
    val id: String,
    val title: String,
    val content: String,
    val color: StickyNoteColor,
    val createdAt: @RawValue Instant = Clock.System.now(),
    val photoUrl: String? = null,
    val hasReminder: Boolean = false
) : Parcelable

@Parcelize
data class SearchResult(
    val id: String,
    val title: String,
    val content: String,
    val type: SearchResultType,
    val source: String,
    val lastModified: @RawValue Instant = Clock.System.now()
) : Parcelable

enum class SearchResultType {
    COPILOT_NOTEBOOK,
    NOTEBOOK_PAGE,
    STICKY_NOTE
}