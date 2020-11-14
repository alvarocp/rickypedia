<p align="center">
    <img src="https://i.imgur.com/2cxIrxH.png" alt="Rickypedia logo" width="200">
</p>

<h3 align="center">Rickypedia</h3>

<p align="center">
    Rickypedia es un proyecto personal donde recopilo todo lo que he ido aprendiendo en desarrollo android y me gustaría compartirlo para ayudar a quien esté aprendiendo y para aprender los desarrolladores que quieran aportarme lo que creen que se debería corregir en este proyecto, así que, sentíos libres de bajar el código y analizarlo.
</p>
<p align="center">
    En este proyecto encontrarás
    
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

La aplicación sigue la arquitectura clean.

<img src="https://res.cloudinary.com/practicaldev/image/fetch/s--20jzrmPl--/c_limit%2Cf_auto%2Cfl_progressive%2Cq_auto%2Cw_880/https://thepracticaldev.s3.amazonaws.com/i/34fsbx5oopko835yl8bj.jpg" alt="Bootstrap logo">

Organización de paquetes

```text
rickypedia/
    ├── common
    ├── data
      ├── api
        └── models
      ├── local
        ├── converters
        ├── dao
        └── models
      ├── mappers
      └── repository
    ├── domain
        ├── entities
        ├── repositories (Interfaces)
        └── usecases
    └── presentation
        ├── di (Hilt)
        ├── common
        ├── entities
          └── mappers
        ├── characters
        ├── episodes
        └── locations
```

## Patrón MVI

<p>MVI es MVVM con el añadido de los eventos de usuario y el estado de la vista, mi aplicación sigue este diseño.</p>

<img src="https://i.imgur.com/aqrFJJA.png">

### Vista
<p>Los fragmentos tendrán inyectados mediante Hilt su viewmodel correspondiente.</p>
<p>La vista será responsable de reaccionar a los inputs del usuario, mandar eventos al viewmodel con los datos que mande el usuario y observar los cambios en los livedata del viewmodel.</p>
<p>La variable del viewmodel que representa la vista es viewState y la variable del viewModel que representa un evento que se debería consumir una única vez es dataState</p>


### ViewModel
<p>Los viewmodel se construyen a partir de la clase abstracta BaseViewModel, que tiene los tipos de datos StateEvent y ViewState</p>
<p>StateEvent son los eventos que puede mandarnos el usuario y ViewState es el liveData que contiene todas las variables necesarias para representar la vista.</p>
<p>El viewmodel tendrá inyectado mediante hilt los casos de uso que requiera, y en la función setStateEvent mirará que evento está mandando el usuario y ejecutará el caso de uso correspondiente</p>
<p>El caso de uso nos devolverá un flow con datos cacheados y de remoto y conforme nos vaya devolviendo datos, emitiremos un liveData con un viewState que representa la vista, o si es un evento, se mandará a través de la variable dataState.</p>

<p>Como todas las acciones del usuario pasan por la función setStateEvent , el BaseViewModel gestiona todo lo relacionado con la corrutina, así como la gestión de los jobs mediante una lista y el estado de los datos (SUCCESS, LOADING, ERROR)
Si el job que se está intentando cargar está actualmente en la lista de jobs, no se hace nada. </p>
<p>Si no está en la lista de jobs, se posteará el valor _isLoading a true y se añadirá el job a la lista hasta que se complete.</p>
<p>Las funciones getJobNameForEvent y getJobForEvent son funciones abstractas que implementarán los diferentes viewmodel.</p>
<p>La función getJobForEvent es la que llamará al caso de uso correspondiente en función de la intención del StateEvent que nos mande el usuario y gestionará los flow que nos devuelva.</p>
<p>La función getJobNameForEvent nos servirá para gestionar la lista de jobs.</p>

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
<p>Los casos de uso en esta aplicación no serían necesarios ya que lo único que hacen es obtener datos de repositorio, pero los he incluído porque el proposito de este proyecto es asemejarloe a una aplicación real.</p>
<p>En una aplicación real, los casos de uso tendrían diferentes repositorios y consultarían datos y los manipularían para devolver el resultado final al viewmodel</p>

<p>En esta aplicación los casos de uso simplemente llaman al repositorio para obtener el flujo de datos y emitirlos al viewmodel</p>


### Repository
<p>Esta capa tiene el propósito de centralizar el acceso a los datos</p>
<p>Los repositorios se pueden implementar de muchas maneras, en este proyecto he elegido tener una base de datos donde almaceno todos los datos que voy obteniendo de remoto, por lo que el flujo del repositorio sería : </p>

```text
1. Obtener datos de local y emitirlos
2. Obtener datos de remoto y emitirlos
3. Actualizar la base de datos con los últimos datos de remoto obtenidos
```