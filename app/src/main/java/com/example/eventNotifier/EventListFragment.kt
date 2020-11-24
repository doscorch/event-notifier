package com.example.eventNotifier

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class EventListFragment : Fragment() {
    private val eventList = ArrayList<Event>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.event_list, container, false)

        val recyclerView = view.findViewById(R.id.eventList) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

        eventList.add(Event("Belal Khan", "Ranchi Jharkhand"))
        eventList.add(Event("Ramiz Khan", "Ranchi Jharkhand"))
        eventList.add(Event("Faiz Khan", "Ranchi Jharkhand"))
        eventList.add(Event("Yashar Khan", "Ranchi Jharkhand"))

        val adapter = EventAdapter(eventList)
        recyclerView.adapter = adapter

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
