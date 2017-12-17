package rekha.com.locationtracker.view

import android.support.v7.app.AppCompatActivity
import android.widget.Toast

open class BaseActivity : AppCompatActivity() {
    fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
