package com.example.kotlin_firebase.messages

import android.content.Intent
import com.xwray.groupie.ViewHolder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlin_firebase.R
import com.example.kotlin_firebase.messages.ChatlogActivity
import com.example.kotlin_firebase.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select User"
        fetchUsers()
    }
    companion object {
        val USER_KEY = "USER_KEY"
    }
    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance("https://kotlinfirebase-26cea-default-rtdb.firebaseio.com").getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        adapter.add(UserItem(user))
                    }
                }
                adapter.setOnItemClickListener { item, view ->
                    //tao userItem de chuyen`data qua chatlog
                    val userItem = item as UserItem

                    val intent = Intent(view.context, ChatlogActivity::class.java)
                    // save data in intent, de chatlog use
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)

                //ket thuc hoat dong
                    finish()
                }
                recyclerview_newmessage.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

class UserItem(val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_new_message.text = user.username

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_new_message)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}

