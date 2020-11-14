<p align="center">
    <img src="https://i.imgur.com/2cxIrxH.png" alt="Rickypedia logo" width="200">
</p>

<h3 align="center">Rickypedia</h3>

<p align="center">
    Rickypedia es un proyecto personal donde recopilo todo lo que he ido aprendiendo en desarrollo android y me gustarÃ­a compartirlo para ayudar a quien estÃ© aprendiendo y para aprender los desarrolladores que quieran aportarme lo que creen que se deberÃ­a corregir en este proyecto, asÃ­ que, sentÃ­os libres de bajar el cÃ³digo y analizarlo.
</p>
<p align="center">
    En este proyecto encontrarÃ¡s
    
    - Clean Architecture
    - MVI , MVVM
    - Mockk
    - Unit Testing
    - AndroitTests (Tests instrumentales)
    - Retrofit
    - Room
    - Hilt (Dagger)
    - Kotlin
    - Coroutines
    - ConstraintLayout
    - MotionLayout
    - Jetpack Navigation
    - SharedElement (Transicion)
</p>

## Arquitectura Clean

La aplicaciÃ³n sigue la arquitectura clean.

<img src="https://res.cloudinary.com/practicaldev/image/fetch/s--20jzrmPl--/c_limit%2Cf_auto%2Cfl_progressive%2Cq_auto%2Cw_880/https://thepracticaldev.s3.amazonaws.com/i/34fsbx5oopko835yl8bj.jpg" alt="Bootstrap logo">

OrganizaciÃ³n de paquetes

```text
rickypedia/
    â”œâ”€â”€ common
    â”œâ”€â”€ data
      â”œâ”€â”€ api
        â””â”€â”€ models
      â”œâ”€â”€ local
        â”œâ”€â”€ converters
        â”œâ”€â”€ dao
        â””â”€â”€ models
      â”œâ”€â”€ mappers
      â””â”€â”€ repository
    â”œâ”€â”€ domain
        â”œâ”€â”€ entities
        â”œâ”€â”€ repositories (Interfaces)
        â””â”€â”€ usecases
    â””â”€â”€ presentation
        â”œâ”€â”€ di (Hilt)
        â”œâ”€â”€ common
        â”œâ”€â”€ entities
          â””â”€â”€ mappers
        â”œâ”€â”€ characters
        â”œâ”€â”€ episodes
        â””â”€â”€ locations
```

## PatrÃ³n MVI

<p>MVI es MVVM con el aÃ±adido de los eventos de usuario y el estado de la vista, mi aplicaciÃ³n sigue este diseÃ±o.</p>

<img src="https://i.imgur.com/aqrFJJA.png">

### Vista
<p>Los fragmentos tendrÃ¡n inyectados mediante Hilt su viewmodel correspondiente.</p>
<p>La vista serÃ¡ responsable de reaccionar a los inputs del usuario, mandar eventos al viewmodel con los datos que mande el usuario y observar los cambios en los livedata del viewmodel.</p>
<p>La variable del viewmodel que representa la vista es viewState y la variable del viewModel que representa un evento que se deberÃ­a consumir una Ãºnica vez es dataState</p>


### ViewModel
<p>Los viewmodel se construyen a partir de la clase abstracta BaseViewModel, que tiene los tipos de datos StateEvent y ViewState</p>
<p>StateEvent son los eventos que puede mandarnos el usuario y ViewState es el liveData que contiene todas las variables necesarias para representar la vista.</p>
<p>El viewmodel tendrÃ¡ inyectado mediante hilt los casos de uso que requiera, y en la funciÃ³n setStateEvent mirarÃ¡ que evento estÃ¡ mandando el usuario y ejecutarÃ¡ el caso de uso correspondiente</p>
<p>El caso de uso nos devolverÃ¡ un flow con datos cacheados y de remoto y conforme nos vaya devolviendo datos, emitiremos un liveData con un viewState que representa la vista, o si es un evento, se mandarÃ¡ a travÃ©s de la variable dataState.</p>

<p>Como todas las acciones del usuario pasan por la funciÃ³n setStateEvent , el BaseViewModel gestiona todo lo relacionado con la corrutina, asÃ­ como la gestiÃ³n de los jobs mediante una lista y el estado de los datos (SUCCESS, LOADING, ERROR)
Si el job que se estÃ¡ intentando cargar estÃ¡ actualmente en la lista de jobs, no se hace nada. </p>
<p>Si no estÃ¡ en la lista de jobs, se postearÃ¡ el valor _isLoading a true y se aÃ±adirÃ¡ el job a la lista hasta que se complete.</p>
<p>Las funciones getJobNameForEvent y getJobForEvent son funciones abstractas que implementarÃ¡n los diferentes viewmodel.</p>
<p>La funciÃ³n getJobForEvent es la que llamarÃ¡ al caso de uso correspondiente en funciÃ³n de la intenciÃ³n del StateEvent que nos mande el usuario y gestionarÃ¡ los flow que nos devuelva.</p>
<p>La funciÃ³n getJobNameForEvent nos servirÃ¡ para gestionar la lista de jobs.</p>

```text
    open fun setStateEvent(stateEvent: StateEvent){
        launch {
            getJobNameForEvent(stateEvent)?.let { jobName ->
                Log.d("JOB", "Jobname -> $jobName")
                getJob(jobName)?.let {
                    Log.d("JOB", "Job $jobName already running")
                } ?: kotlin.run {
                    _isLoading.postValue(true)
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
```

### UseCase
<p>Los casos de uso en esta aplicaciÃ³n no serÃ­an necesarios ya que lo Ãºnico que hacen es obtener datos de repositorio, pero los he incluÃ­do porque el proposito de este proyecto es asemejarloe a una aplicaciÃ³n real.</p>
<p>En una aplicaciÃ³n real, los casos de uso tendrÃ­an diferentes repositorios y consultarÃ­an datos y los manipularÃ­an para devolver el resultado final al viewmodel</p>

<p>En esta aplicaciÃ³n los casos de uso simplemente llaman al repositorio para obtener el flujo de datos y emitirlos al viewmodel</p>


### Repository
<p>Esta capa tiene el propÃ³sito de centralizar el acceso a los datos</p>
<p>Los repositorios se pueden implementar de muchas maneras, en este proyecto he elegido tener una base de datos donde almaceno todos los datos que voy obteniendo de remoto, por lo que el flujo del repositorio serÃ­a : </p>

```text
1. Obtener datos de local y emitirlos
2. Obtener datos de remoto y emitirlos
3. Actualizar la base de datos con los Ãºltimos datos de remoto obtenidos
```
