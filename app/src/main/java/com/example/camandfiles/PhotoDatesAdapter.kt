package com.example.camandfiles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PhotoDatesAdapter(val data: String) : RecyclerView.Adapter<PhotoDatesAdapter.ViewHolder>() {
    val strings = data.dropLast(1).split("\n")

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.list_entry_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = strings.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = strings[position]
    }
}