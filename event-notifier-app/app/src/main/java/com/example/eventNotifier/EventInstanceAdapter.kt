package com.example.eventNotifier

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventInstanceAdapter(val navigationHost: NavigationHost, val eventInstanceList: ArrayList<EventInstance>) : RecyclerView.Adapter<EventInstanceAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventInstanceAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_instance_card, parent, false)
        return ViewHolder(navigationHost, eventInstanceList, v)
    }

    override fun onBindViewHolder(holder: EventInstanceAdapter.ViewHolder, position: Int) {
        holder.bindItems(eventInstanceList[position])
    }

    override fun getItemCount(): Int {
        return eventInstanceList.size
    }

    class ViewHolder(val navigationHost: NavigationHost, val eventInstanceList: ArrayList<EventInstance>, itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(eventInstance: EventInstance) {
            // get elements from view
            val eventName = itemView.findViewById(R.id.eventInstanceName) as TextView
            val eventDate = itemView.findViewById(R.id.eventInstanceDate) as TextView
            val infoButton = itemView.findViewById(R.id.eventInstanceInfoBtn) as ImageButton

            // populate view text
            eventName.text = eventInstance.name
            eventDate.text = eventInstance.date.toString()

            // setup info btn onclick listener
            infoButton.setOnClickListener(){
                // navigate to event instance view
                navigationHost.navigateTo(EventInstanceFragment(eventInstance), false)
            }
        }
    }
}