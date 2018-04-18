package com.demo.developer.deraesw.demomoviewes.utils

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.demo.developer.deraesw.demomoviewes.MainActivity
import com.demo.developer.deraesw.demomoviewes.R

class NotificationTools {
    private val TAG = NotificationTools::class.java.simpleName

    companion object {

        fun showNotificationUpdateContent(context: Context){

            val manager = NotificationManagerCompat.from(context)

            val builder = createNotification(context,
                    Channel.UPDATE_NOTIFICATION_ID,
                    "",
                    "")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val channel = createAChannel(
                        Channel.UPDATE_NOTIFICATION_ID,
                        "",
                        "",
                        NotificationManager.IMPORTANCE_DEFAULT
                )
                //manager.createNotificationChannel = channel
            }

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            builder.setContentIntent(pendingIntent)
            builder.setAutoCancel(true)

            manager.notify(NotificationID.FROM_SYNC, builder.build())
        }

        private fun createNotification(
                context : Context,
                channel: String,
                title: String, body:String ) : NotificationCompat.Builder {

            val build = NotificationCompat.Builder(context, channel)
                    .setSmallIcon(R.drawable.ic_star_white_24dp)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            return build
        }

        @TargetApi(Build.VERSION_CODES.O)
        private fun createAChannel(channelId : String, name : String, description: String, imp : Int ) : NotificationChannel{

            val channel : NotificationChannel = NotificationChannel(
                    channelId,
                    name,
                    imp
            )
            channel.description = description

            return channel
        }
    }

    class NotificationID {
        companion object {
            const val FROM_SYNC = 50
        }
    }

    class Channel {
        companion object {
            const val UPDATE_NOTIFICATION_ID = "UPDATE_NOTIFICATION_ID"
        }
    }
}