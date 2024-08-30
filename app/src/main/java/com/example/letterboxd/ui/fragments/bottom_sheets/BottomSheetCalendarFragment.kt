package com.example.letterboxd.ui.fragments.bottom_sheets

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import com.example.letterboxd.R
import com.example.letterboxd.ui.fragments.OnDateSelectedListener
import com.example.letterboxd.ui.fragments.AddReviewFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetCalendarFragment : BottomSheetDialogFragment() {

    private lateinit var calendarView: CalendarView
    private var listener: OnDateSelectedListener? = null

    fun setOnDateSelectedListener(listener: OnDateSelectedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bottom_sheet_calendar, container, false)
        calendarView = view.findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            listener?.onDateSelected(year, month, dayOfMonth)
            dismiss()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
