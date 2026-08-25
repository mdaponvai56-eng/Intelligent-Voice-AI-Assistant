package com.example.intelligentassistant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.intelligentassistant.R
import com.example.intelligentassistant.data.ChatMessage

class ChatMessageAdapter(context: Context, private val messages: List<ChatMessage>) :
    ArrayAdapter<ChatMessage>(context, 0, messages) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val message = getItem(position)!!
        val view = convertView ?: LayoutInflater.from(context).inflate(
            if (message.isUser) R.layout.item_user_message else R.layout.item_assistant_message,
            parent,
            false
        )

        val messageText: TextView = view.findViewById(R.id.messageText)
        messageText.text = message.text

        return view
    }
}
