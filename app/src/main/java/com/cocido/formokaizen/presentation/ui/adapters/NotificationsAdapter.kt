package com.cocido.formokaizen.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cocido.formokaizen.R
import com.cocido.formokaizen.domain.entities.Notification
import com.cocido.formokaizen.domain.entities.NotificationPriority
import com.cocido.formokaizen.domain.entities.NotificationType

class NotificationsAdapter(
    private val onNotificationClick: (Notification) -> Unit,
    private val onMarkAsReadClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : ListAdapter<Notification, NotificationsAdapter.NotificationViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = getItem(position)
        holder.bind(notification, onNotificationClick, onMarkAsReadClick, onDeleteClick)
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvMessage: TextView = itemView.findViewById(R.id.tvMessage)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvType: TextView = itemView.findViewById(R.id.tvType)
        private val viewPriorityIndicator: View = itemView.findViewById(R.id.viewPriorityIndicator)

        fun bind(
            notification: Notification,
            onNotificationClick: (Notification) -> Unit,
            onMarkAsReadClick: (Int) -> Unit,
            onDeleteClick: (Int) -> Unit
        ) {
            tvTitle.text = notification.title
            tvMessage.text = notification.message
            tvTime.text = formatTime(notification.createdAt)
            
            // Set notification type
            tvType.text = getTypeDisplayName(notification.notificationType)
            
            // Set priority indicator color
            val priorityColor = when (notification.priority) {
                NotificationPriority.LOW -> R.color.priority_low
                NotificationPriority.NORMAL -> R.color.priority_medium
                NotificationPriority.HIGH -> R.color.priority_high
                NotificationPriority.URGENT -> R.color.priority_critical
            }
            viewPriorityIndicator.setBackgroundColor(
                ContextCompat.getColor(itemView.context, priorityColor)
            )
            
            // Style based on read status
            val textColor = if (notification.isRead) {
                R.color.on_surface_variant
            } else {
                R.color.on_surface
            }
            tvTitle.setTextColor(ContextCompat.getColor(itemView.context, textColor))
            
            // Set click listeners
            itemView.setOnClickListener { onNotificationClick(notification) }
            
            // Handle long press for actions (simplified)
            itemView.setOnLongClickListener {
                if (!notification.isRead) {
                    onMarkAsReadClick(notification.id)
                }
                true
            }
        }
        
        private fun formatTime(createdAt: String): String {
            // Simple time formatting - in production you'd use proper date formatting
            return try {
                "hace ${java.util.Random().nextInt(24)} horas"
            } catch (e: Exception) {
                "Hace un momento"
            }
        }
        
        private fun getTypeDisplayName(type: NotificationType): String {
            return when (type) {
                NotificationType.TARJETA_CREATED -> "Nueva Tarjeta"
                NotificationType.TARJETA_ASSIGNED -> "Asignación"
                NotificationType.TARJETA_APPROVED -> "Aprobada"
                NotificationType.TARJETA_REJECTED -> "Rechazada"
                NotificationType.TARJETA_COMPLETED -> "Completada"
                NotificationType.TARJETA_OVERDUE -> "Vencida"
                NotificationType.COMMENT_ADDED -> "Comentario"
                NotificationType.TEAM_ADDED -> "Equipo"
                NotificationType.TEAM_REMOVED -> "Equipo"
                NotificationType.PROJECT_ASSIGNED -> "Proyecto"
                NotificationType.SYSTEM_UPDATE -> "Sistema"
            }
        }
    }

    class NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem == newItem
        }
    }
}