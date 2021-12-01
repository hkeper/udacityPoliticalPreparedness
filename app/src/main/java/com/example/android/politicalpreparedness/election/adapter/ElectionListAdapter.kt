package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ViewholderElectionBinding
import com.example.android.politicalpreparedness.election.model.ElectionModel
import java.text.DateFormat

class ElectionListAdapter(val dateFormatter: DateFormat, private val clickListener: ElectionListener) : ListAdapter<ElectionModel, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener, dateFormatter)
    }

    companion object ElectionDiffCallback : DiffUtil.ItemCallback<ElectionModel>() {
        override fun areItemsTheSame(oldItem: ElectionModel, newItem: ElectionModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ElectionModel, newItem: ElectionModel): Boolean {
            return oldItem == newItem
        }
    }

    class ElectionViewHolder(val binding: ViewholderElectionBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup) = ElectionViewHolder(
                ViewholderElectionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        fun bind(election: ElectionModel, listener: ElectionListener, dateFormatter: DateFormat) {
            binding.election = election
            binding.listener = listener
            binding.dateFormatter = dateFormatter
            binding.executePendingBindings()
        }
    }
}

class ElectionListener(val clickListener: (election: ElectionModel) -> Unit) {
    fun onClick(election: ElectionModel) = clickListener(election)
}
