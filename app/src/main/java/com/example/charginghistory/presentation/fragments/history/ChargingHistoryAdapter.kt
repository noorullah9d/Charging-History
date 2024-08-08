package com.example.charginghistory.presentation.fragments.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.charginghistory.R
import com.example.charginghistory.core.formatDate
import com.example.charginghistory.core.formatDuration
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

                chargingHistory.chargingDuration?.let {
                    tvTitle.text = root.context.getString(R.string.charged_for, it.formatDuration())
                }
                chargingHistory.batteryIncrement?.let {
                    tvBatteryIncrement.text = root.context.getString(R.string.battery_increment, it)
                }

                chargingHistory.overChargeDuration?.let { duration ->
                    tvOverChargeWarning.visibility = View.VISIBLE
                    tvOverChargeWarning.text =
                        root.context.getString(R.string.overcharged_for, duration.formatDuration())
                } ?: run {
                    tvOverChargeWarning.visibility = View.GONE
                }

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
