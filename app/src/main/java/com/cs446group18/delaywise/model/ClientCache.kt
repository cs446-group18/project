package com.cs446group18.delaywise.model

import com.cs446group18.lib.models.Cache
import com.cs446group18.lib.models.Cacheable
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

interface ClientCacheEntity {
    val id: String
    val json: String
    var updatedAt: Long
}

interface ClientCacheDao<T : ClientCacheEntity> {
    suspend fun insert(entity: T)
    fun getItem(id: String): T?
}

class ClientCache<S : ClientCacheEntity, T : Cacheable>(
    private val dao: ClientCacheDao<S>,
    private val createEntity: (String, String) -> S,
    private val encode: (T) -> String,
    private val decode: (String) -> T,
    private val maxCacheTime: Duration,
) : Cache<T> {
    override suspend fun insert(id: String, item: T) {
        val entity: S = createEntity(id, encode(item))
        dao.insert(entity)
    }

    override suspend fun lookup(id: String): T? {
        val item = dao.getItem(id)
        if (item == null || Instant.fromEpochMilliseconds(item.updatedAt) < Clock.System.now() - maxCacheTime) {
            return null
        }
        return decode(item.json)
    }
}