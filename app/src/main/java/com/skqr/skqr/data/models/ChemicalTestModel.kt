package com.skqr.skqr.data.models

data class ChemicalTestModel(
    var user_id: String,
    var qr_id: String,
    var date: String,
    var program: String,
    var sample: String,
    var ref_alloy: String,
    var oprator: String,
    var analysis: String,
    var data: ChemicalData? = ChemicalData()
)

data class ChemicalData(
    var C  : ArrayList<Double> = arrayListOf(),
    var Mn : ArrayList<Double> = arrayListOf(),
    var P  : ArrayList<Double> = arrayListOf(),
    var S  : ArrayList<Double> = arrayListOf(),
    var Cr : ArrayList<Double> = arrayListOf(),
    var Mo : ArrayList<Double> = arrayListOf(),
    var Ni : ArrayList<Double> = arrayListOf(),
    var Al : ArrayList<Double> = arrayListOf(),
    var Si : ArrayList<Double> = arrayListOf(),
    var Cu : ArrayList<Double> = arrayListOf(),
    var Co : ArrayList<Double> = arrayListOf(),
    var Nb : ArrayList<Double> = arrayListOf(),
    var B  : ArrayList<Double> = arrayListOf(),
    var Ti : ArrayList<Double> = arrayListOf(),
    var v  : ArrayList<Double> = arrayListOf(),
    var W  : ArrayList<Double> = arrayListOf(),
    var Sn : ArrayList<Double> = arrayListOf(),
    var Pb : ArrayList<Double> = arrayListOf(),
    var As : ArrayList<Double> = arrayListOf(),
    var Bi : ArrayList<Double> = arrayListOf(),
    var Te : ArrayList<Double> = arrayListOf(),
    var Sb : ArrayList<Double> = arrayListOf(),
    var Ca : ArrayList<Double> = arrayListOf(),
    var Zn : ArrayList<Double> = arrayListOf(),
    var Zr : ArrayList<Double> = arrayListOf(),
    var N  : ArrayList<Double> = arrayListOf(),
    var Fe : ArrayList<Double> = arrayListOf()
)

data class ChemicalTestResponse(
    var status: String,
    var message: String
)
