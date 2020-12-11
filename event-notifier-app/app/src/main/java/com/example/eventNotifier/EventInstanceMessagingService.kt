package com.example.eventNotifier

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class EventInstanceMessagingService : FirebaseMessagingService() {

    // fun called when FCM is received when a user is in the application
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", remoteMessage.data.toString())
        remoteMessage.notification?.let {
            // toast user
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(applicationContext, it.title, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // fun called when new FCM device token is received
    override fun onNewToken(token: String) {
        Log.d("FCM", "Refreshed token: $token")

        // update db
        Firebase.firestore.collection("users")
            .document("user")
            .update(mapOf(
                "registrationToken" to token
            ))
            .addOnSuccessListener { r ->
                Log.d("Installations", "success adding auth token to db" + token)
            }
            .addOnFailureListener { ex ->
                Log.e("Installations", ex.localizedMessage);
            }
    }
}