package es.i12capea.rickypedia.shared.data.local

import com.squareup.sqldelight.db.SqlDriver
import es.i12capea.rickypedia.shared.data.local.model.LocalCharacter
import mylocal.db.LocalDb

expect class DriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): LocalDb {
    val driver = driverFactory.createDriver()

    val adapter = LocalCharacter.Adapter(
        localLocationShortAdapter,
        localLocationShortAdapter,
        listOfIntsAdapter
    )

    return LocalDb(driver, adapter)

    // Do more work with the database (see below).
}

object Database {

    val db = createDatabase(DriverFactory().createDriver())


}