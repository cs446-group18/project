package com.cs446group18.delaywise.ui.savedflights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cs446group18.delaywise.databinding.FragmentSavedflightsBinding
import com.cs446group18.delaywise.ui.components.SavedFlightList

class SavedFlightsFragment : Fragment() {

    private var _binding: FragmentSavedflightsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val slideshowViewModel =
                ViewModelProvider(this).get(SavedFlightsViewModel::class.java)

        _binding = FragmentSavedflightsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.savedFlightList.setContent { SavedFlightList() }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}