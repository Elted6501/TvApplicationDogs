# Dog Viewer TV Application - Complete Documentation

## 📱 Project Overview

**Dog Viewer TV Application** is an Android TV application that allows users to browse and view random dog images using the Dog API. The application is specifically designed for Smart TV interfaces with D-pad navigation support.

### Team Members
- **Development Team**: [Add team member names]
- **Project Date**: November 2024
- **Target Platform**: Android TV / Fire TV Stick

---

## 📸 Application Screenshots

### Main Menu
*[Insert image: Main menu showing two options - "Random Dogs" and "Browse by Breed"]*
> Screenshot showing the home screen with team members in the photo

### Random Dog Feature
*[Insert image: Random dog image display screen]*
> Screenshot showing a random dog image with navigation buttons

### Breed Selection
*[Insert image: Grid view of all available dog breeds]*
> Screenshot showing the breed selection grid with team in the background

### Breed Details
*[Insert image: Specific breed image gallery]*
> Screenshot showing breed-specific images with navigation controls

---

## ✨ Features

1. **Random Dog Viewer**
   - Display random dog images from the Dog API
   - Refresh button to load new random images
   - Smooth animations and transitions

2. **Browse by Breed**
   - Grid view of all available dog breeds
   - Navigate through breeds using TV remote
   - Support for sub-breeds

3. **Breed Details**
   - View all images for a specific breed
   - Navigate through multiple photos
   - Display breed information

4. **TV-Optimized Interface**
   - D-pad navigation support
   - Focus animations
   - Large, readable UI elements
   - Landscape orientation

---

## 🏗️ Technical Architecture

### Technology Stack

- **Language**: Kotlin
- **Minimum SDK**: API 21 (Android 5.0)
- **Target SDK**: API 36 (Android 16)
- **Compile SDK**: API 36
- **Build System**: Gradle with Kotlin DSL

### Dependencies

```kotlin
dependencies {
    // AndroidX Core
    implementation("androidx.core:core-ktx:1.10.1")
    
    // TV-specific libraries
    implementation("androidx.leanback:leanback:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    
    // Image loading
    implementation("com.github.bumptech.glide:glide:4.11.0")
    
    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
}
```

---

## 📁 Project Structure

