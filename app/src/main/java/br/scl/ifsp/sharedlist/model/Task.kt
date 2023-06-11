package br.scl.ifsp.sharedlist.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Task(
    @PrimaryKey var id: String? = "",
    @NonNull var title: String = "",
    @NonNull var description: String = "",
    @NonNull var dateEstimated: Date = Date(),
    @NonNull var dateToBeFinished: Date = Date(),
    @NonNull var userUid: String = "",
    )
