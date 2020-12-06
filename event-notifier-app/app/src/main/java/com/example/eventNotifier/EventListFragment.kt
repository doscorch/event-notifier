package com.example.eventNotifier

import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class EventListFragment : Fragment() {
    private val eventList = ArrayList<Event>();
    private val db = Firebase.firestore;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.event_list, container, false)

        // setup progress bar
        val progressBar = view.findViewById(R.id.progressBar) as ProgressBar
        progressBar.visibility = View.VISIBLE;

        // setup recycler view
        val recyclerView = view.findViewById(R.id.eventList) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        val adapter = EventAdapter((activity as NavigationHost), eventList)
        recyclerView.adapter = adapter

        // get events from the db
        db.collection("events")
            .get()
            .addOnSuccessListener { result ->
                if(!result.isEmpty){
                    for (document in result.documents) {
                        val event = document.toObject(Event::class.java);
                        if(event != null){
                            event.id = document.id;
                            eventList.add(event)
                        }
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.visibility = View.GONE;
                }
            }

        return view;
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.event_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.createEventBtn -> {
                (activity as NavigationHost).navigateTo(EventCreateFragment(), false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
