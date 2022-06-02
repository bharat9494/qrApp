package com.skqr.skqr.view.fragments

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

private const val ARG_TITLE = "title"
private const val ARG_MESSAGE = "confirmationMessage"
private const val ARG_TAG_FOR_ID = "tagForId"

class DialogConfirmationFragment : DialogFragment() {

    var listener: ConfirmationDialogListener? = null

    interface ConfirmationDialogListener {
        fun onFinishConfirmationDialog(action: Boolean, tagForIdReturn: String)
    }

    private var title: String? = null
    private var confirmationMessage: String? = null
    private var tagForId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            confirmationMessage = it.getString(ARG_MESSAGE)
            tagForId = it.getString(ARG_TAG_FOR_ID)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val alertDialogBuilder = AlertDialog.Builder(requireActivity())
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(confirmationMessage)
        alertDialogBuilder.setPositiveButton("Ok") { dialog: DialogInterface?, _: Int ->
            dialog?.dismiss()
            if (listener != null) {
                listener!!.onFinishConfirmationDialog(true, tagForId!!)
            }
        }
        return alertDialogBuilder.create()
    }

    companion object {
        @JvmStatic
        fun newInstance(title: String, confirmationMessage: String, tagForId: String) =
            DialogConfirmationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_MESSAGE, confirmationMessage)
                    putString(ARG_TAG_FOR_ID, tagForId)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as ConfirmationDialogListener
    }
}