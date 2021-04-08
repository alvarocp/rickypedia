package es.i12capea.rickypedia.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.i12capea.domain.exceptions.PredicateNotSatisfiedException
import es.i12capea.domain.exceptions.RequestException
import es.i12capea.domain.exceptions.ResponseException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext


abstract class BaseViewModel<StateEvent, ViewState> (
    val dispatcher: CoroutineDispatcher
) : ViewModel(), CoroutineScope
{
    private val _isLoading : MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error: MutableLiveData<Event<ErrorRym>> = MutableLiveData()
    val error : LiveData<Event<ErrorRym>> = _error

    private val successMessage: MutableLiveData<Event<String>> = MutableLiveData()

    private var _viewState: MutableStateFlow<ViewState>
    val viewState: StateFlow<ViewState> get() = _viewState

    init {
        _viewState = MutableStateFlow(this.initNewViewState())
    }


    private val _networkAvailable : MutableLiveData<Boolean> = MutableLiveData(false)
    val networkAvailable : LiveData<Boolean> = _networkAvailable

    val dataState: MutableLiveData<Event<ViewState>> = MutableLiveData()

    fun getCurrentViewStateOrNew(): ViewState{
        return viewState.value ?: initNewViewState()
    }

    suspend fun setViewState(viewState: ViewState) {
        _viewState.emit(viewState)
    }

    fun setNetworkAvailable(available: Boolean){
        _networkAvailable.postValue(available)
    }

    open fun setStateEvent(stateEvent: StateEvent){
        launch {
            getJobNameForEvent(stateEvent)?.let { jobName ->
                Log.d("JOB", "Jobname -> $jobName")
                getJob(jobName)?.let {
                    Log.d("JOB", "Job $jobName already running")
                } ?: kotlin.run {
                    _isLoading.emit(true)
                    Log.d("JOB", "Job $jobName not running, lets start")
                    getJobForEvent(stateEvent)?.let { job ->
                        addJob(jobName, job)
                        job.invokeOnCompletion {
                            removeJobFromList(jobName)
                        }
                    }
                }
            }
        }
    }

    abstract fun getJobNameForEvent(stateEvent: StateEvent) : String?
    abstract fun getJobForEvent(stateEvent: StateEvent) : Job?

    abstract fun initNewViewState(): ViewState

    override val coroutineContext: CoroutineContext
        get() = dispatcher

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
            viewModelScope.launch {
                delay(300)
                _isLoading.emit(false)
            }
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
                job.cancel()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

    fun handleError(cause: Throwable){
        when(cause){
            is RequestException -> {
                _error.postValue(Event(ErrorRym(1, "No se ha podido realizar la conexión.")))
            }
            is ResponseException -> {
                _error.postValue(Event(ErrorRym(2, "Error en la respuesta de servidor.")))
            }
            is PredicateNotSatisfiedException -> {
                _error.postValue(Event(ErrorRym(3, "No se ha cumplido los predicados.")))
            }
            else -> {
                _error.postValue(Event(ErrorRym(9999, "Error desconocido")))
            }
        }
    }

    fun handleThrowable(cause: Throwable?){
        cause?.let {
            handleError(it)
        }
    }
}