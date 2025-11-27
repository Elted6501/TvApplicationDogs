package com.example.tvapplication

import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.toColorInt

class BreedAdapter(
    private val breeds: List<Breed>,
    private val onBreedClick: (Breed, String?) -> Unit
) : RecyclerView.Adapter<BreedAdapter.BreedViewHolder>() {

    class BreedViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView) {
        val layout: LinearLayout = cardView.getChildAt(0) as LinearLayout
        val nameTextView: TextView = layout.getChildAt(0) as TextView
        val subBreedTextView: TextView = layout.getChildAt(1) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder {
        val context = parent.context
        
        val card = CardView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                dpToPx(context, 240),
                dpToPx(context, 110)
            ).apply {
                setMargins(dpToPx(context, 12), dpToPx(context, 12), dpToPx(context, 12), dpToPx(context, 12))
            }
            radius = dpToPx(context, 12).toFloat()
            cardElevation = dpToPx(context, 6).toFloat()
            setCardBackgroundColor("#1E2749".toColorInt())
            isFocusable = true
            isFocusableInTouchMode = true
        }
        
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dpToPx(context, 16), dpToPx(context, 16), dpToPx(context, 16), dpToPx(context, 16))
        }
        
        val breedName = TextView(context).apply {
            textSize = 17f
            setTextColor("#FFFFFFFF".toColorInt())
            gravity = Gravity.CENTER
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
            maxLines = 2
        }
        
        val subBreedIndicator = TextView(context).apply {
            textSize = 12f
            setTextColor("#FF6B9D".toColorInt())
            gravity = Gravity.CENTER
            visibility = View.GONE
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = dpToPx(context, 4)
            }
        }
        
        layout.addView(breedName)
        layout.addView(subBreedIndicator)
        card.addView(layout)
        
        card.setOnFocusChangeListener { view, hasFocus ->
            val scale = if (hasFocus) 1.05f else 1.0f
            val elevation = if (hasFocus) dpToPx(context, 14).toFloat() else dpToPx(context, 6).toFloat()
            
            view.animate()
                .scaleX(scale)
                .scaleY(scale)
                .setDuration(200)
                .start()
                
            (view as CardView).cardElevation = elevation
        }
        
        return BreedViewHolder(card)
    }

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) {
        val breed = breeds[position]
        val context = holder.cardView.context

        holder.nameTextView.text = breed.displayName

        if (breed.hasSubBreeds) {
            holder.subBreedTextView.visibility = View.VISIBLE
            val quantity = breed.subBreeds.size
            holder.subBreedTextView.text = context.resources.getQuantityString(R.plurals.subbreed_varieties, quantity, quantity)
        } else {
            holder.subBreedTextView.visibility = View.GONE
        }

        holder.cardView.contentDescription = buildString {
            append(breed.displayName)
            if (breed.hasSubBreeds) {
                val quantity = breed.subBreeds.size
                append(", ")
                append(context.resources.getQuantityString(R.plurals.subbreed_varieties, quantity, quantity))
            }
        }
        
        holder.cardView.setOnClickListener {
            onBreedClick(breed, null)
        }
    }

    override fun getItemCount() = breeds.size

    private fun dpToPx(context: android.content.Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}
