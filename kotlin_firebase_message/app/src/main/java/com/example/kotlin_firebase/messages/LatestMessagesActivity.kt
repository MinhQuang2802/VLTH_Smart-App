package com.example.kotlin_firebase.messages

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import com.example.kotlin_firebase.R
import com.example.kotlin_firebase.menu.FoodActivity

import com.example.kotlin_firebase.models.ChatMessage
import com.example.kotlin_firebase.models.Food
import com.example.kotlin_firebase.models.User
import com.example.kotlin_firebase.registerandlogin.RegisterActivity
import com.example.kotlin_firebase.view.LatestMessageRow

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_messages_.*

class LatestMessagesActivity : AppCompatActivity() {
    companion object {
        var currentUser: User? = null
        val TAG = "LatestMessages"
    }

    override fun onStart() {
        super.onStart()
        fetchFood()
        fetchWateryFoods()
        fetchDrinks()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages_)
        recyclerview_latest_messages.adapter = adapter

        // add line giua cac tin nhan
        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
       //set item click listener on your adapter
        adapter.setOnItemClickListener{ item, view ->
            Log.d(TAG,"123")
            val intent = Intent(this, ChatlogActivity::class.java)

            val row = item as LatestMessageRow
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }
        listenForLatestMessage()
        fetchCurrentUser()
        verifyUserIsLoggedIn()

    }
    // create date on firebase
    private fun fetchFood() {
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/menu/DoAnKho")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val  food = it.getValue(Food::class.java)
                    if (food != null) {
                        Log.d("List","ket qua food: ${food.amount}")
                        upLoadMenuFoodsToFireaseDatabase(food, it.key.toString())
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    fun upLoadMenuFoodsToFireaseDatabase(food: Food,nameFood: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/Food/$uid/Food/$nameFood")
        val fFood = Food(food.foodName,food.foodImageUrl,food.price,food.amount)

        ref.setValue(fFood)
            .addOnSuccessListener {
                Log.d("Menu", "Finally we saved the fFood to Firebase Database")
            }
            .addOnFailureListener {
                Log.d("Menu", "Failed to set value to database: ${it.message}")
            }
    }

    // load watery food
    private fun fetchWateryFoods() {
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/menu/DoAnNuoc")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
//                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("WateryFoodActivity", it.toString())
                    val food = it.getValue(Food::class.java)
                    if (food != null) {
//                        adapter.add(FoodItem(food,it.key.toString()))
                        //                   menu.Food_menu.listFood.add(food)

                        upLoadMenuWateryFoodToFireaseDatabase(food, it.key.toString())

                    }
                }

//                recyclerview_food.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    fun upLoadMenuWateryFoodToFireaseDatabase(food: Food,nameFood: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/Food/$uid/WateryFood/$nameFood")

        val fFood = Food(food.foodName,food.foodImageUrl,food.price,food.amount)

        ref.setValue(fFood)
            .addOnSuccessListener {
                Log.d("Menu", "Finally we saved the fFood to Firebase Database")
            }
            .addOnFailureListener {
                Log.d("Menu", "Failed to set value to database: ${it.message}")
            }
    }
    // load Drinks
    private fun fetchDrinks() {
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/menu/Nuoc")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {


                p0.children.forEach {
                    val food = it.getValue(Food::class.java)
                    if (food != null) {
                        upLoadMenuDrinksToFireaseDatabase(food, it.key.toString())
                    }
                }


            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    fun upLoadMenuDrinksToFireaseDatabase(food: Food,nameFood: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/Food/$uid/Drinks/$nameFood")

        val fFood = Food(food.foodName,food.foodImageUrl,food.price,food.amount)

        ref.setValue(fFood)
            .addOnSuccessListener {
                Log.d("Menu", "Finally we saved the fFood to Firebase Database")
            }
            .addOnFailureListener {
                Log.d("Menu", "Failed to set value to database: ${it.message}")
            }
    }

    val latestMessageMap = HashMap<String, ChatMessage>()
    private fun refreshRecyclerviewMessages(){
        adapter.clear()
        latestMessageMap.values.forEach{
            adapter.add(LatestMessageRow(it))
        }
    }
    private fun listenForLatestMessage() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            //khi add se call fun nay`
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
              val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessageMap[p0.key!!] = chatMessage
                refreshRecyclerviewMessages()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessageMap[p0.key!!] = chatMessage
                refreshRecyclerviewMessages()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    val adapter =GroupAdapter<ViewHolder>()
//    private fun setupDummyRows() {
//        val adapter =GroupAdapter<ViewHolder>()
//        adapter.add(LatestMessageRow())
//        adapter.add(LatestMessageRow())
//        adapter.add(LatestMessageRow())
//        recyclerview_latest_messages.adapter = adapter
//    }


    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d("LatestMessages", "Current user ${currentUser?.profileImageUrl}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity :: class.java)
            // xoa cac hoat dong acitivities truoc do trong ngan xep va su dung hoat dong moi
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
//test food
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_shop -> {
                val intent = Intent(this, FoodActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
//                val intent = Intent(this, FoodActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity :: class.java)
                // xoa cac hoat dong acitivities truoc do trong ngan xep va su dung hoat dong moi
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}