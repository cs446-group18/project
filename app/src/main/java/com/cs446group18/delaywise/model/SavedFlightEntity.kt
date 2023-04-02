package com.cs446group18.delaywise.model

import androidx.room.*
import com.cs446group18.lib.models.FlightInfo
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class SavedFlightKey(
    val flightIata: String,
    val date: LocalDate,
)

@Entity(tableName = "saved_flights")
data class SavedFlightEntity(
    @PrimaryKey
    val id: String,
    val json: String,
    var updatedAt: Long = System.currentTimeMillis()
) {
    constructor(flightInfo: FlightInfo, date: LocalDate): this(
        id = Json.encodeToString(SavedFlightKey(flightInfo.ident_iata, date)),
        json = Json.encodeToString(flightInfo),
    )
}

@Dao
interface SavedFlightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SavedFlightEntity)
    suspend fun insert(flight: FlightInfo) = insert(
        SavedFlightEntity(
            Json.encodeToString((SavedFlightKey(flight.ident_iata, flight.getDepartureDate()))),
            Json.encodeToString(flight)))

    @Query("DELETE FROM saved_flights WHERE id = :id")
    fun delete(id: String)
    fun delete(id: SavedFlightKey) = delete(Json.encodeToString(id))
    fun delete(flight: FlightInfo) = delete(SavedFlightKey(flight.ident_iata, flight.getDepartureDate()))

    @Query("SELECT * from saved_flights WHERE id = :id")
    fun getItem(id: String): SavedFlightEntity?
    fun getItem(id: SavedFlightKey) = getItem(Json.encodeToString(id))
    fun getItem(flight: FlightInfo) = getItem(SavedFlightKey(flight.ident_iata, flight.getDepartureDate()))

    @Query("SELECT * from saved_flights")
    fun listFlights(): List<SavedFlightEntity>
}
