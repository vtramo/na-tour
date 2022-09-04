package com.example.natour.ui.home.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import com.example.natour.R
import com.example.natour.databinding.FragmentInboxBinding

class InboxFragment : Fragment() {

    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!

    private lateinit var mChatListAdapter: ChatListAdapter

    private val mChatViewModel: ChatViewModel
        by hiltNavGraphViewModels(R.id.home_nav_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.inboxFragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupChatRecyclerView()
    }

    private fun setupChatRecyclerView() {
        val chatsRecyclerView = binding.recyclerViewChats
        mChatListAdapter = ChatListAdapter({
            mChatViewModel.setFocussedChat(it.id)
            goToChatFragment()
        }, mChatViewModel)
        chatsRecyclerView.adapter = mChatListAdapter

        binding.progressBarRecyclerView.visibility = View.VISIBLE
        mChatViewModel.chatsLiveData.observe(viewLifecycleOwner) { listChats ->
            mChatListAdapter.submitList(listChats)
            binding.progressBarRecyclerView.visibility = View.GONE
        }
    }

    private fun goToChatFragment() {
        val action = InboxFragmentDirections.actionInboxFragmentToChatFragment()
        view?.findNavController()?.navigate(action)
    }

    fun onBackClick() {
        view?.findNavController()?.popBackStack()
    }
}