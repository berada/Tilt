package hu.bme.aut.android.tilt

import android.app.Application
import hu.bme.aut.android.tilt.repository.StageRepository

class StageBrowserApplication : Application() {

    companion object {
        lateinit var stageRepository: StageRepository
            private set
    }

    override fun onCreate() {
        super.onCreate()
        stageRepository = StageRepository()
    }
}