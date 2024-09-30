package msi.crool.gym

import android.app.Application

class WorkOutApp:Application() {
    val db by lazy {
        HistoryDB.getInstance(this)
    }
}