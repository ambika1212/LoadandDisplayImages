package com.example.loadanddisplayimagesscrollablegrid

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loadanddisplayimagesscrollablegrid.data.ImageAdapter
import com.example.loadanddisplayimagesscrollablegrid.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var rvImages: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        mainViewModel = MainViewModel()
        subscribe()

        rvImages = findViewById(R.id.rv_images)

        rvImages.layoutManager = GridLayoutManager(this, 3)
        rvImages.addItemDecoration(SpaceItemDecoration(10))

        Handler(Looper.getMainLooper()).postDelayed({
            mainViewModel.getImagesData()
        }, 2000)
    }


    private fun subscribe() {
        mainViewModel.isLoading.observe(this) { isLoading ->
            // Set the result text to Loading
            if (isLoading) {
                Toast.makeText(this, "Loading images...", Toast.LENGTH_LONG).show()
            }

            mainViewModel.isError.observe(this) { isError ->
                if (isError) {
                    Toast.makeText(this, "Error: ${mainViewModel.errorMessage}", Toast.LENGTH_LONG)
                        .show()

                }
            }

            mainViewModel.imagesData.observe(this) { imagesData ->
                // Display weather data to the UI
                rvImages.adapter = ImageAdapter(imagesData)
            }
        }

    }



    class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect, view: View,
            parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space * 6

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space
            }
        }
    }
}