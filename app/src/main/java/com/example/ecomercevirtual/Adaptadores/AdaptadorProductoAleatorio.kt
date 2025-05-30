package com.example.ecomercevirtual.Adaptadores

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecomercevirtual.Adaptadores.AdaptadorProductoCliente.HolderProducto
import com.example.ecomercevirtual.DetalleProducto.DetalleProductoActivity
import com.example.ecomercevirtual.Modelo.ModeloProducto
import com.example.ecomercevirtual.R
import com.example.ecomercevirtual.databinding.ItemProductoAleatorioBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdaptadorProductoAleatorio : RecyclerView.Adapter<AdaptadorProductoAleatorio.HolderProductosAleatorios> {

    private lateinit var binding: ItemProductoAleatorioBinding
    private var mContext: Context
    var productosArrayList: List<ModeloProducto>

    constructor(mContext: Context, productosArrayList: List<ModeloProducto>) {
        this.mContext = mContext
        this.productosArrayList = productosArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderProductosAleatorios {
        binding = ItemProductoAleatorioBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return HolderProductosAleatorios(binding.root)
    }

    override fun getItemCount(): Int {
        return productosArrayList.size
    }

    override fun onBindViewHolder(holder: HolderProductosAleatorios, position: Int) {
        val modeloProducto = productosArrayList[position]

        val nombreP = modeloProducto.nombre
        val categoriaP = modeloProducto.categoria

        cargarPrimeraImagen(modeloProducto, holder)
        visualizarDescuento(modeloProducto, holder)

        holder.nombreP.text = "${nombreP}"
        //holder..text = "${categoriaP}"

        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, DetalleProductoActivity::class.java)
            intent.putExtra("idProducto", modeloProducto.id)
            mContext.startActivity(intent)
        }
    }

    private fun visualizarDescuento(modeloProducto: ModeloProducto, holder: AdaptadorProductoAleatorio.HolderProductosAleatorios) {
        if (!modeloProducto.precioDesc.equals("0") && !modeloProducto.notaDesc.equals("")){
            //Habilitamos las vistas
            holder.notaDescP.visibility = View.VISIBLE
            holder.precioDescP.visibility = View.VISIBLE

            //Seteamos la informacion
            holder.notaDescP.text = "${modeloProducto.notaDesc}"
            holder.precioDescP.text = "${modeloProducto.precioDesc}${" COP"}"
            holder.precioP.text = "${modeloProducto.precio}${" COP"}"
            holder.precioP.paintFlags = holder.precioP.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG //Tachamos el precio
        }else{

            //El producto no tiene descuento, por ende ocultamos las vistas
            holder.nombreP.visibility = View.GONE
            holder.precioDescP.visibility = View.GONE

            //Seteamos la informacion
            holder.precioP.text = "${modeloProducto.precio}${" COP"}"
            holder.precioP.paintFlags = holder.precioP.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv() //Quitamos el tachado
        }
    }

    private fun cargarPrimeraImagen(modeloProducto: ModeloProducto, holder: AdaptadorProductoAleatorio.HolderProductosAleatorios) {

        val idProducto = modeloProducto.id

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .limitToFirst(1)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ds in snapshot.children){
                        val imagenUrl = "${ds.child("imagenUrl").value}"

                        try {
                            Glide.with(mContext)
                                .load(imagenUrl)
                                .placeholder(R.drawable.item_img_producto)
                                .into(holder.imagenP)

                        }catch (e:Exception){

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }

    inner class HolderProductosAleatorios(item: View) : RecyclerView.ViewHolder(item) {
        var imagenP = binding.imagenP
        var nombreP = binding.itemNombreP
        var precioP = binding.itemPrecioP
        var precioDescP = binding.itemPrecioPDesc
        var notaDescP = binding.itemNotaP
    }
}