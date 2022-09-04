package com.example.natour.ui.home.chat

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.*
import com.example.natour.MainActivity
import com.example.natour.R
import com.example.natour.data.model.Chat
import com.example.natour.data.model.ChatMessage
import com.example.natour.data.repositories.ChatRepository
import com.example.natour.data.repositories.MainUserRepository
import com.example.natour.network.StompService
import com.example.natour.util.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ua.naiksoftware.stomp.dto.StompMessage
import javax.inject.Inject
import com.example.natour.network.services.StompClientService.Companion.STOMP_TAG

@SuppressLint("CheckResult")
@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val mainUserRepository: MainUserRepository,
    private val stompClientService: StompService
) : ViewModel() {

    private val chats = mutableListOf<Chat>()

    private val _chatsLiveData = MutableLiveData<List<Chat>>(listOf())
    val chatsLiveData: LiveData<List<Chat>> get() = _chatsLiveData

    private val chatById = mutableMapOf<Long, Chat>()

    private var _focussedChat: Chat? = null
    var focussedChat
        get() = _focussedChat
        set(value) { _focussedChat = value }

    private var _listOfMessagesFocussedChatLiveData = MutableLiveData<List<ChatMessage>>()
    val listOfMessagesFocussedChatLiveData get() = _listOfMessagesFocussedChatLiveData

    private val _totalUnreadMessagesLiveData = MutableLiveData(0)
    val totalUnreadMessagesLiveData: LiveData<Int> get() = _totalUnreadMessagesLiveData

    init {
        loadChats()
        connectStompClient()
    }

    private fun loadChats() = viewModelScope.launch {
        val idMainUser = mainUserRepository.getDetails().id
        chatRepository.loadChats(idMainUser)
            .catch { handleErrors() }
            .collect { listOfChats ->
                chats.addAll(listOfChats)
                _chatsLiveData.value = chats

                var totalUnreadMessages = 0

                listOfChats.forEach { chat ->
                    chatById[chat.id] = chat
                    totalUnreadMessages += chat.totalUnreadMessages
                }

                _totalUnreadMessagesLiveData.value = totalUnreadMessages
            }
    }

    private fun connectStompClient() {
        with(stompClientService) {
            lastMessage.observeForever(this@ChatViewModel::handleNewStompMessage)

            connect(
                onConnected = {
                    Log.i(STOMP_TAG, "Stomp client connected")
                },
                onClosed = { Log.i(STOMP_TAG, "Stomp client closed") },
                onError = { exception ->
                    Log.e(STOMP_TAG, "Error in connecting stomp client", exception)
                },
                onFailedServerHeartBeat = { Log.i(STOMP_TAG, "onFailedServerHeartBeat") }
            )
        }
    }

    @SuppressLint("NewApi")
    private fun handleNewStompMessage(stompMessage: StompMessage) {
        val chatMessage: ChatMessage = Json.decodeFromString(stompMessage.payload)
        Log.i("STOMP MESSAGE:", chatMessage.toString())

        with(chatMessage) {
            val chat = chatById.computeIfAbsent(idChat) {
                createNewLocalChat(idChat, usernameOwner, idTrail)
            }
            chat.addMessage(this)

            if (!chat.isFocussed()) {
                chat.incrementUnreadMessages(usernameRecipient!!)
                updateTotalUnreadMessagesLiveData(1)
            } else {
                chat.resetUnreadMessages(usernameRecipient!!)
            }
        }
    }

    fun setFocussedChat(chatId: Long) {
        val chat = chatById[chatId]!!
        _listOfMessagesFocussedChatLiveData = MutableLiveData()
        _focussedChat = chat
        _listOfMessagesFocussedChatLiveData.value = chat.messages
    }

    private fun Chat.incrementUnreadMessages(usernameOwner: String) {
        totalUnreadMessages++
        viewModelScope.launch {
            chatRepository.updateUnreadMessages(
                id,
                usernameOwner,
                totalUnreadMessages
            )
        }
    }

    private fun Chat.resetUnreadMessages(usernameOwner: String) {
        totalUnreadMessages = 0
        viewModelScope.launch {
            chatRepository.updateUnreadMessages(
                id,
                usernameOwner,
                0
            )
        }
    }

    @SuppressLint("NewApi")
    fun decrementUnreadMessages(idChat: Long, usernameOwner: String) {
        chatById.computeIfPresent(idChat) { _, chat ->
            if (chat.totalUnreadMessages != 0) {
                updateTotalUnreadMessagesLiveData(-chat.totalUnreadMessages)
                chat.resetUnreadMessages(usernameOwner)
            }
            return@computeIfPresent chat
        }
    }

    private fun updateTotalUnreadMessagesLiveData(amount: Int) {
        _totalUnreadMessagesLiveData.value = _totalUnreadMessagesLiveData.value?.plus(amount)
    }

    private fun Chat.addMessage(chatMessage: ChatMessage) {
        val newListMessages = mutableListOf<ChatMessage>()
        newListMessages.addAll(messages)
        newListMessages.add(chatMessage)
        messages = newListMessages
        if (isFocussed()) {
            _listOfMessagesFocussedChatLiveData.value = messages
        }
    }

    private fun Chat.isFocussed(): Boolean = focussedChat == this

    private fun createNewLocalChat(
        idChat: Long,
        usernameRecipient: String,
        idTrail: Long
    ): Chat {
        val usernameMainUser = mainUserRepository.getDetails().username

        val chat = Chat(
            id = idChat,
            idTrail = idTrail,
            messages = mutableListOf(),
            totalUnreadMessages = 0,
            usernameUser1 = usernameMainUser,
            usernameUser2 = usernameRecipient
        )

        chatById[idChat] = chat
        chats.add(chat)
        _chatsLiveData.value = chats

        return chat
    }

    @SuppressLint("NewApi")
    fun sendMessage(
        message: String,
        time: String,
        usernameRecipient: String,
        idRecipient: Long,
        idTrail: Long = 0,
        onComplete: (Boolean) -> Unit
    ) {
        val idChat = idRecipient + mainUserRepository.getDetails().id

        val chatMessage = ChatMessage(
            idChat = idChat,
            idTrail = idTrail,
            usernameOwner = mainUserRepository.getDetails().username,
            usernameRecipient = usernameRecipient,
            date = getCurrentDate(),
            message = message,
            time = time
        )

        getChat(idChat, idRecipient, usernameRecipient, idTrail) { chat ->
            Log.i("CHATVIEWMODEL", "GetChat Success")
            chat.addMessage(chatMessage)

            stompClientService.sendMessage(
                chatMessage,
                onSend = { onComplete(true)  },
                onError = { onComplete(false) }
            )
        }
    }

    private fun getChat(
        idChat: Long,
        idRecipient: Long,
        usernameRecipient: String,
        idTrail: Long = 0,
        onComplete: (Chat) -> Unit
    ) = viewModelScope.launch {
        val mainUserId = mainUserRepository.getDetails().id
        if (chatById.containsKey(idChat)) {
            onComplete(chatById[idChat]!!)
        } else {
            val chat = createNewLocalChat(idChat, usernameRecipient, idTrail)
            createNewRemoteChat(mainUserId, idRecipient, idTrail) {
                onComplete(chat)
            }
        }
    }

    private suspend fun createNewRemoteChat(
        idOwner: Long,
        idRecipient: Long,
        idTrail: Long,
        onComplete: (Boolean) -> Unit
    ) {
        onComplete(chatRepository.createChat(idOwner, idRecipient, idTrail))
    }

    fun existsChatById(idChat: Long): Boolean = chatById.containsKey(idChat)

    fun setTrailToChat(idChat: Long, idTrail: Long) {
        chatById[idChat]!!.idTrail = idTrail
        viewModelScope.launch { chatRepository.setTrailToChat(idChat, idTrail) }
    }

    fun getTrailChatImage(idTrail: Long, action: (Drawable) -> Unit) = viewModelScope.launch {
        val result = chatRepository.getTrailChatImage(idTrail)
        val image = result.getOrNull()
        action(image ?: MainActivity.getDrawable(R.drawable.sentiero_degli_dei_vista)!!)
    }

    private fun handleErrors() {
        // TODO
    }

    override fun onCleared() {
        super.onCleared()
        stompClientService.disconnect()
    }
}