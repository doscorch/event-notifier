package com.example.eventNotifier

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EventCreateFragment : Fragment() {
    val db = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.event_create, container, false)
        val name = view.findViewById(R.id.eventNameInput) as EditText
        val description = view.findViewById(R.id.eventDescriptionInput) as EditText

        val cancelBtn = view.findViewById(R.id.eventCreateCancelBtn) as Button
        cancelBtn.setOnClickListener {
            (activity as NavigationHost).navigateTo(EventListFragment(), false)
        }

        val createBtn = view.findViewById(R.id.eventCreateCreateBtn) as Button
        createBtn.setOnClickListener {
            if(name.text.isNullOrEmpty()){
                Toast.makeText(context, "name is required", Toast.LENGTH_SHORT).show()
            }
            else if (description.text.isNullOrEmpty()){
                Toast.makeText(context, "description is required", Toast.LENGTH_SHORT).show()
            }
            else {
                val event = Event()
                event.name = name.text.toString()
                event.description = description.text.toString()
                db.collection("events")
                    .add(event.toMap())
                    .addOnSuccessListener { r ->
                        (activity as NavigationHost).navigateTo(EventListFragment(), false)
                    }
            }

        }

        return view;
    }
}
