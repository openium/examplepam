package fr.openium.examplepam.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import fr.openium.examplepam.database.AppDatabase
import fr.openium.examplepam.model.Call

/**
 * Created by t.coulange on 01/02/2021.
 */
class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    val calls: LiveData<List<Call>> by lazy {
        AppDatabase.getInstance(application.applicationContext).callDao().getCalls()
    }

}