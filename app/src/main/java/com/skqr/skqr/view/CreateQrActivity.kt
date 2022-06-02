package com.skqr.skqr.view

import android.bluetooth.BluetoothAdapter
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.brother.ptouch.sdk.NetPrinter
import com.brother.ptouch.sdk.Printer
import com.brother.sdk.lmprinter.*
import com.brother.sdk.lmprinter.setting.CustomPaperSize
import com.brother.sdk.lmprinter.setting.TDPrintSettings
import com.bumptech.glide.Glide
import com.skqr.skqr.R
import com.skqr.skqr.data.models.CreateQrModel
import com.skqr.skqr.databinding.ActivityCreateQrBinding
import com.skqr.skqr.misc.userId
import com.skqr.skqr.viewModel.CreateQrViewModel
import com.skqr.skqr.viewModel.CreateQrViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import kotlin.concurrent.thread


class CreateQrActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityCreateQrBinding
    private lateinit var viewModel: CreateQrViewModel
    private lateinit var viewModelFactory: CreateQrViewModelFactory

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = resources.getColor(R.color.appOrange, theme)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        viewModelFactory = CreateQrViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CreateQrViewModel::class.java)
            .apply {
                typeId = intent.getStringExtra("typeId").toString()
            }

        if(viewModel.typeId.isNotEmpty()) {
            viewModel.createQr(
                CreateQrModel(sharedPreferences.getString(userId, "")!!, viewModel.typeId)
            )
        }

        viewModel.qrResponse.observe(this) {
            if(it.isSuccessful) {
                binding.progressLoader.visibility = View.GONE
                Glide.with(this)
                    .load(it.body()?.qr_image)
                    .centerInside()
                    .error(R.drawable.ic_no_data)
                    .into(binding.imageViewQr)
            }
        }

        binding.printQr.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if(view == binding.printQr) {
            val bitmap = (binding.imageViewQr.drawable as BitmapDrawable).bitmap
            searchBLEPrinter(bitmap)

//            val photoPrinter = PrintHelper(this@CreateQrActivity)
//            photoPrinter.scaleMode = PrintHelper.SCALE_MODE_FIT
//
//            val builder = PrintAttributes.Builder()
//            builder.setMediaSize(PrintAttributes.MediaSize.ISO_B9)
//
//            //Bitmap bitmap = imageView.getDrawingCache(  );
//            val bitmap = (binding.imageViewQr.drawable as BitmapDrawable).bitmap
//            //photoPrinter.printBitmap("printSkQR", bitmap)
//
//            val printManager = this.getSystemService(PRINT_SERVICE) as PrintManager
//            val jobName = "${getString(R.string.app_name)} Document"
//            printManager.print(jobName, PdfDocumentAdapter(this, bitmap), builder.build())
        }
    }

    private fun searchWiFiPrinter(bitmap: Bitmap) {
        try {
            CoroutineScope(Dispatchers.Main).launch {
                val printer = Printer()
                val printerList: Array<NetPrinter> = printer.getNetPrinters("QL-820NWB")
                for (printer in printerList) {
                    Log.d("TAG",
                        String.format(
                            "Model: %s, IP Address: %s",
                            printer.modelName,
                            printer.ipAddress
                        )
                    )

                    printImage(printer.ipAddress, bitmap)
                    break
                }

                if(printerList.isEmpty()) {
                    Toast.makeText(this@CreateQrActivity, "No Printer found!!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(this@CreateQrActivity, ex.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    fun searchBLEPrinter(bitmap: Bitmap) {
        try {
            CoroutineScope(Dispatchers.Main).launch {
                val printer = Printer();
                val printerList = printer.getBLEPrinters(BluetoothAdapter.getDefaultAdapter(), 30);
                for (printer in printerList) {
                    printImage(printer.localName, bitmap)
                    break
                }

                if(printerList.isEmpty()) {
                    Toast.makeText(this@CreateQrActivity, "No Printer found!!", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(this@CreateQrActivity, ex.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun printImage(name: String, bitmap: Bitmap) {
        //val channel: Channel = Channel.newBluetoothLowEnergyChannel(name, this, BluetoothAdapter.getDefaultAdapter())
        val channel: Channel = Channel.newWifiChannel(name)
        val result = PrinterDriverGenerator.openChannel(channel)
        if (result.error.code != OpenChannelError.ErrorCode.NoError) {
            Toast.makeText(this@CreateQrActivity, result.error.toString(), Toast.LENGTH_SHORT).show()
            return
        }

        val printerDriver = result.driver
        val printSettings = TDPrintSettings(PrinterModel.QL_820NWB)
        val margins = CustomPaperSize.Margins(0.0f, 0.0f, 0.0f, 0.0f)
        val customPaperSize =
            CustomPaperSize.newRollPaperSize(62f, margins, CustomPaperSize.Unit.Mm)
        printSettings.customPaperSize = customPaperSize
        val printError: PrintError = printerDriver.printImage(bitmap, printSettings)
        if (printError.code != PrintError.ErrorCode.NoError) {
            Toast.makeText(this@CreateQrActivity, printError.code.toString(), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@CreateQrActivity, "success", Toast.LENGTH_SHORT).show()
        }
        printerDriver.closeChannel()
    }

    /**
     * Thread for printing
     */
//    private class V4PrinterThread private constructor(val context: Context) : Thread() {
//        private var sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
//
//        private fun waitForUSBAuthorizationRequest(port: PrinterInfo.Port) {
//            if (port == PrinterInfo.Port.USB) {
//                while (true) {
//                    if (mUsbAuthorizationState !== UsbAuthorizationState.NOT_DETERMINED) {
//                        break
//                    }
//                    try {
//                        sleep(50)
//                    } catch (e: InterruptedException) {
//                    }
//                }
//            }
//        }
//
//        private fun currentPrintSettings(currentModel: PrinterModel): PrintSettings {
//            val currentModelString = currentModel.name
//            val gson = Gson()
//            if (currentModelString.startsWith("QL")) {
//                val qlPrintSettingsJson: String? =
//                    sharedPreferences.getString("qlV4PrintSettings", "")
//                return if (qlPrintSettingsJson === "") {
//                    QLPrintSettings(currentModel)
//                } else {
//                    gson.fromJson(qlPrintSettingsJson, QLPrintSettings::class.java)
//                        .copyPrintSettings(currentModel)
//                }
//            } else if (currentModelString.startsWith("PT")) {
//                val ptPrintSettingsJson: String? =
//                    sharedPreferences.getString("ptV4PrintSettings", "")
//                return if (ptPrintSettingsJson === "") {
//                    PTPrintSettings(currentModel)
//                } else {
//                    gson.fromJson(ptPrintSettingsJson, PTPrintSettings::class.java)
//                        .copyPrintSettings(currentModel)
//                }
//            } else if (currentModelString.startsWith("PJ")) {
//                val pjPrintSettingsJson: String? =
//                    sharedPreferences.getString("pjV4PrintSettings", "")
//                return if (pjPrintSettingsJson === "") {
//                    PJPrintSettings(currentModel)
//                } else {
//                    gson.fromJson(pjPrintSettingsJson, PJPrintSettings::class.java)
//                        .copyPrintSettings(currentModel)
//                }
//            } else if (currentModelString.startsWith("RJ")) {
//                val rjPrintSettingsJson: String? =
//                    sharedPreferences.getString("rjV4PrintSettings", "")
//                return if (rjPrintSettingsJson === "") {
//                    RJPrintSettings(currentModel)
//                } else {
//                    gson.fromJson(rjPrintSettingsJson, RJPrintSettings::class.java)
//                        .copyPrintSettings(currentModel)
//                }
//            } else if (currentModelString.startsWith("TD")) {
//                val tdPrintSettingsJson: String? =
//                    sharedPreferences.getString("tdV4PrintSettings", "")
//                return if (tdPrintSettingsJson === "") {
//                    TDPrintSettings(currentModel)
//                } else {
//                    gson.fromJson(tdPrintSettingsJson, TDPrintSettings::class.java)
//                        .copyPrintSettings(currentModel)
//                }
//            } else if (currentModelString.startsWith("MW")) {
//                val mwPrintSettingsJson: String? =
//                    sharedPreferences.getString("mwV4PrintSettings", "")
//                return if (mwPrintSettingsJson === "") {
//                    MWPrintSettings(currentModel)
//                } else {
//                    gson.fromJson(mwPrintSettingsJson, MWPrintSettings::class.java)
//                        .copyPrintSettings(currentModel)
//                }
//            }
//            return QLPrintSettings(currentModel)
//        }
//
//        override fun run() {
//            val model = PrinterInfo.Model.valueOf(
//                sharedPreferences.getString("printerModel", "")!!
//            )
//            val port = PrinterInfo.Port.valueOf(
//                sharedPreferences.getString("port", "")!!
//            )
//            val ipAddress: String = sharedPreferences.getString("address", "")!!
//            val macAddress: String = sharedPreferences.getString("macAddress", "")!!
//            val localName: String = sharedPreferences.getString("localName", "")!!
//            waitForUSBAuthorizationRequest(port)
//            val channel: Channel = when (port) {
//                PrinterInfo.Port.BLUETOOTH -> Channel.newBluetoothChannel(
//                    macAddress,
//                    BluetoothAdapter.getDefaultAdapter()
//                )
//                PrinterInfo.Port.BLE -> Channel.newBluetoothLowEnergyChannel(
//                    localName,
//                    context, BluetoothAdapter.getDefaultAdapter()
//                )
//                PrinterInfo.Port.USB -> Channel.newUsbChannel(
//                    context.getSystemService(USB_SERVICE) as UsbManager
//                )
//                PrinterInfo.Port.NET -> Channel.newWifiChannel(ipAddress)
//                else -> return
//            }
//            // start message
//            var msg: Message = mHandle.obtainMessage(MSG_PRINT_START)
//            mHandle.sendMessage(msg)
//
//            // Create a `PrinterDriver` instance
//            val result = PrinterDriverGenerator.openChannel(channel)
//            if (result.error.code != OpenChannelError.ErrorCode.NoError) {
//                mHandle.setResult(result.error.code.toString())
//                mHandle.sendMessage(mHandle.obtainMessage(MSG_PRINT_END))
//                return
//            }
//            val printerDriver = result.driver
//            val gson = Gson()
//            val v4model = PrinterModel.valueOf(model.toString())
//
//            // Initialize `PrintSettings`
//            val printSettings = currentPrintSettings(v4model)
//            val FilePaths: Array<String> = mFiles.toTypedArray<String>()
//            // Print the image
//            val printError = printerDriver.printImage(FilePaths, printSettings)
//            if (printError.code != PrintError.ErrorCode.NoError) {
//                printerDriver.closeChannel()
//                mHandle.setResult(printError.code.toString())
//                mHandle.sendMessage(mHandle.obtainMessage(Common.MSG_PRINT_END))
//                return
//            }
//            printerDriver.closeChannel()
//
//            // end message
//            mHandle.setResult("Success")
//            msg = mHandle.obtainMessage(Common.MSG_PRINT_END)
//            mHandle.sendMessage(msg)
//        }
//    }
}