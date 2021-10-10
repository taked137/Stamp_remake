package com.taked.stamp.view.main.fragment.info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.taked.stamp.databinding.InfoItemBinding
import com.taked.stamp.model.api.Message

class InfoItemAdapter(private val onClickListener: OnClickListener) :
    PagingDataAdapter<Message, InfoItemAdapter.ViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = getItem(position)!!
        holder.binding.infoItem.text = message.message
        holder.itemView.setOnClickListener {
            onClickListener.onClick(message)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(val binding: InfoItemBinding) : RecyclerView.ViewHolder(binding.root)
    class OnClickListener(val clickListener: (message: Message) -> Unit) {
        fun onClick(message: Message) = clickListener(message)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                // 本当はユニークなidなどで比較する
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }
}
