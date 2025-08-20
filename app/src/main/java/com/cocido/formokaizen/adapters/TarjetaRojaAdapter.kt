package com.cocido.formokaizen.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cocido.formokaizen.databinding.ItemTarjetaRojaBinding
import com.cocido.formokaizen.domain.entities.TarjetaRoja

class TarjetaRojaAdapter(
    private val tarjetas: List<TarjetaRoja>,
    private val onItemClick: ((TarjetaRoja) -> Unit)? = null
) : RecyclerView.Adapter<TarjetaRojaAdapter.TarjetaViewHolder>() {

    inner class TarjetaViewHolder(val binding: ItemTarjetaRojaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarjetaViewHolder {
        val binding = ItemTarjetaRojaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TarjetaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TarjetaViewHolder, position: Int) {
        val tarjeta = tarjetas[position]
        
        with(holder.binding) {
            txtNumero.text = "N° ${tarjeta.numero}"
            txtFecha.text = tarjeta.fecha
            txtSector.text = tarjeta.sector
            txtDescripcion.text = tarjeta.descripcion
            txtQuienLoHizo.text = tarjeta.quienLoHizo
            
            // Show fechaFinal if available
            if (tarjeta.fechaFinal?.isNotEmpty() == true) {
                txtFechaFinal.text = "Final: ${tarjeta.fechaFinal}"
                txtFechaFinal.visibility = View.VISIBLE
            } else {
                txtFechaFinal.visibility = View.GONE
            }

            // Show first image if available
            if (tarjeta.images.isNotEmpty()) {
                val firstImage = tarjeta.images.first()
                // TODO: Load image properly with Glide/Picasso
                // For now just show placeholder
                imgFoto.setImageResource(android.R.drawable.ic_menu_camera)
            } else {
                imgFoto.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            root.setOnClickListener {
                onItemClick?.invoke(tarjeta)
            }
        }
    }

    override fun getItemCount(): Int = tarjetas.size
}
