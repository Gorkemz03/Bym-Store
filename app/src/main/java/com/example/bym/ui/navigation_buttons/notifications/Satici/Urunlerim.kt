package com.example.bym.ui.navigation_buttons.notifications.Satici

import UpdateUrun
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.example.bym.R
import com.example.bym.databinding.FragmentUrunlerimBinding
import com.example.bym.ui.navigation_buttons.notifications.Satici.fragments.AddUrun
import com.example.bym.ui.navigation_buttons.notifications.Satici.fragments.DeleteUrun

class Urunlerim : Fragment() {

    // Binding tanımlaması
    private var _binding: FragmentUrunlerimBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment'in layout dosyasını bağla
        _binding = FragmentUrunlerimBinding.inflate(inflater, container, false)

        val view = binding.root

        // Diğer işlemler

        binding.add.setOnClickListener {
            openFragment(AddUrun())
            binding.add.setBackgroundResource(R.color.D3756B)
            binding.delete.setBackgroundResource(android.R.color.white)
            binding.update.setBackgroundResource(android.R.color.white)
        }

        binding.delete.setOnClickListener {
            openFragment(DeleteUrun())
            binding.delete.setBackgroundResource(R.color.D3756B)
            binding.add.setBackgroundResource(android.R.color.white)
            binding.update.setBackgroundResource(android.R.color.white)
        }

        binding.update.setOnClickListener {
            openFragment(UpdateUrun())
            binding.update.setBackgroundResource(R.color.D3756B)
            binding.add.setBackgroundResource(android.R.color.white)
            binding.delete.setBackgroundResource(android.R.color.white)
        }





        return view
    }



    private fun openFragment(fragment: Fragment) {
        val fragmentContainerView = view?.findViewById<FragmentContainerView>(R.id.fragmentContainerView)
        val fragmentManager = childFragmentManager

        // Belirtilen fragment'ı oluştur ve yerine geçir
        fragmentManager.beginTransaction()
            .replace(fragmentContainerView?.id ?: 0, fragment)
            .addToBackStack(null)
            .commit()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
