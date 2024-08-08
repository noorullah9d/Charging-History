package com.example.charginghistory.presentation.fragments.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.charginghistory.core.formatTime
import com.example.charginghistory.data.entity.ChargingHistory
import com.example.charginghistory.databinding.ItemChargingHistoryBinding

class ChargingHistoryAdapter(
    private val onDeleteClick: (ChargingHistory) -> Unit
) : ListAdapter<ChargingHistory, ChargingHistoryAdapter.ChargingHistoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargingHistoryViewHolder {
        val binding =
            ItemChargingHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChargingHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChargingHistoryViewHolder, position: Int) {
        val chargingHistory = getItem(position)
        holder.bind(chargingHistory)
    }

    inner class ChargingHistoryViewHolder(private val binding: ItemChargingHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chargingHistory: ChargingHistory) {
            with(binding) {
                tvInTime.text = "In-Time: ${chargingHistory.inTime.formatTime()}"
                tvOutTime.text = "Out-Time: ${chargingHistory.outTime?.formatTime() ?: "N/A"}"
                tvBatteryStart.text = "Battery Start: ${chargingHistory.batteryPercentageStart}"
                tvBatteryEnd.text = "Battery End: ${chargingHistory.batteryPercentageEnd ?: "N/A"}"

                icDelete.setOnClickListener {
                    onDeleteClick(chargingHistory)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ChargingHistory>() {
        override fun areItemsTheSame(oldItem: ChargingHistory, newItem: ChargingHistory): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ChargingHistory,
            newItem: ChargingHistory
        ): Boolean {
            return oldItem == newItem
        }
    }
}
