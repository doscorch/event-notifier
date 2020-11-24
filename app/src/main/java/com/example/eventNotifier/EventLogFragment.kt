package com.example.eventNotifier

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class EventLogFragment : Fragment() {
    private val eventInstanceList = ArrayList<EventInstance>();

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.event_instance_list, container, false)

        val recyclerView = view.findViewById(R.id.eventInstanceList) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

        eventInstanceList.add(EventInstance("Belal Khan", "Ranchi Jharkhand"))
        eventInstanceList.add(EventInstance("Ramiz Khan", "Ranchi Jharkhand"))
        eventInstanceList.add(EventInstance("Faiz Khan", "Ranchi Jharkhand"))
        eventInstanceList.add(EventInstance("Yashar Khan", "Ranchi Jharkhand"))

        val adapter = EventInstanceAdapter((activity as NavigationHost), eventInstanceList)
        recyclerView.adapter = adapter

        return view;
    }
}
