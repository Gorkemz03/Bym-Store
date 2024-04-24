package com.example.bym.ui.navigation_buttons.notifications.Satici.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bym.databinding.FragmentAddUrunBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddUrun : Fragment() {

    private var _binding: FragmentAddUrunBinding? = null
    private val binding get() = _binding!!

    // Firebase bağlantıları
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddUrunBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.ekle.setOnClickListener {
            urunekle()
        }

        return view
    }

    private fun urunekle(){
        // Eklenen ürün bilgilerini al
        val urunAdi = binding.isim.text.toString()
        val urunAcıklama = binding.AcKlama.text.toString()
        val imageurl = binding.Resim.text.toString()
        val fiyat1 = binding.Fiyat.text.toString()

        if (urunAdi.isBlank() || urunAcıklama.isBlank() || imageurl.isBlank() || fiyat1.isBlank()) {
            Toast.makeText(context, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val fiyat = fiyat1.toDouble()

            val user = auth.currentUser
            user?.let {
                val userId = it.uid
                val urunMap = hashMapOf(
                    "userId" to userId,
                    "İsim" to urunAdi,
                    "Acıklama" to urunAcıklama,
                    "imageurl" to imageurl,
                    "Fiyat" to fiyat
                )

                db.collection("Urunler")
                    .add(urunMap)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(context, "Ürün Başarıyla Eklendi", Toast.LENGTH_SHORT).show()
                        println("DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Ürün eklenirken hata oluştu", Toast.LENGTH_SHORT).show()
                        println("Error adding document: $e")
                    }
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Geçerli bir fiyat girin", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
