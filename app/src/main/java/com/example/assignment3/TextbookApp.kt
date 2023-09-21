package com.example.assignment3

import android.app.Application

class TextbookApp:Application() {
    val db by lazy {
        TextbookDatabase.getInstance(this)
    }
}