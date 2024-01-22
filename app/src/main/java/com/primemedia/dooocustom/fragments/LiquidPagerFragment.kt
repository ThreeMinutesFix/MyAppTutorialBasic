package com.primemedia.dooocustom.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dooo.android.R
import com.google.android.material.button.MaterialButton
import com.primemedia.dooocustom.Dashboard

class LiquidPagerFragment : Fragment() {
    var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt("POSITION")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = if (position == 1) {
            R.layout.first_on_boarding_page
        } else if (position == 2) {
            R.layout.second_on_boarding_page
        } else if (position == 3) {
            R.layout.third_on_boarding_page
        } else if (position == 4) {
            R.layout.third_on_boarding_page
        } else R.layout.liquid_pager_fragment_page_number
        return layoutInflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button =
            view.findViewById<MaterialButton>(R.id.materialid) // Replace with your actual button ID
        button.setOnClickListener {
            val intent = Intent(requireContext(), Dashboard::class.java)
            startActivity(intent)

        }

    }
}