```
TvApplication/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/tvapplication/
│   │       │   ├── MainActivity.kt                 # Main menu screen
│   │       │   ├── RandomDogActivity.kt           # Random dog viewer
│   │       │   ├── BreedSelectionActivity.kt      # Breed selection grid
│   │       │   ├── BreedDetailsActivity.kt        # Breed image gallery
│   │       │   ├── BreedAdapter.kt                # RecyclerView adapter
│   │       │   ├── DogApiService.kt               # Retrofit API interface
│   │       │   ├── RetrofitClient.kt              # Retrofit client setup
│   │       │   ├── Breed.kt                       # Breed data model
│   │       │   ├── DogResponse.kt                 # API response model
│   │       │   ├── BreedsResponse.kt              # Breeds list response
│   │       │   └── DogImagesResponse.kt           # Images response
│   │       ├── res/
│   │       │   ├── drawable/
│   │       │   │   ├── app_icon_your_company.png
│   │       │   │   └── default_background.xml
│   │       │   └── values/
│   │       │       ├── strings.xml
│   │       │       ├── colors.xml
│   │       │       └── themes.xml
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

---

## 🔧 Code Architecture

### 1. Main Activity (MainActivity.kt)

The entry point of the application displaying the main menu.

**Key Features:**
- Programmatic UI creation (no XML layouts)
- Two main menu options: Random Dogs and Browse by Breed
- Focus animations on D-pad navigation
- Custom button creation with CardView

**Code Highlights:**
```kotlin
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create UI programmatically
        val mainLayout = FrameLayout(this).apply {
            setBackgroundColor("#0A0E27".toColorInt())
        }
        
        // Setup menu buttons with focus animations
        val randomDogsButton = createMenuButton(
            getString(R.string.menu_random_title),
            getString(R.string.menu_random_desc),
            "#FF6B9D".toColorInt()
        )
        
        // Navigate to activities
        randomDogsButton.setOnClickListener {
            startActivity(Intent(this, RandomDogActivity::class.java))
        }
    }
    
    private fun createMenuButton(title: String, description: String, color: Int): View {
        // Creates CardView-based button with focus animations
    }
}
```

---

### 2. Random Dog Activity (RandomDogActivity.kt)

Displays random dog images fetched from the Dog API.

**Key Features:**
- Fetches random dog images via Retrofit
- Glide image loading with caching
- Loading states with ProgressBar
- Refresh button functionality
- Smooth fade-in animations

**API Integration:**
```kotlin
private fun fetchRandomDog() {
    loadingProgressBar.visibility = View.VISIBLE
    
    RetrofitClient.instance.getRandomDog()
        .enqueue(object : Callback<DogResponse> {
            override fun onResponse(call: Call<DogResponse>, 
                                   response: Response<DogResponse>) {
                val dogImageUrl = response.body()!!.message
                
                Glide.with(this@RandomDogActivity)
                    .load(dogImageUrl)
                    .apply(requestOptions)
                    .into(dogImageView)
                
                loadingProgressBar.visibility = View.GONE
            }
            
            override fun onFailure(call: Call<DogResponse>, t: Throwable) {
                Toast.makeText(this@RandomDogActivity, 
                    "Network error: ${t.message}", 
                    Toast.LENGTH_SHORT).show()
            }
        })
}
```

---

### 3. Breed Selection Activity (BreedSelectionActivity.kt)

Displays a grid of all available dog breeds.

**Key Features:**
- RecyclerView with GridLayoutManager (4 columns)
- Fetches all breeds from Dog API
- Alphabetically sorted breed list
- TV-optimized focus handling
- Support for breeds with sub-breeds

**Data Processing:**
```kotlin
private fun fetchBreeds() {
    RetrofitClient.instance.getAllBreeds()
        .enqueue(object : Callback<BreedsResponse> {
            override fun onResponse(call: Call<BreedsResponse>, 
                                   response: Response<BreedsResponse>) {
                val breedMap = response.body()!!.message
                breeds.clear()
                
                breedMap.forEach { (breedName, subBreeds) ->
                    val displayName = breedName.replaceFirstChar { it.uppercase() }
                    breeds.add(Breed(breedName, displayName, 
                                    subBreeds, subBreeds.isNotEmpty()))
                }
                
                breeds.sortBy { it.displayName }
                recyclerView.adapter = BreedAdapter(breeds) { breed, subBreed ->
                    onBreedSelected(breed, subBreed)
                }
            }
        })
}
```

---

### 4. Breed Details Activity (BreedDetailsActivity.kt)

Shows all images for a selected breed.

**Key Features:**
- Fetches all images for specific breed
- Image gallery navigation
- Current image counter (e.g., "Photo 3 of 50")
- Next/Previous navigation
- Back button to return to breed selection

**Image Navigation:**
```kotlin
private fun showNextImage() {
    if (allImages.isEmpty()) return
    
    currentImageIndex = (currentImageIndex + 1) % allImages.size
    loadImage(allImages[currentImageIndex])
    
    infoText.text = "📸 Photo ${currentImageIndex + 1} of ${allImages.size}"
}
```

---

### 5. Networking Layer

#### DogApiService.kt
Retrofit API interface defining all endpoints:

```kotlin
interface DogApiService {
    @GET("breeds/image/random")
    fun getRandomDog(): Call<DogResponse>
    
    @GET("breeds/list/all")
    fun getAllBreeds(): Call<BreedsResponse>
    
    @GET("breed/{breed}/images")
    fun getDogImagesByBreed(@Path("breed") breed: String): Call<DogImagesResponse>
}
```

#### RetrofitClient.kt
Singleton Retrofit client:

```kotlin
object RetrofitClient {
    private const val BASE_URL = "https://dog.ceo/api/"
    
