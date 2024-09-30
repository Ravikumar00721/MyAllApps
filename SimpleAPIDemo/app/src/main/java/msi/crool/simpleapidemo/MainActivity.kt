package msi.crool.simpleapidemo

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import msi.crool.simpleapidemo.ui.theme.SimpleAPIDemoTheme
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.URL

class MainActivity : ComponentActivity() {
    private lateinit var customProgressDialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CallAPILoginAsyncTask("ravikumar", "1234567").execute()
    }

    private inner class CallAPILoginAsyncTask(val username: String, val password: String) :
        AsyncTask<Any, Void, String>() {

        override fun doInBackground(vararg params: Any?): String {
            var result: String
            var connection: HttpURLConnection? = null

            try {
                val url = URL("https://run.mocky.io/v3/c543957b-e6f4-4756-9e7a-3a9a15c1d5b3")
                connection = url.openConnection() as HttpURLConnection
                connection.doInput
                connection.doOutput

                connection.instanceFollowRedirects = false

                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-type", "application/json")
                connection.setRequestProperty("charset", "utf-8")
                connection.setRequestProperty("Accept", "application/json")
                connection.useCaches = false

                val writeDataOutPutStream = DataOutputStream(connection.outputStream)
                val jsonRequest = JSONObject()
                jsonRequest.put("username", username)
                jsonRequest.put("password", password)

                writeDataOutPutStream.writeBytes(jsonRequest.toString())
                writeDataOutPutStream.flush()
                writeDataOutPutStream.close()


                val httpResult: Int = connection.responseCode
                if (httpResult == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream

                    val reader = BufferedReader(InputStreamReader(inputStream))

                    val sb = StringBuilder()
                    var line: String?
                    try {
                        while (reader.readLine().also { line = it } != null) {
                            sb.append(line + "\n")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        try {
                            inputStream.close()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    result = sb.toString()
                } else {
                    result = connection.responseMessage
                }
            } catch (e: SocketTimeoutException) {
                result = "ConnectionTimeOut"
            } catch (e: Exception) {
                result = "Error : " + e.message
            } finally {
                connection?.disconnect()
            }
            Log.d("RESULT", result)
            return result;
        }

        override fun onPreExecute() {
            super.onPreExecute()
            showProgressDialog()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            cancelProgressDialog()

            val responseData = Gson().fromJson(result, ResponseData::class.java)
            // Logging each field from the ResponseData object
            Log.d("ResponseData", "ID: ${responseData.id}")
            Log.d("ResponseData", "Type: ${responseData.type}")
            Log.d("ResponseData", "Name: ${responseData.name}")
            Log.d("ResponseData", "PPU: ${responseData.ppu}")

// Logging nested Batters object
            Log.d("ResponseData", "Batters Type: ${responseData.batters.type}")
            val listType = object : TypeToken<List<ResponseData>>() {}.type
            val responseDataList: List<ResponseData> = Gson().fromJson(result, listType)

// Iterate through the list
            for (item in responseDataList) {
                Log.d("ResponseData", "ID: ${item.id}")
                Log.d("ResponseData", "Type: ${item.type}")
                Log.d("ResponseData", "Name: ${item.name}")
                Log.d("ResponseData", "PPU: ${item.ppu}")
                Log.d("ResponseData", "Batters Type: ${item.batters.type}")

//            val jsonObject= result?.let { JSONObject(it) }
//            val color= jsonObject?.optJSONObject("batters")
//            val real=color?.optString("type")
//                if (real != null) {
//                    Log.d("MESSAGE", real)
//                }

            }

        }

        private fun showProgressDialog() {
            customProgressDialog = Dialog(this@MainActivity)
            customProgressDialog.setContentView(R.layout.dialogcustomview)
            customProgressDialog.show()
        }

        private fun cancelProgressDialog() {
            customProgressDialog.dismiss()
        }
    }
}
