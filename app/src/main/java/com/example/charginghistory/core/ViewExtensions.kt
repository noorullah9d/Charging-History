package com.example.charginghistory.core

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

infix fun View.click(onClick: (View) -> Unit) {
    this.setOnClickListener {
        onClick.invoke(it)
    }
}