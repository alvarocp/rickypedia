package es.i12capea.rickandmortyapiclient.presentation.common

import es.i12capea.rickandmortyapiclient.common.DataState

interface DataStateChangeListener{

    fun onDataStateChange(dataState: DataState<*>?)

    fun expandAppBar()

    fun hideSoftKeyboard()

}