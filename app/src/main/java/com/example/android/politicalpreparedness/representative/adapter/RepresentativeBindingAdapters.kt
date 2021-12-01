package com.example.android.politicalpreparedness.representative.adapter

import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.utils.CircleTransform
import com.example.android.politicalpreparedness.utils.fadeIn
import com.example.android.politicalpreparedness.utils.fadeOut
import com.squareup.picasso.Picasso

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        Picasso.with(view.context)
            .load(uri)
            .transform(CircleTransform())
            .error(R.drawable.ic_profile)
            .placeholder(R.drawable.ic_profile)
            .into(view)
    }
}

@BindingAdapter("representativeData")
fun bindRepresentativesRecyclerView(recyclerView: RecyclerView, data: List<Representative>?) {
    val adapter = recyclerView.adapter as RepresentativeListAdapter
    adapter.submitList(data)
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)
    val position = when (adapter.getItem(0)) {
        is String -> adapter.getPosition(value)
        else -> this.selectedItemPosition
    }
    if (position >= 0) {
        setSelection(position)
    }
    onItemSelectedListener
}

@BindingAdapter("showProgress")
fun bindProgressView(view: ProgressBar, loadingData: LiveData<Boolean?>) {

    loadingData.value?.let {
        if (it) {
            view.fadeIn()
        } else {
            view.fadeOut()
        }
    }
}

inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T> {
    return adapter as ArrayAdapter<T>
}