    val instance: DogApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            
        retrofit.create(DogApiService::class.java)
    }
}
```

---

### 6. Data Models

#### DogResponse.kt
```kotlin
data class DogResponse(
    val message: String,  // Image URL
    val status: String
)
```

#### BreedsResponse.kt
```kotlin
data class BreedsResponse(
    val message: Map<String, List<String>>,  // breed -> sub-breeds
    val status: String
)
```

#### Breed.kt
```kotlin
data class Breed(
    val name: String,
    val displayName: String,
    val subBreeds: List<String>,
    val hasSubBreeds: Boolean
)
```

---

## 📱 Android Manifest Configuration

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <!-- Internet permission for API calls -->
    <uses-permission android:name="android.permission.INTERNET" />

    <!-- TV-specific features -->
    <uses-feature
        android:name="android.hardware.touchscreen"
        android:required="false" />
    <uses-feature
        android:name="android.software.leanback"
        android:required="true" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.TvApplication">
        
        <!-- Main Activity with Leanback Launcher -->
        <activity
            android:name=".MainActivity"
            android:banner="@drawable/app_icon_your_company"
            android:exported="true"
            android:screenOrientation="landscape">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
            </intent-filter>
        </activity>
        
        <!-- Other activities -->
        <activity android:name=".RandomDogActivity" />
        <activity android:name=".BreedSelectionActivity" />
        <activity android:name=".BreedDetailsActivity" />
    </application>
</manifest>
```

---

## 🎨 UI/UX Design

### Color Palette

```kotlin
// Primary Background
val primaryBackground = "#0A0E27"  // Dark blue

// Card Background
val cardBackground = "#1E2749"     // Lighter blue

// Accent Colors
val accentPink = "#FF6B9D"         // Pink for Random Dogs
val accentBlue = "#6B7FFF"         // Blue for Browse Breeds

// Text Colors
val primaryText = "#FFFFFF"        // White
val secondaryText = "#B8C5D6"      // Light blue-gray
```

### Design Principles

1. **Large Touch Targets**: All interactive elements are minimum 120dp in height
2. **High Contrast**: White text on dark backgrounds for readability
3. **Focus Indicators**: Scale and elevation animations on focus
4. **Consistent Spacing**: 16dp grid system
5. **Smooth Animations**: 200-300ms transitions

---

## 🚀 Building and Deployment

### Prerequisites

- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK API 36
- Fire TV Stick or Android TV device (API 21+)

### Building the APK

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd TvApplication
   ```

2. **Open in Android Studio**
   - File → Open → Select project directory

3. **Build the APK**
   ```bash
   ./gradlew clean assembleDebug
   ```

4. **Locate the APK**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

---

## 📦 Installation on Fire TV Stick

### Method 1: ADB (Recommended)

1. **Enable ADB on Fire TV**
   - Settings → My Fire TV → Developer Options
   - Enable "ADB Debugging" and "Apps from Unknown Sources"

2. **Find Fire TV IP Address**
   - Settings → My Fire TV → About → Network

3. **Connect via ADB**
   ```bash
   adb connect <FIRE_TV_IP>:5555
   ```

4. **Install APK**
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```

### Method 2: Downloader App

1. Install "Downloader" app from Amazon Appstore
2. Upload APK to a cloud service (Dropbox, Google Drive)
3. Get download link
4. Open Downloader app and enter the URL
5. Install the APK

---

## 🐛 Troubleshooting

### Common Issues

#### Issue: "There was a problem parsing the package"

**Solution 1**: Check minSdk version
```kotlin
// In build.gradle.kts
defaultConfig {
    minSdk = 21  // Must be 21 for Fire TV compatibility
}
```

**Solution 2**: Check compileSdk format
```kotlin
// Correct format:
compileSdk = 36

// Incorrect format:
compileSdk {
    version = release(36)  // ❌ Don't use this
}
```

#### Issue: App not appearing in launcher

**Solution**: Verify AndroidManifest.xml has correct intent filter:
```xml
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.LEANBACK_LAUNCHER" />
</intent-filter>
```

#### Issue: Images not loading

**Solution**: Check internet permission in AndroidManifest.xml:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 🔑 Key Learning Points

### 1. TV Navigation
- D-pad navigation requires `isFocusable = true` and `isFocusableInTouchMode = true`
- Focus change listeners provide visual feedback
- Landscape orientation is mandatory for TV apps

### 2. Programmatic UI
- Creating UI in code provides more flexibility
- No XML layouts needed for simple interfaces
- Dynamic measurements using `dpToPx()` helper

### 3. Retrofit Integration
- Simple REST API integration
- Automatic JSON parsing with Gson
- Proper error handling with callbacks

### 4. Image Loading
- Glide provides efficient caching
- Placeholder and error handling
- Memory optimization for TV devices

### 5. TV-Specific Considerations
- Leanback library for TV UI components
- Banner image requirement (320x180dp)
- Touch screen marked as not required
- Landscape-only orientation

---

## 📊 API Reference

