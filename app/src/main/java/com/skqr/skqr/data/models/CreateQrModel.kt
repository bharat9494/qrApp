package com.skqr.skqr.data.models

data class CreateQrModel(
    var user_id: String,
    var type_id: String
)

data class CreateQrResponse(
    var status: String,
    var message: String,
    var qr_id: String,
    var qr_image: String
)

data class QrModel(
    var type: String,
    var qr_id: String
)

data class QrModelResponse(
    var status: String,
    var message: String,
    var data_exist: String
)
