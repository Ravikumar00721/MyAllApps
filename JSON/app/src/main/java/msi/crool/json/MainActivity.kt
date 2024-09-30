package msi.crool.json

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CallAPILoginAsync("Ravi Kumar","12ka4").execute()
    }

    private inner class CallAPILoginAsync(val username:String,val password:String) : AsyncTask<Any, Void, String>() {
        private lateinit var customProgressDialog: Dialog

        override fun doInBackground(vararg params: Any?): String {
            var result = ""
            var connection: HttpURLConnection? = null
            try {
                val url = URL("https://run.mocky.io/v3/00c7ca1e-acd1-4074-947c-4f4dacd460d5")
                connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.doOutput = true
                //used for redirect purpose
                connection.instanceFollowRedirects=false
                connection.requestMethod="POST"
                connection.setRequestProperty("Content-Type","application/json")
                connection.setRequestProperty("charset","utf-8")
                connection.setRequestProperty("Accept","application/json")
                //Setting useCaches to false ensures that the request does not use any cached data and that the response is fetched directly from the server.
                connection.useCaches=false

                val writeDAtaOutputStream=DataOutputStream(connection.outputStream)
                val jsonRequest=JSONObject()
                jsonRequest.put("username",username)
                jsonRequest.put("password",password)
                writeDAtaOutputStream.writeBytes(jsonRequest.toString())
                writeDAtaOutputStream.flush()
                writeDAtaOutputStream.close()

                val httpResult: Int = connection.responseCode
                if (httpResult == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String?
                    try {
                        while (reader.readLine().also { line = it } != null) {
                            stringBuilder.append(line + "\n")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        try {
                            inputStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    result = stringBuilder.toString()
                } else {
                    result = connection.responseMessage
                }
            } catch (e: SocketTimeoutException) {
                result = "Connection TimeOut"
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }
            return result
        }

        override fun onPreExecute() {
            super.onPreExecute()
            showProgressDialog()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            cancelProgress()
            Log.d("RESULT",result.toString())
            // You can now use the result variable here to update the UI or handle the result
        }

        private fun showProgressDialog() {
            customProgressDialog = Dialog(this@MainActivity)
            customProgressDialog.setContentView(R.layout.customprogressdialog)
            customProgressDialog.show()
        }

        private fun cancelProgress() {
            customProgressDialog.dismiss()
        }
    }
}
