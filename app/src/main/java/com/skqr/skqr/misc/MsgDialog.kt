package com.skqr.skqr.misc

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Message
import com.skqr.skqr.R

class MsgDialog {

    private var mContext: Context? = null
    private var mProgressDialog: ProgressDialog? = null
    private var mHandle: MsgHandle? = null

    fun MsgDialog(context: Context?) {
        mContext = context
    }

    /**
     * set handle
     */
    fun setHandle(handle: MsgHandle?) {
        mHandle = handle
    }

    /**
     * show message
     */
    fun showStartMsgDialog(message: String?) {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
        mProgressDialog = ProgressDialog(mContext)
        mProgressDialog!!.setMessage(message)
        mProgressDialog!!.isIndeterminate = true
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog!!.setButton(
            mContext!!.getString(R.string.button_cancel)
        ) { dialog, which ->
            //BasePrint.cancel()
            val msg: Message = mHandle!!.obtainMessage(MSG_PRINT_CANCEL)
            mHandle!!.sendMessage(msg)
        }
        mProgressDialog!!.show()
    }

    /**
     * show the end message
     */
    fun showPrintCompleteMsgDialog(message: String?) {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
        mProgressDialog = ProgressDialog(mContext)
        mProgressDialog!!.setMessage(mContext!!.getString(R.string.close_connect))
        mProgressDialog!!.isIndeterminate = true
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.setMessage(message)
        mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog!!.setButton(
            mContext!!.getString(R.string.button_ok)
        ) { dialog, i -> dialog.cancel() }
        mProgressDialog!!.show()
    }

    /**
     * update complete dialog's message
     */
    fun setMessage(msg: String?) {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.setMessage(msg)
        }
    }

    /**
     * show message
     */
    fun showMsgNoButton(title: String?, message: String?) {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.setMessage(message)
        }
        mProgressDialog = ProgressDialog(mContext)
        mProgressDialog!!.setTitle(title)
        mProgressDialog!!.setMessage(message)
        mProgressDialog!!.isIndeterminate = true
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        mProgressDialog!!.show()
    }

    /**
     * close dialog
     */
    fun close() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }

    /**
     * show alert dialog
     */
    fun showAlertDialog(title: String?, msg: String?) {
        val dialog = AlertDialog.Builder(mContext)
        dialog.setTitle(title)
        dialog.setMessage(msg)
        dialog.setCancelable(false)
        dialog.setPositiveButton(R.string.button_ok, null)
        dialog.show()
    }

    fun disableCancel() {
        mProgressDialog!!.getButton(DialogInterface.BUTTON_POSITIVE).isEnabled = false
    }

}