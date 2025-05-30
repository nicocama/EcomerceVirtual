package com.example.ecomercevirtual.Cliente.Bottom_Nav_Fragments_Cliente

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecomercevirtual.Adaptadores.AdaptadorProductoCliente
import com.example.ecomercevirtual.Modelo.ModeloProducto
import com.example.ecomercevirtual.R
import com.example.ecomercevirtual.databinding.FragmentFavoritosClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentFavoritosCliente : Fragment() {

    private lateinit var binding: FragmentFavoritosClienteBinding
    private lateinit var mContext : Context
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var productosArrayList: ArrayList<ModeloProducto>
    private lateinit var productoAdaptador : AdaptadorProductoCliente

    override fun onAttach(context: Context) {
        this.mContext = context
        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritosClienteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fireBaseAuth = FirebaseAuth.getInstance()

        cargarProductoFav()
    }

    private fun cargarProductoFav() {

        productosArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(fireBaseAuth.uid!!).child("Favoritos")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    productosArrayList.clear()

                    for (ds in snapshot.children){
                        val idProducto = "${ds.child("idProducto").value}"

                        val refProd = FirebaseDatabase.getInstance().getReference("Productos")
                        refProd.child(idProducto)
                            .addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    try {
                                        val modeloProducto = snapshot.getValue(ModeloProducto::class.java)
                                        productosArrayList.add(modeloProducto!!)

                                    }catch (e:Exception){}
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })
                    }

                    Handler().postDelayed({
                        productoAdaptador = AdaptadorProductoCliente(mContext, productosArrayList)
                        binding.favoritosRV.adapter = productoAdaptador
                    }, 500)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


    }
}