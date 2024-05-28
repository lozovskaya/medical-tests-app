import android.util.Log
import com.github.mikephil.charting.data.Entry
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


object PdfParserApiClient {
    private val TAG: String = PdfParserApiClient::class.java.simpleName
    //private val SERVERURL = "https://localhost:5000" // todo: replace
    private val SERVERURL = "http://10.0.2.2:5000" // todo: replace

    fun parsePdfTest(callback: PdfParseCallback) {
        val resultsInvitro = ArrayList<MedicalTestResultFromPDF>()
        resultsInvitro.add(MedicalTestResultFromPDF("ТТГ", 2.84F, 0.4F, 4.0F, "мЕд/л"))
        resultsInvitro.add(MedicalTestResultFromPDF("ФСГ", 4.05F, 0.57F, 8.77F, "мМЕд/мл"))
        resultsInvitro.add(MedicalTestResultFromPDF("ЛГ", 9.10F, 0F, 15.97F, "мМЕд/мл"))
        resultsInvitro.add(MedicalTestResultFromPDF("Эстрадиол", 77F, 68F, 1269F, "пмоль/л"))
        resultsInvitro.add(MedicalTestResultFromPDF("ДЭА - SO4", 12.7f, 3.6f, 11.1f, "мкмоль/л"))
        resultsInvitro.add(MedicalTestResultFromPDF("Тестостерон", 1.11f, 0.52f, 1.72f, "нмоль/л"))
        resultsInvitro.add(MedicalTestResultFromPDF("ГСПГ", 28.3f, 14.7f, 122.5f, "нмоль/л"))
        resultsInvitro.add(MedicalTestResultFromPDF("Пролактин", 305f, 109f, 557f, "мЕд/л"))
        callback.onSuccess(resultsInvitro)
    }


    fun parsePdf(pdfFile: File, callback: PdfParseCallback) {

        val client = OkHttpClient()
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "file",
                pdfFile.name,
                RequestBody.create("application/pdf".toMediaTypeOrNull(), pdfFile)
            )
            .build()

        val request: Request = Request.Builder()
            .url("$SERVERURL/")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonData: String = response.body!!.string()
                    callback.onSuccess(null)
                } else {
                    val errorMessage = "Failed to parse PDF on server side: " + response.message
                    Log.e(TAG, errorMessage)
                    callback.onFailure(errorMessage)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback.onFailure("Failed to parse PDF: " + e.message)
            }
        })
    }

    interface PdfParseCallback {
        fun onSuccess(parsedData: List<MedicalTestResultFromPDF>?)
        fun onFailure(errorMessage: String?)
    }

    data class MedicalTestResultFromPDF(
        val name : String,
        val result : Float,
        val referenceRangeMin : Float,
        val referenceRangeMax: Float,
        val units : String,
    )
}