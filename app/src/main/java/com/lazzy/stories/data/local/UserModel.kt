package com.lazzy.stories.data.local

data class UserModel (
    val name: String,
    val email: String,
    val password: String,
    val isLogin: Boolean,
    val token: String
)