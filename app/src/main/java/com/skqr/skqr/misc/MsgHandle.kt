package com.skqr.skqr.misc

import android.content.Context
import android.os.Handler
import android.os.Message
import com.brother.ptouch.sdk.PrinterInfo
import com.skqr.skqr.R

class MsgHandle(context: Context, Dialog: MsgDialog) : Handler() {
    private val mContext: Context
    private val mDialog: MsgDialog
    private var mResult: String? = null
    private var mBattery: String? = null
    private var isCancelled = false
    private var funcID = FUNC_OTHER

    /**
     * set the function id
     */
    fun setFunction(funcID: Int) {
        this.funcID = funcID
    }

    /**
     * set the printing result
     */
    fun setResult(results: String?) {
        mResult = results
    }

    /**
     * set the Battery info.
     */
    fun setBattery(battery: String?) {
        mBattery = battery
    }

    /**
     * Message Handler which deal with the messages from UI thread or print
     * thread START: start message SDK_EVENT: message from SDK UPDATE: end
     * message
     */
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            MSG_PRINT_START, MSG_DATA_SEND_START -> mDialog.showStartMsgDialog(
                mContext
                    .getString(R.string.progress_dialog_message_communication_start)
            )
            MSG_TRANSFER_START -> mDialog.showStartMsgDialog(
                mContext
                    .getString(R.string.progress_dialog_message_transfer_start)
            )
            MSG_GET_FIRM -> mDialog.showPrintCompleteMsgDialog("Result = $mResult")
            MSG_SDK_EVENT -> {
                val strMsg = msg.obj.toString()
                if (strMsg == PrinterInfo.Msg.MESSAGE_START_COMMUNICATION.toString()) {
                    mDialog.setMessage(
                        mContext.getString(R.string.progress_dialog_message_preparing_connection)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_START_CREATE_DATA.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_createData)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_START_SEND_DATA.toString()) {
                    if (funcID != FUNC_OTHER) {
                        mDialog.setMessage(
                            mContext
                                .getString(R.string.progress_dialog_message_sendingData)
                        )
                    } else {
                        mDialog.setMessage(
                            mContext
                                .getString(R.string.progress_dialog_message_sendingPrintingData)
                        )
                    }

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_END_SEND_DATA.toString()) {
                    if (funcID == FUNC_OTHER) {
                        mDialog.setMessage(
                            mContext
                                .getString(R.string.progress_dialog_message_startPrint)
                        )
                    } else if (funcID == FUNC_TRANSFER) {
                        mDialog.setMessage(
                            mContext
                                .getString(R.string.progress_dialog_message_dataSent)
                        )
                    }

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_END_SEND_TEMPLATE.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_dataSent)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_PRINT_COMPLETE.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_printed)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_PRINT_ERROR.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.error_message_runtime)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_END_COMMUNICATION.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_close_connection)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_PAPER_EMPTY.toString()) {
                    if (!isCancelled) mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_set_paper)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_START_COOLING.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_cooling_start)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_END_COOLING.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_cooling_end)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_WAIT_PEEL.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_waiting_peel)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_START_SEND_TEMPLATE.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_sending_template)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_START_UPDATE_BLUETOOTH_SETTING.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_setting_bluetooth_setting)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_START_GET_BLUETOOTH_SETTING.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_getting_bluetooth_setting)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_START_GET_TEMPLATE_LIST.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_getting_template)
                    )

                } else if (strMsg == PrinterInfo.Msg.MESSAGE_START_REMOVE_TEMPLATE_LIST.toString()) {
                    mDialog.setMessage(
                        mContext
                            .getString(R.string.progress_dialog_message_removing_template)
                    )

                } else {
                    //do nothing
                }
            }
            MSG_PRINT_END, MSG_DATA_SEND_END -> {
                if (mBattery != null && mBattery != "") {
                    mDialog.showPrintCompleteMsgDialog("$mResult\nBattery: $mBattery")
                } else {
                    mDialog.showPrintCompleteMsgDialog(mResult)
                }
                isCancelled = false
            }
            MSG_PRINT_CANCEL -> {
                mDialog.showStartMsgDialog(
                    mContext.getString(R.string.cancel_printer_msg)
                )
                mDialog.disableCancel()
                isCancelled = true
            }
            MSG_WRONG_OS -> mDialog.showPrintCompleteMsgDialog(
                mContext.getString(R.string.error_message_weong_os)
            )
            MSG_NO_USB -> mDialog.showPrintCompleteMsgDialog(
                mContext.getString(R.string.error_message_no_usb)
            )
            else -> {}
        }
    }

    companion object {
        const val FUNC_SETTING = 2
        const val FUNC_TRANSFER = 3
        private const val FUNC_OTHER = 1
    }

    init {
        funcID = FUNC_OTHER
        mContext = context
        mDialog = Dialog
    }
}