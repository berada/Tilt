package hu.bme.aut.android.tilt.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hu.bme.aut.android.tilt.StageBrowserApplication
import hu.bme.aut.android.tilt.model.Stage
import hu.bme.aut.android.tilt.repository.StageRepository

class StageListViewModel : ViewModel() {
    private val stageRepository: StageRepository
    val stageList: MutableLiveData<MutableList<Stage>>


    init {
        stageRepository = StageBrowserApplication.stageRepository
        stageList = stageRepository.getAll()
    }

    fun add(stage: Stage) {
        stageRepository.add(stage)
    }
}