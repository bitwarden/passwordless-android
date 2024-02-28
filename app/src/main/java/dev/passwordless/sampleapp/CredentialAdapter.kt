package dev.passwordless.sampleapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import dev.passwordless.sampleapp.contracts.CredentialResponse
import java.sql.Date

class CredentialAdapter(private val context: Context, private val data: List<CredentialResponse>) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): CredentialResponse {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // You can also use a unique identifier from your data
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val view: View

        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.credential_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView!!
            viewHolder = view.tag as ViewHolder
        }

        val credential = getItem(position)
        viewHolder.nicknameText.text = credential.nickname
        viewHolder.aaGuidText.text = credential.userId
        viewHolder.createdAtText.text = credential.createdAt

        return view
    }

    // Inner class for view holder
    inner class ViewHolder(view: View) {
        val nicknameText: TextView = view.findViewById(R.id.nickname_text)
        val aaGuidText: TextView = view.findViewById(R.id.aaguid_text)
        val createdAtText: TextView = view.findViewById(R.id.created_at_text)
    }
}
