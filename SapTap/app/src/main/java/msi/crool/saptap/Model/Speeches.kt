package msi.crool.saptap.Model

import android.os.Parcel
import android.os.Parcelable

data class Speeches(
    val response: String? // Adjusted to match the JSON response field
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(response)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Speeches> {
        override fun createFromParcel(parcel: Parcel): Speeches {
            return Speeches(parcel)
        }

        override fun newArray(size: Int): Array<Speeches?> {
            return arrayOfNulls(size)
        }
    }
}
