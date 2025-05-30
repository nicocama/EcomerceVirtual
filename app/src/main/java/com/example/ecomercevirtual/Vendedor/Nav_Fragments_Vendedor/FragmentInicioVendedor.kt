package com.example.ecomercevirtual.Vendedor.Nav_Fragments_Vendedor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ecomercevirtual.R
import com.example.ecomercevirtual.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentMisOrdenesVendedor
import com.example.ecomercevirtual.Vendedor.Bottom_Nav_Fragments_Vendedor.FragmentMisProductosVendedor
import com.example.ecomercevirtual.Vendedor.Productos.AgregarProductoActivity
import com.example.ecomercevirtual.databinding.FragmentInicioVendedorBinding
import com.example.ecomercevirtual.databinding.FragmentMisProductosVendedorBinding

class FragmentInicioVendedor : Fragment() {

    private lateinit var binding : FragmentInicioVendedorBinding
    private lateinit var mContext : Context

    override fun onAttach(context: Context) {
        mContext = context
        super.onAttach(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInicioVendedorBinding.inflate(inflater,container, false)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.op_mis_productos_v -> {
                    replaceFragment(FragmentMisProductosVendedor())
                }

                R.id.op_mis_ordenes_v -> {
                    replaceFragment(FragmentMisOrdenesVendedor())
                }
            }

            true
        }

        replaceFragment(FragmentMisProductosVendedor())
        binding.bottomNavigation.selectedItemId = R.id.op_mis_productos_v

        binding.addFab.setOnClickListener {
            val intent = Intent(mContext, AgregarProductoActivity::class.java)
            intent.putExtra("Edicion", false)
            mContext.startActivity(intent)
        }

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.bottomFragment, fragment)
            .commit()

    }
}