package msi.crool.roomdemo

import android.app.Application

class EmployeeApp:Application() {
    val db by lazy {
        EmpDB.getInstance(this)
    }
}