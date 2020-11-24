package com.example.eventNotifier

data class Event (val name: String, val description: String){
    var isSubscribed = true;
}