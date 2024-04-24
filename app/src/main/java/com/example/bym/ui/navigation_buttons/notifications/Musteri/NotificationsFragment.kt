package com.example.bym.ui.navigation_buttons.notifications.Musteri

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bym.databinding.FragmentNotificationsBinding
import com.example.bym.recycler_Sepet.dataSepet
import com.example.bym.recycler_Sepet.sepetRecyclerview
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sepetAdapter: sepetRecyclerview
    private lateinit var sepetList: ArrayList<dataSepet>
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var userID: String? = null
    private lateinit var newSepetList: ArrayList<dataSepet>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // ActionBar'ın başlığını değiştir
        (activity as AppCompatActivity).supportActionBar?.title = "Sepet"

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        userID = auth.currentUser?.uid

        sepetList = ArrayList()
        newSepetList = ArrayList()

        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        sepetAdapter = sepetRecyclerview(sepetList)
        binding.recyclerView2.adapter = sepetAdapter

        // RecyclerView öğelerine tıklanma olayını dinleyin
        sepetAdapter.setOnItemClickListener { position ->
            sepetDelete(position)
        }

        sepetUpdate()

        binding.imageButton.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Tüm Öğeleri Sil")
            builder.setMessage("Tüm öğeleri silmek istediğinizden emin misiniz?")

            builder.setPositiveButton("Evet") { _, _ ->
                deleteAllItems()
            }

            builder.setNegativeButton("Hayır") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        binding.button.setOnClickListener {
            sepetSatinAl()
        }

        return root
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalPrice() {
        try {
            var totalPrice = 0.0

            for (item in sepetList) {
                totalPrice += item.fiyat.toDouble()
            }

            binding.totalAmount.text = "Toplam Ücret: $totalPrice"
        } catch (e: Exception) {
            // Hata durumunda hatayı logla
            println( "updateTotalPrice error: ${e.message}")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun gecmisSiparisler() {
        // Sepet içindeki ürünleri ve toplam ücreti al
        val siparisUrunler = sepetList.map { it.isim to it.fiyat.toDouble() }.toMap()
        val toplamUcret = sepetList.sumByDouble { it.fiyat.toDouble() }

        // GecmisSiparisler koleksiyonunu referans al
        val gecmisSiparislerRef = db.collection("GecmisSiparisler").document(userID.toString())

        // Yeni bir belge oluştur ve verileri ekle
        val yeniSiparis = hashMapOf(
            "userId" to userID,
            "urunler" to siparisUrunler,
            "toplamUcret" to toplamUcret,
            "tarih" to FieldValue.serverTimestamp()
        )

        // Yeni belgeyi koleksiyona ekle
        gecmisSiparislerRef.collection("Siparisler").add(yeniSiparis)
            .addOnSuccessListener {
                // Ekleme başarılı olduğunda yapılacak işlemler
                println("Sipariş başarıyla eklendi")

                // TODO: Sepeti sıfırla veya başka bir işlem yapabilirsin
                sepetList.clear()
                sepetAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Ekleme başarısız olduğunda yapılacak işlemler
                println("Sipariş eklenirken hata oluştu. Error: $e")
            }
    }

    private fun sepetSatinAl() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Sepet Onay")
        builder.setMessage("Sepeti satın almak istiyor musunuz?")

        builder.setPositiveButton("Evet") { dialog, which ->
            Toast.makeText(requireContext(), "Ürünler satın alındı", Toast.LENGTH_SHORT).show()
            deleteAllItems()
            gecmisSiparisler()
        }

        builder.setNegativeButton("Hayır") { dialog, which ->
            Toast.makeText(requireContext(), "Satın alma işlemi iptal edildi", Toast.LENGTH_SHORT).show()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun sepetUpdate() {
        userID?.let {
            db.collection("Sepet").document(it).collection("Urunler")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        logError("Firebase'den sepet verileri alınırken hata", error)
                        Toast.makeText(requireContext(), "Bir hata oluştu", Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }

                    newSepetList = ArrayList()

                    value?.let { snapshot ->
                        for (document in snapshot.documents) {
                            val isim = document.getString("isim")
                            val fiyat = document.getDouble("fiyat")
                            val documentId = document.id

                            if (isim != null && fiyat != null && documentId != null) {
                                val dataSepet = dataSepet(isim, fiyat.toInt(), documentId)
                                newSepetList.add(dataSepet)
                            }
                        }
                    }
                    sepetAdapter.updateData(newSepetList)
                    updateTotalPrice()
                }
        }
    }

    private fun sepetDelete(position: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Silme Onayı")
        builder.setMessage("Bu öğeyi silmek istediğinizden emin misiniz?")

        builder.setPositiveButton("Evet") { dialog, which ->
            deleteSingleItem(position)
            Toast.makeText(requireContext(), "Öğe silindi", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Hayır") { dialog, which ->
            Toast.makeText(requireContext(), "Silme iptal edildi", Toast.LENGTH_SHORT).show()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteSingleItem(position: Int) {
        try {
            // Güvenlik kontrolü: sepetList içinde belirli bir pozisyon için öğe var mı?
            if (isValidPosition(position)) {
                val db = FirebaseFirestore.getInstance()
                val userID = auth.currentUser?.uid
                val documentId = sepetList[position].documentId

                // Güvenlik kontrolü: documentId null veya boş mu?
                if (!documentId.isNullOrBlank()) {
                    // Listedeki aynı documentId'ye sahip öğeleri say
                    val itemCount = sepetList.count { it.documentId == documentId }

                    if (userID != null && itemCount > 0) {
                        val userCollectionRef = db.collection("Sepet").document(userID).collection("Urunler")

                        // Sadece bir tane varsa silme işlemi yap
                        if (itemCount == 1) {
                            val documentReference = userCollectionRef.document(documentId)

                            documentReference.delete()
                                .addOnSuccessListener {
                                    // Silme işlemi başarılı olduğunda lokal listeden de silme işlemini gerçekleştiriyoruz
                                    deleteFromLocalList(position)
                                    logMessage("Silme işlemi başarılı.")
                                }
                                .addOnFailureListener { e ->
                                    // Hata durumunda kullanıcıya bilgi veriyoruz
                                    logError("Silme işlemi başarısız: $e", e)
                                }
                        } else {
                            // Birden fazla varsa sadece lokal listeden silme yap
                            deleteFromLocalList(position)
                            logMessage("Silme işlemi başarılı.")
                        }
                    }
                } else {
                    logError("Document ID null veya boş.", null)
                }
            } else {
                logError("Geçersiz pozisyon: $position", null)
            }
        } catch (e: Exception) {
            // Hata durumu
            logError("Beklenmeyen bir hata oluştu: $e", e)
        }
    }

    private fun deleteFromLocalList(position: Int) {
        if (isValidPosition(position)) {
            sepetList.removeAt(position)
            sepetAdapter.notifyItemRemoved(position) // RecyclerView'ı güncelle
        } else {
            logError("Geçersiz pozisyon: $position", null)
        }
    }

    private fun isValidPosition(position: Int): Boolean {
        return position in 0 until sepetList.size
    }

    private fun logMessage(message: String) {
        // Loglama işlemleri burada yapılabilir, örneğin Log.d("TAG", message)
        println(message)
    }

    private fun logError(message: String, exception: Exception?) {
        // Hata loglama işlemleri burada yapılabilir, örneğin Log.e("TAG", message, exception)
        println("$message Exception: $exception")
    }

    private fun deleteAllItems() {
        try {
            val db = FirebaseFirestore.getInstance()
            val userID = FirebaseAuth.getInstance().currentUser?.uid

            // Güvenlik kontrolü: userID null veya boş mu?
            if (!userID.isNullOrBlank()) {
                val userCollectionRef = db.collection("Sepet").document(userID).collection("Urunler")

                userCollectionRef.get()
                    .addOnSuccessListener { snapshot ->
                        val batch = db.batch()

                        for (document in snapshot.documents) {
                            batch.delete(userCollectionRef.document(document.id))
                        }

                        batch.commit()
                            .addOnSuccessListener {
                                // Firebase'den silme işlemi başarılı olduğunda lokal listeden de silme işlemini gerçekleştiriyoruz
                                sepetList.clear()
                                sepetAdapter.notifyDataSetChanged()
                                Toast.makeText(context, "Tüm öğeler başarıyla kaldırıldı.", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                // Hata durumunda kullanıcıya bilgi veriyoruz
                                logError("Tüm öğeleri silme işlemi başarısız: $e", e)
                            }
                    }
                    .addOnFailureListener { e ->
                        // Hata durumunda kullanıcıya bilgi veriyoruz
                        logError("Firebase'den tüm öğeleri alma işlemi başarısız: $e", e)
                    }
            } else {
                logError("Kullanıcı kimliği (userID) null veya boş.", null)
            }
        } catch (e: Exception) {
            // Hata durumu
            logError("Beklenmeyen bir hata oluştu: $e", e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
