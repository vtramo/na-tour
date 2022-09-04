package com.example.natour.ui.home.trail.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.example.natour.R
import com.example.natour.databinding.DialogFragmentSendMessageBinding
import com.example.natour.ui.MainUserViewModel
import com.example.natour.ui.home.chat.ChatViewModel
import com.example.natour.util.createProgressAlertDialog
import com.example.natour.util.getCurrentTime

class SendMessageDialogFragment: DialogFragment() {

    private val mTrailDetailsViewModel: TrailDetailsViewModel
        by hiltNavGraphViewModels(R.id.home_nav_graph)

    private val mMainUserViewModel: MainUserViewModel by activityViewModels()

    private val mChatViewModel: ChatViewModel
        by hiltNavGraphViewModels(R.id.home_nav_graph)

    companion object {
        const val TAG = "SendMessageDialogFragment"
    }

    private var _binding: DialogFragmentSendMessageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentSendMessageBinding.inflate(
            layoutInflater,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.sendMessageDialogFragment = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogWindow()
        setupViews()
    }

    private fun setupDialogWindow() {
        val dialog = dialog!!
        val window = dialog.window!!
        dialog.setContentView(binding.root)
        window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        val params = window.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.attributes = params
        window.setGravity(Gravity.BOTTOM)
    }

    @SuppressLint("SetTextI18n")
    private fun setupViews() {
        with(binding) {
            sendAMessageLabelTextView.text =
                "Send a message to ${mTrailDetailsViewModel.thisTrail.owner.username}"

            messageTextInputEditText.addTextChangedListener {
                sendButton.isEnabled = it!!.isNotBlank()
            }
        }
    }

    fun onSendMessage() {
        val progressDialog = createProgressAlertDialog(
            "Sending the message...",
            requireParentFragment().requireContext()
        )
        progressDialog.show()

        val message = binding.messageTextInputEditText.text!!.toString()

        with(mChatViewModel) {
            sendMessage(
                message = message,
                usernameRecipient = mTrailDetailsViewModel.thisTrail.owner.username,
                idRecipient = mTrailDetailsViewModel.thisTrail.owner.id,
                time = getCurrentTime(),
                idTrail = mTrailDetailsViewModel.thisTrail.idTrail
            ) { isSend ->
                if (isSend) {
                    val idMainUser = mMainUserViewModel.mainUser.value!!.id
                    val idTrailOwner = mTrailDetailsViewModel.thisTrail.owner.id
                    val idChat = idMainUser + idTrailOwner
                    mChatViewModel.setFocussedChat(idChat)
                    goToChatFragment()
                }
                progressDialog.dismiss()
                dismiss()
            }
        }
    }

    private fun goToChatFragment() {
        val action = TrailDetailsFragmentDirections.actionTrailDetailFragmentToChatFragment()
        parentFragment?.findNavController()?.navigate(action)
    }
}