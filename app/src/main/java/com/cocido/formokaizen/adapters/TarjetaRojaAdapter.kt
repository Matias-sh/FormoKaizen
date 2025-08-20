package com.cocido.formokaizen.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cocido.formokaizen.databinding.ItemTarjetaRojaBinding
// import com.cocido.formokaizen.models.TarjetaRoja

// Temporary placeholder to avoid compilation errors
data class TarjetaRoja(
    val sector: String = "",
    val descripcion: String = "",
    val motivo: String = "",
    val fotoUri: String = ""
)

class TarjetaRojaAdapter(
    private val tarjetas: List<TarjetaRoja>
) : RecyclerView.Adapter<TarjetaRojaAdapter.TarjetaViewHolder>() {

    inner class TarjetaViewHolder(val binding: ItemTarjetaRojaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarjetaViewHolder {
        val binding = ItemTarjetaRojaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TarjetaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TarjetaViewHolder, position: Int) {
        val tarjeta = tarjetas[position]
        holder.binding.txtSector.text = "Sector: ${tarjeta.sector}"
        holder.binding.txtDescripcion.text = "Descripción: ${tarjeta.descripcion}"
        holder.binding.txtMotivo.text = "Motivo: ${tarjeta.motivo}"

        // Mostrar la foto
        if (tarjeta.fotoUri.isNotEmpty()) {
            val bitmap = BitmapFactory.decodeFile(tarjeta.fotoUri)
            holder.binding.imgFoto.setImageBitmap(bitmap)
        }
    }

    override fun getItemCount(): Int = tarjetas.size
}
