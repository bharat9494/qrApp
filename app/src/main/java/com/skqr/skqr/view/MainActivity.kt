package com.skqr.skqr.view

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.integration.android.IntentIntegrator
import com.skqr.skqr.R
import com.skqr.skqr.databinding.ActivityMainBinding
import com.skqr.skqr.misc.userId
import com.skqr.skqr.view.fragments.ShowTypeMasterFragment
import com.skqr.skqr.viewModel.MainActivityViewModel
import com.skqr.skqr.viewModel.MainActivityViewModelFactory

class MainActivity : AppCompatActivity(), View.OnClickListener, ShowTypeMasterFragment.SelectedTypeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: MainActivityViewModelFactory

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.appOrange, theme)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        viewModelFactory = MainActivityViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)

        if(sharedPreferences.getString(userId, "") == "") {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnCreateQr.setOnClickListener(this)
        binding.btnSavePhy.setOnClickListener(this)
        binding.btnSaveChem.setOnClickListener(this)
        binding.btnViewDetails.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if(view == binding.btnCreateQr) {
            val fm = supportFragmentManager
            val bottomTypeMasterFragment = ShowTypeMasterFragment.newInstance("", "")
            bottomTypeMasterFragment.show(fm, "bottomSheet")
        }

        else if(view == binding.btnSavePhy) {
            val intent = Intent(this, SavePhysicalTestActivity::class.java)
            startActivity(intent)
        }

        else if(view == binding.btnSaveChem) {
            val intent = Intent(this, SaveChemicalTestActivity::class.java)
            startActivity(intent)
        }

        else if(view == binding.btnViewDetails) {
            IntentIntegrator(this)
                .setPrompt("Scan a QR Code")
                .setOrientationLocked(true)
                .initiateScan()
        }

    }

    override fun onSelectedType(typeId: String?) {
        if(typeId != null) {
            val intent = Intent(this, CreateQrActivity::class.java)
            intent.putExtra("typeId", typeId)
            startActivity(intent)
        }
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
                val intent = Intent(this, ViewProductDetailsActivity::class.java)
                intent.putExtra("qr_id", intentResult.contents.split("id=")[1])
                startActivity(intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}