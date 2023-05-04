package ch.hearc.shaketodo.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ch.hearc.shaketodo.R
import ch.hearc.shaketodo.model.ToDo

class CustomAdapter(private val context: Context, private val data: List<ToDo>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val name : TextView
        val notes : TextView
        val imageView : ImageView
        val todo_priority : RatingBar
        val todo_duedate : TextView
        val parent_layout : LinearLayout

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
            name = view.findViewById(R.id.name)
            name.text = data[position].name
            imageView = view.findViewById(R.id.imageView)
            imageView?.setImageURI(Uri.parse(data[position].imagelocation))
            todo_priority = view.findViewById(R.id.todo_priority)
            todo_priority.rating = (data[position].priority ?: 0) / 2F

            todo_duedate = view.findViewById(R.id.todo_duedate)
            todo_duedate.text = data[position].duedate

            // If the task is done
            parent_layout = view.findViewById(R.id.parent_layout)
            if (data[position].completed == true) {
                // Strike the text
                name.paintFlags = name.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
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