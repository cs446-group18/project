package com.cs446group18.delaywise

import android.content.Context
import android.os.Environment
import androidx.room.*
import com.cs446group18.lib.models.*
import com.opencsv.CSVReader
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.csv.Csv
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass
import kotlin.time.DurationUnit
import kotlin.time.toDuration


val client = HttpClient(CIO) {
    install(Logging)
}

data class ClientFetcher(
    val apiKey: String? = null
) : Fetcher {
    override suspend fun makeAeroApiCall(url: String): HttpResponse {
        val baseUrl = when(apiKey) {
            null -> "http://10.0.2.2:8082"
            else -> "https://aeroapi.flightaware.com/aeroapi"
        }
        val response = client.get(baseUrl + url) {
            headers {
                if(apiKey != null) {
                    append("x-apikey", apiKey)
                }
            }
        }
        if(response.status != HttpStatusCode.OK) {
            throw Exception("returned invalid status code ${response.status}")
        }
        return response
    }
}

@Serializable
data class Airport(
    override val iata: String,
    val icao: String,
    val airport: String,
) : IataMapping

@Serializable
data class Airline(
    override val iata: String,
    val icao: String,
    val airline: String,
) : IataMapping

fun FlightInfo.getAirlineName(): String? {
    return ClientModel.getInstance().airlinesByIata[operator_iata]?.airline
}

interface ClientCacheEntity {
    val id: String
    val json: String
    var updatedAt: Long
}

@Entity(tableName = "flight_info")
data class FlightInfoEntity (
    @PrimaryKey
    override val id: String,
    override val json: String,
    override var updatedAt: Long = System.currentTimeMillis()
) : ClientCacheEntity

interface ClientCacheDao<T : ClientCacheEntity> {
    suspend fun insert(entity: T)
    fun getItem(id: String): T?
}

@Dao
interface FlightInfoDao : ClientCacheDao<FlightInfoEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: FlightInfoEntity)
    @Query("SELECT * from flight_info WHERE id = :id")
    override fun getItem(id: String): FlightInfoEntity?
}

@Database(entities = [FlightInfoEntity::class], version = 2, exportSchema = false)
abstract class DelayWiseLocalDatabase : RoomDatabase() {
    abstract fun flightInfoDao(): FlightInfoDao
}

class ClientCache<S : ClientCacheEntity, T: Cacheable>(
    private val dao: ClientCacheDao<S>,
    private val createEntity: (String, String) -> S,
    private val encode: (T) -> String,
    private val decode: (String) -> T,
) : Cache<T> {
    override suspend fun insert(id: String, item: T) {
        val entity: S = createEntity(id, encode(item))
        dao.insert(entity)
    }
    override suspend fun lookup(id: String): Pair<T?, Instant?> {
        return when(val item = dao.getItem(id)) {
            null -> Pair(null, null)
            else ->  Pair(decode(item.json), Instant.fromEpochMilliseconds(item.updatedAt))
        }
    }
}

interface IataMapping {
    val iata: String
}

inline fun <reified T : IataMapping>loadMapping(context: Context, filename: String) : Map<String, T> {
    val rawText = context.assets.open(filename).bufferedReader().use { it.readText() }.trimIndent()
    return Csv { hasHeaderRecord = true }.decodeFromString<List<T>>(rawText).associateBy{ it.iata }
}

class ClientModel(
    fetcher : ClientFetcher,
    flightInfoCache : ClientCache<FlightInfoEntity, FlightInfoResponse>,
    val airlinesByIata : Map<String, Airline>,
    val airportsByIata : Map<String, Airport>,
) : Model(
    fetcher = fetcher,
    flightInfoCache = flightInfoCache,
) {

    companion object {
        @Volatile
        private var INSTANCE: ClientModel? = null
        fun getInstance(): ClientModel {
            return INSTANCE ?: throw Exception("ClientModel not yet initialized; only call ClientModel.getInstance() after setup in MainActivity.kt")
        }
        fun init(context: Context) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                DelayWiseLocalDatabase::class.java,
                "delaywise_local_database"
            ).fallbackToDestructiveMigration().build()
            INSTANCE = ClientModel(
                fetcher = ClientFetcher(),
                flightInfoCache = ClientCache(
                    db.flightInfoDao(), ::FlightInfoEntity, { Json.encodeToString(it) }, { Json.decodeFromString(it) }
                ),
                airlinesByIata = loadMapping(context, "airline_codes.csv"),
                airportsByIata = loadMapping(context, "airport_codes.csv"),
            )
        }
    }

    suspend fun getFlight(flightIata: String) : FlightInfo {
        val match = """^(.*?)(\d+)$""".toRegex().matchEntire(flightIata)
        match ?: throw Exception("could not extract airline code from $flightIata")
        val (airlineIata, flightNumber) = match.destructured
        val airlineIcao = airlinesByIata[airlineIata]?.icao ?: throw Exception("could not find matching an airline matching $airlineIata")
        val flightInfoResponse = getFlightRaw(airlineIcao + flightNumber)
        val activeInstance = flightInfoResponse.flights.find {
            it.scheduled_out.toLocalDateTime(TimeZone.currentSystemDefault()).date ==
                    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        } ?: flightInfoResponse.flights.minBy {
            (it.scheduled_out - Clock.System.now()).absoluteValue
        }
        return activeInstance
    }
}

