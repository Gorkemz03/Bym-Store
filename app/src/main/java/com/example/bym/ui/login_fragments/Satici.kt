package com.example.bym.ui.login_fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bym.MainActivity
import com.example.bym.databinding.FragmentKullaniciBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class Satici : Fragment() {
    private var _binding: FragmentKullaniciBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentKullaniciBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        binding.kaydet.setOnClickListener(){
            createUser()
        }
    }

    fun createUser() {
        val email = binding.KullanCAd.text.toString()
        val password = binding.Password.text.toString()
        val resultKullanici = false

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    // Kullanıcı oluşturma başarılı
                    val uid = authResult.user?.uid

                    // Firestore'a kullanıcı bilgilerini ekle
                    if (isAdded) {
                        addUserToFirestore(uid, email, resultKullanici)
                        Toast.makeText(requireContext(), "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        Toast.makeText(requireContext(), "Fragment bağlı değil.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    // Kullanıcı oluşturma başarısız
                    Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Lütfen Mail ve Şifrenizi Giriniz.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun addUserToFirestore(uid: String?, email: String, resultKullanici: Boolean) {
        val db = FirebaseFirestore.getInstance()

        // Firestore kullanıcı koleksiyonu
        val usersCollection = db.collection("Users")

        // Kullanıcı türü alanını belgeye ekle
        val data = hashMapOf(
            "id" to uid!!,
            "email" to email,
            "resultKullanici" to resultKullanici
        )

        usersCollection.document(uid ?: "").set(data)
            .addOnSuccessListener {
                // Firestore'a başarıyla eklendi
                Toast.makeText(requireContext(), "Firestore'a başarıyla eklendi", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Hata
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}