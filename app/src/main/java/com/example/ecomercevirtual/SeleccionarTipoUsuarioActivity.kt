package com.example.ecomercevirtual

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ecomercevirtual.Cliente.LoginClienteActivity
import com.example.ecomercevirtual.Vendedor.LoginVendedorActivity
import com.example.ecomercevirtual.databinding.ActivitySeleccionarTipoUsuarioBinding

class SeleccionarTipoUsuarioActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySeleccionarTipoUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeleccionarTipoUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tipoVendedor.setOnClickListener {
            startActivity(Intent(this@SeleccionarTipoUsuarioActivity, LoginVendedorActivity::class.java))
        }

        binding.tipoCliente.setOnClickListener{
            startActivity(Intent(this@SeleccionarTipoUsuarioActivity, LoginClienteActivity::class.java))
        }
    }
}