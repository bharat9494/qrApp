package com.skqr.skqr.data.models

data class PhysicalTestModel(
    var user_id: String,
    var qr_id : String, //to be extracted from qr code url
    var date : String,
    var sample_indentification : String,
    var sample_type : String,
    var c_s_area : String,
    var length : String,
    var weight : String,
    var og_gauge_len : String,
    var final_gauge_len : String,
    var max_load : String,
    var disp_at_max_load : String,
    var ult_stress : String,
    var elongation : String,
    var yield_stress : String,
    var yield_load : String,
    var uts_ys : String,
)

data class PhysicalTestResponse(
    var status: String,
    var message: String
)
