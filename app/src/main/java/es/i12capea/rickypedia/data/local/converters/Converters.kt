package es.i12capea.rickypedia.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.i12capea.rickypedia.data.api.models.Info
import es.i12capea.rickypedia.data.api.models.character.RemoteCharacter
import es.i12capea.rickypedia.data.local.model.LocalLocationShort
import java.lang.reflect.Type

class Converters {
    @TypeConverter
    fun fromRemoteListCharacter(list: List<RemoteCharacter>) : String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toRemoteListCharacter(str: String) : List<RemoteCharacter>{
        val listType: Type = object : TypeToken<List<RemoteCharacter>>() {}.type
        return Gson().fromJson(str, listType)
    }


    @TypeConverter
    fun fromRemoteLocationShort(locationShort: LocalLocationShort) : String{
        return "${locationShort.locationId};${locationShort.name}"
    }

    @TypeConverter
    fun stringToRemoteLocationShort(str: String) : LocalLocationShort {
        val split = str.split(';')
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


    @TypeConverter
    fun fromListInt(list: List<Int>) : String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromStringToListIn(value : String) : List<Int>{
        val listType: Type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType: Type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromInfo(info: Info) : String{
        return Gson().toJson(info)
    }

    @TypeConverter
    fun toInfo(str: String) : Info {
        return Gson().fromJson(str, Info::class.java)
    }
}