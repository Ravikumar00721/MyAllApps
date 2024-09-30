package msi.crool.roomdatabase

import android.app.Application

class EmpApplication:Application() {
    val db by lazy {
        EmpDataBase.getInstance(this)
    }
}