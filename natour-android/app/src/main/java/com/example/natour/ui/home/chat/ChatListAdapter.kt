package com.example.natour.ui.home.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.natour.data.MainUser
import com.example.natour.data.model.Chat
import com.example.natour.databinding.ChatViewBinding

class ChatListAdapter (
    private val chatClickListener: (Chat) -> Unit,
    private val mChatViewModel: ChatViewModel
): ListAdapter<Chat, ChatListAdapter.ChatViewHolder>(ChatDiffCallBack) {

    companion object {
        private val ChatDiffCallBack = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean =
                oldItem == newItem
        }
    }

    inner class ChatViewHolder(
        private val binding: ChatViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindChat(chat: Chat) = with(binding) {
            setChatImage(chat)
            lastMessageTextView.text = formatLastMessage(getLastMessageContent(chat))
            setUnreadMessages(chat.totalUnreadMessages)
            usernameChatTextView.text = getUsername(chat)
            dateTextView.text = getLastMessageDate(chat)
        }

        private fun setChatImage(chat: Chat) {
            binding.progressBarChatImage.visibility = View.VISIBLE
            mChatViewModel.getTrailChatImage(chat.idTrail) { image ->
                binding.chatImage.setImageDrawable(image)
                binding.progressBarChatImage.visibility = View.GONE
            }
        }

        private fun getLastMessageContent(chat: Chat): String =
            with(chat.messages) {
                if (isEmpty()) ""
                else last().message
            }

        private fun getLastMessageDate(chat: Chat): String =
            with(chat.messages) {
                if (isEmpty()) ""
                else last().date
            }

        private fun formatLastMessage(lastMessage: String): String =
            if (lastMessage.length <= 83) lastMessage
            else lastMessage.substring(0..83).plus("...")

        private fun setUnreadMessages(totalUnreadMessages: Int) {
            with(binding) {
                if (totalUnreadMessages == 0) {
                    counterUnreadMessagesTextView.visibility = View.GONE
                    return
                }

                counterUnreadMessagesTextView.text = totalUnreadMessages.toString()
                counterUnreadMessagesTextView.visibility = View.VISIBLE
            }
        }

        private fun getUsername(chat: Chat): String = with(chat) {
            if (usernameUser1 == MainUser.username) {
                usernameUser2
            } else usernameUser1
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatViewHolder {
        val binding = ChatViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        val viewHolder = ChatViewHolder(binding)

        binding.chatConstraintLayout.setOnClickListener {
            val position = viewHolder.adapterPosition
            chatClickListener(getItem(position))
        }

        return viewHolder
    }

    override fun onBindViewHolder(chatViewHolder: ChatViewHolder, position: Int) {
        chatViewHolder.bindChat(getItem(position))
    }
}