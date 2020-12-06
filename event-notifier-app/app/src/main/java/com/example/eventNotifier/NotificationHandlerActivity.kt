package com.example.eventNotifier

import android.os.Bundle

class NotificationHandlerActivity: MainActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize();

        // init container on eventList fragment
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, EventLogFragment())
                .commit()
        }
    }
}