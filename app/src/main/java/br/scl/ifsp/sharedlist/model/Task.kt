package br.scl.ifsp.sharedlist.model

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

data class Task(
    @PrimaryKey var id: Int? = -1,
    @NonNull var title: String,
    @NonNull var description: String,
    @NonNull var dateEstimated: LocalDate,
    @NonNull var dateDone: LocalDate,
    @NonNull var userUid: String,
    ) {}
