package com.example.kotlin_firebase.view

import com.example.kotlin_firebase.R
import com.example.kotlin_firebase.models.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.food_row.view.*

class FoodItem(var food: Food, var nameFood: String, var typeFood: String): Item<ViewHolder>() {



    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.food_name.text = food.foodName
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/Food/$uid/$typeFood/$nameFood")
        val billRef = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/Food/$uid/Bill/$nameFood")

        viewHolder.itemView.plus_button.setOnClickListener{
            food.amount = food.amount + 1
            if(food.amount >0) {
                billRef.setValue(food)
            } else {
                food.amount = 0
                billRef.removeValue()
            }
            viewHolder.itemView.count_food.text= food.amount.toString()
            ref.child("amount").setValue(food.amount)
        }

        viewHolder.itemView.minus_button.setOnClickListener {
            food.amount = food.amount - 1
            if(food.amount >0) {
                billRef.setValue(food)
            } else {
                food.amount = 0
                billRef.removeValue()
            }
            ref.child("amount").setValue(food.amount)
            viewHolder.itemView.count_food.text= food.amount.toString()
        }
        viewHolder.itemView.count_food.text= food.amount.toString()
        viewHolder.itemView.price.text = food.price + ".000VND"
        Picasso.get().load(food.foodImageUrl).into(viewHolder.itemView.food_image)

    }



    override fun getLayout(): Int {
        return R.layout.food_row
    }
}