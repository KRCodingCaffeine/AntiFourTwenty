package com.arista.antifourtwenty.features.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arista.antifourtwenty.R
import com.arista.antifourtwenty.common.data.local.database.entities.CallLog

class CallLogAdapter(
    private var callLogs: List<CallLog>
) : RecyclerView.Adapter<CallLogAdapter.CallLogViewHolder>() {

    class CallLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mobileNumberTextView: TextView = itemView.findViewById(R.id.tvMobile)
        val dateTextView: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallLogViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_call_log, parent, false)
        return CallLogViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CallLogViewHolder, position: Int) {
        val callLog = callLogs[position]
        holder.mobileNumberTextView.text = callLog.mobileNumber
        holder.dateTextView.text = callLog.date + " " + callLog.time
        //holder.timeTextView.text = callLog.time
    }

    override fun getItemCount() = callLogs.size

    fun updateCallLogs(newCallLogs: List<CallLog>) {
        callLogs = newCallLogs
        notifyDataSetChanged()
    }
}