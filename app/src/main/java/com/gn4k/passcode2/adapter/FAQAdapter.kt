package com.gn4k.passcode2.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gn4k.passcode2.R

class FAQAdapter(private val faqList: List<Pair<String, String>>) :
    RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.faq_item, parent, false)
        return FAQViewHolder(view)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val currentItem = faqList[position]
        holder.questionTextView.text = currentItem.first
        holder.answerTextView.text = currentItem.second
        holder.btnShowHide.setOnClickListener {
            if (holder.answerTextView.visibility == View.VISIBLE) {
                holder.answerTextView.visibility = View.GONE
                holder.btnShowHide.rotation = 0f
            } else {
                holder.answerTextView.visibility = View.VISIBLE
                holder.btnShowHide.rotation = 180f
            }

        }
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    inner class FAQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val questionTextView: TextView = itemView.findViewById(R.id.tvQuestion)
        val answerTextView: TextView = itemView.findViewById(R.id.tvAns)
        val btnShowHide: ImageView = itemView.findViewById(R.id.btnShowHide)
    }
}
