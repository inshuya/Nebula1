package com.example.onenoteclone.repository

import com.example.onenoteclone.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.days

class FakeRepository {
    
    // Sample Users
    private val sampleUsers = listOf(
        User("1", "John Doe", "JD"),
        User("2", "Sarah Wilson", "SW"),
        User("3", "Mike Chen", "MC"),
        User("4", "Emily Brown", "EB"),
        User("5", "Alex Johnson", "AJ")
    )
    
    // Sample Suggestions
    private val _suggestions = MutableStateFlow(
        listOf(
            Suggestion(
                "1",
                "Craft a Sales Proposal tailored for VanArsdel",
                "Get started on your new Notebook based on your recent focus areas",
                3
            ),
            Suggestion(
                "2", 
                "Plan Summer 2025 OPGD Morale Activities",
                "Organize team building and engagement activities",
                2
            ),
            Suggestion(
                "3",
                "Create Q4 Marketing Strategy",
                "Develop comprehensive marketing approach",
                5
            ),
            Suggestion(
                "4",
                "Design Mobile App User Experience",
                "Plan and prototype app user flows",
                4
            )
        )
    )
    
    // Sample Notebooks
    private val _notebooks = MutableStateFlow(
        listOf(
            Notebook(
                "1",
                "Mobile app growth",
                isLocked = true,
                lastModified = Clock.System.now()
            ),
            Notebook(
                "2",
                "Marketing Launch",
                isShared = true,
                groupCount = 4,
                lastModified = Clock.System.now() - 4.hours,
                collaborators = sampleUsers.take(3)
            ),
            Notebook(
                "3",
                "Team Offsite",
                isLocked = true,
                lastModified = Clock.System.now() - 1.days
            ),
            Notebook(
                "4",
                "Product Launch",
                isShared = true,
                groupCount = 2,
                lastModified = Clock.System.now() - 1.days
            ),
            Notebook(
                "5",
                "Design System",
                isFavorite = true,
                lastModified = Clock.System.now() - 2.days
            ),
            Notebook(
                "6",
                "User Research",
                isShared = true,
                groupCount = 3,
                lastModified = Clock.System.now() - 3.days,
                collaborators = sampleUsers.drop(2)
            )
        )
    )
    
    // Sample Recent Pages
    private val _recentPages = MutableStateFlow(
        listOf(
            NotebookPage(
                "1",
                "The Solar System formed 4.6 billion...",
                "school-1",
                "School",
                "My Notebook » School",
                lastModified = Clock.System.now()
            ),
            NotebookPage(
                "2",
                "Baking Essentials: Sugar, Flour, eggs, Whole milk...",
                "recipes-1",
                "Recipes",
                "My Notebook » Recipes",
                lastModified = Clock.System.now() - 2.hours
            ),
            NotebookPage(
                "3",
                "Work Notebook",
                "work-1",
                "Work",
                "Work Notebook » Inv...",
                lastModified = Clock.System.now() - 5.hours
            ),
            NotebookPage(
                "4",
                "My Notebook",
                "school-2",
                "School",
                "My Notebook » School",
                lastModified = Clock.System.now() - 1.days
            ),
            NotebookPage(
                "5",
                "Mom's recipe: Blueberry muffins. 2 cups all-purpose fl...",
                "recipes-2",
                "Recipes",
                "My Notebook » Recipes",
                lastModified = Clock.System.now() - 1.days
            ),
            NotebookPage(
                "6",
                "Photosynthesis: They consist of countless star...",
                "school-3",
                "School",
                "My Notebook » School",
                lastModified = Clock.System.now() - 2.days
            ),
            NotebookPage(
                "7",
                "Astronomy Assignment: Rings of Saturn",
                "school-4",
                "School",
                "My Notebook » School",
                lastModified = Clock.System.now() - 2.days
            ),
            NotebookPage(
                "8",
                "The Solar System formed 4.6 billion...",
                "school-5",
                "School",
                "My Notebook » School",
                lastModified = Clock.System.now() - 3.days
            )
        )
    )
    
