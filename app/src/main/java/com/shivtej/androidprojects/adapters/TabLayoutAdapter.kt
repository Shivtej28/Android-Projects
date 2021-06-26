package com.shivtej.androidprojects.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shivtej.androidprojects.ui.fragments.MarkAsDoneFragment
import com.shivtej.androidprojects.ui.fragments.TodoDoneFragment
import com.shivtej.androidprojects.ui.fragments.TodoFragment

class TabLayoutAdapter(fragmentManager: FragmentManager?, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager!!, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TodoFragment()
            1 -> MarkAsDoneFragment()
            else -> TodoDoneFragment()
        }
    }
}