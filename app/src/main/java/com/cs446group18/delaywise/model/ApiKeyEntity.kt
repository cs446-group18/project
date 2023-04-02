package com.cs446group18.delaywise.model

import androidx.room.*

@Entity(tableName = "api_key")
data class ApiKeyEntity(
    @PrimaryKey
    val id: String,
    var updatedAt: Long = System.currentTimeMillis()
)

@Dao
interface ApiKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ApiKeyEntity)

    @Query("SELECT * FROM api_key")
    suspend fun listApiKeys(): List<ApiKeyEntity>

    @Query("DELETE FROM api_key")
    suspend fun clear()
}
