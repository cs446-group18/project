package com.cs446group18.delaywise

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.cs446group18.lib.models.FlightInfo

class NotificationService : Service() {
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler = Handler(Looper.getMainLooper())
        println("STARTING NOTIFICATION SERVICE!!!")
        runnable = Runnable {
            println("RUNNING NOTIFICATION SERVICE!!!")
            println("RUNNING NOTIFICATION SERVICE!!!")
            println("RUNNING NOTIFICATION SERVICE!!!")
            // query the API and compare to SQLite row
            handler.postDelayed(this.runnable, 60000) // schedule again in 1 minute
        }
        handler.post(runnable) // start the first execution
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) // remove scheduled executions
    }
}

fun createFlightNotification(context: Context, flightInfo: FlightInfo) {
    val channelId = "delaywise_flight_notifications"
    val notificationId = 1

    // Create intent to open your Jetpack Compose destination
    val intent = Intent(context, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        action = "FlightInfoView"
        putExtra("flightIata", flightInfo.ident_iata)
    }
    val pendingIntent = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(intent)
        getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    val notification = NotificationCompat.Builder(context, channelId)
        .setContentTitle("${flightInfo.ident_iata} Delayed")
        .setContentText("Flight ${flightInfo.ident_iata} is now delayed by ${flightInfo.getDepartureDelay().inWholeMinutes} minutes")
        .setContentIntent(pendingIntent)
        .setSmallIcon(R.drawable.__plane_icon)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, "DelayWise Flight Notifications", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)
    }
    notificationManager.notify(notificationId, notification)
}
