# Dog Photo TV App 🐕

A beautiful Android TV application that fetches and displays random dog photos from the Dog CEO API with smooth animations, modern design, and breed browsing capabilities.

## API Used
- **Random Dog**: https://dog.ceo/api/breeds/image/random
- **All Breeds**: https://dog.ceo/api/breeds/list/all
- **Breed Images**: https://dog.ceo/api/breed/{breed}/images
- **Documentation**: https://dog.ceo/dog-api/

## Features

### 🎯 **Main Features**
- **Random Dog Mode**: Get random dog photos from any breed
- **Browse by Breed**: Select from 100+ dog breeds
- **Breed Details**: View multiple photos for each breed
- **Image Gallery**: Browse through breed-specific photo collections
- **Smooth Navigation**: Intuitive TV remote control navigation

### ✨ **Modern UI Design**
- Beautiful gradient dark theme with deep blue tones
- Rounded card design with elevation shadows
- Smooth fade-in animations for images
- Interactive buttons with focus animations
- Loading spinner with custom styling
- Emoji-enhanced text for a friendly feel

### 🎮 **TV-Optimized**
- Fully functional with TV remote controls
- Focus-aware buttons with scale animations
- Large, readable fonts optimized for TV screens
- Center-cropped images for best viewing experience
- Grid layout for breed selection
- D-pad navigation support

### 📱 **User Interface**

**Home Screen:**
- Random dog photo display
- "Random Dog" button - Get any random dog
- "Browse Breeds" button - Navigate to breed selection

**Breed Selection Screen:**
- Grid view of all available dog breeds (100+ breeds)
- Shows number of varieties for breeds with sub-breeds
- Smooth scrolling with TV remote
- Focus animations on breed cards

**Breed Details Screen:**
- Display breed name and photo count
- Browse through all photos for selected breed
- "Next Photo" button to cycle through images
- "Back" button to return to breed selection
- Photo counter (e.g., "Photo 5 of 23")

## UI/UX Highlights
- **Color Scheme**: 
  - Deep navy background (#0A0E27)
  - Pink accent button (#FF6B9D)
  - Blue accent button (#6B7FFF)
  - Card backgrounds (#1E2749)
  - Soft blue-gray text (#B8C5D6)

- **Animations**:
  - Smooth button press animations
  - Image fade-in transitions
  - Card scale animations on new image load
  - Focus animations for TV navigation
  - RecyclerView smooth scrolling

- **Typography**:
  - Large 48sp bold titles
  - 26sp button text
  - 22sp card text
  - 20sp info text
  - Drop shadows for enhanced readability

## Technical Stack
- **Network**: Retrofit 2.9.0 for REST API calls
- **JSON**: Gson for parsing
- **Images**: Glide with disk caching
- **UI**: CardView, RecyclerView, custom layouts
- **Platform**: Android TV (Leanback)
- **Language**: Kotlin

## How to Use

### Main Screen
1. Launch the app
2. See a random dog photo automatically
3. Press OK on "Random Dog" button for more random dogs
4. Press OK on "Browse Breeds" button to select specific breeds

### Breed Selection
1. Navigate through breeds using D-pad
2. Select a breed by pressing OK/Enter
3. Breeds with sub-breeds show variety count

### Breed Details
1. View breed-specific photos
2. Press "Next Photo" or OK/Enter to see next photo
3. Photo counter shows your position in the gallery
4. Press "Back" button to return to breed selection
5. Press Back on remote to exit

## API Endpoints Used
```
GET /breeds/image/random          - Random dog from any breed
GET /breeds/list/all              - List all breeds with sub-breeds
GET /breed/{breed}/images         - All images for a specific breed
GET /breed/{breed}/images/random  - Random image from specific breed
```

## Changes Made

### New Features
1. **Breed Selection Screen**:
   - RecyclerView with GridLayoutManager (4 columns)
   - Displays all 100+ dog breeds
   - Shows sub-breed counts
   - Smooth scrolling and animations

2. **Breed Details Screen**:
   - Shows all photos for selected breed
   - Image gallery with navigation
   - Photo counter display
   - Next/Back navigation buttons

3. **Enhanced Main Screen**:
   - Added "Browse Breeds" button
   - Dual-mode navigation (Random vs Browse)
   - Improved button layout

### New Files
- `Breed.kt` - Breed data model
- `BreedsResponse.kt` - API response for breed list
- `DogImagesResponse.kt` - API response for breed images
- `BreedSelectionActivity.kt` - Breed grid view
- `BreedAdapter.kt` - RecyclerView adapter
- `BreedDetailsActivity.kt` - Breed photo gallery

### Updated Files
- `DogApiService.kt` - Added breed endpoints
- `MainActivity.kt` - Added breed navigation button
- `AndroidManifest.xml` - Registered new activities

### Dependencies Added
- RecyclerView 1.3.2 (for breed grid)
- CardView 1.0.0 (for UI cards)
- Retrofit 2.9.0 (REST API)
- Gson (JSON parsing)

## Technical Details
- Minimum SDK: 33
- Target SDK: 36
- Language: Kotlin
- Architecture: Multiple Activities with programmatic UI
- Image Resolution: 800x600dp cards
- Grid Layout: 4 columns for breed selection
- Animations: Property animations with interpolators
- Breeds Available: 100+ breeds (retrieved from API)

## Screenshots Flow
```
[Home Screen] 
    ↓
    ├─→ [Random Dog Button] → Shows random dog from any breed
    │
    └─→ [Browse Breeds Button] → [Breed Selection Grid]
                                        ↓
                                  [Select Breed]
                                        ↓
                                  [Breed Details]
                                  - View all photos
                                  - Navigate through gallery
                                  - Return to breeds
```
