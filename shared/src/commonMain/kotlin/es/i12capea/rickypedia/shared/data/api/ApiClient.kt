package es.i12capea.rickypedia.shared.data.api

import es.i12capea.rickypedia.shared.domain.common.Constants
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

object ApiClient {

    val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json { ignoreUnknownKeys = true })
        }
        /* Ktor specific logging: reenable if needed to debug requests */
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }

    }


    suspend inline fun <reified T:Any> getResponse(endpoint : String, params: List<Pair<String,String>>? = null): T {
        val url = Constants.BASE_URL+endpoint
        try {
            // please notice, Ktor Client is switching to a background thread under the hood
            // so the http call doesn't happen on the main thread, even if the coroutine has been launched on Dispatchers.Main
            //debugLogger.log("$url API SUCCESS")
            return client.get(url){
                params?.let {
                    for (param in it) {
                        parameter(param.first, param.second)
                    }
                }
            }
        } catch (e: Exception) {
            throw e
            //debugLogger.log("$url API FAILED: "+e.message )
        }
    }

}
