package com.example.natour.user

object MainUser {
    private var _id = Int.MIN_VALUE
    var id
        get() = _id
        set(value) { _id = value }

    private var _username = ""
    var username
        get() = _username
        set(value) { _username = value }

    private var _email = ""
    var email
        get() = _email
        set(value) { _email = value }
}
