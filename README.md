# Bean Grabber - Android Application

<div align="center">
  <h3>🎯 Lightweight, Optimized Chat Interface for Real-Time Reward Link Grabbing</h3>
</div>

## 📱 Overview

Bean Grabber is a high-performance Android application designed for capturing and interacting with real-time clickable reward links ("Beans Bags") in chat environments. Built with efficiency and speed as top priorities, this app removes unnecessary features like video/audio streaming to focus purely on the chat and link-grabbing experience.

## ✨ Core Features

### 🚫 Performance Mode
- ✅ **No Video/Audio Streaming** - Completely disabled for maximum performance
- ✅ **Chat-Only Focus** - Optimized UI rendering and low latency
- ✅ **Efficient Memory Usage** - Limits chat history to last 100 messages

### 💬 Optimized Chat Interface
- ✅ **Expanded Chat Area** - Occupies 80-90% of screen space
- ✅ **Auto-Scroll** - Enabled by default, pauses on user interaction
- ✅ **RecyclerView Optimization** - DiffUtil for efficient updates
- ✅ **Dark Mode** - Eye-friendly interface with high contrast

### 🎯 Smart Link Detection & Highlighting
- ✅ **Automatic Detection** - Identifies "Beans bag" and similar keywords
- ✅ **Link Extraction** - Regex-based URL extraction
- ✅ **Visual Highlighting** - Gold background with enlarged, clickable UI
- ✅ **One-Tap Access** - Direct button on highlighted messages

### ⚡ Grip System (Core Feature)
- ✅ **Floating Panel** - Dedicated section for active reward links
- ✅ **Deduplication** - Shows only unique, latest links
- ✅ **Large Tap Targets** - Optimized for speed and accuracy
- ✅ **Active Filtering** - Automatically hides expired/clicked links

### 🔔 Notification System
- ✅ **Top Banner** - Appears when new Beans Bags arrive
- ✅ **Direct Action** - Tap to open or scroll to link
- ✅ **Vibration Feedback** - Optional haptic feedback on new links

### 👥 Multi-Account System
- ✅ **Unlimited Accounts** - No 4-account limitation
- ✅ **Quick Switching** - Fast account switching
- ✅ **Secure Storage** - Token-based authentication with SharedPreferences
- ✅ **Guest Mode** - Use without logging in

### 🧠 Intelligent Filtering
- ✅ **Reward-Only Mode** - Toggle to show only messages with links
- ✅ **Spam Filtering** - Focus on priority messages
- ✅ **Real-Time Updates** - Instant filter application

### 🎁 Bonus Features
- ✅ **Auto-Click Toggle** - Optional automatic link opening
- ✅ **Vibration Alerts** - Haptic feedback on new bags
- ✅ **Statistics Tracking** - Total grabbed, success rate, daily stats
- ✅ **Link Deduplication** - Prevents duplicate link displays

## 🏗️ Architecture

### MVVM Pattern (Model-View-ViewModel)

```
app/
├── data/
│   ├── model/              # Data classes
│   │   ├── ChatMessage.kt
│   │   ├── BeansBag.kt
│   │   ├── UserAccount.kt
│   │   └── GrabStatistics.kt
│   └── repository/         # Data management
│       └── ChatRepository.kt
├── ui/
│   ├── main/              # Main screen
│   │   ├── MainActivity.kt
│   │   ├── ChatViewModel.kt
│   │   └── AccountViewModel.kt
│   └── adapter/           # RecyclerView adapters
│       ├── ChatMessageAdapter.kt
│       └── BeansBagAdapter.kt
└── util/                  # Utilities
    ├── LinkDetector.kt
    └── AccountManager.kt
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 24+ (Android 7.0)
- Kotlin 1.9.20+
- Gradle 8.2.0+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/Adnanawan05/Bean_grabber.git
cd Bean_grabber
```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory
   - Wait for Gradle sync to complete

3. **Build the project**
```bash
./gradlew build
```

4. **Run on device/emulator**
   - Connect Android device or start emulator
   - Click "Run" in Android Studio
   - Or use: `./gradlew installDebug`

### Running on PC (via Emulator)

1. **Install Android Studio** on your PC
2. **Create an AVD (Android Virtual Device)**:
   - Tools → Device Manager → Create Device
   - Select a phone (e.g., Pixel 5)
   - Choose system image (API 24+)
   - Finish setup
3. **Run the app** on the emulator
4. The app will function exactly as on a physical device

## 📖 Usage Guide

### Basic Usage

1. **Launch the app** - Opens directly to the main chat interface
2. **View chat messages** - Messages appear in real-time
3. **Spot Beans Bags** - Highlighted in gold with "GRAB NOW" button
4. **Grab links** - Tap the button to open in browser
5. **Check active bags** - View the Grip Panel at the top

### Advanced Features

#### Reward Filter
- Toggle "Show Only Rewards" to see only Beans Bag messages
- Reduces noise and focuses on important links

#### Auto-Click Mode
- Enable to automatically open new Beans Bags
- Useful for fastest possible grabbing

#### Statistics
- Tap the info icon to view your grab statistics
- Track success rate and daily performance

#### Account Management
- Tap the account icon to view/switch accounts
- Add unlimited accounts for different sessions

### Testing with Sample Data

The app includes a sample data generator for testing:
- Sample messages with Beans Bags are automatically loaded
- Test all features without a live connection

## 🎨 UI/UX Design

### Design Principles
- **Minimalistic** - No clutter, focus on content
- **Dark Mode** - Reduces eye strain
- **High Contrast** - Gold highlights for important elements
- **Large Tap Targets** - Easy, accurate tapping
- **Smooth Scrolling** - No lag or stuttering

