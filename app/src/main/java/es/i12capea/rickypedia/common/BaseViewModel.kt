package es.i12capea.rickypedia.common

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.i12capea.domain.common.Constants
import es.i12capea.domain.exceptions.PredicateNotSatisfiedException
import es.i12capea.domain.exceptions.RequestException
import es.i12capea.domain.exceptions.ResponseException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext


abstract class BaseViewModel<StateEvent, ViewState : BaseViewState> (
    val dispatcher: CoroutineDispatcher
) : ViewModel(), CoroutineScope
{
    private var _viewState: MutableStateFlow<ViewState>
    val viewState: StateFlow<ViewState>

    init {
        _viewState = MutableStateFlow(this.initNewViewState())
        viewState = _viewState
    }

    private val _networkAvailable : MutableLiveData<Boolean> = MutableLiveData(false)
    val networkAvailable : LiveData<Boolean> = _networkAvailable

    fun getCurrentViewStateOrNew(): ViewState{
        return viewState.value ?: initNewViewState()
    }

    fun setViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun setNetworkAvailable(available: Boolean){
        _networkAvailable.postValue(available)
    }

    fun setLoading(isLoading: Boolean){
        val update = getCurrentViewStateOrNew()
        update.isLoading = isLoading
        setViewState(update)
    }

    open fun setStateEvent(stateEvent: StateEvent){
        viewModelScope.launch {
            getJobNameForEvent(stateEvent).let { jobName ->
                Log.d("JOB", "Jobname -> $jobName")
                getJob(jobName)?.let {
                    Log.d("JOB", "Job $jobName already running")
                } ?: kotlin.run {
                    setLoading(true)
                    Log.d("JOB", "Job $jobName not running, lets start")
                    getJobForEvent(stateEvent).let { job ->
                        addJob(jobName, job)
                        job.invokeOnCompletion {
                            removeJobFromList(jobName)
                        }
                    }
                }
            }
        }
    }

    abstract fun getJobNameForEvent(stateEvent: StateEvent) : String
    abstract fun getJobForEvent(stateEvent: StateEvent) : Job

    abstract fun initNewViewState(): ViewState

    override val coroutineContext: CoroutineContext
        get() = dispatcher

    private val jobs: HashMap<String, Job> = HashMap()

    private fun addJob(methodName: String, job: Job){
        cancelJob(methodName)
        jobs[methodName] = job
    }

    private fun logJobsName(){
        for((methodName, _) in jobs) {
            Log.d("JOB", "Job: $methodName")
        }
    }

    private fun removeJobFromList(methodName: String){
        Log.d("JOB", "Remove from list")
        jobs.remove(methodName)
        if (jobs.isEmpty()){
            setLoading(false)
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

    private fun cancelActiveJobs(){
        for((_, job) in jobs){
            if(job.isActive){
                job.cancel()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }

    private suspend fun handleError(cause: Throwable){
        val update = getCurrentViewStateOrNew()
        when(cause){
            is RequestException -> {
                update.errorRym = Event(ErrorRym(Constants.NO_NETWORK, "No se ha podido realizar la conexión."))
            }
            is ResponseException -> {
                update.errorRym = Event(ErrorRym(Constants.BAD_NETWORK_RESPONSE, "Error en la respuesta de servidor."))
            }
            is PredicateNotSatisfiedException -> {
                update.errorRym = Event(ErrorRym(Constants.PREDICATE_NOT_SATISFIED, "No se ha cumplido los predicados."))
            }
            else -> {
                update.errorRym = Event(ErrorRym(Constants.UNKNOWN_ERROR, "Error desconocido"))
            }
        }
        setViewState(update)
    }

    suspend fun handleThrowable(cause: Throwable?){
        cause?.let {
            handleError(it)
        }
    }
}