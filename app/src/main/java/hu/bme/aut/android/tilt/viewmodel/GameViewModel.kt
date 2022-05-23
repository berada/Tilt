package hu.bme.aut.android.tilt.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.tilt.StageBrowserApplication
import hu.bme.aut.android.tilt.model.Stage

class GameViewModel : ViewModel() {

    fun getById(id: Long) =
        StageBrowserApplication.stageRepository.getById(id).let { MutableLiveData<Stage>(it) }
}