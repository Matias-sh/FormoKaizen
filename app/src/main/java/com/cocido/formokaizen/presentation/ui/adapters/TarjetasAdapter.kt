package com.cocido.formokaizen.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cocido.formokaizen.R
import com.cocido.formokaizen.databinding.ItemTarjetaBinding
import com.cocido.formokaizen.domain.entities.TarjetaRoja
import com.cocido.formokaizen.domain.entities.TarjetaStatus
import com.cocido.formokaizen.domain.entities.Priority
import java.text.SimpleDateFormat
import java.util.*

class TarjetasAdapter(
    private val onItemClick: (TarjetaRoja) -> Unit
) : ListAdapter<TarjetaRoja, TarjetasAdapter.TarjetaViewHolder>(TarjetaDiffCallback()) {
    
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarjetaViewHolder {
        val binding = ItemTarjetaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TarjetaViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: TarjetaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class TarjetaViewHolder(
        private val binding: ItemTarjetaBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(getItem(position))
                }
            }
        }
        
        fun bind(tarjeta: TarjetaRoja) {
            with(binding) {
                // Basic info based on new model
                tvTitle.text = "N° ${tarjeta.numero}"
                tvDescription.text = tarjeta.descripcion
                tvId.text = tarjeta.sector
                
                // Author and date from new fields
                tvAuthor.text = tarjeta.quienLoHizo
                tvDate.text = tarjeta.fecha
                
                // Priority indicator
                setPriorityIndicator(tarjeta.priority)
                
                // Sector chip (replacing category)
                chipCategory.text = tarjeta.sector
                chipCategory.visibility = View.VISIBLE
                
                // Status chip
                setStatusChip(tarjeta.status)
                
                // Priority badge for critical items
                if (tarjeta.priority == Priority.CRITICAL) {
                    ivPriorityBadge.visibility = View.VISIBLE
                    tvPriorityText.visibility = View.VISIBLE
                } else {
                    ivPriorityBadge.visibility = View.GONE
                    tvPriorityText.visibility = View.GONE
                }
            }
        }
        
        private fun setPriorityIndicator(priority: Priority) {
            val color = when (priority) {
                Priority.LOW -> R.color.priority_low
                Priority.MEDIUM -> R.color.priority_medium
                Priority.HIGH -> R.color.priority_high
                Priority.CRITICAL -> R.color.priority_critical
            }
            binding.vPriorityIndicator.setBackgroundColor(
                binding.root.context.getColor(color)
            )
        }
        
        private fun setStatusChip(status: TarjetaStatus) {
            with(binding.chipStatus) {
                text = getStatusText(status)
                chipBackgroundColor = binding.root.context.getColorStateList(getStatusColor(status))
                setTextColor(binding.root.context.getColor(android.R.color.white))
            }
        }
        
        private fun getStatusText(status: TarjetaStatus): String {
            return when (status) {
                TarjetaStatus.OPEN -> "Abierta"
                TarjetaStatus.PENDING_APPROVAL -> "Pendiente"
                TarjetaStatus.APPROVED -> "Aprobada"
                TarjetaStatus.IN_PROGRESS -> "En Progreso"
                TarjetaStatus.RESOLVED -> "Resuelta"
                TarjetaStatus.CLOSED -> "Cerrada"
                TarjetaStatus.REJECTED -> "Rechazada"
            }
        }
        
        private fun getStatusColor(status: TarjetaStatus): Int {
            return when (status) {
                TarjetaStatus.OPEN -> R.color.status_open
                TarjetaStatus.PENDING_APPROVAL -> R.color.status_pending
                TarjetaStatus.APPROVED -> R.color.status_approved
                TarjetaStatus.IN_PROGRESS -> R.color.status_in_progress
                TarjetaStatus.RESOLVED -> R.color.status_resolved
                TarjetaStatus.CLOSED -> R.color.status_closed
                TarjetaStatus.REJECTED -> R.color.status_rejected
            }
        }
    }
}

class TarjetaDiffCallback : DiffUtil.ItemCallback<TarjetaRoja>() {
    override fun areItemsTheSame(oldItem: TarjetaRoja, newItem: TarjetaRoja): Boolean {
        return oldItem.id == newItem.id
    }
    
    override fun areContentsTheSame(oldItem: TarjetaRoja, newItem: TarjetaRoja): Boolean {
        return oldItem == newItem
    }
}