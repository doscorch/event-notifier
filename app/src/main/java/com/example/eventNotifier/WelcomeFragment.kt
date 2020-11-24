package com.example.eventNotifier

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.welcome, container, false)
        return view;
    }
}
