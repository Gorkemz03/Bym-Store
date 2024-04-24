package com.example.bym.ui.navigation_buttons.notifications.Satici.fragments.recyclerUpdate

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bym.databinding.DeleteRowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UpdateAdapter(private val dataList: List<dataUpdate>) : RecyclerView.Adapter<UpdateAdapter.UpdateHolder>() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid
    class UpdateHolder(val deleteRowBinding: DeleteRowBinding) : RecyclerView.ViewHolder(deleteRowBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpdateHolder {
        val binding = DeleteRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UpdateHolder(binding)
    }

    override fun onBindViewHolder(holder: UpdateHolder, position: Int) {
        val currentItem = dataList[position]
        val recyclerRowBinding = holder.deleteRowBinding
        recyclerRowBinding.urunadi.text = currentItem.İsim.toString()

        holder.itemView.setOnClickListener {
            updateData(currentItem.productId)
        }
    }

    private fun updateData(productId: String) {
        // productId ile ilgili belgeyi alın
        userId?.let { uid ->
            val documentReference = firestore.collection("Urunler").document(productId)

            // Belgeyi güncelleyin
            documentReference.update("İsim", "Yeni İsim")
                .addOnSuccessListener {
                    // Güncelleme başarılıysa yapılacak işlemler
                }
                .addOnFailureListener { exception ->
                    // Güncelleme başarısızsa yapılacak işlemler
                }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
