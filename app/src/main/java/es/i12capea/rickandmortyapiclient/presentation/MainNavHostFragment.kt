package es.i12capea.rickandmortyapiclient.presentation

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainNavHostFragment
    : NavHostFragment() {

    @Inject
    lateinit var fragmentsFactory: MainFragmentsFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager.fragmentFactory = fragmentsFactory
    }
}