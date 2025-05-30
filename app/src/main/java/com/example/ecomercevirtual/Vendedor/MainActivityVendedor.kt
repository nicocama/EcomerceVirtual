package com.example.ecomercevirtual.Vendedor

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.ecomercevirtual.R
import com.example.ecomercevirtual.SeleccionarTipoUsuarioActivity
import com.example.ecomercevirtual.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentMisOrdenesVendedor
import com.example.ecomercevirtual.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentMisProductosVendedor
import com.example.ecomercevirtual.Vendedor.Nav_Fragments_Vendedor.FragmentCategoriasVendedor
import com.example.ecomercevirtual.Vendedor.Nav_Fragments_Vendedor.FragmentInicioVendedor
import com.example.ecomercevirtual.Vendedor.Nav_Fragments_Vendedor.FragmentMiTiendaVendedor
import com.example.ecomercevirtual.Vendedor.Nav_Fragments_Vendedor.FragmentReseniaVendedor
import com.example.ecomercevirtual.databinding.ActivityMainVendedorBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivityVendedor : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding : ActivityMainVendedorBinding
    private var firebaseAuth : FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainVendedorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        firebaseAuth = FirebaseAuth.getInstance()

        comprobarSesion()

        binding.navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(FragmentInicioVendedor())
        binding.navigationView.setCheckedItem(R.id.op_inicio_v)
    }

    private fun cerrarSesion(){
        firebaseAuth!!.signOut()
        startActivity(Intent(applicationContext, SeleccionarTipoUsuarioActivity::class.java))
        finish()
        Toast.makeText(applicationContext, "¡Hasta pronto! Has cerrado sesión.", Toast.LENGTH_SHORT).show()
    }

    private fun comprobarSesion() {
        /*Si el usuario no ha iniciado sesión, lo que hará el sistema es redireccionarlo al apartado de registro*/
        if (firebaseAuth!!.currentUser == null){
            startActivity(Intent(applicationContext, SeleccionarTipoUsuarioActivity::class.java))
            Toast.makeText(applicationContext, "Vendedor no registrado o no logueado", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(applicationContext, "Actualmente conectado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.navFragment, fragment)
            .commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.op_inicio_v->{
                replaceFragment(FragmentInicioVendedor())
            }
            R.id.op_mi_tienda_v->{
                replaceFragment(FragmentMiTiendaVendedor())
            }
            R.id.op_categorias_v->{
                replaceFragment(FragmentCategoriasVendedor())
            }
            R.id.op_resenia_v->{
                replaceFragment(FragmentReseniaVendedor())
            }
            R.id.op_cerrar_sesion_v->{
                cerrarSesion()
            }
            R.id.op_mis_productos_v->{
                replaceFragment(FragmentMisProductosVendedor())
            }
            R.id.op_mis_ordenes_v->{
                replaceFragment(FragmentMisOrdenesVendedor())
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}