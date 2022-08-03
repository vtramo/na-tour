package com.example.natour.data.sources.impl

import android.content.Context
import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.sources.MainUserFileStoreDataSource
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainUserFileStoreDataSourceImpl(private val context: Context) : MainUserFileStoreDataSource {

    private companion object {
        const val USER_FILE = "user"
    }

    override fun isAlreadyLoggedIn(): Boolean {
        val fileList = context.fileList()
        return fileList.contains(USER_FILE)
    }

    override fun save(authenticationResponse: AuthenticationResponse) : Boolean {
        val fileOutputStream = context.openFileOutput(USER_FILE, Context.MODE_PRIVATE)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)
        objectOutputStream.writeObject(authenticationResponse)
        objectOutputStream.close()
        fileOutputStream.close()
        return true
    }

    override fun clear() = context.deleteFile(USER_FILE)

    override fun load(): AuthenticationResponse {
        val fileInputStream = context.openFileInput(USER_FILE)
        val objectInputStream = ObjectInputStream(fileInputStream)
        val authentication = objectInputStream.readObject() as AuthenticationResponse
        fileInputStream.close()
        objectInputStream.close()
        return authentication
    }
}