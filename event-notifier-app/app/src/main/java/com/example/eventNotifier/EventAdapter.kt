package com.example.eventNotifier

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EventAdapter(val navigationHost: NavigationHost, val eventList: ArrayList<Event>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.event_card, parent, false)
        return ViewHolder(navigationHost, parent.context,this, eventList, v)
    }

    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {
        holder.bindItems(eventList[position])
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class ViewHolder(val navigationHost: NavigationHost, val context: Context, val adapter: EventAdapter, val eventList: ArrayList<Event>, itemView: View) : RecyclerView.ViewHolder(itemView) {
        val db = Firebase.firestore
        fun bindItems(event: Event) {
            val eventName = itemView.findViewById(R.id.eventName) as TextView
            val subBtn = itemView.findViewById(R.id.eventNotificationBtn) as ImageButton
            setSubBtnBackgroundColor(event, subBtn)
            subBtn.setOnClickListener(){
                event.subscribed = !event.subscribed
                if(event.subscribed){
                    Toast.makeText(context, "subscribed to event", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "unsubscribed from event", Toast.LENGTH_SHORT).show()
                }
                setSubBtnBackgroundColor(event, subBtn)

                var data = event.toMap();
                db.collection("events").document(event.id).set(data)

            }

            val deleteButton = itemView.findViewById(R.id.eventDeleteBtn) as ImageButton
            deleteButton.setOnClickListener(){
                eventList.remove(event)
                adapter.notifyDataSetChanged()
                Toast.makeText(context, "deleted event", Toast.LENGTH_SHORT).show()
                db.collection("events").document(event.id).delete()
            }

            val eventLogBtn = itemView.findViewById(R.id.singleEventLogNavBtn) as ImageButton
            eventLogBtn.setOnClickListener(){
                navigationHost.navigateTo(EventLogFragment(event.name), false)
            }

            eventName.text = event.name
        }

        private fun setSubBtnBackgroundColor(event:Event, btn:ImageButton) {
            Log.d("dea", event.name+": " + event.subscribed)
            if(!event.subscribed){
                btn.setBackgroundColor(0xff60a0e0.toInt());
            }
            else {
                btn.setBackgroundColor(Color.GREEN);
            }
        }
    }
}