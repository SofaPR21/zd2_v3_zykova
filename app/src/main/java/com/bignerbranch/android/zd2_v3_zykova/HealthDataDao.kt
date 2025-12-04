package com.bignerbranch.android.zd2_v3_zykova

import androidx.room.*

@Dao
interface HealthDataDao {
    @Insert
    fun insert(healthData: HealthData)

    @Query("SELECT * FROM health_data ORDER BY id DESC")
    fun getAll(): List<HealthData>

    @Delete
    fun delete(healthData: HealthData)
}