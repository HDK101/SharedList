package br.scl.ifsp.sharedlist.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity
data class Task(
    @PrimaryKey var id: String? = "",
    @NonNull var title: String = "",
    @NonNull var description: String = "",
    @NonNull var dateCreation: Date = Date(),
    @NonNull var dateEstimated: Date = Date(),
    @NonNull var finished: Boolean = false,
    @NonNull var userUid: String = "",
    ): Parcelable
