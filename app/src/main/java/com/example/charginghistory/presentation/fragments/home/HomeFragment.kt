package com.example.charginghistory.presentation.fragments.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.charginghistory.R
import com.example.charginghistory.core.openFragment
import com.example.charginghistory.data.service.MyForegroundService
import com.example.charginghistory.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPreferences: SharedPreferences

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, proceed with enabling the service
            startService()
        } else {
            // Permission denied, show an explanation or handle appropriately
            Toast.makeText(
                requireContext(),
                "Notification permission is required to use this feature",
                Toast.LENGTH_SHORT
            ).show()
            binding.serviceSwitch.isChecked = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleSwitch()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.button.setOnClickListener {
            activity?.openFragment(R.id.historyFragment, true)
        }
    }

    private fun checkNotificationPermissionAndStartService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permission is granted, start the service
                    startService()
                }

                shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Show an explanation to the user
                    showPermissionExplanationDialog()
                }

                else -> {
                    // Directly ask for the permission
                    requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        } else {
            // For Android versions below 13, directly start the service
            startService()
        }
    }

    private fun handleSwitch() {
        sharedPreferences =
            requireActivity().getSharedPreferences("ServicePrefs", Context.MODE_PRIVATE)

        // Restore switch state
        val isServiceRunning = sharedPreferences.getBoolean("serviceRunning", false)
        binding.serviceSwitch.isChecked = isServiceRunning

        // Set up the switch to start/stop the service
        binding.serviceSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkNotificationPermissionAndStartService()
            } else {
                stopService()
            }
            // Save switch state
            sharedPreferences.edit().putBoolean("serviceRunning", isChecked).apply()
        }
    }

    private fun startService() {
        // Start the foreground service
        val serviceIntent = Intent(requireContext(), MyForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= 26) requireActivity().startForegroundService(serviceIntent)
        else requireActivity().startService(serviceIntent)
    }

    private fun stopService() {
        // Stop the foreground service
        val serviceIntent = Intent(requireContext(), MyForegroundService::class.java)
        requireActivity().stopService(serviceIntent)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Notification Permission Needed")
            .setMessage("This app requires notification permission to notify you when the service is running.")
            .setPositiveButton("OK") { _, _ ->
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}