package com.doodleblue.contactutils.ui.activity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.doodleblue.contactutils.R
import com.doodleblue.contactutils.db.data.ContactsInfo

class MainAdapter(var contactsInfoList: ArrayList<ContactsInfo>, var context: Context) :
    RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var mOnItemClickListener: OnItemClickListener? =
        null

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val displayName: TextView
        val phonenumber: TextView
        val ivsave: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            displayName = itemView.findViewById(R.id.displayName)
            phonenumber = itemView.findViewById(R.id.phoneNumber)
            ivsave = itemView.findViewById(R.id.iv_save)
        }

    }

    interface OnItemClickListener {
        /**
         * On item click.
         *
         * @param view     the view
         * @param obj      the obj
         * @param position the position
         */
        fun onItemClick(obj: ContactsInfo?, position: Int)
    }

    /**
     * Sets on item click listener.
     *
     * @param mItemClickListener the m item click listener
     */
    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = mItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_layout, parent, false)

        return MainViewHolder(view)

    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        holder.displayName.text = contactsInfoList[position].displayName
        holder.phonenumber.text = contactsInfoList[position].phoneNumber

        holder.ivsave.setOnClickListener {

            mOnItemClickListener?.onItemClick(contactsInfoList[position], position)
        }

    }

    override fun getItemCount(): Int {

        return contactsInfoList.size
    }
}