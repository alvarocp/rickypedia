package es.i12capea.rickandmortyapiclient.presentation.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.i12capea.rickandmortyapiclient.common.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


abstract class BaseViewModel<StateEvent, ViewState> : ViewModel(),
        CoroutineScope
{

    val TAG: String = "Pruebas"

    protected val _stateEvent: MutableLiveData<StateEvent> = MutableLiveData()
    protected val _viewState: MutableLiveData<ViewState> = MutableLiveData()

    val viewState: LiveData<ViewState>
        get() = _viewState

    val dataState: MutableLiveData<DataState<ViewState>> = MutableLiveData()

    fun getCurrentViewStateOrNew(): ViewState{
        return viewState.value ?: initNewViewState()
    }

    fun setViewState(viewState: ViewState) {
        _viewState.postValue(viewState)
    }

    abstract fun setStateEvent(stateEvent: StateEvent)

    abstract fun initNewViewState(): ViewState

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO


    private val jobs: HashMap<String, Job> = HashMap()

    fun addJob(methodName: String, job: Job){
        cancelJob(methodName)
        jobs[methodName] = job
    }

    fun cancelJob(methodName: String){
        getJob(methodName)?.cancel()
    }

    fun getJob(methodName: String): Job? {
        if(jobs.containsKey(methodName)){
            jobs[methodName]?.let {
                return it
            }
        }
        return null
    }

    fun cancelActiveJobs(){
        for((methodName, job) in jobs){
            if(job.isActive){
                Log.e(TAG, "cancelling job in method: '$methodName'")
                job.cancel()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "Se limpia")
        cancelActiveJobs()
    }
}