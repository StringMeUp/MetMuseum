package com.sr.metmuseum.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sr.metmuseum.databinding.IdsItemBinding

class IdsAdapter : RecyclerView.Adapter<IdsAdapter.IdsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return IdsViewHolder(IdsItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: IdsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size

    class IdsViewHolder(private val binding: IdsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Int) {
            binding.idTextView.text = item.toString()
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
}