    // Sample Sticky Notes
    private val _stickyNotes = MutableStateFlow(
        listOf(
            StickyNote(
                "1",
                "Remember",
                "Remember to give the keys to Jason",
                StickyNoteColor.YELLOW,
                Clock.System.now() - 1.hours
            ),
            StickyNote(
                "2",
                "Grocery List",
                "Toothpaste, Harissa paste, Milk",
                StickyNoteColor.PURPLE,
                Clock.System.now() - 3.hours
            ),
            StickyNote(
                "3",
                "Meeting Notes",
                "Discuss Q4 objectives with team",
                StickyNoteColor.GREEN,
                Clock.System.now() - 1.days
            ),
            StickyNote(
                "4",
                "Vacation Plans",
                "Book flights for December trip",
                StickyNoteColor.BLUE,
                Clock.System.now() - 2.days,
                photoUrl = "sample_photo"
            ),
            StickyNote(
                "5",
                "Birthday Gift",
                "Buy gift for mom's birthday next week",
                StickyNoteColor.PINK,
                Clock.System.now() - 3.days
            ),
            StickyNote(
                "6",
                "Doctor Appointment",
                "Call to schedule annual checkup",
                StickyNoteColor.YELLOW,
                Clock.System.now() - 4.days,
                hasReminder = true
            )
        )
    )
    
    // Public flows
    val suggestions: Flow<List<Suggestion>> = _suggestions.asStateFlow()
    val notebooks: Flow<List<Notebook>> = _notebooks.asStateFlow()
    val recentPages: Flow<List<NotebookPage>> = _recentPages.asStateFlow()
    val stickyNotes: Flow<List<StickyNote>> = _stickyNotes.asStateFlow()
    
    suspend fun refreshSuggestions(): Boolean {
        delay(900) // Simulate network delay
        return true
    }
    
    suspend fun refreshNotebooks(): Boolean {
        delay(900)
        return true
    }
    
    suspend fun refreshRecentPages(): Boolean {
        delay(900)
        return true
    }
    
    suspend fun refreshStickyNotes(): Boolean {
        delay(900)
        return true
    }
    
    fun searchContent(query: String): Flow<List<SearchResult>> {
        val results = mutableListOf<SearchResult>()
        
        // Search in notebooks
        _notebooks.value.filter { 
            it.title.contains(query, ignoreCase = true) 
        }.forEach { notebook ->
            results.add(
                SearchResult(
                    notebook.id,
                    notebook.title,
                    "Notebook",
                    SearchResultType.COPILOT_NOTEBOOK,
                    "Copilot Notebooks",
                    notebook.lastModified
                )
            )
        }
        
        // Search in pages
        _recentPages.value.filter { 
            it.title.contains(query, ignoreCase = true) || 
            it.previewText.contains(query, ignoreCase = true) 
        }.forEach { page ->
            results.add(
                SearchResult(
                    page.id,
                    page.title,
                    page.previewText,
                    SearchResultType.NOTEBOOK_PAGE,
                    page.breadcrumb,
                    page.lastModified
                )
            )
        }
        
        // Search in sticky notes
        _stickyNotes.value.filter { 
            it.title.contains(query, ignoreCase = true) || 
            it.content.contains(query, ignoreCase = true) 
        }.forEach { note ->
            results.add(
                SearchResult(
                    note.id,
                    note.title,
                    note.content,
                    SearchResultType.STICKY_NOTE,
                    "Sticky Notes",
                    note.createdAt
                )
            )
        }
        
        return MutableStateFlow(results).asStateFlow()
    }
    
    companion object {
        @Volatile
        private var INSTANCE: FakeRepository? = null
        
        fun getInstance(): FakeRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FakeRepository().also { INSTANCE = it }
            }
        }
    }
}