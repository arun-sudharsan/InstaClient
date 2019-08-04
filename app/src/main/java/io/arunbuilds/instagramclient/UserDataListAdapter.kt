package io.arunbuilds.instagramclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.arunbuilds.instagramclient.Db.enitity.UserData
import kotlinx.android.synthetic.main.list_item_user_data.view.*

class UserDataListAdapter(private var userlist: List<UserData>) :
    RecyclerView.Adapter<UserDataListAdapter.UserDataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserDataViewHolder(parent.inflate(R.layout.list_item_user_data))

    override fun getItemCount() = userlist.size

    override fun onBindViewHolder(holder: UserDataViewHolder, position: Int) =
        holder.bind(getItem(position))


    private fun getItem(position: Int): UserData = userlist[position]

    inner class UserDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(userData: UserData) {
            with(itemView) {
                tvBio.text = userData.userbio
                tvDate.text = userData.date.toString()
                Glide.with(context)
                    .load(userData.userprofilepic)
                    .placeholder(R.drawable.ic_loading_foreground)
                    .error(R.drawable.ic_error_foreground)
                    .into(ivDp)
            }
        }
    }

    fun updateDataSet(data: List<UserData>) {
        userlist = data
        notifyDataSetChanged()
    }

}

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}