package com.example.ecomercevirtual.Cliente.Nav_Fragments_Cliente

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecomercevirtual.Cliente.Bottom_Nav_Fragments_Cliente.FragmentCarritoCliente
import com.example.ecomercevirtual.Cliente.Bottom_Nav_Fragments_Cliente.FragmentFavoritosCliente
import com.example.ecomercevirtual.Cliente.Bottom_Nav_Fragments_Cliente.FragmentMisOrdenesCliente
import com.example.ecomercevirtual.Cliente.Bottom_Nav_Fragments_Cliente.FragmentTiendaCliente
import com.example.ecomercevirtual.R
import com.example.ecomercevirtual.databinding.FragmentInicioClienteBinding

class FragmentInicioCliente : Fragment() {

    private lateinit var binding : FragmentInicioClienteBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInicioClienteBinding.inflate(inflater, container, false)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.op_tienda_c ->{
                    replaceFragment(FragmentTiendaCliente())
                }
                R.id.op_mis_favoritos_c ->{
                    replaceFragment(FragmentFavoritosCliente())
                }
                R.id.op_carrito_c ->{
                    replaceFragment(FragmentCarritoCliente())
                }
                R.id.op_mis_ordenes_c ->{
                    replaceFragment(FragmentMisOrdenesCliente())
                }
            }
            true
        }

        replaceFragment(FragmentTiendaCliente())
        binding.bottomNavigation.selectedItemId = R.id.op_tienda_c

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()
    }
}