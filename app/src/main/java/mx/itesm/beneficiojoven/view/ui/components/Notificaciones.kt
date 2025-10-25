package mx.itesm.beneficiojoven.view.ui.components

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import mx.itesm.beneficiojoven.R
import mx.itesm.beneficiojoven.view.MainActivity
import kotlin.random.Random

/**
 * Servicio para gestionar la recepción de Firebase Cloud Messaging (FCM).
 *
 * Esta clase es responsable de dos tareas principales:
 * 1. Recibir nuevos tokens de registro de dispositivo a través de [onNewToken].
 * 2. Recibir mensajes de datos o notificaciones cuando la app está en primer plano
 * a través de [onMessageReceived].
 *
 * @see FirebaseMessagingService
 */
class ServicioMensajesFB: FirebaseMessagingService() {

    /**
     * Callback invocado cuando se genera un nuevo token de FCM para el dispositivo.
     *
     * Es crucial registrar este token en el backend del servidor para poder
     * enviar notificaciones push a este dispositivo específico.
     *
     * @param token El nuevo token de registro del dispositivo.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
// Registrar el Token en el servidor (o un tema)
        println("Token de este dispositivo: $token")
    }

    /**
     * Callback invocado cuando se recibe un mensaje de FCM.
     *
     * Este método se activa principalmente cuando la aplicación está en primer plano.
     * Si el mensaje contiene un payload de `notification`, se extrae y se pasa
     * a [enviarNotificacion] para mostrarla localmente.
     *
     * @param message El mensaje remoto recibido de FCM.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        println("Llegó la notificación REMOTA $message")
        message.notification?.let {
            enviarNotificacion(it)
        }
    }

    /**
     * Construye y muestra una notificación local en la barra de estado del dispositivo.
     *
     * Crea un [Intent] para abrir [MainActivity] al pulsar la notificación.
     * Configura el canal de notificación (requerido para Android 8.0 Oreo y superior).
     *
     * @param message El objeto de notificación extraído del [RemoteMessage].
     */
    private fun enviarNotificacion(message: RemoteMessage.Notification) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, FLAG_IMMUTABLE
        )
        val channelId = this.getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.notificacion)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }
        manager.notify(Random.nextInt(), notificationBuilder.build())
    }

    /**
     * Constantes utilizadas por [ServicioMensajesFB].
     */
    companion object {
        /** Nombre legible para el canal de notificaciones de FCM. */
        const val CHANNEL_NAME = "FCM notification channel"
    }
}