package com.example.android.politicalpreparedness.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.model.ElectionModel

@BindingAdapter("setVisible")
fun setVisible(view: View, count: Int?) {
    if (count != null) {
        view.visibility = if (count > 0) View.VISIBLE else View.GONE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ElectionModel>?) {
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}

@BindingAdapter("showError")
fun showError(view: View, count: Int?) {
    if (count != null) {
        view.visibility = if (count > 0) View.GONE else View.VISIBLE
    } else {
        view.visibility = View.VISIBLE
    }
}

@BindingAdapter("refreshing")
fun bindRefreshing(refreshView: SwipeRefreshLayout, loading: Boolean?) {
    refreshView.isRefreshing = loading == true
}

@BindingAdapter("voterActionLabel")
fun bindButtonText(textView: TextView, saved: Boolean?) {
    if (saved != null) {
        val text = textView.context.getString(if (saved) R.string.remove_from_saved else R.string.add_to_saved)
        textView.fadeIn()
        textView.text = text
        textView.contentDescription = text
    } else {
        textView.visibility = View.GONE
    }
}

//animate changing the view visibility
fun View.fadeIn() {
    this.visibility = View.VISIBLE
    this.alpha = 0f
    this.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeIn.alpha = 1f
        }
    })
}

//animate changing the view visibility
fun View.fadeOut() {
    this.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeOut.alpha = 1f
            this@fadeOut.visibility = View.GONE
        }
    })
}
