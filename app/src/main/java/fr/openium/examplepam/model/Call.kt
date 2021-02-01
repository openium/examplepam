package fr.openium.examplepam.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Call(
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null,
    @ColumnInfo
    var contactName: String? = null,
    @ColumnInfo
    var length: Long = 0,
    @ColumnInfo
    var startDate: Date = Date()
)