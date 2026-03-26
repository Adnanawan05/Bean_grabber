# Bean Grabber - Quick Start Guide

## 🚀 5-Minute Setup

### Prerequisites
- Android Studio (latest version recommended)
- JDK 8 or higher
- Android SDK with API 24+ installed

### Installation Steps

1. **Clone the Repository**
```bash
git clone https://github.com/Adnanawan05/Bean_grabber.git
cd Bean_grabber
```

2. **Open in Android Studio**
   - Launch Android Studio
   - File → Open
   - Select the `Bean_grabber` folder
   - Wait for Gradle sync (may take 2-5 minutes on first run)

3. **Run the App**
   - Connect an Android device (USB debugging enabled) OR start an emulator
   - Click the green "Run" button (or Shift+F10)
   - App will install and launch automatically

## 📱 First Launch

When you first run the app:

1. **Sample Data Loaded**: The app automatically loads sample messages with Beans Bags
2. **Chat Interface**: You'll see the main chat screen with messages
3. **Grip Panel**: Active Beans Bags appear in the gold panel at the top
4. **Try Grabbing**: Tap "GRAB NOW" or "GRAB" buttons to test the system

## 🎮 Testing Features

### Test Link Detection
1. Type a message in the input field at the bottom
2. Include the text "Beans bag" and a URL like `https://example.com/test`
3. Click "Send"
4. Message should be highlighted in gold
5. Link should appear in the Grip Panel

**Example Message**:
```
Check out this Beans bag https://example.com/reward123 Click for more info >
```

### Test Filtering
1. Toggle "Show Only Rewards" switch at the bottom
2. Chat should show only messages with Beans Bags
3. Toggle off to see all messages again

### Test Statistics
1. Grab a few Beans Bags
2. Tap the info icon (ⓘ) at the bottom
3. View your grab statistics

### Test Accounts
1. Tap the account icon (⚙) at the bottom
2. See guest mode active
3. You can add accounts through the AccountViewModel

## 🛠️ Development Tips

### Project Structure Overview
```
app/src/main/java/com/beangrabber/
├── data/
│   ├── model/          # Data classes
│   └── repository/     # Data management
├── ui/
│   ├── main/          # Main activity & ViewModels
│   └── adapter/       # RecyclerView adapters
└── util/              # Utility classes

app/src/main/res/
├── layout/            # XML layouts
├── values/            # Strings, colors, themes
└── xml/               # Backup rules
```

### Key Files to Know

1. **MainActivity.kt** - Main UI controller
   - Location: `app/src/main/java/com/beangrabber/ui/main/MainActivity.kt`
   - Controls all UI interactions

2. **ChatViewModel.kt** - Business logic for chat
   - Location: `app/src/main/java/com/beangrabber/ui/main/ChatViewModel.kt`
   - Manages messages and beans bags

3. **LinkDetector.kt** - Link detection logic
   - Location: `app/src/main/java/com/beangrabber/util/LinkDetector.kt`
   - Customize keywords here

4. **activity_main.xml** - Main layout
   - Location: `app/src/main/res/layout/activity_main.xml`
   - Customize UI here

### Common Customizations

#### Change Link Detection Keywords
Edit `LinkDetector.kt`:
```kotlin
private val BEANS_BAG_KEYWORDS = listOf(
    "beans bag",
    "your keyword here",
    "another keyword"
)
```

#### Change Message Limit
Edit `ChatRepository.kt`:
```kotlin
private val MAX_MESSAGES = 100  // Change this number
```

#### Change Link Expiry Time
Edit `BeansBag.kt`:
```kotlin
fun isExpired(): Boolean {
    val fiveMinutes = 5 * 60 * 1000  // Change to 10 * 60 * 1000 for 10 minutes
    return System.currentTimeMillis() - timestamp > fiveMinutes
}
```

#### Change Colors
Edit `app/src/main/res/values/colors.xml`:
```xml
<color name="gold">#FFD700</color>  <!-- Change to your preferred color -->
```

## 🧪 Testing on Emulator (PC)

### Create an Emulator
1. Android Studio → Tools → Device Manager
2. Click "Create Device"
3. Select "Phone" → "Pixel 5" (recommended)
4. Select System Image → "R" (API 30) or higher
5. Click "Finish"

### Run on Emulator
1. Start the emulator (green play button in Device Manager)
2. Wait for emulator to boot (~30 seconds)
3. Click "Run" in Android Studio
4. App will install on emulator

**Note**: Emulator will work exactly like a real device!

## 📝 Build APK for Distribution

### Debug APK (for testing)
```bash
./gradlew assembleDebug
```
APK location: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK (optimized)
```bash
./gradlew assembleRelease
```
APK location: `app/build/outputs/apk/release/app-release.apk`

**Note**: Release APK requires signing for production use.

## 🔧 Troubleshooting

### Gradle Sync Failed
- Solution: File → Invalidate Caches → Invalidate and Restart

### App Crashes on Start
- Check: Android version is API 24+ (Android 7.0+)
- Check: Logcat for error messages

### Links Not Opening
- Ensure: INTERNET permission in AndroidManifest.xml
- Check: Browser app is installed on device

### No Sample Data Appearing
- Check: `ChatViewModel.loadSampleData()` is called in `MainActivity.onCreate()`
- Restart the app

## 🎯 Next Steps

1. **Customize UI**: Modify layouts in `app/src/main/res/layout/`
2. **Add Real API**: Replace `ChatRepository` simulation with real API calls
3. **Add Features**: Extend functionality as needed
4. **Test Thoroughly**: Test on multiple devices/API levels

## 📚 Additional Resources

- **README.md** - Comprehensive feature documentation
- **ARCHITECTURE.md** - Detailed architecture documentation
- **Code Comments** - All files have detailed comments

## 💡 Tips for Success

1. **Use Sample Data**: The built-in sample data is perfect for testing
2. **Check Logcat**: Android Studio → Logcat tab for debugging
3. **Hot Reload**: Use Apply Changes (Ctrl+F10) for faster iteration
4. **Read Comments**: Code is heavily commented for learning

## 🤝 Need Help?

- Check the README.md for detailed documentation
- Review ARCHITECTURE.md for system design
- Read code comments in key files
- Open an issue on GitHub

## ✅ Verification Checklist

After setup, verify these work:

- [ ] App launches without crashes
- [ ] Sample messages appear in chat
- [ ] Beans Bags are highlighted in gold
- [ ] Grip Panel shows active bags
- [ ] Tapping "GRAB" opens browser
- [ ] Filter toggle works
- [ ] Statistics dialog shows data
- [ ] Sending messages works

If all items are checked, you're ready to develop! 🎉

---

**Happy Coding!** 🚀
