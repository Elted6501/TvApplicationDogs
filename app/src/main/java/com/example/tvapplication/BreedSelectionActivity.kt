package com.example.tvapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.graphics.toColorInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BreedSelectionActivity : FragmentActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var titleText: TextView
    private val breeds = mutableListOf<Breed>()

    private var currentCall: Call<BreedsResponse>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val mainLayout = FrameLayout(this).apply {
            setBackgroundColor("#0A0E27".toColorInt())
        }
        
        val contentLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dpToPx(48), dpToPx(40), dpToPx(48), dpToPx(40))
        }
        
        titleText = TextView(this).apply {
            text = getString(R.string.breed_select_title)
            textSize = 48f
            setTextColor("#FFFFFFFF".toColorInt())
            gravity = Gravity.CENTER
            typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.BOLD)
            setShadowLayer(8f, 0f, 4f, "#80000000".toColorInt())
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = dpToPx(32)
            }
            contentDescription = getString(R.string.breed_select_title)
        }
        
        val recyclerContainer = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
        
        recyclerView = RecyclerView(this).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            layoutManager = GridLayoutManager(this@BreedSelectionActivity, 4)
            setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(20))
            clipToPadding = false
            clipChildren = false
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
        
        recyclerContainer.addView(recyclerView)
        recyclerContainer.addView(loadingProgressBar)
        
        contentLayout.addView(titleText)
        contentLayout.addView(recyclerContainer)
        
        mainLayout.addView(contentLayout)
        setContentView(mainLayout)
        
        fetchBreeds()
    }
    
    private fun fetchBreeds() {
        loadingProgressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        
        currentCall?.cancel()
        currentCall = RetrofitClient.instance.getAllBreeds()
        currentCall?.enqueue(object : Callback<BreedsResponse> {
            override fun onResponse(call: Call<BreedsResponse>, response: Response<BreedsResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val breedMap = response.body()!!.message
                    breeds.clear()
                    
                    breedMap.forEach { (breedName, subBreeds) ->
                        val displayName = breedName.replaceFirstChar { it.uppercase() }
                        breeds.add(Breed(breedName, displayName, subBreeds, subBreeds.isNotEmpty()))
                    }
                    
                    breeds.sortBy { it.displayName }
                    
                    recyclerView.adapter = BreedAdapter(breeds) { breed, subBreed ->
                        onBreedSelected(breed, subBreed)
                    }
                    
                    loadingProgressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    
                    Log.d("BreedSelection", "Loaded ${breeds.size} breeds")
                } else {
                    loadingProgressBar.visibility = View.GONE
                    Toast.makeText(this@BreedSelectionActivity, getString(R.string.failed_load_breeds), Toast.LENGTH_SHORT).show()
                }
            }
            
            override fun onFailure(call: Call<BreedsResponse>, t: Throwable) {
                if (call.isCanceled) return
                Log.e("BreedSelection", "Error loading breeds", t)
                loadingProgressBar.visibility = View.GONE
                Toast.makeText(this@BreedSelectionActivity, getString(R.string.network_error, t.message ?: ""), Toast.LENGTH_SHORT).show()
            }
        })
    }
    
    private fun onBreedSelected(breed: Breed, subBreed: String?) {
        val intent = Intent(this, BreedDetailsActivity::class.java)
        intent.putExtra("breed_name", breed.name)
        intent.putExtra("breed_display_name", breed.displayName)
        intent.putExtra("sub_breed", subBreed)
        startActivity(intent)
    }
    
    private fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    override fun onDestroy() {
        currentCall?.cancel()
        super.onDestroy()
    }
}