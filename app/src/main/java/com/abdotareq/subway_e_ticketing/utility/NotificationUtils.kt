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
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.abdotareq.subway_e_ticketing.R
import com.abdotareq.subway_e_ticketing.ui.activity.HomeLandActivity

// Notification ID.
private val BUY_NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

// extension function to send messages
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(notificationTitle: String, messageBody: String, buyNotificationChannelId: String, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // create PendingIntent to open the app when click the notification
    val pendingIntent = NavDeepLinkBuilder(applicationContext)
            .setComponentName(HomeLandActivity::class.java)
            .setGraph(R.navigation.mobile_navigation)
            .setDestination(R.id.pocketFragment)
            .createPendingIntent()

    // Build the notification
    val builder = NotificationCompat.Builder(
            applicationContext,
            buyNotificationChannelId
    )
            .setSmallIcon(R.drawable.ic_subway_icon)
            .setContentTitle(notificationTitle)
            .setContentText(messageBody)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

            //To support devices running API level 25 or lower, you must also call setPriority()
            // for each notification, using a constant from the NotificationCompat class
            //Before running the app, uninstall the app from your device or emulator to clear previous channel settings.
            .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(BUY_NOTIFICATION_ID, builder.build())

}

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
fun createChannel(channelId: String, channelName: String, applicationContext: Context) {
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

        val notificationManager =
                applicationContext.getSystemService(NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(notificationChannel)

    }
}