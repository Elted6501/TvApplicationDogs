package com.example.tvapplication

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
import androidx.core.graphics.toColorInt
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RandomDogActivity : FragmentActivity() {
    private lateinit var dogImageView: ImageView
    private lateinit var titleText: TextView
    private lateinit var instructionText: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var imageCard: CardView
    private lateinit var refreshButton: View

    private var currentCall: Call<DogResponse>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val mainLayout = FrameLayout(this).apply {
            setBackgroundColor(0xFF0A0E27.toInt())
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
            text = getString(R.string.random_dog_title)
            textSize = 34f
            setTextColor("#FFFFFFFF".toColorInt())
            gravity = Gravity.CENTER
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
            setShadowLayer(8f, 0f, 4f, "#80000000".toColorInt())
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
            setCardBackgroundColor("#1E2749".toColorInt())
        }
        
        val imageContainer = FrameLayout(this)
        
        dogImageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            contentDescription = getString(R.string.cd_dog_image)
        }
        
        loadingProgressBar = ProgressBar(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                dpToPx(80),
                dpToPx(80)
            ).apply {
                gravity = Gravity.CENTER
            }
            indeterminateTintList = android.content.res.ColorStateList.valueOf("#FF6B9D".toColorInt())
        }
        
        imageContainer.addView(dogImageView)
        imageContainer.addView(loadingProgressBar)
        imageCard.addView(imageContainer)
        
        refreshButton = createButton(getString(R.string.random_dog_button), "#FF6B9D".toColorInt())

        instructionText = TextView(this).apply {
            text = getString(R.string.random_press_ok)
            textSize = 16f
            setTextColor("#B8C5D6".toColorInt())
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dpToPx(10)
            }
        }
        
        contentLayout.addView(titleText)
        contentLayout.addView(imageCard)
        contentLayout.addView(refreshButton)
        contentLayout.addView(instructionText)
        
        scrollView.addView(contentLayout)
        mainLayout.addView(scrollView)
        
        setContentView(mainLayout)
        
        // Request focus on button after layout
        refreshButton.post {
            refreshButton.requestFocus()
        }
        
        fetchRandomDog()
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
                setPadding(dpToPx(24), dpToPx(12), dpToPx(24), dpToPx(12))
            }
            
            val buttonText = TextView(context).apply {
                this.text = text
                textSize = 20f
                setTextColor("#FFFFFFFF".toColorInt())
                typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
                contentDescription = text
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
                fetchRandomDog()
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
    
    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
            animateButtonPress(refreshButton)
            fetchRandomDog()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    
    private fun fetchRandomDog() {
        loadingProgressBar.visibility = View.VISIBLE
        dogImageView.alpha = 0.3f
        instructionText.text = getString(R.string.random_fetching)

        currentCall?.cancel()
        currentCall = RetrofitClient.instance.getRandomDog()
        currentCall?.enqueue(object : Callback<DogResponse> {
            override fun onResponse(call: Call<DogResponse>, response: Response<DogResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val dogImageUrl = response.body()!!.message
                    Log.d("RandomDogActivity", "Dog image URL: $dogImageUrl")
                    
                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                    
                    Glide.with(this@RandomDogActivity)
                        .load(dogImageUrl)
                        .apply(requestOptions)
                        .into(dogImageView)
                    
                    dogImageView.contentDescription = getString(R.string.cd_dog_image)

                    dogImageView.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .start()
                    
                    loadingProgressBar.visibility = View.GONE
                    instructionText.text = getString(R.string.random_press_ok)

                    animateImageCard()
                } else {
                    loadingProgressBar.visibility = View.GONE
                    dogImageView.alpha = 1f
                    instructionText.text = getString(R.string.random_failed)
                    Toast.makeText(this@RandomDogActivity, getString(R.string.random_failed), Toast.LENGTH_SHORT).show()
                }
            }
            
            override fun onFailure(call: Call<DogResponse>, t: Throwable) {
                if (call.isCanceled) return
                Log.e("RandomDogActivity", "Error fetching dog", t)
                loadingProgressBar.visibility = View.GONE
                dogImageView.alpha = 1f
                instructionText.text = getString(R.string.random_network_error)
                Toast.makeText(this@RandomDogActivity, getString(R.string.network_error, t.message ?: ""), Toast.LENGTH_SHORT).show()
            }
        })
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

    override fun onDestroy() {
        currentCall?.cancel()
        super.onDestroy()
    }
}