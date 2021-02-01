package fr.openium.examplepam.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import fr.openium.examplepam.R
import fr.openium.examplepam.database.AppDatabase
import fr.openium.examplepam.model.Call
import kotlinx.coroutines.runBlocking
import java.util.*
import java.util.concurrent.TimeUnit

class CallService : Service() {
    private var call: Call? = null
    private var callStartTime: Long = 0

    // Used to dialog with the rest of the app, like an Activity, won't be used here
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val command = intent.getIntExtra(KEY_COMMAND, stop)
        when (command) {
            start -> {
                val name = intent.getStringExtra(KEY_NAME)
                val pendingIntent = PendingIntent.getService(
                    applicationContext, 0xaa, getStopIntent(
                        applicationContext
                    ), PendingIntent.FLAG_UPDATE_CURRENT
                )
                val action = NotificationCompat.Action.Builder(
                    R.drawable.ic_call_end,
                    getString(R.string.dismiss),
                    pendingIntent
                ).build()
                val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setContentTitle(getString(R.string.calling_format, name))
                    .setSmallIcon(R.drawable.ic_notif_call)
                    .setUsesChronometer(true)
                    .setOngoing(true)
                    .setLocalOnly(true)
                    .addAction(action)
                    .build()
                callStartTime = System.currentTimeMillis()
                call = Call(contactName = name, startDate = Date(callStartTime))
                startForeground(NOTIFICATION_ID, notification)
                return START_STICKY
            }
            stop -> {
                call?.length =
                    TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - callStartTime)

                // required to insert on another thread
                runBlocking {
                    val database = AppDatabase.getInstance(applicationContext)
                    database.callDao().insertAll(call)
                }

                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.notification_channel_title)
            val description = getString(R.string.notification_channel_content)
            val importance = NotificationManager.IMPORTANCE_MAX
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManagerCompat.from(applicationContext).createNotificationChannel(channel)
        }
    }

    companion object {
        const val start = 0xa
        const val stop = 0xb
        const val KEY_COMMAND = "KEY_COMMAND"
        const val KEY_NAME = "KEY_NAME"
        const val CHANNEL_ID = "callnotification"
        const val NOTIFICATION_ID = 0x78

        @JvmStatic
        fun getStartIntent(context: Context?, name: String?): Intent {
            val intent = Intent(context, CallService::class.java)
            intent.putExtra(KEY_NAME, name)
            intent.putExtra(KEY_COMMAND, start)
            return intent
        }

        fun getStopIntent(context: Context?): Intent {
            val intent = Intent(context, CallService::class.java)
            intent.putExtra(KEY_COMMAND, stop)
            return intent
        }
    }
}