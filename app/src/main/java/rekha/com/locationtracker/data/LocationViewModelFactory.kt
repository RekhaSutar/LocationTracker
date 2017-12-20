package rekha.com.locationtracker.data

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class LocationViewModelFactory : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            return  LocationViewModel() as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
