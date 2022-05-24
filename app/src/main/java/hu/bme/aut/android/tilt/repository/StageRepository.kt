package hu.bme.aut.android.tilt.repository

import androidx.lifecycle.MutableLiveData
import hu.bme.aut.android.tilt.model.Stage

class StageRepository {
    private val stages = mutableListOf(
        Stage(
            1,
            1,
            10,
            "Aladár",
            ""
        )
    )

    fun getAll() = MutableLiveData(stages)

    fun getById(id: Long) = stages.filter { it.id == id }.getOrNull(0).also { MutableLiveData(it) }

    fun add(stage: Stage) {
        stages.add(stage)
    }
}