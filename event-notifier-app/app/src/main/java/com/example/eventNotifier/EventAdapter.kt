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
            // get elements from view
            val eventName = itemView.findViewById(R.id.eventName) as TextView
            val subBtn = itemView.findViewById(R.id.eventNotificationBtn) as ImageButton
            val deleteButton = itemView.findViewById(R.id.eventDeleteBtn) as ImageButton
            val eventLogBtn = itemView.findViewById(R.id.singleEventLogNavBtn) as ImageButton

            // setup initial view
            setSubBtnBackgroundColor(event, subBtn)
            eventName.text = event.name

            // subscribe btn onclick listener
            subBtn.setOnClickListener(){
                // flip flag
                event.subscribed = !event.subscribed

                // toast to user
                if(event.subscribed){
                    Toast.makeText(context, "subscribed to event", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "unsubscribed from event", Toast.LENGTH_SHORT).show()
                }

                // update view
                setSubBtnBackgroundColor(event, subBtn)

                // update db
                var data = event.toMap();
                db.collection("events").document(event.id).set(data)
            }

            // delete btn onclick listener
            deleteButton.setOnClickListener(){
                // remove from view
                eventList.remove(event)
                adapter.notifyDataSetChanged()

                // toast user
                Toast.makeText(context, "deleted event", Toast.LENGTH_SHORT).show()

                // remove from db
                db.collection("events").document(event.id).delete()
            }

            // event log btn onclick listener
            eventLogBtn.setOnClickListener(){
                // navigate to event log for particular event
                navigationHost.navigateTo(EventLogFragment(event.name), false)
            }
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