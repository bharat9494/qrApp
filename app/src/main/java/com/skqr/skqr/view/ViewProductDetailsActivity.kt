package com.skqr.skqr.view

import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.gson.JsonObject
import com.skqr.skqr.R
import com.skqr.skqr.data.models.QrCodeDetailModel
import com.skqr.skqr.data.models.QrCodeDetailResponse
import com.skqr.skqr.databinding.ActivityViewProductDetailsBinding
import com.skqr.skqr.misc.userId
import com.skqr.skqr.viewModel.ViewProductDetailsViewModel
import com.skqr.skqr.viewModel.ViewProductDetailsViewModelFactory
import org.json.JSONObject


class ViewProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewProductDetailsBinding
    private lateinit var viewModel: ViewProductDetailsViewModel
    private lateinit var viewModelFactory: ViewProductDetailsViewModelFactory

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.appOrange, theme)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        viewModelFactory = ViewProductDetailsViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ViewProductDetailsViewModel::class.java)
            .apply {
                getProductDetails(
                    intent.getStringExtra("qr_id")!!,
                    "1",
                    sharedPreferences.getString(userId, "")!!
                )
            }

        //binding.textviewProductDetail.movementMethod = ScrollingMovementMethod()
        viewModel.productDetailsObj.observe(this) {
            binding.progressLoader.visibility = View.GONE
            if(it.isSuccessful) {
                val data = it.body()
                if(data?.RETURN_STATUS == true) {
                    showDataOnUI(data.RETURN_DATA)
                } else {
                    Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show()
                }

            } else {
                //binding.textviewProductDetail.text = it.message()
            }
        }

    }

    fun showDataOnUI(data: QrCodeDetailModel?) {

        val chemicalData = data?.chemical_data
        val physicalData = data?.physical_data

        binding.textviewQcIdValue.text = data?.qc_id
        binding.textviewUserIdValue.text = data?.qc_user_id
        binding.textviewTypeIdValue.text = data?.qc_type_id
        binding.textviewDateValue.text = data?.qc_date
        binding.textviewFirstNameValue.text = data?.FIRST_NAME
        binding.textviewTypeMasterNameValue.text = data?.tm_name

        if(!chemicalData.isNullOrEmpty()) {
            val chemicalTestData = chemicalData[0]
            binding.textviewChemicalTestIdValue.text = chemicalTestData.ct_id
            binding.textviewChemicalTestDateValue.text = chemicalTestData.ct_date
            binding.textviewProgramValue.text = chemicalTestData.ct_program
            binding.textviewSampleValue.text = chemicalTestData.ct_sample
            binding.textviewRefAlloyValue.text = chemicalTestData.ct_ref_alloy
            binding.textviewOperatorValue.text = chemicalTestData.ct_oprator
            binding.textviewAnalysisValue.text = chemicalTestData.ct_analysis
            binding.textviewChemTestDataValue.text = chemicalTestData.ct_data
        } else {
            binding.linearLayoutChemicalData.visibility = View.GONE
        }

        if(!physicalData.isNullOrEmpty()) {
            val physicalTestData = physicalData[0]
            binding.textviewPhyTestIdValue.text = physicalTestData.pt_id
            binding.textviewPhyTestDateValue.text = physicalTestData.pt_date
            binding.textviewSIValue.text = physicalTestData.pt_sample_indentification
            binding.textviewSTValue.text = physicalTestData.pt_sample_type
            binding.textviewCSAValue.text = physicalTestData.pt_c_s_area
            binding.textviewLengthValue.text = physicalTestData.pt_length
            binding.textviewWeightValue.text = physicalTestData.pt_weight
            binding.textviewOGGLValue.text = physicalTestData.pt_og_gauge_len
            binding.textviewFGLValue.text = physicalTestData.pt_final_gauge_len
            binding.textviewMaxLoadValue.text = physicalTestData.pt_max_load
            binding.textviewYieldLoadValue.text = physicalTestData.pt_yield_load
            binding.textviewDAMLValue.text = physicalTestData.pt_disp_at_max_load
            binding.textviewULTSValue.text = physicalTestData.pt_ult_stress
            binding.textviewElongValue.text = physicalTestData.pt_elongation
            binding.textviewYSValue.text = physicalTestData.pt_yield_stress
            binding.textviewUTSYSValue.text = physicalTestData.pt_uts_ys
        } else {
            binding.linearLayoutPhysicalData.visibility = View.GONE
        }

    }
}