### Dog CEO API

**Base URL**: `https://dog.ceo/api/`

**Endpoints Used:**

1. **Get Random Dog Image**
   ```
   GET /breeds/image/random
   Response: { "message": "https://...", "status": "success" }
   ```

2. **Get All Breeds**
   ```
   GET /breeds/list/all
   Response: { 
     "message": { 
       "breed1": ["sub1", "sub2"], 
       "breed2": [] 
     }, 
     "status": "success" 
   }
   ```

3. **Get Breed Images**
   ```
   GET /breed/{breed}/images
   Response: { 
     "message": ["url1", "url2", ...], 
     "status": "success" 
   }
   ```

---

## 🎯 Future Enhancements

1. **Favorites System**
   - Save favorite dog images
   - Persistent storage with SharedPreferences

2. **Search Functionality**
   - Search breeds by name
   - Voice search integration

3. **Image Sharing**
   - Share images via Android Share Sheet
   - Save images to device

4. **Slideshow Mode**
   - Automatic image rotation
   - Configurable timing

5. **Breed Information**
   - Integrate breed information API
   - Display breed characteristics

---

## 📝 Code Quality and Best Practices

### Implemented Best Practices

1. **Separation of Concerns**
   - Network layer separated from UI
   - Singleton pattern for Retrofit client
   - Data models for type safety

2. **Error Handling**
   - Try-catch blocks for critical operations
   - User-friendly error messages
   - Proper null checking

3. **Resource Management**
   - Cancel network calls in onDestroy()
   - Proper lifecycle management
   - Memory leak prevention

4. **Accessibility**
   - Content descriptions for images
   - Focus management for TV navigation
   - High contrast UI

5. **Performance**
   - Image caching with Glide
   - Lazy loading with RecyclerView
   - Efficient API calls

---

## 👥 Team Contributions

### Development Team
- **[Name]**: Main activity and navigation
- **[Name]**: API integration and networking
- **[Name]**: UI/UX design and animations
- **[Name]**: Testing and deployment

### Special Thanks
- Dog CEO API for providing free dog images
- Android Leanback team for TV components
- Open source community

---

## 📄 License

This project is developed for educational purposes.

**API Credits**: [Dog CEO API](https://dog.ceo/dog-api/)

---

## 📞 Contact Information

- **Project Repository**: [GitHub URL]
- **Email**: [Contact Email]
- **Team Website**: [Website URL]

---

## 📅 Version History

### Version 1.0 (November 2024)
- Initial release
- Main menu with two options
- Random dog image viewer
- Browse breeds functionality
- Breed-specific image gallery
- Fire TV compatibility

---

## 🎬 Demonstration

### How to Use the App

1. **Launch the App**
   - Find "TvApplication" in your TV launcher
   - Use remote to navigate

2. **Random Dogs Feature**
   - Select "🎲 Random Dogs" from main menu
   - Press OK to load random dog images
   - Press OK again to refresh

3. **Browse by Breed**
   - Select "🐕 Browse by Breed" from main menu
   - Navigate grid using D-pad
   - Press OK to view breed details
   - Use "Next Photo" to browse images
   - Press "Back" to return

---

## 📸 Image Placeholders

**Note**: Please add the following screenshots to complete the documentation:

1. `screenshot_main_menu_with_team.jpg` - Main menu screen with team members
2. `screenshot_random_dog_feature.jpg` - Random dog display screen
3. `screenshot_breed_grid.jpg` - Breed selection grid view
4. `screenshot_breed_details.jpg` - Breed detail screen with images
5. `screenshot_app_on_tv.jpg` - Application running on actual TV with team

### Recommended Screenshot Locations
```
TvApplication/
└── docs/
    └── images/
        ├── screenshot_main_menu.jpg
        ├── screenshot_random_dog.jpg
        ├── screenshot_breeds.jpg
        └── screenshot_details.jpg
```

---

## 🏆 Project Achievements

✅ Successfully deployed to Fire TV Stick  
✅ Smooth D-pad navigation  
✅ Professional UI/UX design  
✅ Efficient API integration  
✅ Error handling and loading states  
✅ Landscape-optimized layouts  
✅ Image caching and optimization  

---

**Document Last Updated**: November 28, 2024  
**Application Version**: 1.0  
**Compatible Devices**: Fire TV Stick, Android TV (API 21+)
