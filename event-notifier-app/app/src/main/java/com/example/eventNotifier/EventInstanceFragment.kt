package com.example.eventNotifier

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class EventInstanceFragment(val eventInstance: EventInstance) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.event_instance, container, false)

        // get elements from view
        val name = view.findViewById(R.id.eventInstanceName) as TextView
        val date = view.findViewById(R.id.eventInstanceDate) as TextView
        val details = view.findViewById(R.id.eventInstanceDetails) as TextView

        // populate view
        name.text = eventInstance.name
        date.text = eventInstance.date.toString()
        details.text = eventInstance.details

        return view;
    }
}
