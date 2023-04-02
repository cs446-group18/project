package com.cs446group18.delaywise.model

import androidx.room.*

@Entity(tableName = "weather_info")
data class WeatherInfoEntity(
    @PrimaryKey
    override val id: String,
    override val json: String,
    override var updatedAt: Long = System.currentTimeMillis()
) : ClientCacheEntity

@Dao
interface WeatherInfoDao : ClientCacheDao<WeatherInfoEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: WeatherInfoEntity)

    @Query("SELECT * from weather_info WHERE id = :id")
    override fun getItem(id: String): WeatherInfoEntity?
}