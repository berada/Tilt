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
        ),
        Stage(
            2,
            2,
            9,
            "Béla",
            ""
        ),

        Stage(
            3,
            3,
            9,
            "Cecil",
            ""
        ),

        Stage(
            4,
            4,
            9,
            "Dénes",
            ""
        ),
        Stage(
            5,
            5,
            9,
            "Elemér",
            ""
    )
    )

    fun getAll() = MutableLiveData(stages)

    fun getById(id: Long) = stages.filter { it.id == id }.getOrNull(0).also { MutableLiveData(it) }

    fun add(stage: Stage) {
        stages.add(stage)
    }
}