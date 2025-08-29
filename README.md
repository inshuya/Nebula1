# OneNote Clone - Android App

A complete Android application built with Jetpack Compose and Material 3 that recreates the Microsoft OneNote mobile experience. This app demonstrates modern Android development practices with a focus on clean architecture and beautiful UI design.

## 🚀 Features

### Core Functionality
- **4 Main Tabs**: Copilot Notebooks, Notebooks, Sticky Notes, and Search
- **Bottom Navigation**: Material 3 styled navigation with proper state management
- **Pull-to-Refresh**: SwipeRefresh implementation on all list screens
- **Edge-to-Edge**: Full edge-to-edge display with proper insets handling
- **Dark/Light Theme**: Material 3 dynamic theming with custom color schemes

### Copilot Notebooks Tab
- Gradient top app bar with hamburger menu
- ✨ Suggested Notebooks carousel with gradient placeholder cards
- Filter pills: All, Favorites, Shared with you
- List/Grid view toggle
- Notebook list with icons, timestamps, collaborator avatars
- Lock and group status indicators

### Notebooks Tab
- Tab switcher: Recent pages vs Notebook list
- Recent Pages: 2-column grid of mini notebook cards with breadcrumbs
- Notebook List: Traditional list view with "New notebook" button
- Purple OneNote brand icons and date chips

### Sticky Notes Tab
- Masonry-style staggered grid (2 columns)
- Colorful sticky notes (Yellow, Purple, Green, Blue, Pink)
- Photo thumbnails on select notes
- Floating action pods (Camera, Edit, Checkbox)
- Realistic drop shadows and spacing

### Search Tab
- Large search field with voice search icon
- Recent searches as clickable chips
- Empty state illustration
- Search results grouped by type
- Query highlighting in results
- Debounced search with loading states

## 🏗️ Architecture

### MVVM Pattern
- **ViewModels**: Each screen has dedicated ViewModel with StateFlow
- **Repository**: Fake in-memory repository with sample data
- **State Management**: Reactive UI with proper error handling

### Navigation
- **Navigation Compose**: Single-activity architecture
- **Bottom Navigation**: State preservation across tab switches
- **Deep Linking**: Ready for URL-based navigation

### UI Components
Reusable components following Material 3 design:
- `TopAppBarLarge`: Gradient header with navigation
- `SuggestionCard`: Cards with gradient thumbnails
- `PillTabs`: Rounded segmented control
- `ViewToggle`: List/Grid toggle buttons
- `Avatar` & `AvatarGroup`: User avatars with overlap
- `NotebookListItem`: Complex list items with metadata
- `MiniNotebookCard`: Grid cards with breadcrumbs
- `StickyNoteCard`: Colorful note tiles
- `SearchField`: Search input with icons
- `TagChip` & `ShimmerPlaceholder`: Utility components

## 🎨 Design System

### Material 3 Theme
- **Primary Color**: Purple (#7B2CF5)
- **Typography**: Defined hierarchy (headlineLarge to labelSmall)
- **Shapes**: 24dp corner radius for "2xl" rounded feel
- **Colors**: Full light/dark theme with dynamic color support

### Custom Colors
- Sticky note colors: Yellow, Purple, Green, Blue, Pink
- Error handling colors
- Surface variants and outlines

## 📱 Screenshots Matching
The app closely recreates the provided reference screenshots:
- **CPNBHome_Android.png**: Copilot Notebooks layout
- **NotebooksHome_Android.png**: Notebooks grid and list views
- **SNHome_Android.png**: Sticky Notes masonry grid

## 🛠️ Technology Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose
- **Architecture**: MVVM + Repository Pattern
- **Navigation**: Navigation Compose
- **Dependency Injection**: Manual (easily replaceable with Hilt)
- **State Management**: StateFlow + Compose State
- **Image Loading**: Coil
- **Date/Time**: Kotlinx DateTime
- **System UI**: Accompanist System UI Controller
- **Layout**: Staggered Grid, LazyColumn, LazyRow

## 🔧 Setup & Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd OneNoteClone
   ```

2. **Open in Android Studio**:
   - Use Android Studio Hedgehog (2023.1.1) or newer
   - SDK: Compile SDK 34, Min SDK 26

3. **Build and Run**:
   ```bash
   ./gradlew assembleDebug
   ```

4. **Install on device**:
   ```bash
   ./gradlew installDebug
   ```

## 📦 Project Structure

```
app/src/main/java/com/example/onenoteclone/
├── MainActivity.kt                    # Entry point
├── data/                             # Data models
│   └── Models.kt
├── repository/                       # Data layer
│   └── FakeRepository.kt
├── viewmodel/                        # ViewModels
│   ├── CopilotNotebooksViewModel.kt
│   ├── NotebooksViewModel.kt
│   ├── StickyNotesViewModel.kt
│   └── SearchViewModel.kt
├── navigation/                       # Navigation setup
│   ├── Screen.kt
│   └── AppNavigation.kt
├── ui/
│   ├── theme/                       # Material 3 theme
│   │   ├── Color.kt
│   │   ├── Type.kt
│   │   ├── Shape.kt
│   │   └── Theme.kt
│   └── components/                  # Reusable UI components
│       ├── TopAppBarLarge.kt
│       ├── SuggestionCard.kt
│       ├── PillTabs.kt
│       ├── ViewToggle.kt
│       ├── Avatar.kt
│       ├── NotebookListItem.kt
│       ├── MiniNotebookCard.kt
│       ├── StickyNoteCard.kt
│       ├── SearchField.kt
│       └── CommonComponents.kt
├── feature/                         # Feature modules
│   ├── cpnb/
│   │   └── CopilotNotebooksScreen.kt
│   ├── notebooks/
│   │   └── NotebooksScreen.kt
│   ├── sticky/
│   │   └── StickyNotesScreen.kt
│   └── search/
│       └── SearchScreen.kt
└── utils/                          # Utilities
    └── TimeUtils.kt
```

## ✨ Key Features Implemented

### Accessibility
- Content descriptions for all interactive elements
- Semantic headers for screen readers
- Minimum touch target sizes (44dp)
- High contrast color schemes

### Performance
- Lazy loading for all lists and grids
- Efficient state management with StateFlow
- Image caching with Coil
- Debounced search to reduce API calls

### User Experience
- Pull-to-refresh on all screens
- Loading states with shimmer placeholders
- Error handling with snackbar messages
- Smooth navigation transitions
- Realistic time formatting ("Just now", "4h ago", "Yesterday")

### Behaviors
- Filter switching with smooth animations
- List/Grid view toggle with crossfade
- Search with real-time results
- Collaborative avatars with overflow indicators
- Floating Action Button context-aware display

## 🎯 Demo Interactions

All interactive elements show snackbar messages:
- Tapping notebooks, suggestions, or sticky notes
- Menu actions (overflow menus)
- Filter and view toggles
- Search functionality
- Create new item actions
- Floating action button groups

## 🚧 Future Enhancements

- Real backend integration
- Offline support with Room database
- Push notifications
- File attachments
- Collaborative editing
- Advanced search filters
- Export functionality
- Voice notes integration

## 📄 License

This project is for educational and demonstration purposes. UI design inspired by Microsoft OneNote.

---

Built with ❤️ using Jetpack Compose and Material 3