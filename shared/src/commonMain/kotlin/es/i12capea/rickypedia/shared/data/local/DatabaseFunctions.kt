package es.i12capea.rickypedia.shared.data.local

import com.squareup.sqldelight.ColumnAdapter
import es.i12capea.rickypedia.shared.data.local.model.LocalLocationShort

val listOfStringsAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String) =
        if (databaseValue.isEmpty()) {
            listOf()
        } else {
            databaseValue.split(";")
        }
    override fun encode(value: List<String>) = value.joinToString(separator = ";")
}

val listOfIntsAdapter = object : ColumnAdapter<List<Int>, String> {
    override fun decode(databaseValue: String) =
        if (databaseValue.isEmpty()) {
            listOf()
        } else {
            val intValues = ArrayList<Int>()
            val strValues = databaseValue.split(";")
            for (value in strValues){
                kotlin.runCatching {
                    intValues.add(value.toInt())
                }
            }
            intValues
        }
    override fun encode(value: List<Int>) =  value.joinToString(separator = ";")
}

val localLocationShortAdapter = object : ColumnAdapter<LocalLocationShort, String>{
    override fun decode(databaseValue: String): LocalLocationShort {
        val split = databaseValue.split(';')
        val id = try {
            split[0].toInt()
        }catch (e: NumberFormatException){
            0
        }
        return LocalLocationShort(
            id,
            split[1]
        )
    }

    override fun encode(value: LocalLocationShort): String {
        return "${value.locationId};${value.name}"
    }
}