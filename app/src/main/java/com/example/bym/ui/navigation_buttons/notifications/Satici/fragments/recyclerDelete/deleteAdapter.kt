// DeleteAdapter sınıfı
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bym.databinding.DeleteRowBinding
import com.example.bym.ui.navigation_buttons.notifications.Satici.fragments.recyclerDelete.datadelete
import com.google.firebase.firestore.FirebaseFirestore

class DeleteAdapter(private val itemList: MutableList<datadelete>) :
    RecyclerView.Adapter<DeleteAdapter.DeleteViewHolder>() {

    class DeleteViewHolder(val binding: DeleteRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeleteViewHolder {
        val recyclerRowBinding =
            DeleteRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeleteViewHolder(recyclerRowBinding)
    }

    override fun onBindViewHolder(holder: DeleteViewHolder, position: Int) {
        val currentItem = itemList[position]
        val recyclerRowBinding = holder.binding
        recyclerRowBinding.urunadi.text = currentItem.isim.toString()

        holder.itemView.setOnClickListener {
            deleteItem(position, currentItem)
        }
    }

    private fun deleteItem(position: Int, currentItem: datadelete) {
        val productId = currentItem.productId
        if (productId != null) {
            val firestore = FirebaseFirestore.getInstance()

            firestore.collection("Urunler")
                .document(productId)
                .delete()
                .addOnSuccessListener {
                    itemList.removeAt(position)
                    notifyItemRemoved(position)
                    println("Ürün silindi")
                }
                .addOnFailureListener { exception ->
                    println("Ürün silinemedi $exception")

                }

        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}