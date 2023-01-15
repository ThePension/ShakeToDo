package ch.hearc.shaketodo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import ch.hearc.shaketodo.R
import ch.hearc.shaketodo.model.ToDo

class CustomAdapter(private val context: Context, private val data: List<ToDo>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val name : TextView
        val notes : TextView
        val imagelocation : TextView

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            name = view.findViewById(R.id.name)
            name.text = data[position].name
            notes = view.findViewById(R.id.notes)
            notes.text = data[position].notes
            imagelocation = view.findViewById(R.id.imagelocation)
            imagelocation.text = data[position].imagelocation
        } else {
            view = convertView
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

}