package com.example.eventNotifier

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EventLogFragment : Fragment() {
    private val eventInstanceList = ArrayList<EventInstance>();
    private val db = Firebase.firestore;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.event_instance_list, container, false)

        // setup progress bar
        val progressBar = view.findViewById(R.id.progressBar) as ProgressBar
        progressBar.visibility = View.VISIBLE;

        // setup recycler view
        val recyclerView = view.findViewById(R.id.eventInstanceList) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        val adapter = EventInstanceAdapter((activity as NavigationHost), eventInstanceList)
        recyclerView.adapter = adapter

        // get event instances from the db
        db.collection("events")
            .get()
            .addOnSuccessListener { res ->
                if(!res.isEmpty){
                    val eventNames = ArrayList<String>();
                    for (document in res.documents) {
                        val event = document.toObject(Event::class.java);
                        if(event != null){
                            event.id = document.id;
                            eventNames.add(event.name)
                        }
                    }
                    db.collection("eventInstances")
                        .whereIn("name", eventNames.toList())
                        .orderBy("date", Query.Direction.DESCENDING)
                        .get()
                        .addOnSuccessListener { result ->
                            if(!result.isEmpty){
                                for (document in result.documents) {
                                    val eventInstance = document.toObject(EventInstance::class.java);
                                    if(eventInstance != null){
                                        eventInstance.id = document.id;
                                        eventInstanceList.add(eventInstance)
                                    }
                                }
                                adapter.notifyDataSetChanged();
                                progressBar.visibility = View.GONE;
                            }
                        }
                        .addOnFailureListener { ex ->
                            Log.e("TAG", ex.localizedMessage);
                        }
                }
            }
            .addOnFailureListener { ex ->
                Log.e("TAG", ex.localizedMessage);
            }


        return view;
    }
}
