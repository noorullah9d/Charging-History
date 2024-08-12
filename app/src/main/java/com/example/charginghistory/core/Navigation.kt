package com.example.charginghistory.core

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.charginghistory.R

fun Activity.openFragment(
    destinationId: Int,
    addToBackStack: Boolean,
    bundle: Bundle? = null
) {

    val navController = Navigation
        .findNavController(
            findViewById(R.id.nav_host_fragment)
        )

    if (!addToBackStack) {
        navController.popBackStack()
    }

    val navOptions = NavOptions.Builder()
        .setEnterAnim(R.anim.open_enter)
        .setExitAnim(R.anim.open_exit)
        .setPopEnterAnim(R.anim.close_enter)
        .setPopExitAnim(R.anim.close_exit)
        .build()

    navController.navigate(destinationId, bundle, navOptions)
}

fun Fragment.popBackStack() {

    val navController = Navigation
        .findNavController(
            requireActivity()
                .findViewById(R.id.nav_host_fragment)
        )

    navController.popBackStack()
}

fun Fragment.openFragmentExclusively(currentFragmentId:Int,destinationId: Int, bundle: Bundle? = null) {

    val navController = Navigation
        .findNavController(
            requireActivity()
                .findViewById(R.id.nav_host_fragment)
        )

    val navOptions: NavOptions =
        NavOptions.Builder().setPopUpTo(currentFragmentId, true)
            .setEnterAnim(R.anim.open_enter)
            .setExitAnim(R.anim.open_exit)
            .setPopEnterAnim(R.anim.close_enter)
            .setPopExitAnim(R.anim.close_exit)
            .build()

    navController.navigate(destinationId, bundle, navOptions)
}