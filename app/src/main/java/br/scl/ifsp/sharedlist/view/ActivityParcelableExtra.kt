package br.scl.ifsp.sharedlist.view

import android.os.Build
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity

inline fun <reified T : Parcelable> AppCompatActivity.wrappedGetParcelable(name: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(name, T::class.java)
    } else {
        intent.getParcelableExtra(name)
    }
}
