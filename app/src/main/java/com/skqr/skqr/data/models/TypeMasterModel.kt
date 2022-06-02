package com.skqr.skqr.data.models

data class TypeMasterModel(
    var tm_id: String,
    var tm_name: String,

    @Transient
    var selected: Boolean = false
)

data class TypeMasterResponse(
    var status: String,
    var data: List<TypeMasterModel>
)