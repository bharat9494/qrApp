package com.skqr.skqr.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.zxing.integration.android.IntentIntegrator
import com.skqr.skqr.R
import com.skqr.skqr.data.models.ChemicalData
import com.skqr.skqr.data.models.ChemicalTestModel
import com.skqr.skqr.data.models.QrModel
import com.skqr.skqr.databinding.ActivitySaveChemicalTestBinding
import com.skqr.skqr.misc.afterTextChanged
import com.skqr.skqr.misc.toListOfDouble
import com.skqr.skqr.misc.userId
import com.skqr.skqr.view.fragments.DatePickerFragment
import com.skqr.skqr.view.fragments.DialogConfirmationFragment
import com.skqr.skqr.viewModel.SaveChemicalTestViewModel
import com.skqr.skqr.viewModel.SaveChemicalTestViewModelFactory

class SaveChemicalTestActivity : AppCompatActivity(),
    View.OnClickListener,
    DatePickerFragment.DatePickerDialogListener,
    DialogConfirmationFragment.ConfirmationDialogListener {

    private lateinit var binding: ActivitySaveChemicalTestBinding
    private lateinit var viewModel: SaveChemicalTestViewModel
    private lateinit var viewModelFactory: SaveChemicalTestViewModelFactory

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySaveChemicalTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.appOrange, theme)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        viewModelFactory = SaveChemicalTestViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SaveChemicalTestViewModel::class.java)

        viewModel.saveResponse.observe(this) {
            if(it.isSuccessful) {
                Toast.makeText(this, it.body()?.message, Toast.LENGTH_SHORT).show()

                if(it.body()?.status == "1") {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // do nothing
                }
            } else {
                Toast.makeText(this, it.body()?.message, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.qrModelResponse.observe(this) {
            if(it.isSuccessful) {
                if(it.body()?.data_exist == "1") {
                    val dialog = DialogConfirmationFragment.newInstance(
                        title = "Information",
                        confirmationMessage = it.body()?.message ?: "",
                        tagForId = "1"
                    )
                    dialog.isCancelable = false
                    dialog.dialog?.setCancelable(false)
                    dialog.dialog?.setCanceledOnTouchOutside(false)
                    dialog.show(supportFragmentManager, "")
                } else {
                    // do nothing
                }
            } else {
                //do nothing
            }
        }

        binding.etQrId.afterTextChanged {
            validateForm()
        }

        binding.etDate.afterTextChanged {
            validateForm()
        }

        binding.etprogram.afterTextChanged {
            validateForm()
        }

        binding.etSample.afterTextChanged {
            validateForm()
        }

        binding.etRefAlloy.afterTextChanged {
            validateForm()
        }

        binding.etOprator.afterTextChanged {
            validateForm()
        }

        binding.etAnalysis.afterTextChanged {
            validateForm()
        }

        binding.etQrId.setOnClickListener(this)
        binding.etDate.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if(view == binding.etQrId) {
            IntentIntegrator(this)
                .setPrompt("Scan a QR Code")
                .setOrientationLocked(true)
                .initiateScan()
        }
        else if(view == binding.etDate) {
            val fm = supportFragmentManager
            val datePicker = DatePickerFragment.newInstance("test", "2")
            datePicker.show(fm, "datePicker")
        }
        else if(view == binding.btnSubmit) {
            saveChemicalTest()
        }
    }

    private fun validateForm() {
        val qrId = binding.etQrId.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val program = binding.etprogram.text.toString().trim()
        val sample = binding.etSample.text.toString().trim()
        val refAlloy = binding.etRefAlloy.text.toString().trim()
        val operator = binding.etOprator.text.toString().trim()
        val analysis = binding.etAnalysis.text.toString().trim()

        binding.btnSubmit.isEnabled = (qrId.isNotEmpty()
                && date.isNotEmpty()
                && program.isNotEmpty()
                && sample.isNotEmpty()
                && refAlloy.isNotEmpty()
                && operator.isNotEmpty()
                && analysis.isNotEmpty())
    }

    private fun saveChemicalTest() {
        val qrId = binding.etQrId.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val program = binding.etprogram.text.toString().trim()
        val sample = binding.etSample.text.toString().trim()
        val refAlloy = binding.etRefAlloy.text.toString().trim()
        val operator = binding.etOprator.text.toString().trim()
        val analysis = binding.etAnalysis.text.toString().trim()

        val elementC = binding.etEC.text.toString().trim().toListOfDouble()
        val elementMn = binding.etEMN.text.toString().trim().toListOfDouble()
        val elementP = binding.etEP.text.toString().trim().toListOfDouble()
        val elementS = binding.etES.text.toString().trim().toListOfDouble()
        val elementCr = binding.etECR.text.toString().trim().toListOfDouble()
        val elementMo = binding.etEMO.text.toString().trim().toListOfDouble()
        val elementNi = binding.etENI.text.toString().trim().toListOfDouble()
        val elementAl = binding.etEAL.text.toString().trim().toListOfDouble()
        val elementSi = binding.etESI.text.toString().trim().toListOfDouble()
        val elementCu = binding.etECU.text.toString().trim().toListOfDouble()
        val elementCo = binding.etECO.text.toString().trim().toListOfDouble()
        val elementNb = binding.etENB.text.toString().trim().toListOfDouble()
        val elementB = binding.etEB.text.toString().trim().toListOfDouble()
        val elementTi = binding.etETI.text.toString().trim().toListOfDouble()
        val elementV = binding.etEV.text.toString().trim().toListOfDouble()
        val elementW = binding.etEW.text.toString().trim().toListOfDouble()
        val elementSn = binding.etESN.text.toString().trim().toListOfDouble()
        val elementPb = binding.etEPB.text.toString().trim().toListOfDouble()
        val elementAs = binding.etEAS.text.toString().trim().toListOfDouble()
        val elementBi = binding.etEBI.text.toString().trim().toListOfDouble()
        val elementTe = binding.etETE.text.toString().trim().toListOfDouble()
        val elementSb = binding.etESB.text.toString().trim().toListOfDouble()
        val elementCa = binding.etECA.text.toString().trim().toListOfDouble()
        val elementZn = binding.etEZN.text.toString().trim().toListOfDouble()
        val elementZr = binding.etEZR.text.toString().trim().toListOfDouble()
        val elementN = binding.etEN.text.toString().trim().toListOfDouble()
        val elementFe = binding.etEFE.text.toString().trim().toListOfDouble()

        if(qrId.isEmpty()) {
            Toast.makeText(this, "Qr id can not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if(date.isEmpty()) {
            Toast.makeText(this, "date can not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if(program.isEmpty()) {
            Toast.makeText(this, "program can not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if(sample.isEmpty()) {
            Toast.makeText(this, "sample can not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if(refAlloy.isEmpty()) {
            Toast.makeText(this, "refAlloy can not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if(operator.isEmpty()) {
            Toast.makeText(this, "operator can not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if(analysis.isEmpty()) {
            Toast.makeText(this, "analysis can not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val chemicalData = ChemicalData(
            C = ArrayList(elementC),
            Mn = ArrayList(elementMn),
            P = ArrayList(elementP),
            S = ArrayList(elementS),
            Cr = ArrayList(elementCr),
            Mo = ArrayList(elementMo),
            Ni = ArrayList(elementNi),
            Al = ArrayList(elementAl),
            Si = ArrayList(elementSi),
            Cu = ArrayList(elementCu),
            Co = ArrayList(elementCo),
            Nb = ArrayList(elementNb),
            B = ArrayList(elementB),
            Ti = ArrayList(elementTi),
            v = ArrayList(elementV),
            W = ArrayList(elementW),
            Sn = ArrayList(elementSn),
            Pb = ArrayList(elementPb),
            As = ArrayList(elementAs),
            Bi = ArrayList(elementBi),
            Te = ArrayList(elementTe),
            Sb = ArrayList(elementSb),
            Ca = ArrayList(elementCa),
            Zn = ArrayList(elementZn),
            Zr = ArrayList(elementZr),
            N = ArrayList(elementN),
            Fe = ArrayList(elementFe)
        )

        val chemicalTestModel = ChemicalTestModel(
            qr_id = qrId,
            user_id = sharedPreferences.getString(userId, "")!!,
            date = date,
            program = program,
            sample = sample,
            ref_alloy = refAlloy,
            oprator = operator,
            analysis = analysis,
            data = chemicalData
        )

        viewModel.saveChemicalTest(chemicalTestModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.contents == null) {
                Toast.makeText(baseContext, "Cancelled", Toast.LENGTH_SHORT).show()
            } else {
//                binding.etQrId.setText(intentResult.contents)
                val qrId = intentResult.contents.split("id=")[1]
                binding.etQrId.setText(qrId)
                viewModel.checkIfDataExist(QrModel(
                    qr_id = qrId,
                    type = "1"
                ))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onFinishDatePickerDialog(year: Int, month: Int, day: Int, tagForId: String) {
        val monthPlus = month + 1
        binding.etDate.setText("$year-$monthPlus-$day")
    }

    override fun onFinishConfirmationDialog(action: Boolean, tagForIdReturn: String) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}