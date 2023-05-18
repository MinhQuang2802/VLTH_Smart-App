package com.example.kotlin_firebase.menu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlin_firebase.R
import com.example.kotlin_firebase.models.Food
import com.example.kotlin_firebase.view.FoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_drinks.*
import kotlinx.android.synthetic.main.fragment_drinks.view.*
import kotlinx.android.synthetic.main.fragment_food.*
import kotlinx.android.synthetic.main.fragment_food.view.*


class DrinksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_drinks, container, false)
        view.search_button_drinks.setOnClickListener{
            var foodName = view.editext_search_drinks.text.toString()
            searchMenu(foodName)
            Log.d("search", foodName)
        }
        fetchMenu()
        return view
    }
    companion object {
        val TYPE_FOOD = "Drinks"
    }
    private fun searchMenu(foodName: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/Food/$uid/Drinks")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    val food = it.getValue(Food::class.java)
                    if(food!!.foodName.contains(foodName,true)) {
                        adapter.add(FoodItem(food!!, it.key.toString(), TYPE_FOOD))
                    }
                    adapter.notifyDataSetChanged()
                }
                recyclerview_drinks.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
    private fun fetchMenu() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/Food/$uid/Drinks")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    Log.d("tag", "danh sach : ${it.toString()} ")
                    val food = it.getValue(Food::class.java)
                    if (food != null) {
                        adapter.add(FoodItem(food!!, it.key.toString(), TYPE_FOOD))
                    }
                    adapter.notifyDataSetChanged()
                }
                recyclerview_drinks.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
}