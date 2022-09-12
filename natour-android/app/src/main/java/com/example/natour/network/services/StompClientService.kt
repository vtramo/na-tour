package com.example.natour.network.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.natour.data.model.ChatMessage
import com.example.natour.network.StompService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompHeader
import ua.naiksoftware.stomp.dto.StompMessage

class StompClientService() : Service(), StompService {

    companion object {
        private const val WS_URL = "ws://natour-lb-1117664134.us-east-1.elb.amazonaws.com:80/ws/websocket"
        private const val DESTINATION = "/app/chat"
        private const val DESTINATION_PATH = "/users/queue/messages"
        const val STOMP_TAG = "StompClient"
    }

    private lateinit var username: String
    private lateinit var usernameHeader: List<StompHeader>

    private val mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WS_URL)
    private lateinit var mCompositeDisposable: CompositeDisposable

    private val _lastMessage = MutableLiveData<StompMessage>()
    override val lastMessage: LiveData<StompMessage> get() = _lastMessage

    init {
        resetSubscriptions()
    }

    override fun setUsername(username: String) {
        this.username = username
        usernameHeader = mutableListOf(StompHeader("username", username))
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun connect(
        onConnected: () -> Unit,
        onError: (Exception) -> Unit,
        onClosed: () -> Unit,
        onFailedServerHeartBeat: () -> Unit
    ) {
        with(mStompClient) {

            withClientHeartbeat(1000)
                .withServerHeartbeat(1000)

            observeLifecycle(
                onConnected =  onConnected,
                onError     =  onError,
                onClosed    =  onClosed,
                onFailedServerHeartBeat = onFailedServerHeartBeat
            )

            listenToIncomingMessages()
            connect(usernameHeader)
        }
    }

    override fun reconnect() {
        listenToIncomingMessages()
        mStompClient.connect(usernameHeader)
    }

    private fun StompClient.observeLifecycle(
        onConnected: () -> Unit,
        onError: (Exception) -> Unit,
        onClosed: () -> Unit,
        onFailedServerHeartBeat: () -> Unit
    ) {
        val disposableLifecycle = lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { event ->
                when(event.type!!) {
                    LifecycleEvent.Type.OPENED -> onConnected()
                    LifecycleEvent.Type.ERROR -> onError(event.exception)
                    LifecycleEvent.Type.CLOSED -> onClosed()
                    LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT -> onFailedServerHeartBeat()
                }
            }
        mCompositeDisposable.add(disposableLifecycle)
    }

    private fun listenToIncomingMessages() {
        with(mStompClient) {
            val topicDisposable = topic(DESTINATION_PATH)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this@StompClientService::updateLastMessage) { throwable ->
                    Log.e(STOMP_TAG, "Error on subscribe topic", throwable)
                }
            mCompositeDisposable.add(topicDisposable)
        }
    }

    private fun updateLastMessage(stompMessage: StompMessage) {
        _lastMessage.value = stompMessage
    }

    private fun resetSubscriptions() {
        if (this::mCompositeDisposable.isInitialized) mCompositeDisposable.dispose()
        mCompositeDisposable = CompositeDisposable()
    }

    override fun sendMessage(chatMessage: ChatMessage, onSend: () -> Unit, onError: () -> Unit) {
        mCompositeDisposable.add(
            mStompClient.send(DESTINATION, Json.encodeToString(chatMessage))
                .compose { upstream ->
                    upstream
                        .unsubscribeOn(Schedulers.newThread())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                }
                .subscribe(
                    { onSend() },
                    { throwable ->
                        Log.e(STOMP_TAG, "Error send STOMP Message", throwable)
                        onError()
                    }
                )
        )
    }

    override fun disconnect() {
        mStompClient.disconnect()

        if (this::mCompositeDisposable.isInitialized) mCompositeDisposable.dispose()
        super.onDestroy()
    }
}