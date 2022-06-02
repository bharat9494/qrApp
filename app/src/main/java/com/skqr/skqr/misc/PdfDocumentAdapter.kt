package com.skqr.skqr.misc

import android.content.Context
import android.content.Context.PRINT_SERVICE
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.pdf.PrintedPdfDocument
import android.util.Log
import java.io.*


class PdfDocumentAdapter(val context: Context, val bitmap: Bitmap): PrintDocumentAdapter() {
    var pathName = ""
    var pdfDocument: PrintedPdfDocument? = null

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback,
        extras: Bundle?
    ) {
        // Create a new PdfDocument with the requested page attributes

        val printAttrs = PrintAttributes.Builder().setColorMode(PrintAttributes.COLOR_MODE_COLOR)
            .setMediaSize(PrintAttributes.MediaSize.ISO_B9)
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            .build()

        pdfDocument = PrintedPdfDocument(context, printAttrs)

        // Respond to cancellation request
        if (cancellationSignal?.isCanceled == true) {
            callback.onLayoutCancelled()
            return
        }

        PrintDocumentInfo.Builder("print_output.pdf")
            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
            .setPageCount(1)
            .build()
            .also { info ->
                // Content layout reflow is complete
                callback.onLayoutFinished(info, true)
            }
    }

    override fun onWrite(
        pageRanges: Array<out PageRange>,
        destination: ParcelFileDescriptor,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback
    ) {
        pdfDocument?.startPage(0)?.also { page ->

            // check for cancellation
            if (cancellationSignal?.isCanceled == true) {
                callback.onWriteCancelled()
                pdfDocument?.close()
                pdfDocument = null
                return
            }

            // Draw page content for printing
            drawPage(page)

            // Rendering is complete, so page can be finalized.
            pdfDocument?.finishPage(page)
        }

        // Write PDF document to file
        try {
            pdfDocument?.writeTo(FileOutputStream(destination.fileDescriptor))
        } catch (e: IOException) {
            callback.onWriteFailed(e.toString())
            return
        } finally {
            pdfDocument?.close()
            pdfDocument = null
        }
        callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
    }

    private fun drawPage(page: PdfDocument.Page) {
        page.canvas.apply {
            Log.i("TAG", "drawPage: ")
//            val paint = Paint()
//            paint.color = Color.BLACK
            drawBitmap(bitmap, 0f, 0f, null);
        }
    }
}