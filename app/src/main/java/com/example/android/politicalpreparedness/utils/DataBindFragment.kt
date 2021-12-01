package com.example.android.politicalpreparedness.utils

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class DataBindFragment<T: ViewDataBinding>: Fragment() {

    protected var _binding: T? = null

    val binding: T
        get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}