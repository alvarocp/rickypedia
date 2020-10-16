package es.i12capea.rickandmortyapiclient.data.local.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.i12capea.rickandmortyapiclient.data.api.models.Info
import es.i12capea.rickandmortyapiclient.data.api.models.character.RemoteCharacter
import es.i12capea.rickandmortyapiclient.data.api.models.character.RemoteLocationShort
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
    fun fromRemoteLocationShort(locationShort: RemoteLocationShort) : String{
        return "${locationShort.url};${locationShort.name}"
    }

    @TypeConverter
    fun stringToRemoteLocationShort(str: String) : RemoteLocationShort {
        return RemoteLocationShort(
            str.split(";")[0],
            str.split(";")[1]

        )
    }

    @TypeConverter
    fun fromString(value: String): ArrayList<String> {
        val listType: Type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
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