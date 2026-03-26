# Bean Grabber - Architecture Documentation

## Table of Contents
1. [Architecture Overview](#architecture-overview)
2. [Design Patterns](#design-patterns)
3. [Component Structure](#component-structure)
4. [Data Flow](#data-flow)
5. [Key Components](#key-components)
6. [Performance Considerations](#performance-considerations)

## Architecture Overview

Bean Grabber follows the **MVVM (Model-View-ViewModel)** architectural pattern, which provides:
- Clear separation of concerns
- Testable components
- Reactive UI updates
- Lifecycle-aware components

### Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                        VIEW LAYER                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  MainActivity │  │   Adapters   │  │  XML Layouts │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
                            ↕ (observes)
┌─────────────────────────────────────────────────────────┐
│                    VIEWMODEL LAYER                       │
│  ┌──────────────┐  ┌──────────────┐                     │
│  │ChatViewModel │  │AccountViewModel│                    │
│  └──────────────┘  └──────────────┘                     │
└─────────────────────────────────────────────────────────┘
                            ↕ (interacts)
┌─────────────────────────────────────────────────────────┐
│                      MODEL LAYER                         │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │ Repository   │  │     Models   │  │   Utilities  │  │
│  └──────────────┘  └──────────────┘  └──────────────┘  │
└─────────────────────────────────────────────────────────┘
```

## Design Patterns

### 1. MVVM (Model-View-ViewModel)

**Model**
- Data classes: `ChatMessage`, `BeansBag`, `UserAccount`, `GrabStatistics`
- Repository: `ChatRepository`
- Managers: `AccountManager`

**View**
- `MainActivity` - Main UI controller
- XML Layouts - UI structure
- Adapters - RecyclerView adapters

**ViewModel**
- `ChatViewModel` - Manages chat and beans bag state
- `AccountViewModel` - Manages user accounts

### 2. Repository Pattern

`ChatRepository` acts as a single source of truth for data:
```kotlin
class ChatRepository {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    fun addMessage(userId: String, username: String, messageText: String) {
        // Add message logic
    }
}
```

Benefits:
- Centralized data management
- Easy to test
- Can be replaced with real API calls

### 3. Observer Pattern

Uses Kotlin Flow for reactive programming:
```kotlin
lifecycleScope.launch {
    chatViewModel.getAllMessages().collect { messages ->
        chatAdapter.submitList(messages)
    }
}
```

### 4. Adapter Pattern

RecyclerView adapters with DiffUtil:
```kotlin
class ChatMessageAdapter : ListAdapter<ChatMessage, ViewHolder>(MessageDiffCallback()) {
    // Efficient list updates
}
```

### 5. Singleton Pattern

Utility classes like `LinkDetector` use object declarations:
```kotlin
object LinkDetector {
    fun containsBeansBag(message: String): Boolean { ... }
}
```

## Component Structure

### Data Layer

#### Models
```kotlin
// ChatMessage.kt - Represents a chat message
data class ChatMessage(
    val id: String,
    val userId: String,
    val username: String,
    val message: String,
    val timestamp: Long,
    val containsBeansBag: Boolean,
    val beansBagLinks: List<String>,
    val isHighlighted: Boolean
)

// BeansBag.kt - Represents a reward link
data class BeansBag(
    val id: String,
    val link: String,
    val messageId: String,
    val timestamp: Long,
    val isActive: Boolean,
    val isClicked: Boolean
)

// UserAccount.kt - Represents a user account
data class UserAccount(
    val id: String,
    val username: String,
    val token: String,
    val isActive: Boolean,
    val lastLoginTime: Long
)

// GrabStatistics.kt - Tracks user statistics
data class GrabStatistics(
    val totalGrabbed: Int,
    val successfulGrabs: Int,
    val missedGrabs: Int,
    val todayGrabs: Int,
    val lastGrabTime: Long?
)
```

#### Repository
```kotlin
// ChatRepository.kt
class ChatRepository {
    // StateFlow for reactive data
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    private val _activeBeansBags = MutableStateFlow<List<BeansBag>>(emptyList())

    // Public read-only flows
    val messages: StateFlow<List<ChatMessage>>
    val activeBeansBags: StateFlow<List<BeansBag>>

    // Methods for data manipulation
    fun addMessage(...)
    fun markBeansBagClicked(...)
    fun getRewardMessages(): List<ChatMessage>
}
```

#### Utilities
```kotlin
// LinkDetector.kt - Smart link detection
object LinkDetector {
    fun containsBeansBag(message: String): Boolean
    fun extractLinks(message: String): List<String>
    fun parseChatMessage(...): ChatMessage
    fun extractBeansBags(chatMessage: ChatMessage): List<BeansBag>
    fun deduplicateBeansBags(beansBags: List<BeansBag>): List<BeansBag>
}

// AccountManager.kt - Account storage
class AccountManager(context: Context) {
    fun getAllAccounts(): List<UserAccount>
    fun addAccount(account: UserAccount): Boolean
    fun setActiveAccount(accountId: String)
    fun getActiveAccount(): UserAccount?
}
```

### ViewModel Layer

#### ChatViewModel
```kotlin
class ChatViewModel(application: Application) : AndroidViewModel(application) {
    // Repository instance
    private val chatRepository = ChatRepository()

    // UI State
    private val _showOnlyRewards = MutableStateFlow(false)
    private val _statistics = MutableStateFlow(GrabStatistics())
    private val _newBeansBagNotification = MutableStateFlow<BeansBag?>(null)
    private val _autoClickEnabled = MutableStateFlow(false)

    // Public methods
    fun getMessages(): StateFlow<List<ChatMessage>>
    fun getActiveBeansBags(): StateFlow<List<BeansBag>>
    fun addMessage(userId: String, username: String, message: String)
    fun grabBeansBag(beansBagId: String)
    fun toggleRewardFilter()
    fun toggleAutoClick()
}
```

#### AccountViewModel
```kotlin
class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val accountManager = AccountManager(application)

    private val _accounts = MutableStateFlow<List<UserAccount>>(emptyList())
    private val _activeAccount = MutableStateFlow<UserAccount?>(null)

    fun addAccount(username: String, token: String): Boolean
    fun switchAccount(accountId: String)
    fun removeAccount(accountId: String)
}
```

### View Layer

#### MainActivity
```kotlin
class MainActivity : AppCompatActivity() {
    // ViewBinding for type-safe view access
    private lateinit var binding: ActivityMainBinding

    // ViewModels (by viewModels() delegate)
    private val chatViewModel: ChatViewModel by viewModels()
    private val accountViewModel: AccountViewModel by viewModels()

    // Adapters
    private lateinit var chatAdapter: ChatMessageAdapter
    private lateinit var beansBagAdapter: BeansBagAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupChatRecyclerView()
        setupGripPanel()
        setupControls()
        observeViewModels()
    }
}
```

#### Adapters
```kotlin
// ChatMessageAdapter.kt
class ChatMessageAdapter(
    private val onLinkClick: (String) -> Unit
) : ListAdapter<ChatMessage, MessageViewHolder>(MessageDiffCallback()) {
    // Efficient list updates with DiffUtil
}

// BeansBagAdapter.kt
class BeansBagAdapter(
    private val onGrabClick: (BeansBag) -> Unit
) : ListAdapter<BeansBag, BeansBagViewHolder>(BeansBagDiffCallback()) {
    // Efficient list updates with DiffUtil
}
```

## Data Flow

### Message Addition Flow

```
1. User types message in EditText
   ↓
2. MainActivity.sendMessage()
   ↓
3. ChatViewModel.addMessage(userId, username, message)
   ↓
4. ChatRepository.addMessage(...)
   ↓
5. LinkDetector.parseChatMessage(...) → ChatMessage
   ↓
6. If contains beans bag:
   LinkDetector.extractBeansBags(...) → List<BeansBag>
   ↓
7. Update StateFlows:
   - _messages.value = updated list
   - _activeBeansBags.value = updated list
   ↓
8. MainActivity observes StateFlow
   ↓
9. ChatAdapter.submitList(messages)
   ↓
10. DiffUtil calculates changes
   ↓
11. RecyclerView updates UI efficiently
```

### Beans Bag Grabbing Flow

```
1. User taps "GRAB" button
   ↓
2. BeansBagAdapter.onGrabClick(beansBag)
   ↓
3. MainActivity.grabBeansBag(beansBag)
   ↓
4. ChatViewModel.grabBeansBag(beansBagId)
   ↓
5. ChatRepository.markBeansBagClicked(beansBagId)
   ↓
6. Update StateFlows
   ↓
7. MainActivity.openLink(link)
   ↓
8. Intent opens browser
   ↓
9. Statistics updated
   ↓
10. Notification cleared
```

### Notification Flow

```
1. New BeansBag added to activeBeansBags
   ↓
2. ChatViewModel observes activeBeansBags
   ↓
3. Detects new bag (not in previous list)
   ↓
4. _newBeansBagNotification.value = newBag
   ↓
5. MainActivity observes newBeansBagNotification
   ↓
6. Show notification banner
   ↓
7. Vibrate phone
   ↓
8. User taps notification or close
   ↓
9. ChatViewModel.clearNotification()
```

## Key Components

### 1. Link Detection System

**Purpose**: Automatically detect and extract Beans Bag links from messages

**Components**:
- Keyword matching (case-insensitive)
- Regex-based URL extraction
- Message parsing

**Algorithm**:
```kotlin
fun parseChatMessage(userId, username, messageText): ChatMessage {
    1. Check if message contains keywords:
       - "beans bag", "beansbag", "bean bag"
       - "click for more info", "reward link"

    2. If keywords found:
       - Extract all URLs using regex pattern
       - Set containsBeansBag = true
       - Set isHighlighted = true

    3. Return ChatMessage with all data
}
```

### 2. Grip System

**Purpose**: Display active reward links in a dedicated panel

**Features**:
- Floating panel at top of screen
- Shows only active (non-clicked, non-expired) bags
- Large, easy-to-tap buttons
- Auto-updates on new bags

**Implementation**:
```kotlin
// Observe active bags
lifecycleScope.launch {
    chatViewModel.getActiveBeansBags().collect { bags ->
        beansBagAdapter.submitList(bags)
        updateVisibility(bags.isEmpty())
    }
}
```

### 3. Intelligent Filtering

**Purpose**: Toggle between all messages and reward-only messages

**Implementation**:
```kotlin
fun getMessages(): StateFlow<List<ChatMessage>> {
    return if (_showOnlyRewards.value) {
        // Filter to show only messages with beans bags
        MutableStateFlow(chatRepository.getRewardMessages()).asStateFlow()
    } else {
        // Show all messages
        chatRepository.messages
    }
}
```

### 4. Multi-Account System

**Purpose**: Support unlimited user accounts with quick switching

**Storage**: SharedPreferences with Gson serialization

**Features**:
- Add/remove accounts
- Switch between accounts
- Secure token storage
- Guest mode support

## Performance Considerations

### 1. RecyclerView Optimization

**DiffUtil**: Calculates minimal changes between lists
```kotlin
class MessageDiffCallback : DiffUtil.ItemCallback<ChatMessage>() {
    override fun areItemsTheSame(oldItem, newItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem, newItem): Boolean {
        return oldItem == newItem
    }
}
```

**Benefits**:
- Only updates changed items
- Smooth animations
- Efficient memory usage

**Additional Optimizations**:
```kotlin
recyclerView.apply {
    setHasFixedSize(true)          // Fixed size for performance
    setItemViewCacheSize(20)       // Cache 20 items
}
```

### 2. Memory Management

**Message Limit**: Keep only last 100 messages
```kotlin
private val MAX_MESSAGES = 100

if (currentMessages.size > MAX_MESSAGES) {
    currentMessages.removeAt(0)  // Remove oldest
}
```

**Beans Bag Expiry**: Auto-remove old bags
```kotlin
fun isExpired(): Boolean {
    val fiveMinutes = 5 * 60 * 1000
    return System.currentTimeMillis() - timestamp > fiveMinutes
}
```

### 3. Threading

**Kotlin Coroutines**: All async operations use coroutines
```kotlin
viewModelScope.launch {
    // Background work
    chatRepository.addMessage(...)
}
```

**StateFlow**: Reactive, lifecycle-aware updates
```kotlin
lifecycleScope.launch {
    chatViewModel.getAllMessages().collect { messages ->
        // Update UI on main thread
        chatAdapter.submitList(messages)
    }
}
```

### 4. UI Rendering

**ViewBinding**: Type-safe, efficient view access
```kotlin
private lateinit var binding: ActivityMainBinding
binding = ActivityMainBinding.inflate(layoutInflater)
```

**ConstraintLayout**: Flat hierarchy, fast rendering
```xml
<androidx.constraintlayout.widget.ConstraintLayout>
    <!-- All views in single layer -->
</androidx.constraintlayout.widget.ConstraintLayout>
```

### 5. Link Detection Optimization

**Compiled Pattern**: Regex pattern compiled once
```kotlin
private val URL_PATTERN = Pattern.compile(
    "(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=]+)",
    Pattern.CASE_INSENSITIVE
)
```

**Early Exit**: Check keywords before regex
```kotlin
fun parseChatMessage(...): ChatMessage {
    val containsBeansBag = containsBeansBag(messageText)
    val links = if (containsBeansBag) extractLinks(messageText) else emptyList()
    // Only extract links if keywords found
}
```

## Testing Strategy

### Unit Tests
- Model classes (data validation)
- LinkDetector logic
- ViewModel logic
- Repository operations

### Integration Tests
- ViewModel + Repository interaction
- Account management flow
- Message processing flow

### UI Tests
- RecyclerView scrolling
- Button clicks
- Filter toggling
- Notification display

## Future Improvements

### Scalability
- Real API integration (replace simulated data)
- WebSocket for real-time chat
- Cloud backup for accounts
- Firebase integration

### Features
- Floating overlay mode
- Custom notification sounds
- Export statistics
- Backup/restore functionality

### Performance
- Pagination for large message lists
- Image caching (if avatars added)
- Background service for notifications
- WorkManager for scheduled tasks

---

**Document Version**: 1.0
**Last Updated**: 2026-03-26
**Maintained By**: Bean Grabber Development Team
