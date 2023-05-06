package com.sr.metmuseum.util

import android.os.SystemClock
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

fun EditText.afterTextChangedEvents(): Flow<String> {
    return callbackFlow {
        val listener = object : Watcher() {
            override fun afterTextChanged(s: Editable?) {
                s?.let { trySend(it.toString()) }
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }.onStart { emit(text.toString()) }
}


fun View.clickWithDebounce(debounceTime: Long = 500L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

fun View.hide(){
    this.visibility = View.GONE
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun String?.validate(): String {
    return if (this.isNullOrBlank()) "-"
    else this
}