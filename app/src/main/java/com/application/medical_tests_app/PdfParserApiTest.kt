package com.application.medical_tests_app

import PdfParserApiClient.PdfParseCallback
import PdfParserApiClient.parsePdf
import android.util.Log
import java.io.File


fun main(args: Array<String>) {
    val pdfFilePath = "/Users/alina/AndroidStudioProjects/medical_tests_app/app/src/main/java/com/application/medical_tests_app/emias.pdf"

    parsePdf(File(pdfFilePath), object : PdfParseCallback {
        override fun onSuccess(parsedData: String?) {
            println("Parsing successful:")
            if (parsedData != null) {
                println(parsedData)
            }
        }

        override fun onFailure(errorMessage: String?) {
            println("Parsing failed:")
            if (errorMessage != null) {
                println(errorMessage)
            }
        }
    })
}

