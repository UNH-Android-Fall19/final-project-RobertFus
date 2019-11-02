package models

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidappfinalproject.R
import com.google.firebase.firestore.FirebaseFirestore

class AddSearchRecyclerViewAdapter(private val storeList: MutableList<Stores>,
                                   private val context: Context,
                                   private val db: FirebaseFirestore
) :
    RecyclerView.Adapter<AddSearchRecyclerViewAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val store = storeList[position]
        Log.d("FireBase", "1Information Added to FireStore")

        holder.storeName.text = store.storeName
        holder.storeAddress.text = store.storeAddress

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("FireBase", "2Information Added to FireStore")
        val view = LayoutInflater.from(parent!!.context)
            .inflate(R.layout.item_store, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {
        Log.d("FireBase", "3Information Added to FireStore")
        return storeList.size
    }

    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var storeName: TextView = view.findViewById(R.id.store_name_textview)
        internal var storeAddress: TextView = view.findViewById(R.id.store_address_type_textview)
    }

}