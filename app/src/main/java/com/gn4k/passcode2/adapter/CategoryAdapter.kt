package com.gn4k.passcode2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R
import com.gn4k.passcode2.data.PassData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryAdapter(private val categoryList: List<String>, private val context: Context) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName: TextView = itemView.findViewById(R.id.tvCategory)
        val rv : RecyclerView = itemView.findViewById(R.id.rvList)
        val item: LinearLayout = itemView.findViewById(R.id.item)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_list, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryName.text = category
        fetchPsswordList(category, holder.rv, holder.item)
    }

    fun fetchPsswordList(catogery : String, rv : RecyclerView, item : LinearLayout){

        val list: MutableList<PassData> = mutableListOf()
        val keylist: MutableList<String> = mutableListOf()


        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val reference: DatabaseReference = database.reference
            .child("users")
            .child(loadLocalData())
            .child("password")
            .child(catogery)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                keylist.clear()

                for (itemSnapshot in snapshot.children) {
                    val passData: PassData? = itemSnapshot.getValue(PassData::class.java)
                    if (passData != null) {
                        list.add(passData)
                        itemSnapshot.key?.let { keylist.add(it) }
                    }
                }

//                Toast.makeText(context, ""+keylist, Toast.LENGTH_SHORT).show()

//                Toast.makeText(context, ""+list, Toast.LENGTH_SHORT).show()

                val layoutManager = LinearLayoutManager(context)
                val adapter = PassDataAdapter(catogery, keylist, list, context)
                rv.layoutManager = layoutManager
                rv.adapter = adapter

                if (adapter.itemCount == 0){
                    item.visibility = View.GONE
                    val layoutParams = item.layoutParams

                    layoutParams.height = 0

                    item.layoutParams = layoutParams
                }



                // Now, entertainmentList contains your PassData items
                // You can use this list as needed
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    fun loadLocalData() : String{

        val sharedPreferences = context?.getSharedPreferences("login", Context.MODE_PRIVATE)
        val id = sharedPreferences?.getString("userName", null).toString()
        return id
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}
