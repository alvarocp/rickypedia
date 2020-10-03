package es.i12capea.rickandmortyapiclient.presentation.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.i12capea.rickandmortyapiclient.common.DataState
import es.i12capea.rickandmortyapiclient.common.ErrorRym
import es.i12capea.rickandmortyapiclient.common.Event
import es.i12capea.rickandmortyapiclient.domain.exceptions.PredicateNotSatisfiedException
import es.i12capea.rickandmortyapiclient.domain.exceptions.RequestException
import es.i12capea.rickandmortyapiclient.domain.exceptions.ResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


abstract class BaseViewModel<StateEvent, ViewState> : ViewModel(),
        CoroutineScope
{

    val TAG: String = "Pruebas"

    private val isLoading : MutableLiveData<Boolean> = MutableLiveData(false)
    fun isLoading() : LiveData<Boolean> = isLoading
    fun setLoadingTrue() = isLoading.postValue(true)

    private val error: MutableLiveData<Event<ErrorRym>> = MutableLiveData()

    private val successMessage: MutableLiveData<Event<String>> = MutableLiveData()

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

    open fun setStateEvent(stateEvent: StateEvent){
        getJobNameForEvent(stateEvent)?.let { jobName ->
            Log.d("JOB", "Jobname -> $jobName")
            getJob(jobName)?.let {
                Log.d("JOB", "Job $jobName already running")
            } ?: kotlin.run {
                isLoading.postValue(true)
                Log.d("JOB", "Job $jobName not running, lets start")
                getJobForEvent(stateEvent)?.let { job ->
                    addJob(jobName,job)
                    job.invokeOnCompletion {
                        removeJobFromList(jobName)
                    }
                }
            }
        }
    }

    abstract fun getJobNameForEvent(stateEvent: StateEvent) : String?
    abstract fun getJobForEvent(stateEvent: StateEvent) : Job?

    abstract fun initNewViewState(): ViewState

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    private val jobs: HashMap<String, Job> = HashMap()

    private fun addJob(methodName: String, job: Job){
        cancelJob(methodName)
        jobs[methodName] = job
    }

    private fun logJobsName(){
        for((methodName, job) in jobs) {
            Log.d("JOB", "Job: $methodName")
        }
    }

    private fun removeJobFromList(methodName: String){
        Log.d("JOB", "Remove from list")
        jobs.remove(methodName)
        if (jobs.isEmpty()){
            isLoading.postValue(false)
        }
    }

    private fun cancelJob(methodName: String){
        getJob(methodName)?.let {
            removeJobFromList(methodName)
            it.cancel()
        }
    }

    private fun clearCompletedJobs(){
        Log.d("JOB", "Clear completed jobs")
        for((methodName, job) in jobs){
            if(job.isCompleted){
                jobs.remove(methodName)
            }
        }
    }

    private fun getJob(methodName: String): Job? {
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
        Log.d(TAG, "Se limpia base viewModel")
        cancelActiveJobs()
    }

    fun handleError(cause: Throwable){
        when(cause){
            is RequestException -> {
                dataState.postValue(
                    DataState.error(1, "No se ha podido realizar la conexión.")
                )
            }
            is ResponseException -> {
                dataState.postValue(
                    DataState.error(2, "Error en la respuesta de servidor.")
                )
            }
            is PredicateNotSatisfiedException -> {
                dataState.postValue(
                    DataState.error(3, "No se ha cumplido los predicados.")
                )
            }
            else -> {
                dataState.postValue(
                    DataState.error(9999, "Error desconocido")
                )
            }
        }
    }

    fun handleCompletion(cause: Throwable?){
        cause?.let {
            handleError(it)
        }
    }
}