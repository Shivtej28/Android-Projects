package com.shivtej.androidprojects.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.shivtej.androidprojects.R
import com.shivtej.androidprojects.models.LearnBlog
import com.shivtej.androidprojects.viewModels.SavedPostViewModel

class LearnAdapter(private val onClicked: OnClicked) :
    RecyclerView.Adapter<LearnAdapter.ViewHolder>() {

    //    private var like: Boolean = true
    private var postList = emptyList<LearnBlog>()
    private var roomPostList = emptyList<LearnBlog>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.title)
        val tvContent: TextView = itemView.findViewById(R.id.some_content)
        val bookmark: LottieAnimationView = itemView.findViewById(R.id.bookmark_post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.learn_list_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = postList[position]
        holder.tvTitle.text = currentItem.title
        holder.tvContent.text = currentItem.description


        holder.itemView.setOnClickListener {
            onClicked.onLearnBlogClicked(currentItem)
        }


        if (roomPostList.contains(currentItem)) {
//           // holder.bookmark.playAnimation()
            holder.bookmark.setMinAndMaxProgress(0.5f, 1.0f) // black
        } else {
            holder.bookmark.setMinAndMaxProgress(0.0f, 0.5f) // white
            //holder.bookmark.playAnimation()
        }

        holder.bookmark.setOnClickListener {
            if (roomPostList.contains(currentItem)) {
//                holder.bookmark.setMinAndMaxProgress(0.0f, 0.5f)
//                holder.bookmark.playAnimation()
                onClicked.deletePost(currentItem)
                //currentItem.isSaved = false
            } else {
//                holder.bookmark.setMinAndMaxProgress(0.5f, 1.0f)
//                holder.bookmark.playAnimation()
               onClicked.savePost(currentItem)
                //currentItem.isSaved = true
            }
        }
    }

    override fun getItemCount(): Int = postList.size

    fun setData(list: List<LearnBlog>) {
        this.postList = list
        notifyDataSetChanged()
    }

    fun getRoomPostList(list: List<LearnBlog>) {
        this.roomPostList = list
    }


}

interface OnClicked {
    fun onLearnBlogClicked(currentItem: LearnBlog)

    fun savePost(currentItem: LearnBlog)

    fun deletePost(currentItem: LearnBlog)
}