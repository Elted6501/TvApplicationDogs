package com.example.tvapplication

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.core.graphics.toColorInt

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val mainLayout = FrameLayout(this).apply {
            setBackgroundColor("#0A0E27".toColorInt())
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
            setPadding(dpToPx(32), dpToPx(24), dpToPx(32), dpToPx(24))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            clipChildren = false
            clipToPadding = false
        }
        
        val titleText = TextView(this).apply {
            text = getString(R.string.title_app)
            textSize = 44f
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
            contentDescription = getString(R.string.title_app)
        }
        
        val subtitleText = TextView(this).apply {
            text = getString(R.string.subtitle_app)
            textSize = 20f
            setTextColor("#B8C5D6".toColorInt())
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dpToPx(32)
            }
            contentDescription = getString(R.string.subtitle_app)
        }
        
        val menuLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            clipChildren = false
            clipToPadding = false
        }
        
        val randomDogsButton = createMenuButton(
            getString(R.string.menu_random_title),
            getString(R.string.menu_random_desc),
            "#FF6B9D".toColorInt()
        ).apply {
            contentDescription = getString(R.string.menu_random_title) + ". " + getString(R.string.menu_random_desc)
        }

        val browseBreedsButton = createMenuButton(
            getString(R.string.menu_browse_title),
            getString(R.string.menu_browse_desc),
            "#6B7FFF".toColorInt()
        ).apply {
            contentDescription = getString(R.string.menu_browse_title) + ". " + getString(R.string.menu_browse_desc)
        }

        randomDogsButton.setOnClickListener {
            animateButtonPress(randomDogsButton)
            val intent = Intent(this, RandomDogActivity::class.java)
            startActivity(intent)
        }
        
        browseBreedsButton.setOnClickListener {
            animateButtonPress(browseBreedsButton)
            val intent = Intent(this, BreedSelectionActivity::class.java)
            startActivity(intent)
        }
        
        menuLayout.addView(randomDogsButton)
        menuLayout.addView(browseBreedsButton)
        
        val instructionText = TextView(this).apply {
            text = getString(R.string.instruction_navigate)
            textSize = 16f
            setTextColor("#B8C5D6".toColorInt())
            gravity = Gravity.CENTER
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dpToPx(32)
            }
            contentDescription = getString(R.string.instruction_navigate)
        }
        
        contentLayout.addView(titleText)
        contentLayout.addView(subtitleText)
        contentLayout.addView(menuLayout)
        contentLayout.addView(instructionText)
        
        scrollView.addView(contentLayout)
        mainLayout.addView(scrollView)
        setContentView(mainLayout)
        
        // Request focus after layout is set
        randomDogsButton.post {
            randomDogsButton.requestFocus()
        }
    }
    
    private fun createMenuButton(title: String, description: String, color: Int): View {
        val card = CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                dpToPx(600),
                dpToPx(120)
            ).apply {
                setMargins(0, dpToPx(16), 0, dpToPx(16))
            }
            radius = dpToPx(16).toFloat()
            cardElevation = dpToPx(10).toFloat()
            setCardBackgroundColor(color)
            isFocusable = true
            isFocusableInTouchMode = true
        }
        
        val contentLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dpToPx(32), dpToPx(16), dpToPx(32), dpToPx(16))
        }
        
        val titleTextView = TextView(this).apply {
            text = title
            textSize = 26f
            setTextColor("#FFFFFFFF".toColorInt())
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dpToPx(4)
            }
        }
        
        val descriptionTextView = TextView(this).apply {
            text = description
            textSize = 15f
            setTextColor("#E0FFFFFF".toColorInt())
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        
        contentLayout.addView(titleTextView)
        contentLayout.addView(descriptionTextView)
        card.addView(contentLayout)
        
        card.setOnFocusChangeListener { view, hasFocus ->
            val scale = if (hasFocus) 1.05f else 1.0f
            val elevation = if (hasFocus) dpToPx(18).toFloat() else dpToPx(10).toFloat()
            
            view.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(200)
                .start()
                
            (view as CardView).cardElevation = elevation
        }
        
        return card
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
}
