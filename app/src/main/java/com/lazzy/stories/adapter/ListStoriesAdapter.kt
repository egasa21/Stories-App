package com.lazzy.stories.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lazzy.stories.data.remote.response.ListStoryItem
import com.lazzy.stories.databinding.ItemListBinding
import com.lazzy.stories.tools.withDateFormat

class ListStoriesAdapter(private val uploadedOn: String) : PagingDataAdapter<ListStoryItem, ListStoriesAdapter.ListViewholder>(DIFF_CALLBACK){
    inner class ListViewholder(
        var binding: ItemListBinding,
        private val uploadedOn: String
    ): RecyclerView.ViewHolder(binding.root){
        init {
            itemView.rootView.setOnClickListener { itemRootView ->
                val story = getItem(bindingAdapterPosition)
                story?.let {
                    onItemClick?.invoke(itemRootView, it)
                }
            }
        }

        fun bind(data: ListStoryItem){
            binding.apply {
                Glide.with(itemView)
                    .load(data.photoUrl)
                    .into(ivPhoto)

                val uploaded = StringBuilder(uploadedOn).append(" ")
                    .append(data.createdAt.withDateFormat())

                tvItemName.text = data.name
                tvDesc.text = data.description
                tvUploaded.text = uploaded
            }
        }
    }

    var onItemClick : ((View, ListStoryItem) -> Unit)? = null



    override fun onBindViewHolder(holder: ListStoriesAdapter.ListViewholder, position: Int) {
        val data = getItem(position)
        if (data != null){
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListStoriesAdapter.ListViewholder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewholder(binding, uploadedOn)
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}