### Color Scheme
- **Background**: Dark (#121212)
- **Primary**: Dark Gray (#1E1E1E)
- **Accent**: Gold (#FFD700)
- **Text**: White with gray usernames
- **Highlights**: Yellow/Gold for Beans Bags

## ⚡ Performance Optimizations

1. **RecyclerView Optimizations**
   - DiffUtil for efficient updates
   - View recycling with ViewHolder pattern
   - Fixed size optimization
   - Item view cache (20 items)

2. **Memory Management**
   - Limited message history (100 messages)
   - Automatic cleanup of old messages
   - Efficient data structures

3. **Threading**
   - Kotlin Coroutines for async operations
   - StateFlow for reactive UI updates
   - Background processing for link detection

4. **UI Rendering**
   - No overdraw in layouts
   - Efficient constraint layouts
   - Hardware acceleration enabled

## 🔒 Security & Privacy

### Data Storage
- **SharedPreferences** for account data
- **Token-based authentication**
- **No plain text passwords**
- **Local storage only** - No cloud sync by default

### Permissions
- **INTERNET** - For opening links
- **VIBRATE** - For haptic feedback
- **ACCESS_NETWORK_STATE** - Check connectivity

### Privacy Notes
- This app is for **personal use only**
- Does not attempt to bypass or hack services
- No data collection or analytics
- All data stored locally on device

## 🛠️ Tech Stack

### Frontend
- **Language**: Kotlin
- **UI**: XML layouts with ViewBinding
- **Architecture**: MVVM
- **Async**: Kotlin Coroutines + Flow

### Libraries
- **AndroidX Core KTX** - Kotlin extensions
- **Lifecycle & ViewModel** - Architecture components
- **RecyclerView** - Efficient list display
- **Material Components** - UI components
- **Gson** - JSON serialization

### Build System
- **Gradle** 8.2.0 with Kotlin DSL
- **ProGuard** for release optimization

## 📊 Core Logic

### Link Detection Algorithm

```kotlin
1. Scan incoming message for keywords:
   - "beans bag", "beansbag", "bean bag"
   - "click for more info", "reward link"

2. If keywords found:
   - Extract all URLs using regex
   - Create ChatMessage with isHighlighted=true
   - Extract BeansBag objects

3. Deduplication:
   - Keep only latest instance of each link
   - Filter out expired links (>5 minutes)
   - Update active beans bags list
```

### Grip System Logic

```kotlin
1. Monitor active beans bags
2. Display in floating panel
3. On new bag:
   - Show notification banner
   - Vibrate phone
   - Add to top of grip panel
4. On grab:
   - Open link in browser
   - Mark as clicked
   - Remove from active list
   - Update statistics
```

## 🧪 Testing

### Manual Testing
1. Use the built-in sample data generator
2. Send test messages with Beans Bag keywords
3. Verify highlighting and grip system
4. Test filters and auto-click mode

### Emulator Testing
1. Run on Android Emulator (API 24+)
2. Test all UI interactions
3. Verify vibration (if supported)
4. Test link opening

## 📝 Implementation Plan

### Phase 1: Core Structure ✅
- [x] Android project setup
- [x] Gradle configuration
- [x] MVVM architecture

### Phase 2: Data Layer ✅
- [x] Data models
- [x] Repository layer
- [x] Link detection utility

### Phase 3: UI Layer ✅
- [x] Main activity layout
- [x] Chat adapter
- [x] Beans bag adapter
- [x] ViewModels

### Phase 4: Features ✅
- [x] Grip system
- [x] Notification banner
- [x] Filtering
- [x] Multi-account support
- [x] Statistics

### Phase 5: Polish ✅
- [x] Vibration feedback
- [x] Auto-click mode
- [x] Dark theme
- [x] Performance optimizations

## 🔧 Configuration

### Adjust Performance Settings

Edit `ChatRepository.kt`:
```kotlin
private val MAX_MESSAGES = 100  // Change message limit
```

### Customize Link Expiry

Edit `BeansBag.kt`:
```kotlin
fun isExpired(): Boolean {
    val fiveMinutes = 5 * 60 * 1000  // Change expiry time
    return System.currentTimeMillis() - timestamp > fiveMinutes
}
```

### Modify Keywords

Edit `LinkDetector.kt`:
```kotlin
private val BEANS_BAG_KEYWORDS = listOf(
    "beans bag",
    "your custom keyword"
)
```

## 🤝 Contributing

This is a personal-use application. If you want to extend it:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is for personal use only. Not for commercial distribution.

## ⚠️ Disclaimer

- **Personal Use Only** - Not intended for public distribution
- **No Service Hacking** - Does not attempt to bypass any services
- **Educational Purpose** - Demonstrates Android development best practices
- **No Warranty** - Provided as-is without any guarantees

## 🎯 Future Enhancements (Optional)

- [ ] WebSocket support for real-time chat
- [ ] Firebase integration for cloud sync
- [ ] Custom notification sounds
- [ ] Floating overlay mode (chat heads)
- [ ] Export statistics to CSV
- [ ] Dark/Light theme toggle
- [ ] Customizable highlight colors
- [ ] Link preview before opening
- [ ] Backup/restore accounts

## 📧 Support

For issues or questions:
- Open an issue on GitHub
- Check existing documentation
- Review code comments

## 🌟 Acknowledgments

Built with modern Android development best practices:
- **MVVM Architecture**
- **Kotlin Coroutines**
- **Material Design**
- **RecyclerView Optimization**

---

**Made with ❤️ for efficient bean grabbing!**
