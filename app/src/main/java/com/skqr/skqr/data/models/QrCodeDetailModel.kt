package com.skqr.skqr.data.models

data class QrCodeDetailResponse(
    val RETURN_DATA: QrCodeDetailModel,
    val RETURN_STATUS: Boolean
)

data class QrCodeDetailModel(
    val qc_id: String,
    val qc_user_id: String,
    val qc_type_id: String,
    val qc_image: String,
    val qc_date: String,
    val FIRST_NAME: String,
    val tm_name: String,
    val chemical_data: ArrayList<QrCodeDetailChemicalDataModel>,
    val physical_data: ArrayList<QrCodeDetailPhysicalDataModel>
)

data class QrCodeDetailChemicalDataModel(
    val ct_id: String,
    val ct_qr_id: String,
    val ct_user_id: String,
    val ct_date: String,
    val ct_program: String,
    val ct_sample: String,
    val ct_ref_alloy: String,
    val ct_oprator: String,
    val ct_analysis: String,
    val ct_data: String //ChemicalData? = ChemicalData()
)

data class QrCodeDetailPhysicalDataModel(
    val pt_id: String,
    val pt_user_id: String,
    val pt_qr_id: String,
    val pt_date: String,
    val pt_sample_indentification: String, //pt_sample_identification
    val pt_sample_type: String,
    val pt_c_s_area: String,
    val pt_length: String,
    val pt_weight: String,
    val pt_og_gauge_len: String,
    val pt_final_gauge_len: String,
    val pt_max_load: String,
    val pt_yield_load: String,
    val pt_disp_at_max_load: String,
    val pt_ult_stress: String,
    val pt_elongation: String,
    val pt_yield_stress: String,
    val pt_uts_ys: String,
    val pt_create_at: String,
)
