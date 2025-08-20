package com.cocido.formokaizen.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cocido.formokaizen.R
// import com.cocido.formokaizen.presentation.activities.MainActivity
// import com.google.firebase.messaging.FirebaseMessagingService
// import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint

// @AndroidEntryPoint // Commented until Firebase is properly configured
// Temporarily commented until Firebase is properly configured
/*
class FormoKaizenFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Procesar la notificación
        remoteMessage.notification?.let { notification ->
            showNotification(
                title = notification.title ?: "FormoKaizen",
                body = notification.body ?: "",
                data = remoteMessage.data
            )
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Enviar el token al servidor para poder enviar notificaciones específicas
        sendTokenToServer(token)
    }

    private fun showNotification(title: String, body: String, data: Map<String, String>) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear canal de notificación para Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "FormoKaizen Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de tarjetas rojas y actualizaciones del sistema"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Intent para abrir la app
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Agregar datos extra si es necesario
            data.forEach { (key, value) ->
                putExtra(key, value)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Construir la notificación
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun sendTokenToServer(token: String) {
        // TODO: Implementar el envío del token al backend
        // Esto debería ser manejado por un repository/use case
        android.util.Log.d("FCM", "New token: $token")
    }

    companion object {
        private const val CHANNEL_ID = "formokaizen_notifications"
        private const val NOTIFICATION_ID = 1001
    }
}
*/