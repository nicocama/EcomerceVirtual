package com.example.ecomercevirtual.Cliente.Bottom_Nav_Fragments_Cliente

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ecomercevirtual.Adaptadores.AdaptadorCarritoCliente
import com.example.ecomercevirtual.Modelo.ModeloProductoCarrito
import com.example.ecomercevirtual.R
import com.example.ecomercevirtual.databinding.FragmentCarritoClienteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentCarritoCliente : Fragment() {

    private lateinit var binding : FragmentCarritoClienteBinding

    private lateinit var mContext : Context
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var productosArrayList : ArrayList<ModeloProductoCarrito>
    private lateinit var productoAdaptadorCarritoC: AdaptadorCarritoCliente

    override fun onAttach(context: Context) {
        this.mContext = context
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCarritoClienteBinding.inflate(inflater, container , false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        cargarProdCarrito()
        sumaProductos()
    }

    private fun eliminarProductosCarrito(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
            .child(uid!!).child("CarritoCompras")

        ref.removeValue().addOnCompleteListener {
            Toast.makeText(mContext, "Los productos se han eliminado del carrito", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener {e->
                Toast.makeText(mContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun sumaProductos() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var suma = 0.0
                    for (producto in snapshot.children){

                        val precioFinal = producto.child("precioFinal").getValue(String::class.java)

                        if (precioFinal!=null){
                            suma += precioFinal.toDouble()
                        }

                        binding.sumaProductos.setText("Total: ${suma.toString().plus(" COP")}")

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun cargarProdCarrito() {

        productosArrayList = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(firebaseAuth.uid!!).child("CarritoCompras")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    productosArrayList.clear()
                    for (ds in snapshot.children){
                        val modeloProductoCarrito = ds.getValue(ModeloProductoCarrito::class.java)
                        productosArrayList.add(modeloProductoCarrito!!)
                    }

                    productoAdaptadorCarritoC = AdaptadorCarritoCliente(mContext, productosArrayList)
                    binding.carritoRv.adapter = productoAdaptadorCarritoC
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }


}