package msi.crool.happyplaces.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.AsyncTask
import java.lang.StringBuilder
import java.util.Locale

class GetAddressFromLatLng(
    context: Context,
    private val latitude: Double,
    private val longitude: Double
) : AsyncTask<Void, Void, String?>() {

    private val geocoder: Geocoder = Geocoder(context, Locale.getDefault())
    private var addressListener: AddressListener? = null

    override fun doInBackground(vararg params: Void?): String? {
        return try {
            val addressList: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]
                val sb = StringBuilder()
                for (i in 0..address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append(" ")
                }
                sb.toString().trim()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(result: String?) {
        if (result.isNullOrEmpty()) {
            addressListener?.onError()
        } else {
            addressListener?.onAddressFound(result)
        }
    }

    fun setAddressListener(listener: AddressListener) {
        this.addressListener = listener
    }

    fun getAddress() {
        execute()
    }

    interface AddressListener {
        fun onAddressFound(address: String?)
        fun onError()
    }
}
