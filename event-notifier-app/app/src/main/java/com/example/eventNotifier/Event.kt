package com.example.eventNotifier

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Event (
    var id: String = "",
    var name: String = "name",
    var description: String = "description",
    var subscribed: Boolean = true
){
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "description" to description,
            "subscribed" to subscribed
        )
    }
}