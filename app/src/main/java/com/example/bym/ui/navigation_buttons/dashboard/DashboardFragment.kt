package com.example.bym.ui.navigation_buttons.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bym.R
import com.example.bym.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var usernameTextView: TextView

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        // TextView'ı bağlamak için
        usernameTextView = binding.usernameTextView

        // Firebase authentication'ı başlatın
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        // Kullanıcının oturum açtığı kontrol ediliyor
        if (currentUser != null) {
            // Kullanıcı oturum açtıysa, e-postayı alın
            val userEmail = currentUser.email

            usernameTextView.text = "Hoşgeldiniz $userEmail"
        }

        binding.KullanCAyar.setOnClickListener {
            // Yeni bir fragment'a geçiş yap
            findNavController().navigate(R.id.hesapAyrlari)
        }

        binding.mail.setOnClickListener {
            //findNavController().navigate(R.id.gecmisSipraisler)
            sendEmail()
        }

        binding.GecmisSiparis.setOnClickListener {
            findNavController().navigate(R.id.gecmisSipraisler)

        }
        return binding.root
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun sendEmail() {
        val recipientEmail = "gorkemcetinn55@gmail.com"
        val subject = "HELLLOO PROJEEE"
        val content = "SELAAAMMLAAARRRR"

        // Bu fragment'ın bağlı olduğu activity'nin context'ini kullan
        val context = requireContext()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, content)
        }

        // E-posta göndermek için uygun bir uygulama bulunamazsa
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
            Toast.makeText(context, "Başarılı", Toast.LENGTH_SHORT).show()
        } else {
            // Kullanıcıya e-posta göndermek için bir uygulama seçme seçeneği sun
            Toast.makeText(context, "E-posta göndermek için uygun bir uygulama bulunamadı.", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
