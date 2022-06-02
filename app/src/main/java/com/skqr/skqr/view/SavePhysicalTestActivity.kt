package com.skqr.skqr.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.google.zxing.integration.android.IntentIntegrator
import com.skqr.skqr.R
import com.skqr.skqr.data.models.PhysicalTestModel
import com.skqr.skqr.data.models.QrModel
import com.skqr.skqr.databinding.ActivitySavePhysicalTestBinding
import com.skqr.skqr.misc.afterTextChanged
import com.skqr.skqr.misc.userId
import com.skqr.skqr.view.fragments.DatePickerFragment
import com.skqr.skqr.view.fragments.DialogConfirmationFragment
import com.skqr.skqr.viewModel.SavePhysicalTestViewModel
import com.skqr.skqr.viewModel.SavePhysicalTestViewModelFactory


class SavePhysicalTestActivity : AppCompatActivity(),
    View.OnClickListener,
    DatePickerFragment.DatePickerDialogListener,
    DialogConfirmationFragment.ConfirmationDialogListener {

    private lateinit var binding: ActivitySavePhysicalTestBinding
    private lateinit var viewModel: SavePhysicalTestViewModel
    private lateinit var viewModelFactory: SavePhysicalTestViewModelFactory

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavePhysicalTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.appOrange, theme)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        viewModelFactory = SavePhysicalTestViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SavePhysicalTestViewModel::class.java)

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

        binding.etSI.afterTextChanged {
            validateForm()
        }

        binding.etST.afterTextChanged {
            validateForm()
        }

        binding.etCSA.afterTextChanged {
            validateForm()
        }

        binding.etLength.afterTextChanged {
            validateForm()
        }

        binding.etWeight.afterTextChanged {
            validateForm()
        }

        binding.etOGL.afterTextChanged {
            validateForm()
        }

        binding.etFGL.afterTextChanged {
            validateForm()
        }

        binding.etML.afterTextChanged {
            validateForm()
        }

        binding.etDML.afterTextChanged {
            validateForm()
        }

        binding.etULTS.afterTextChanged {
            validateForm()
        }

        binding.etElong.afterTextChanged {
            validateForm()
        }

        binding.etYS.afterTextChanged {
            validateForm()
        }

        binding.etUtsYs.afterTextChanged {
            validateForm()
        }

        binding.etYLoad.afterTextChanged {
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
            savePhysicalTest()
        }
    }

    private fun validateForm() {
        val qrId = binding.etQrId.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val sampleId = binding.etSI.text.toString().trim()
        val sampleType = binding.etST.text.toString().trim()
        val csa = binding.etCSA.text.toString().trim()
        val length = binding.etLength.text.toString().trim()
        val weight = binding.etWeight.text.toString().trim()
        val oggl = binding.etOGL.text.toString().trim()
        val fgl = binding.etFGL.text.toString().trim()
        val maxLoad = binding.etML.text.toString().trim()
        val daml = binding.etDML.text.toString().trim()
        val ultstress = binding.etULTS.text.toString().trim()
        val elong = binding.etElong.text.toString().trim()
        val ystress = binding.etYS.text.toString().trim()
        val utsys = binding.etUtsYs.text.toString().trim()
        val yload = binding.etYLoad.text.toString().trim()

        binding.btnSubmit.isEnabled = (qrId.isNotEmpty()
                && date.isNotEmpty()
                && sampleId.isNotEmpty()
                && sampleType.isNotEmpty()
                && csa.isNotEmpty()
                && length.isNotEmpty()
                && weight.isNotEmpty()
                && oggl.isNotEmpty()
                && fgl.isNotEmpty()
                && maxLoad.isNotEmpty()
                && daml.isNotEmpty()
                && ultstress.isNotEmpty()
                && elong.isNotEmpty()
                && ystress.isNotEmpty()
                && utsys.isNotEmpty()
                && yload.isNotEmpty())
    }

    private fun savePhysicalTest() {
        val qrId = binding.etQrId.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val sampleId = binding.etSI.text.toString().trim()
        val sampleType = binding.etST.text.toString().trim()
        val csa = binding.etCSA.text.toString().trim()
        val length = binding.etLength.text.toString().trim()
        val weight = binding.etWeight.text.toString().trim()
        val oggl = binding.etOGL.text.toString().trim()
        val fgl = binding.etFGL.text.toString().trim()
        val maxLoad = binding.etML.text.toString().trim()
        val daml = binding.etDML.text.toString().trim()
        val ultstress = binding.etULTS.text.toString().trim()
        val elong = binding.etElong.text.toString().trim()
        val ystress = binding.etYS.text.toString().trim()
        val utsys = binding.etUtsYs.text.toString().trim()
        val yload = binding.etYLoad.text.toString().trim()

        if(qrId.isEmpty()) {
            Toast.makeText(this, "Qr id can not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if(date.isEmpty()) {
            Toast.makeText(this, "date can not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if(sampleId.isEmpty()) {
            Toast.makeText(this, "sampleId can not be empty", Toast.LENGTH_SHORT).show()
            binding.etSI.requestFocus()
            return
        }

        if(sampleType.isEmpty()) {
            Toast.makeText(this, "sampleType can not be empty", Toast.LENGTH_SHORT).show()
            binding.etST.requestFocus()
            return
        }

        if(csa.isEmpty()) {
            Toast.makeText(this, "csa can not be empty", Toast.LENGTH_SHORT).show()
            binding.etCSA.requestFocus()
            return
        }

        if(length.isEmpty()) {
            Toast.makeText(this, "length can not be empty", Toast.LENGTH_SHORT).show()
            binding.etLength.requestFocus()
            return
        }

        if(weight.isEmpty()) {
            Toast.makeText(this, "weight can not be empty", Toast.LENGTH_SHORT).show()
            binding.etWeight.requestFocus()
            return
        }

        if(oggl.isEmpty()) {
            Toast.makeText(this, "oggl can not be empty", Toast.LENGTH_SHORT).show()
            binding.etOGL.requestFocus()
            return
        }

        if(fgl.isEmpty()) {
            Toast.makeText(this, "fgl can not be empty", Toast.LENGTH_SHORT).show()
            binding.etFGL.requestFocus()
            return
        }

        if(maxLoad.isEmpty()) {
            Toast.makeText(this, "maxLoad can not be empty", Toast.LENGTH_SHORT).show()
            binding.etML.requestFocus()
            return
        }

        if(daml.isEmpty()) {
            Toast.makeText(this, "daml can not be empty", Toast.LENGTH_SHORT).show()
            binding.etDML.requestFocus()
            return
        }

        if(ultstress.isEmpty()) {
            Toast.makeText(this, "ultstress can not be empty", Toast.LENGTH_SHORT).show()
            binding.etULTS.requestFocus()
            return
        }

        if(elong.isEmpty()) {
            Toast.makeText(this, "Elong can not be empty", Toast.LENGTH_SHORT).show()
            binding.etElong.requestFocus()
            return
        }

        if(ystress.isEmpty()) {
            Toast.makeText(this, "ystress can not be empty", Toast.LENGTH_SHORT).show()
            binding.etYS.requestFocus()
            return
        }

        if(utsys.isEmpty()) {
            Toast.makeText(this, "utsys can not be empty", Toast.LENGTH_SHORT).show()
            binding.etUtsYs.requestFocus()
            return
        }

        if(yload.isEmpty()) {
            Toast.makeText(this, "yeild load can not be empty", Toast.LENGTH_SHORT).show()
            binding.etYLoad.requestFocus()
            return
        }

        val physicalTestModel = PhysicalTestModel(
            qr_id = qrId,
            user_id = sharedPreferences.getString(userId, "")!!,
            c_s_area = csa,
            date = date,
            disp_at_max_load = daml,
            elongation = elong,
            final_gauge_len = fgl,
            length = length,
            max_load = maxLoad,
            og_gauge_len = oggl,
            sample_indentification = sampleId,
            sample_type = sampleType,
            ult_stress = ultstress,
            uts_ys = utsys,
            weight = weight,
            yield_stress = ystress,
            yield_load = yload
        )

//        Log.i("TAG", "savePhysicalTest: ${qrId.split("id=")[1]}")
//        Log.i("TAG", "savePhysicalTest: ${physicalTestModel}")

        viewModel.savePhysicalTest(physicalTestModel)
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
                // if the intentResult is not null we'll set
                // the content and format of scan message

                    val qrId = intentResult.contents.split("id=")[1]
                    binding.etQrId.setText(qrId)
                    viewModel.checkIfDataExist(QrModel(
                        qr_id = qrId,
                        type = "2"
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