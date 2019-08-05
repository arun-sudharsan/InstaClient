package io.arunbuilds.instagramclient.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.arunbuilds.instagramclient.Db.enitity.UserData
import io.arunbuilds.instagramclient.R
import io.arunbuilds.instagramclient.utils.Helper
import kotlinx.android.synthetic.main.list_item_user_data.view.*

typealias OnclickListenerCallBack = (UserData) -> Unit

class UserDataListAdapter(private var userlist: List<UserData>, private val listener: OnclickListenerCallBack) :
    RecyclerView.Adapter<UserDataListAdapter.UserDataViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        UserDataViewHolder(parent.inflate(R.layout.list_item_user_data))

    override fun getItemCount() = userlist.size

    override fun onBindViewHolder(holder: UserDataViewHolder, position: Int) =
        holder.bind(getItem(position), listener)


    private fun getItem(position: Int): UserData = userlist[position]

    inner class UserDataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(userData: UserData, listener: OnclickListenerCallBack) {
            with(itemView) {
                tvBio.text = userData.userbio
                tvDate.text = Helper.getTimeAgofromNow(userData.date.time)


                ivDp.load(userData.userprofilepic)

                itemView.setOnClickListener {
                    listener(userData)
                }
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

fun ImageView.load(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.ic_loading_foreground)
        .error(R.drawable.ic_error_foreground)
        .into(this)
}