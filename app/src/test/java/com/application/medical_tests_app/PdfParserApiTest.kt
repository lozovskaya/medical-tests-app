package com.application.medical_tests_app

import PdfParserApiClient.PdfParseCallback
import PdfParserApiClient.parsePdf
import java.io.File


object PdfParserApiTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val pdfFilePath = "test.pdf"

        parsePdf(File(pdfFilePath), object : PdfParseCallback {
            override fun onSuccess(parsedData: String?) {
                println("Parsing successful:")
                println(parsedData)
            }

            override fun onFailure(errorMessage: String?) {
                println("Parsing failed:")
                println(errorMessage)
            }
        })
    }
}
