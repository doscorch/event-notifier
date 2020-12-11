package com.example.eventNotifier

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

open class MainActivity() : AppCompatActivity(), NavigationHost  {
    lateinit var toolbar: ActionBar
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize();

        // init container on welcome fragment
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, WelcomeFragment())
                .commit()
        }
    }

    fun initialize(){

        // setup content view
        setContentView(R.layout.container)
        toolbar = supportActionBar!!

        // setup bottom navigation
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeNav -> {
                    navigateTo(WelcomeFragment(), false)
                }
                R.id.eventLogNav -> {
                    navigateTo(EventLogFragment(), false)
                }
                R.id.eventsNav -> {
                    navigateTo(EventListFragment(), false)
                }
            }
            true
        }

        // setup google cloud FCM service
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCMToken", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token from db
            val token = task.result;
            Log.d("FCMToken", task.result.toString())
            db.collection("users")
                .document("user")
                .update(mapOf(
                    "registrationToken" to task.result
                ))
                .addOnSuccessListener { r ->
                    Log.d("Installations", "success adding auth token to db " + task.result)
                }
                .addOnFailureListener { ex ->
                    Log.e("Installations", ex.localizedMessage);
                }
        })
    }

    // navigation handler
    override fun navigateTo(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
}