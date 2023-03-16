import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.g2_qc.location
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*



/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.update", appContext.packageName)
    }
    @Test
    fun testStartLocationUpdates() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val locationObj = location(context)

        locationObj.startLocationUpdates()

        assertNotNull(locationObj.fusedLocationProviderClient)
        assertNotNull(locationObj.locationCallback)

        locationObj.stopLocationUpdates()
    }

    @Test
    fun testStopLocationUpdates() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val locationObj = location(context)

        locationObj.startLocationUpdates()

        assertNotNull(locationObj.fusedLocationProviderClient)
        assertNotNull(locationObj.locationCallback)

        locationObj.stopLocationUpdates()

        assertNull(locationObj.fusedLocationProviderClient)
        assertNull(locationObj.locationCallback)
    }
}