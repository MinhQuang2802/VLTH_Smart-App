package com.example.kotlin_firebase.menu

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.kotlin_firebase.R
import com.example.kotlin_firebase.messages.ChatlogActivity
import com.example.kotlin_firebase.messages.LatestMessagesActivity
import com.example.kotlin_firebase.messages.NewMessageActivity
import com.example.kotlin_firebase.models.Food
import com.example.kotlin_firebase.models.User
import com.example.kotlin_firebase.view.CustomDialogFragment
import com.example.kotlin_firebase.view.FoodItem
import com.example.kotlin_firebase.view.LatestMessageRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_bill.*
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.fragment_custom_dialog.*
import kotlinx.android.synthetic.main.row_bill.view.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class BillActivity : AppCompatActivity() {
    var totalBill = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)
        fetchBill()
        payment_button_bill.setOnClickListener{

            if( totalBill != 0) {
                removeData()
                Toast.makeText(this, "Success payment", Toast.LENGTH_SHORT).show()
                var dialog = CustomDialogFragment()
                dialog.show(supportFragmentManager, "customDialog")

            } else {
                Toast.makeText(this, "Your basket is empty, please buy something", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


        }
    }

    companion object {

    }
    private fun removeData() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/Food/$uid")
        ref.removeValue()
    }
    private fun getBill(){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/Food/$uid/Bill")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {

                p0.children.forEach{
                    val food = it.getValue(Food::class.java)
                    Log.d("bill","bill la ${food?.foodName}, ${food?.amount},${food?.price}" )
                    if (food != null) {
                        Log.d("bill","food la ${food?.foodName}, ${food?.amount},${food?.price}" )
                        totalBill = food.amount * food.price.toInt() + totalBill

                    }

                }
                price_bill_text.text = totalBill.toString() + ".000VND"
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })

    }
    fun fetchBill() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/Food/$uid/Bill")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    val food = it.getValue(Food::class.java)
                    Log.d("bill","bill la ${food?.foodName}")
                    if (food != null) {
                        adapter.add(BillItem(food!!))
                    }
                    adapter.notifyDataSetChanged()
                }
                recyclerview_bill.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
        getBill()
    }
}
class BillItem(val food: Food): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.food_name_bill.text = food.foodName
        viewHolder.itemView.amount_bill.text = food.amount.toString()
        viewHolder.itemView.price_bill.text = food.price + ".000VND"

        Picasso.get().load(food.foodImageUrl).into(viewHolder.itemView.imageview_food_bill)
    }

    override fun getLayout(): Int {
        return R.layout.row_bill
    }
}