package com.cs446group18.delaywise.ui.flightinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cs446group18.delaywise.databinding.FragmentFlightinfoBinding

class FlightInfoFragment : Fragment() {

    private var _binding: FragmentFlightinfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val flightInfoViewModel =
                ViewModelProvider(this).get(FlightInfoViewModel::class.java)

        _binding = FragmentFlightinfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textGallery
//        flightInfoViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}