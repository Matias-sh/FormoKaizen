package com.cocido.formokaizen.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.cocido.formokaizen.R
import com.cocido.formokaizen.databinding.FragmentNotificationsBinding
import com.cocido.formokaizen.domain.entities.Notification
import com.cocido.formokaizen.presentation.ui.adapters.NotificationsAdapter
import com.cocido.formokaizen.presentation.viewmodel.NotificationsViewModel
import com.cocido.formokaizen.utils.Resource
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotificationsFragment : Fragment() {
    
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    
    private val notificationsViewModel: NotificationsViewModel by viewModels()
    private lateinit var notificationsAdapter: NotificationsAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupClickListeners()
        setupFilters()
        observeViewModel()
    }
    
    private fun setupRecyclerView() {
        notificationsAdapter = NotificationsAdapter(
            onNotificationClick = { notification ->
                handleNotificationClick(notification)
            },
            onMarkAsReadClick = { notificationId ->
                notificationsViewModel.markAsRead(notificationId)
            },
            onDeleteClick = { notificationId ->
                notificationsViewModel.deleteNotification(notificationId)
            }
        )
        
        binding.rvNotifications.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = notificationsAdapter
        }
    }
    
    private fun setupClickListeners() {
        binding.btnMarkAllRead.setOnClickListener {
            notificationsViewModel.markAllAsRead()
        }
        
        binding.btnRetry.setOnClickListener {
            notificationsViewModel.loadNotifications()
        }
    }
    
    private fun setupFilters() {
        binding.chipGroupFilters.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val checkedChip = group.findViewById<Chip>(checkedIds[0])
                when (checkedChip.id) {
                    R.id.chipAll -> notificationsViewModel.filterByUnreadOnly(false)
                    R.id.chipUnread -> notificationsViewModel.filterByUnreadOnly(true)
                    R.id.chipTarjetas -> notificationsViewModel.filterByType("TARJETA")
                    R.id.chipComments -> notificationsViewModel.filterByType("COMMENT")
                }
            }
        }
    }
    
    private fun observeViewModel() {
        // Observe notifications
        viewLifecycleOwner.lifecycleScope.launch {
            notificationsViewModel.notifications.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        showLoading(true)
                        showError(false)
                        showEmptyState(false)
                    }
                    is Resource.Success -> {
                        showLoading(false)
                        showError(false)
                        
                        val notifications = resource.data ?: emptyList()
                        if (notifications.isEmpty()) {
                            showEmptyState(true)
                        } else {
                            showEmptyState(false)
                            notificationsAdapter.submitList(notifications)
                        }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        showError(true, resource.message)
                        showEmptyState(false)
                        
                        Toast.makeText(
                            context,
                            resource.message ?: "Error al cargar notificaciones",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Idle -> {
                        showLoading(false)
                        showError(false)
                        showEmptyState(false)
                    }
                }
            }
        }
        
        // Observe mark as read state
        viewLifecycleOwner.lifecycleScope.launch {
            notificationsViewModel.markAsReadState.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Toast.makeText(context, "Notificación marcada como leída", Toast.LENGTH_SHORT).show()
                        notificationsViewModel.resetStates()
                    }
                    is Resource.Error -> {
                        Toast.makeText(
                            context, 
                            resource.message ?: "Error al marcar notificación",
                            Toast.LENGTH_SHORT
                        ).show()
                        notificationsViewModel.resetStates()
                    }
                    else -> {}
                }
            }
        }
        
        // Observe delete state
        viewLifecycleOwner.lifecycleScope.launch {
            notificationsViewModel.deleteState.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Toast.makeText(context, "Notificación eliminada", Toast.LENGTH_SHORT).show()
                        notificationsViewModel.resetStates()
                    }
                    is Resource.Error -> {
                        Toast.makeText(
                            context, 
                            resource.message ?: "Error al eliminar notificación",
                            Toast.LENGTH_SHORT
                        ).show()
                        notificationsViewModel.resetStates()
                    }
                    else -> {}
                }
            }
        }
    }
    
    private fun handleNotificationClick(notification: Notification) {
        // Mark as read if not already read
        if (!notification.isRead) {
            notificationsViewModel.markAsRead(notification.id)
        }
        
        // Navigate to related content if available
        notification.contentObjectData?.let { contentData ->
            when (contentData.type.lowercase()) {
                "tarjeta" -> {
                    // Navigate to tarjeta detail
                    // val action = NotificationsFragmentDirections.actionNotificationsToTarjetaDetail(contentData.id)
                    // findNavController().navigate(action)
                    Toast.makeText(context, "Navegando a tarjeta ${contentData.displayName}", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(context, notification.title, Toast.LENGTH_SHORT).show()
                }
            }
        } ?: run {
            Toast.makeText(context, notification.title, Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun showLoading(show: Boolean) {
        binding.llLoading.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvNotifications.visibility = if (show) View.GONE else View.VISIBLE
    }
    
    private fun showError(show: Boolean, message: String? = null) {
        binding.llErrorState.visibility = if (show) View.VISIBLE else View.GONE
        if (show && message != null) {
            binding.tvErrorMessage.text = message
        }
        binding.rvNotifications.visibility = if (show) View.GONE else View.VISIBLE
    }
    
    private fun showEmptyState(show: Boolean) {
        binding.llEmptyState.visibility = if (show) View.VISIBLE else View.GONE
        binding.rvNotifications.visibility = if (show) View.GONE else View.VISIBLE
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}