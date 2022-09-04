package com.example.natour.ui.home.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.data.MainUser
import com.example.natour.databinding.FragmentChatBinding
import com.example.natour.util.getCurrentTime

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val mChatViewModel: ChatViewModel
        by hiltNavGraphViewModels(R.id.home_nav_graph)

    private lateinit var mUsernameRecipient: String
    private var mIdRecipient: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.chatFragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mUsernameRecipient = getUsernameRecipient()
        mIdRecipient = getIdRecipient()

        binding.progressBarRecyclerView.visibility = View.VISIBLE
        binding.toolbarTitleUsernameTextView.text = mUsernameRecipient

        mChatViewModel.decrementUnreadMessages(
            mChatViewModel.focussedChat!!.id,
            MainUser.username
        )

        setupRecyclerViewMessages()
    }

    private fun getUsernameRecipient(): String {
        val usernameMainUser = MainUser.username
        return with(mChatViewModel.focussedChat!!) {
            if (usernameUser1 == usernameMainUser) usernameUser2
            else usernameUser1
        }
    }

    private fun getIdRecipient(): Long {
        val idMainUser = MainUser.id
        return with(mChatViewModel.focussedChat!!) {
            id - idMainUser
        }
    }

    private fun setupRecyclerViewMessages() {
        val recyclerViewMessages = binding.recyclerViewMessages
        val adapter = ChatMessageListAdapter(MainUser.username, mChatViewModel)
        recyclerViewMessages.adapter = adapter

        mChatViewModel.listOfMessagesFocussedChatLiveData.observe(viewLifecycleOwner) { listMessages ->
            adapter.submitList(listMessages) {
                recyclerViewMessages.layoutManager!!.scrollToPosition(listMessages.size - 1)
                binding.progressBarRecyclerView.visibility = View.GONE
            }
        }
    }

    fun onBackClick() {
        view?.findNavController()?.popBackStack()
    }

    fun onSendClick() {
        with(binding.editTextChatMessage) {
            val message = text.toString()
            if (message.isBlank()) return

            mChatViewModel.sendMessage(
                message,
                getCurrentTime(),
                mUsernameRecipient,
                mIdRecipient
            ) { }

            text = null
        }
    }

    override fun onDestroy() {
        mChatViewModel.focussedChat = null
        super.onDestroy()
    }
}