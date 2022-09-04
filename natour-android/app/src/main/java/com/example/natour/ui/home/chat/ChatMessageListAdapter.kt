package com.example.natour.ui.home.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.natour.MainActivity
import com.example.natour.R
import com.example.natour.data.model.ChatMessage
import com.example.natour.databinding.ChatMessageViewBinding
import com.example.natour.util.toDrawable

class ChatMessageListAdapter(
    private val usernameMainUser: String,
    private val mChatViewModel: ChatViewModel
): ListAdapter<ChatMessage, ChatMessageListAdapter.ChatMessageViewHolder>(ChatMessageDiffCallBack) {

    companion object {
        private val ChatMessageDiffCallBack = object : DiffUtil.ItemCallback<ChatMessage>() {
            override fun areItemsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: ChatMessage, newItem: ChatMessage): Boolean =
                oldItem == newItem
        }
    }

    inner class ChatMessageViewHolder(
        private val binding: ChatMessageViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindChatMessage(chatMessage: ChatMessage) = with(binding) {
            setChatMessageTrailImage(chatMessage.idTrail)
            setupMessage(chatMessage)
            messageTextView.text = chatMessage.message
            timeMessageTextView.text = chatMessage.time
        }

        private fun setChatMessageTrailImage(idTrail: Long) {
            if (idTrail == 0L) return
            with(binding) {
                progressBarMessageImage.visibility = View.VISIBLE
                mChatViewModel.getTrailChatImage(idTrail) { image ->
                    messageImage.setImageDrawable(image)
                    cardMessageImage.visibility = View.VISIBLE
                    progressBarMessageImage.visibility = View.GONE
                }
            }
        }

        @SuppressLint("NewApi")
        private fun setupMessage(chatMessage: ChatMessage) = with(binding) {
            if (chatMessage.usernameOwner == usernameMainUser) {
                viewPositionMessage.layoutParams = LinearLayout.LayoutParams(0, 0, 1f)
                linearLayoutMessage.background = MainActivity.getDrawable(R.drawable.bg_send_message)

                messageTextView.setTextColor(MainActivity.getColor(R.color.white))

                viewTimePositionMessage.layoutParams = LinearLayout.LayoutParams(0, 0, 1f)
            } else {
                viewPositionMessage.layoutParams = LinearLayout.LayoutParams(0, 0, 0f)
                linearLayoutMessage.background = MainActivity.getDrawable(R.drawable.bg_receive_message)

                messageTextView.setTextColor(MainActivity.getColor(R.color.black))

                viewTimePositionMessage.layoutParams = LinearLayout.LayoutParams(0, 0, 0f)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatMessageViewHolder {
        val binding = ChatMessageViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChatMessageViewHolder(binding)
    }

    override fun onBindViewHolder(chatViewHolder: ChatMessageViewHolder, position: Int) {
        chatViewHolder.bindChatMessage(getItem(position))
    }
}