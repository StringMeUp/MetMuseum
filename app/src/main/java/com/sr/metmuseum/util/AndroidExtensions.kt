package com.sr.metmuseum.util

import android.text.Editable
import android.view.View
import android.widget.EditText
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, f: (T) -> Unit) {
    this.observe(owner) { t -> t?.let(f) }
}

fun EditText.afterTextChangedEvents(): Flow<CharSequence> {
    return callbackFlow<CharSequence> {
        val listener = object : Watcher() {
            override fun afterTextChanged(s: Editable?) {
                s?.let { trySend(it) }
            }
        }
        this@afterTextChangedEvents.addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text) }
}

fun View.hide(){
    this.visibility = View.GONE
}

fun View.show(){
    this.visibility = View.VISIBLE
}