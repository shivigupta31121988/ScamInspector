package com.example.scaminspector.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scaminspector.MainActivity
import com.example.scaminspector.Models.ClassMessages
import com.example.scaminspector.R


class MessagesAdapter(var context: Context, val mList: ArrayList<ClassMessages>) :
    RecyclerView.Adapter<MessagesAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_card, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.messageTextView.text = ItemsViewModel.MessageText

        // sets the text to the textview from our itemHolder class
        holder.messageNumberView.text = ItemsViewModel.Number

        holder.phishingButtonView.setOnClickListener {
            (context as MainActivity).sendEmail(
                "shivithegame@gmail.com",
                holder.messageNumberView.text.toString(),
                holder.messageTextView.text.toString()
            )
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.tvMessage)
        val messageNumberView: TextView = itemView.findViewById(R.id.tvNumber)
        val phishingButtonView: Button = itemView.findViewById(R.id.btnPhishing)
    }
}