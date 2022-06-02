package com.skqr.skqr.view.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.skqr.skqr.R
import java.util.*

private const val TAG = "tag"
private const val ARG_PARAM2 = "param2"

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var listener: DatePickerDialogListener? = null

    private var tagForId: String = ""
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tagForId = it.getString(TAG).toString()
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as DatePickerDialogListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]

        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DatePickerFragment().apply {
                arguments = Bundle().apply {
                    putString(TAG, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        if (listener != null) {
            listener!!.onFinishDatePickerDialog(year, month, day, tagForId)
        }
    }

    interface DatePickerDialogListener {
        fun onFinishDatePickerDialog(year: Int, month: Int, day: Int, tagForId: String)
    }
}