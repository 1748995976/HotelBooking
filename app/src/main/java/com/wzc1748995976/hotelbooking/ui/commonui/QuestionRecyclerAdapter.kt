package com.wzc1748995976.hotelbooking.ui.commonui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewDelegate
import com.wzc1748995976.hotelbooking.R
import org.w3c.dom.Text

data class QuestionInfo(
    val date: String,//酒店名称
    val price: String,//酒店图片
    val number: String
)

class QuestionInfoDelegate: ItemViewDelegate<QuestionInfo, QuestionInfoDelegate.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val detail: TextView = itemView.findViewById(R.id.detail)
        val number: TextView = itemView.findViewById(R.id.number)
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: QuestionInfo) {
        holder.run {
            date.text = item.date
            detail.text = item.price
            number.text = item.number
        }
    }
}