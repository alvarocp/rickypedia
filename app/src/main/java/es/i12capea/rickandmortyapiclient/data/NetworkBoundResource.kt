package es.i12capea.rickandmortyapiclient.data

import es.i12capea.rickandmortyapiclient.data.api.call
import es.i12capea.rickandmortyapiclient.domain.exceptions.RequestException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call

abstract class NetworkBoundResource<RemoteType, LocalType, DomainType> {

    protected abstract fun shouldLoadFromCache() : Boolean

    protected abstract fun saveCallResult(item: LocalType)

    protected abstract fun shouldFetch(): Boolean

    protected abstract fun loadFromDb(): LocalType?

    protected abstract fun createCall(): Call<RemoteType>

    protected abstract fun remoteToLocal(item: RemoteType) : LocalType

    protected abstract fun remoteToDomain(item: RemoteType) : DomainType

    protected abstract fun localToDomain(item: LocalType) : DomainType

    fun call() : Flow<DomainType>{
        return flow {
            if (shouldLoadFromCache()){
                localFlow()?.let {
                    emit(it)
                }
            }

            if (shouldFetch()){
                try {
                    val result = createCall().call()
                    saveCallResult(remoteToLocal(result))
                    emit(remoteToDomain(result))
                } catch (t: Throwable){
                    if (t !is RequestException){
                        throw t
                    }
                }
            }
        }
    }

    private fun localFlow() : DomainType?{
        val dbSource = loadFromDb()
        return dbSource?.let {
            localToDomain(it)
        }
    }

    private fun remoteFlow(){

    }
}