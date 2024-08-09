package com.example.charginghistory.presentation.fragments.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.charginghistory.R
import com.example.charginghistory.core.click
import com.example.charginghistory.core.formatDate
import com.example.charginghistory.core.formatDuration
import com.example.charginghistory.core.gone
import com.example.charginghistory.core.visible
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
                tvInTime.text =
                    root.context.getString(R.string.in_time, chargingHistory.inTime.formatDate())
                tvOutTime.text = root.context.getString(
                    R.string.out_time,
                    chargingHistory.outTime?.formatDate() ?: root.context.getString(R.string.na)
                )
                tvBatteryStart.text = root.context.getString(
                    R.string.battery_start,
                    chargingHistory.batteryPercentageStart
                )
                tvBatteryEnd.text = root.context.getString(
                    R.string.battery_end,
                    chargingHistory.batteryPercentageEnd?.toString()
                        ?: root.context.getString(R.string.na)
                )

                chargingHistory.chargingDuration?.let { duration ->
                    tvTitle.text =
                        root.context.getString(R.string.charged_for, duration.formatDuration())
                } ?: run {
                    tvTitle.text = root.context.getString(R.string.charging)
                }

                chargingHistory.batteryIncrement?.let { increment ->
                    tvBatteryIncrement.visible()
                    tvBatteryIncrement.text =
                        root.context.getString(R.string.battery_increment, increment)
                } ?: run {
                    tvBatteryIncrement.gone()
                }

                val startPercentage = chargingHistory.batteryPercentageStart
                val endPercentage = chargingHistory.batteryPercentageEnd ?: startPercentage
                val incrementedPercentage = chargingHistory.batteryIncrement ?: endPercentage

                progressBarBattery.setMax(100f)

                chargingHistory.batteryIncrement?.let {
                    progressBarBattery.setPrimaryProgress(startPercentage.toFloat())
                    progressBarBattery.setSecondaryProgress(incrementedPercentage.toFloat())
                } ?: run {
                    progressBarBattery.setPrimaryProgress(startPercentage.toFloat())
                    progressBarBattery.setSecondaryProgress(0f)
                }

                chargingHistory.overChargeDuration?.let { duration ->
                    tvOverChargeWarning.visible()
                    tvOverChargeWarning.text =
                        root.context.getString(R.string.overcharged_for, duration.formatDuration())
                } ?: run {
                    tvOverChargeWarning.gone()
                }

                icDelete.click {
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
