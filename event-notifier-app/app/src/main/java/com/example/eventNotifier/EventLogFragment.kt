package com.example.eventNotifier

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.event_list.*


class EventLogFragment(eventName: String = "") : Fragment() {
    private val eventName = eventName;
    private val allEventInstanceList = ArrayList<EventInstance>();
    private val eventInstanceList = ArrayList<EventInstance>();
    private val db = Firebase.firestore;
    private lateinit var adapter: EventInstanceAdapter;
    private lateinit var spinner: ProgressBar;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.event_instance_list, container, false)
        spinner = view.findViewById(R.id.progressBar) as ProgressBar
        adapter = EventInstanceAdapter((activity as NavigationHost), eventInstanceList);
        // setup recycler view
        val recyclerView = view.findViewById(R.id.eventInstanceList) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter

        updateLog();

        return view;
    }

    private fun updateLog(){
        eventInstanceList.clear();
        adapter.notifyDataSetChanged();
        spinner.visibility = View.VISIBLE;

        if(eventName == ""){
            getAllEvents()
        }
        else {
            getSingleEventList()
        }

    }

    private fun getSingleEventList() {
        val eventNames = ArrayList<String>();
        eventNames.add(eventName);
        getEventInstances(eventNames);
    }

    private fun getAllEvents(){
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
                    getEventInstances(eventNames)
                }
            }
            .addOnFailureListener { ex ->
                Log.e("TAG", ex.localizedMessage);
            }
    }

    private fun getEventInstances(eventNames: ArrayList<String>){
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
                            allEventInstanceList.add(eventInstance);
                            eventInstanceList.add(eventInstance);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    spinner.visibility = View.GONE;
                }
            }
            .addOnFailureListener { ex ->
                Log.e("TAG", ex.localizedMessage);
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.event_log_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refreshLog -> {
                updateLog()
                true
            }
            R.id.searchLog -> {
                val input = EditText(context)
                val dialog: AlertDialog = AlertDialog.Builder(context)
                    .setTitle("Search log")
                    .setView(input)
                    .setPositiveButton("Search"
                    ) { _, _ ->
                        val search = input.text.toString()
                        eventInstanceList.clear();
                        for(i in allEventInstanceList){
                            if(i.name.contains(search) || i.details.contains(search)){
                                eventInstanceList.add(i);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
                dialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
