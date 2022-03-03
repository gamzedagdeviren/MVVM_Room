package com.kotlin.mvvm_room.model

import androidx.room.*

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    var id: Int,

    @ColumnInfo(name = "user_name")
    var name: String,

    @ColumnInfo(name = "user_email")
    var email: String,

    @ColumnInfo(name = "user_username")
    var username: String,

    @ColumnInfo(name = "user_phone")
    var phone: String,
)
