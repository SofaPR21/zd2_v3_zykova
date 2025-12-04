package com.bignerbranch.android.zd2_v3_zykova

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "health_data")
data class HealthData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val steps: Int,
    val sleep: Float,
    val weight: Float,
    val sugar: Float,
    val date: String
)