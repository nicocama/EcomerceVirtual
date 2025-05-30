package com.example.ecomercevirtual.Vendedor.Bottom_Nav_Fragments_Vendedor

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecomercevirtual.Adaptadores.AdaptadorProducto
import com.example.ecomercevirtual.Modelo.ModeloProducto
import com.example.ecomercevirtual.R
import com.example.ecomercevirtual.databinding.FragmentMisProductosVendedorBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentMisProductosVendedor : Fragment() {

    private lateinit var binding: FragmentMisProductosVendedorBinding
    private lateinit var mContext : Context

    private lateinit var productoArrayList: ArrayList<ModeloProducto>
    private lateinit var adaptadorProducto : AdaptadorProducto

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMisProductosVendedorBinding.inflate(LayoutInflater.from(mContext), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listarProductos()
    }

    private fun listarProductos() {
        productoArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productoArrayList.clear()
                for (ds in snapshot.children){
                    val modeloProducto = ds.getValue(ModeloProducto::class.java)
                    productoArrayList.add(modeloProducto!!)
                }

                adaptadorProducto = AdaptadorProducto(mContext, productoArrayList)
                binding.productosRV.adapter = adaptadorProducto

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}