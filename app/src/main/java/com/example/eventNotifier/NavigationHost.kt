package com.example.eventNotifier

import androidx.fragment.app.Fragment

interface NavigationHost {
    fun navigateTo(fragment: Fragment, addToBackstack: Boolean)
}
