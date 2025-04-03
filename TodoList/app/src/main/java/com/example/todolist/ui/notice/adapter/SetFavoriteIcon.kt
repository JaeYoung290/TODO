package com.example.todolist.ui.notice.adapter

import android.widget.ImageButton
import androidx.databinding.BindingAdapter
import com.example.todolist.R

@BindingAdapter("isFavorite")
fun setFavoriteIcon(button: ImageButton, isFavorite: Boolean) {
    if (isFavorite) {
        button.setImageResource(R.drawable.ic_star_selected)
    } else {
        button.setImageResource(R.drawable.ic_star)
    }
}