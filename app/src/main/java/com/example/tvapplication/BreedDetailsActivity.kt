package com.example.tvapplication

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BreedDetailsActivity : FragmentActivity() {
    private lateinit var dogImageView: ImageView
    private lateinit var titleText: TextView
    private lateinit var infoText: TextView
    private lateinit var instructionText: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var imageCard: CardView
    private lateinit var refreshButton: View
    
    private var breedName: String = ""
    private var breedDisplayName: String = ""
    private var subBreed: String? = null
    private var allImages: List<String> = emptyList()
    private var currentImageIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        breedName = intent.getStringExtra("breed_name") ?: ""
        breedDisplayName = intent.getStringExtra("breed_display_name") ?: ""
        subBreed = intent.getStringExtra("sub_breed")
        
        val mainLayout = FrameLayout(this).apply {
            setBackgroundColor(Color.parseColor("#0A0E27"))
        }
        
        val scrollView = android.widget.ScrollView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        }
        
        val contentLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dpToPx(24), dpToPx(20), dpToPx(24), dpToPx(20))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        
        titleText = TextView(this).apply {
            text = "🐕 $breedDisplayName"
            textSize = 32f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
            setShadowLayer(8f, 0f, 4f, Color.parseColor("#80000000"))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dpToPx(6)
            }
        }
        
        infoText = TextView(this).apply {
            text = "Loading breed information..."
            textSize = 15f
            setTextColor(Color.parseColor("#B8C5D6"))
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dpToPx(12)
            }
        }
        
        imageCard = CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                dpToPx(600),
                dpToPx(420)
            ).apply {
                bottomMargin = dpToPx(16)
            }
            radius = dpToPx(16).toFloat()
            cardElevation = dpToPx(10).toFloat()
            setCardBackgroundColor(Color.parseColor("#1E2749"))
        }
        
        val imageContainer = FrameLayout(this)
        
        dogImageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_INSIDE
        }
        
        loadingProgressBar = ProgressBar(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                dpToPx(80),
                dpToPx(80)
            ).apply {
                gravity = Gravity.CENTER
            }
            indeterminateTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#FF6B9D"))
        }
        
        imageContainer.addView(dogImageView)
        imageContainer.addView(loadingProgressBar)
        imageCard.addView(imageContainer)
        
        val buttonLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        
        refreshButton = createButton("🔄 Next Photo", Color.parseColor("#FF6B9D"))
        val backButton = createButton("⬅ Back", Color.parseColor("#6B7FFF"))
        
        backButton.setOnClickListener {
            finish()
        }
        
        buttonLayout.addView(backButton)
        buttonLayout.addView(refreshButton)
        
        instructionText = TextView(this).apply {
            text = "Use remote to navigate • Press OK for next photo"
            textSize = 15f
            setTextColor(Color.parseColor("#B8C5D6"))
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dpToPx(10)
            }
        }
        
        contentLayout.addView(titleText)
        contentLayout.addView(infoText)
        contentLayout.addView(imageCard)
        contentLayout.addView(buttonLayout)
        contentLayout.addView(instructionText)
        
        scrollView.addView(contentLayout)
        mainLayout.addView(scrollView)
        setContentView(mainLayout)
        
        // Request focus on back button after layout
        backButton.post {
            backButton.requestFocus()
        }
        
        fetchBreedImages()
    }
    
    private fun createButton(text: String, color: Int): View {
        return CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(dpToPx(8), 0, dpToPx(8), 0)
            }
            radius = dpToPx(12).toFloat()
            cardElevation = dpToPx(8).toFloat()
            setCardBackgroundColor(color)
            isFocusable = true
            isFocusableInTouchMode = true
            
            val buttonLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER
                setPadding(dpToPx(20), dpToPx(10), dpToPx(20), dpToPx(10))
            }
            
            val buttonText = TextView(context).apply {
                this.text = text
                textSize = 18f
                setTextColor(Color.WHITE)
                typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
            }
            
            buttonLayout.addView(buttonText)
            addView(buttonLayout)
            
            setOnFocusChangeListener { view, hasFocus ->
                val scale = if (hasFocus) 1.05f else 1.0f
                val elevation = if (hasFocus) dpToPx(14).toFloat() else dpToPx(8).toFloat()
                
                view.animate()
                    .scaleX(scale)
                    .scaleY(scale)
                    .setDuration(200)
                    .start()
                    
                cardElevation = elevation
            }
            
            setOnClickListener {
                animateButtonPress(this)
                showNextImage()
            }
        }
    }
    
    private fun animateButtonPress(button: View) {
        button.animate()
            .scaleX(0.98f)
            .scaleY(0.98f)
            .setDuration(100)
            .withEndAction {
                button.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                showNextImage()
                return true
            }
            KeyEvent.KEYCODE_BACK -> {
                finish()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
    
    private fun fetchBreedImages() {
        loadingProgressBar.visibility = View.VISIBLE
        dogImageView.alpha = 0.3f
        
        RetrofitClient.instance.getDogImagesByBreed(breedName).enqueue(object : Callback<DogImagesResponse> {
            override fun onResponse(call: Call<DogImagesResponse>, response: Response<DogImagesResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    allImages = response.body()!!.message
                    currentImageIndex = 0
                    
                    val displayBreed = if (subBreed != null) "$breedDisplayName - $subBreed" else breedDisplayName
                    infoText.text = "📸 ${allImages.size} photos available • Breed: $displayBreed"
                    
                    if (allImages.isNotEmpty()) {
                        loadImage(allImages[currentImageIndex])
                    } else {
                        loadingProgressBar.visibility = View.GONE
                        dogImageView.alpha = 1f
                        Toast.makeText(this@BreedDetailsActivity, "No images found for this breed", Toast.LENGTH_SHORT).show()
                    }
                    
                    Log.d("BreedDetails", "Loaded ${allImages.size} images for $breedName")
                } else {
                    loadingProgressBar.visibility = View.GONE
                    dogImageView.alpha = 1f
                    infoText.text = "Failed to load breed images"
                    Toast.makeText(this@BreedDetailsActivity, "Failed to fetch images", Toast.LENGTH_SHORT).show()
                }
            }
            
            override fun onFailure(call: Call<DogImagesResponse>, t: Throwable) {
                Log.e("BreedDetails", "Error fetching images", t)
                loadingProgressBar.visibility = View.GONE
                dogImageView.alpha = 1f
                infoText.text = "Network error"
                Toast.makeText(this@BreedDetailsActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun showNextImage() {
        if (allImages.isEmpty()) return
        
        currentImageIndex = (currentImageIndex + 1) % allImages.size
        loadImage(allImages[currentImageIndex])
        
        val displayBreed = if (subBreed != null) "$breedDisplayName - $subBreed" else breedDisplayName
        infoText.text = "📸 Photo ${currentImageIndex + 1} of ${allImages.size} • Breed: $displayBreed"
    }
    
    private fun loadImage(imageUrl: String) {
        loadingProgressBar.visibility = View.VISIBLE
        dogImageView.alpha = 0.3f
        
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(android.R.drawable.ic_menu_gallery)
        
        Glide.with(this)
            .load(imageUrl)
            .apply(requestOptions)
            .into(dogImageView)
        
        dogImageView.animate()
            .alpha(1f)
            .setDuration(500)
            .start()
        
        loadingProgressBar.visibility = View.GONE
        animateImageCard()
    }
    
    private fun animateImageCard() {
        imageCard.animate()
            .scaleX(1.05f)
            .scaleY(1.05f)
            .setDuration(300)
            .withEndAction {
                imageCard.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(300)
                    .start()
            }
            .start()
    }
    
    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}
