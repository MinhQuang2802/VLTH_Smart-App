package com.example.kotlin_firebase.menu

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
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
import kotlinx.android.synthetic.main.activity_latest_messages_.*
import kotlinx.android.synthetic.main.fragment_food.*
import kotlinx.android.synthetic.main.fragment_food.view.*


class foodFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fetchMenu()
        val view = inflater.inflate(R.layout.fragment_food, container, false)
         view.search_button_food.setOnClickListener{
            var foodName = view.editext_search_food.text.toString()
            searchMenu(foodName)
            Log.d("search", foodName)
        }
        return view
    }
    companion object {

        val TYPE_FOOD = "Food"
    }

    private fun searchMenu(foodName: String){

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/Food/$uid/Food")
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
                recyclerview_food.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
    private fun fetchMenu() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/Food/$uid/Food")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    val food = it.getValue(Food::class.java)

                    if (food != null) {
                        adapter.add(FoodItem(food!!, it.key.toString(), TYPE_FOOD))
                    }
                    adapter.notifyDataSetChanged()
                }
                recyclerview_food.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

}