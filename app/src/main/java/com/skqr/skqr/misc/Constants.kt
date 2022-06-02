package com.skqr.skqr.misc

const val userId = "userId"
const val baseUrl = "http://salenserve.xyz/"

enum class UsbAuthorizationState {
    NOT_DETERMINED, APPROVED, DENIED
}

var mUsbAuthorizationState: UsbAuthorizationState? = null
const val MSG_SDK_EVENT = 10001
const val MSG_PRINT_START = 10002
const val MSG_PRINT_END = 10003
const val MSG_PRINT_CANCEL = 10004
const val MSG_TRANSFER_START = 10005
const val MSG_WRONG_OS = 10006
const val MSG_NO_USB = 10007
const val MSG_DATA_SEND_START = 10030
const val MSG_DATA_SEND_END = 10031
const val MSG_GET_FIRM = 10099