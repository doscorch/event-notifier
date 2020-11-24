package com.example.eventNotifier

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment

class EventCreateFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.event_create, container, false)

        val cancelBtn = view.findViewById(R.id.eventCreateCancelBtn) as Button
        cancelBtn.setOnClickListener {
            (activity as NavigationHost).navigateTo(EventListFragment(), false)
        }

        val createBtn = view.findViewById(R.id.eventCreateCreateBtn) as Button
        createBtn.setOnClickListener {
            (activity as NavigationHost).navigateTo(EventListFragment(), false)
        }

        return view;
    }
}
