package com.example.natour.data.sources.impl

import android.content.Context
import com.example.natour.data.model.AuthenticatedUser
import com.example.natour.data.model.AuthenticationResponse
import com.example.natour.data.sources.MainUserDataSource
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class MainUserLocalDataSource(
    private val context: Context,
) : MainUserDataSource {

    private companion object {
        const val USER_FILE = "user"
    }

    override suspend fun isAlreadyLoggedIn(): Boolean {
        val fileList = context.fileList()
        return fileList.contains(USER_FILE)
    }

    override suspend fun save(authenticatedUser: AuthenticatedUser) : Boolean {
        val fileOutputStream = context.openFileOutput(USER_FILE, Context.MODE_PRIVATE)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)
        objectOutputStream.writeObject(authenticatedUser)
        objectOutputStream.close()
        fileOutputStream.close()
        return true
    }

    override suspend fun clear() = context.deleteFile(USER_FILE)

    override suspend fun load(): AuthenticatedUser {
        val fileInputStream = context.openFileInput(USER_FILE)
        val objectInputStream = ObjectInputStream(fileInputStream)
        val authentication = objectInputStream.readObject() as AuthenticatedUser
        fileInputStream.close()
        objectInputStream.close()
        return authentication
    }
}
