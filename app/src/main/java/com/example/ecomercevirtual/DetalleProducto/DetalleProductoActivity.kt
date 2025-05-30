package com.example.ecomercevirtual.DetalleProducto

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ecomercevirtual.Adaptadores.AdaptadorImgSlider
import com.example.ecomercevirtual.Modelo.ModeloImgSlider
import com.example.ecomercevirtual.Modelo.ModeloProducto
import com.example.ecomercevirtual.R
import com.example.ecomercevirtual.databinding.ActivityDetalleProductoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetalleProductoActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetalleProductoBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var idProducto = ""
    private lateinit var imagenSlider : ArrayList<ModeloImgSlider>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        //Obtenemos el id del producto enviado desde el adaptador

        idProducto = intent.getStringExtra("idProducto").toString()

        cargarImagenesProd()
        cargarInformacionProd()

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun cargarInformacionProd() {
        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val modeloProducto = snapshot.getValue(ModeloProducto::class.java)

                    val nombre = modeloProducto?.nombre
                    val descripcion = modeloProducto?.descripcion
                    val precio = modeloProducto?.precio
                    val precioDesc = modeloProducto?.precioDesc
                    val notasDesc = modeloProducto?.notaDesc

                    binding.nombrePD.text = nombre
                    binding.descripcionPD.text = descripcion
                    binding.precioPD.text = precio.plus(" COP")

                    if (!precioDesc.equals("") && !notasDesc.equals("")){
                        binding.precioDescPD.text = precioDesc.plus(" COP")
                        binding.notaDescuentoPD.text = notasDesc

                        binding.precioPD.paintFlags = binding.precioPD.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    }else{
                        binding.precioDescPD.visibility = View.GONE
                        binding.notaDescuentoPD.visibility = View.GONE
                    }



                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun cargarImagenesProd() {
        imagenSlider = ArrayList()

        val ref = FirebaseDatabase.getInstance().getReference("Productos")
        ref.child(idProducto).child("Imagenes")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    imagenSlider.clear()
                    for (ds in snapshot.children){
                        try {

                            val modeloImgSlider = ds.getValue(ModeloImgSlider::class.java)
                            imagenSlider.add(modeloImgSlider!!)

                        }catch (e:Exception){

                        }
                    }

                    val adaptadorImgSlider = AdaptadorImgSlider(this@DetalleProductoActivity, imagenSlider)
                    binding.imagenVP.adapter = adaptadorImgSlider
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}