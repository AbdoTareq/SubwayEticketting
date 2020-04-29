/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.abdotareq.subway_e_ticketing.utility

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.ui.activity.HomeLandActivity

// Notification ID.
private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// extension function to send messages
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(notificationTitle: String, messageBody: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
    // TODO: Step 1.12 create PendingIntent to open the app when click the notification
    val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(HomeLandActivity::class.java)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.pocketFragment)
            .createPendingIntent()


    // TODO: Step 2.0 add style
    val image = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_subway_icon)

    val bigPictureStyle =
            NotificationCompat.BigPictureStyle().bigPicture(image).bigLargeIcon(null)
    // TODO: Step 2.2 add snooze action
//    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
//    val snoozePendingIntent = PendingIntent.getBroadcast(
//        applicationContext, REQUEST_CODE, snoozeIntent,
//        FLAGS
//    )

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification

    val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.buy_notification_channel_id)
    )

            // TODO: Step 1.3 set title, text and icon to builder
            .setSmallIcon(R.drawable.ic_subway_icon)
            .setContentTitle(notificationTitle)
            .setContentText(messageBody)

            // TODO: Step 1.13 set content intent
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

            // TODO: Step 2.1 add style to builder
            .setStyle(bigPictureStyle)
            .setLargeIcon(image)

            // TODO: Step 2.3 add snooze action
//        .addAction(
//            R.drawable.egg_icon,
//            applicationContext.getString(R.string.snooze),
//            snoozePendingIntent
//        )

            // TODO: Step 2.5 set priority
            //To support devices running API level 25 or lower, you must also call setPriority()
            // for each notification, using a constant from the NotificationCompat class
            //Before running the app, uninstall the app from your device or emulator to clear previous channel settings.
            .setPriority(NotificationCompat.PRIORITY_HIGH)

    // TODO: Step 1.4 call notify
    notify(NOTIFICATION_ID, builder.build())


}

// TODO: Step 1.14 Cancel all notifications
/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}

/**    [channelId] unique channel id
 *     [channelName]  channel name which users will also see in their settings screen
 */
fun NotificationManager.createChannel(channelId: String, channelName: String, applicationContext: Context) {
    // TODO: Step 1.6 START create a channel
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                        // TODO: Step 2.6 disable badges for this channel
                        .apply {
                            setShowBadge(true)
                        }
        notificationChannel.enableLights(true)
        notificationChannel.enableVibration(true)
        notificationChannel.lightColor = R.color.primaryColor
        notificationChannel.description = applicationContext.getString(R.string.buy_notification_channel_name)

        val notificationManager =
                applicationContext.getSystemService(NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(notificationChannel)

    }
    // TODO: Step 1.6 END create a channel


}