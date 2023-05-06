package com.sr.metmuseum.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sr.metmuseum.databinding.DefaultItemBinding
import com.sr.metmuseum.databinding.ErrorItemBinding
import com.sr.metmuseum.databinding.IdsItemBinding
import com.sr.metmuseum.databinding.OtherItemBinding
import com.sr.metmuseum.util.clickWithDebounce

class IdsAdapter(private val onItemClick: (ArtItem) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = mutableListOf<ArtItem>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            MainViewModel.ObjectType.ART.ordinal -> IdsViewHolder(IdsItemBinding.inflate(inflater,
                parent,
                false))
            MainViewModel.ObjectType.ERROR.ordinal -> ErrorViewHolder(ErrorItemBinding.inflate(
                inflater,
                parent,
                false))
            MainViewModel.ObjectType.DEFAULT.ordinal -> DefaultViewHolder(DefaultItemBinding.inflate(
                inflater,
                parent,
                false))
            else -> EmptyStateViewHolder(OtherItemBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is IdsViewHolder -> holder.bind(items[position])
            is ErrorViewHolder -> holder.bind(items[position])
            is DefaultViewHolder -> holder.bind(items[position])
            is EmptyStateViewHolder -> holder.bind(items[position])
        }

        holder.itemView.clickWithDebounce {
            onItemClick.invoke(items[position])
        }
    }

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = items[position].type.ordinal

    class IdsViewHolder(private val binding: IdsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArtItem) {
            binding.idTextView.text = item.id.toString()
        }
    }

    class ErrorViewHolder(private val binding: ErrorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArtItem) {
            binding.errorTextView.text = binding.root.context.getString(item.id)
        }
    }

    class DefaultViewHolder(private val binding: DefaultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArtItem) {
            binding.defaultTextView.text = binding.root.context.getString(item.id)
        }
    }

    class EmptyStateViewHolder(private val binding: OtherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArtItem) {
            binding.otherTextView.text = binding.root.context.getString(item.id)
        }
    }
}