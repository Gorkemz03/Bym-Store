// GecmisOnClick.kt
package com.example.bym.ui.navigation_buttons.dashboard.MyProfil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bym.databinding.FragmentGecmisOnClickBinding

class GecmisOnClick : Fragment() {

    private var _binding: FragmentGecmisOnClickBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using ViewBinding
        _binding = FragmentGecmisOnClickBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // arguments'ı kontrol et
        val args = arguments
        if (args != null) {
            val fiyat = args.getString("fiyat")
            val date = args.getString("date")

            // TextView'lere argümanları set et
            binding.fiyat.text = fiyat
            binding.date.text = date
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
