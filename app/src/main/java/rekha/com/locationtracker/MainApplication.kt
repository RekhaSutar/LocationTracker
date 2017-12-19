package rekha.com.locationtracker

import android.app.Application
import com.facebook.stetho.Stetho

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        application = this

        Stetho.initializeWithDefaults(this);
    }

    companion object {

        lateinit var application: MainApplication
        fun getInstance(): MainApplication {
            return application
        }
    }

}