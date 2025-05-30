package com.example.ecomercevirtual.Cliente.Bottom_Nav_Fragments_Cliente

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecomercevirtual.Adaptadores.AdaptadorCategoriaCliente
import com.example.ecomercevirtual.Adaptadores.AdaptadorProductoAleatorio
import com.example.ecomercevirtual.Modelo.ModeloCategoria
import com.example.ecomercevirtual.Modelo.ModeloProducto
import com.example.ecomercevirtual.R
import com.example.ecomercevirtual.databinding.FragmentTiendaClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentTiendaCliente : Fragment() {

    private lateinit var binding : FragmentTiendaClienteBinding
    private lateinit var mContext : Context
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var categoriaArrayList: ArrayList<ModeloCategoria>
    private lateinit var adaptadorCategoria : AdaptadorCategoriaCliente

    private lateinit var productosArrayList: ArrayList<ModeloProducto>
    private lateinit var adaptadorProducto: AdaptadorProductoAleatorio

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTiendaClienteBinding.inflate(LayoutInflater.from(mContext), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        leerInfoCliente()
        listarCategorias()
        obtenerProductosAleatorios()
    }

    private fun leerInfoCliente(){
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("nombres").value}"
                    val direccion = "${snapshot.child("direccion").value}"
                    binding.bienvenidaTXT.setText("Bienvenido(a): ${nombres}")
                    binding.direccionTXT.setText("${direccion}")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun obtenerProductosAleatorios() {
        productosArrayList = ArrayList()

        var ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                productosArrayList.clear()
                for (ds in snapshot.children){
                    val modeloProducto = ds.getValue(ModeloProducto::class.java)
                    productosArrayList.add((modeloProducto!!))
                }

                val listaAleatoria = productosArrayList.shuffled().take(3)

                adaptadorProducto = AdaptadorProductoAleatorio(mContext, listaAleatoria)
                binding.productosAleatRV.adapter = adaptadorProducto
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun listarCategorias() {
        categoriaArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Categorias")
            .orderByChild("categorias")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categoriaArrayList.clear()
                for (ds in snapshot.children){
                    val modeloCat = ds.getValue(ModeloCategoria::class.java)
                    categoriaArrayList.add(modeloCat!!)
                }

                adaptadorCategoria = AdaptadorCategoriaCliente(mContext, categoriaArrayList)
                binding.categoriasRV.adapter = adaptadorCategoria
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}