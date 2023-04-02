package com.cs446group18.server

import com.cs446group18.lib.models.Cache
import com.cs446group18.lib.models.Fetcher
import com.cs446group18.lib.models.Model
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

const val API_KEY_AERO = "Yyj2JugSpnL9ZUITA7QSTg4fkstUqsUK"

val client = HttpClient()

class ServerFetcher : Fetcher {
    override suspend fun makeAeroApiCall(url: String, block: HttpRequestBuilder.() -> Unit): HttpResponse {
        val response = client.get("https://aeroapi.flightaware.com/aeroapi$url") {
            headers {
                append("x-apikey", API_KEY_AERO)
            }
            block()
        }
        if(response.status != HttpStatusCode.OK) {
            throw Exception("returned invalid status code ${response.status}")
        }
        return response
    }
}

@OptIn(InternalCoroutinesApi::class)
class ServerCache<T>(val maxCacheTime: Duration) : Cache<T> {
    // NOTE: could replace this with redis, or a specialized Cache type in stdlib/3rd-party lib
    private val map = HashMap<String, Pair<T, Instant>>()
    init {
        Thread {
            while (true) {
                Thread.sleep(60_000L) // every minute
                // tradeoff: we lock the entire map (rather locking one entry at a time)
                //     which could lead to pauses in the API, but it saves on memory
                synchronized(map) {
                    // evict expired entries
                    val toRemove = ArrayList<String>()
                    for ((key, value) in map.entries) {
                        if (value.second <= Clock.System.now() - maxCacheTime) {
                            toRemove.add(key)
                        }
                    }
                    for(key in toRemove) { map.remove(key) }
                }
            }
        }.start()
    }
    override suspend fun insert(id: String, item: T) {
        synchronized(map) {
            map[id] = Pair(item, Clock.System.now())
        }
    }
    override suspend fun lookup(id: String): T? {
        val pair = map[id]
        pair ?: return null
        val (value, cachedAt) = pair
        if(cachedAt <= Clock.System.now() - maxCacheTime) {
            return null
        }
        return value
    }
}

val ServerModel = Model(
    fetcher = ServerFetcher(),
    flightInfoCache = ServerCache(maxCacheTime = 1.toDuration(DurationUnit.MINUTES)),
    airportDelayCache = ServerCache(maxCacheTime = 5.toDuration(DurationUnit.MINUTES)),
    scheduledFlightCache = ServerCache(maxCacheTime = 3.toDuration(DurationUnit.HOURS)),
    airportCache = ServerCache(maxCacheTime = 15.toDuration(DurationUnit.DAYS)),
    weatherCache = ServerCache(maxCacheTime = 15.toDuration(DurationUnit.MINUTES))
)
