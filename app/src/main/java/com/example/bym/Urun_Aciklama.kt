package com.example.bym

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.bym.databinding.ActivityUrunAciklamaBinding
import com.example.bym.recycler_Sepet.dataSepet
import com.example.bym.recycler_Sepet.sepetRecyclerview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class Urun_Aciklama : AppCompatActivity() {
    private lateinit var binding: ActivityUrunAciklamaBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var dataArrayList: ArrayList<Any>
    // Sepet verileri için değişkenler
    private lateinit var sepetList: ArrayList<dataSepet>
    private lateinit var sepetAdapter: sepetRecyclerview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUrunAciklamaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = FirebaseFirestore.getInstance()

        dataArrayList = intent.getSerializableExtra("dataArrayList") as ArrayList<Any>

        binding.baslik.text = dataArrayList[0].toString()
        binding.aciklama.text = dataArrayList[1].toString()
        // Urun_Aciklama içindeki image1 ImageView'ına resmi yükle
        val imageURL = dataArrayList[2] as String // 2. eleman resmin URL'si
        Picasso.get().load(imageURL).into(binding.image1)

        // Sepet için değişkenleri oluştur
        sepetList = ArrayList()
        sepetAdapter = sepetRecyclerview(sepetList)
    }



    @SuppressLint("NotifyDataSetChanged")
    fun satinAl(view: View) {
        // Kullanıcı ID'sini al
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        // Firebase veritabanından fiyatı al
        val fiyat = (dataArrayList[3] as? Int)?.toDouble() ?: 0.0
        println("Fiyat: $fiyat")
        println("dataArrayList: $dataArrayList")

        // Satın alma işlemi gerçekleştirildikten sonra Firestore'a veri ekle
        val sepetData = hashMapOf(
            "isim" to binding.baslik.text.toString(),
            "fiyat" to fiyat
        )

        // Kullanıcı ID'sini kullanarak belirli bir kullanıcının sepetine ekleyin
        db.collection("Sepet").document(userID!!).collection("Urunler").add(sepetData)
            .addOnSuccessListener { documentReference ->
                val newDocumentId = documentReference.id // Eklenen belgenin ID'sini al

                Toast.makeText(this, "Ürün sepete eklendi.", Toast.LENGTH_SHORT).show()

                // Sepet verilerini güncelle
                sepetList.add(dataSepet(binding.baslik.text.toString(), fiyat.toInt(), newDocumentId))
                sepetAdapter.notifyDataSetChanged()

            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }






}

