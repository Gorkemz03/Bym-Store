package com.example.bym.ui.navigation_buttons.dashboard.MyProfil

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bym.R
import com.example.bym.databinding.FragmentHesapAyrlariBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class HesapAyrlari : Fragment() {

    private var _binding: FragmentHesapAyrlariBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHesapAyrlariBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.Password.setOnClickListener {
            sifreDegis()
        }
    }

    private fun sifreDegis() {
        val oldPassword = binding.oldPassword.text.toString()
        val newPassword = binding.newPassword.text.toString()

        if (oldPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(context, "Lütfen mevcut ve yeni şifrenizi giriniz.", Toast.LENGTH_SHORT).show()
            return
        }

        val user = FirebaseAuth.getInstance().currentUser

        // Mevcut şifreyi doğrula
        val credential = EmailAuthProvider.getCredential(user?.email!!, oldPassword)
        user?.reauthenticate(credential)
            ?.addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    // Mevcut şifre doğrulandı, yeni şifreyi değiştir
                    if (oldPassword == newPassword) {
                        // Mevcut şifre ile yeni şifre aynı ise şifre değiştirme
                        Toast.makeText(context, "Mevcut şifre ile yeni şifre aynı olamaz.", Toast.LENGTH_SHORT).show()
                    } else {
                        user?.updatePassword(newPassword)
                            ?.addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    // Şifre değiştirme başarılı
                                    Toast.makeText(context, "Şifre başarıyla değiştirildi.", Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.navigation_dashboard)

                                } else {
                                    // Şifre değiştirme başarısız
                                    Toast.makeText(context, "Şifre değiştirme başarısız: ${updateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    // Mevcut şifre doğrulama başarısız
                    Toast.makeText(context, "Mevcut şifre doğrulama başarısız: ${reauthTask.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
