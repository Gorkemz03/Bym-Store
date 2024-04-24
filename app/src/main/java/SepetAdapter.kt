import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bym.R
/*
class SepetAdapter(private val context: Context, private val itemList: List<SepetItem>) :
    RecyclerView.Adapter<SepetAdapter.SepetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SepetViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.sepet_item, parent, false)
        return SepetViewHolder(view)
    }

    override fun onBindViewHolder(holder: SepetViewHolder, position: Int) {
        val item = itemList[position]

        holder.nameTextView.text = item.name
        holder.fiyatTextView.text = item.fiyat
        // Resimleri yüklemek için ek işlemleri burada yapabilirsiniz.

        holder.imageButton2.setOnClickListener {
            // ImageButton2'ye tıklandığında yapılacak işlemleri buraya ekleyin
        }

        holder.imageButton3.setOnClickListener {
            // ImageButton3'e tıklandığında yapılacak işlemleri buraya ekleyin
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class SepetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val fiyatTextView: TextView = itemView.findViewById(R.id.fiyat)
        val imageButton2: ImageButton = itemView.findViewById(R.id.imageButton2)
        val imageButton3: ImageButton = itemView.findViewById(R.id.imageButton3)
    }
}*/
