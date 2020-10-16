package es.i12capea.rickandmortyapiclient

import android.app.Application
import android.content.Context
import android.net.*
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*

@HiltAndroidApp
class App : Application(){}