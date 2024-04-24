package com.example.bym.recycler_Sepet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bym.databinding.SepetRecyclerviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class sepetRecyclerview(private val sepetList: ArrayList<dataSepet>) : RecyclerView.Adapter<sepetRecyclerview.SepetHolder>() {
    class SepetHolder(val binding: SepetRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root)
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var db: FirebaseFirestore = FirebaseFirestore.getInstance() // db özelliğini burada tanımladık
    private var onItemClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SepetHolder {
        val binding = SepetRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SepetHolder(binding)
    }

    override fun getItemCount(): Int {
        return sepetList.size
    }

    override fun onBindViewHolder(holder: SepetHolder, position: Int) {
        val currentData = sepetList[position]
        holder.binding.name.text = currentData.isim

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }

    fun updateData(newList: List<dataSepet>) {
        sepetList.clear()
        sepetList.addAll(newList)
        notifyDataSetChanged()
    }


    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

}
