package com.example.kotlin_firebase.menu

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.internal.BottomNavigationMenu
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.kotlin_firebase.R
import com.example.kotlin_firebase.messages.NewMessageActivity
import com.example.kotlin_firebase.models.Food
import com.example.kotlin_firebase.registerandlogin.RegisterActivity
import com.example.kotlin_firebase.view.FoodItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.fragment_food.*

class FoodActivity : AppCompatActivity() {

    lateinit var navBottom: BottomNavigationView
    lateinit var viewPager: ViewPager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        navBottom  = findViewById(R.id.bottom_nav)
        viewPager = findViewById(R.id.view_pager)
        setUpViewPager()
        navBottom.setOnNavigationItemSelectedListener( object: BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item?.itemId) {
                    R.id.action_food -> {
                    viewPager.setCurrentItem(0)
                        Log.d("nav","food")
                    }
                    R.id.action_wateryfood -> {
                        viewPager.setCurrentItem(1)
                    }
                    R.id.action_drinks -> {
                        viewPager.setCurrentItem(2)
                    }

                }
               return true
            }



        } )

//        payment_button.setOnClickListener{
////            var dialog = DialogFragmentBill()
////            dialog.show(supportFragmentManager,"customDialog")
//
//            val intent = Intent(this, BillActivity::class.java)
//
//            startActivity(intent)
//        }


    }
    fun setUpViewPager(){
      var  viewPagerAdapter= ViewPagerAdapter(getSupportFragmentManager())
        viewPager.adapter = viewPagerAdapter
        viewPager.addOnAdapterChangeListener(object: ViewPager.OnPageChangeListener,
            ViewPager.OnAdapterChangeListener {
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                when (p0) {
                    0 -> {
                        navBottom.menu.findItem(R.id.action_food)
                    }
                    1-> {
                        navBottom.menu.findItem(R.id.action_wateryfood)
                    }
                    2->{
                        navBottom.menu.findItem(R.id.action_drinks)
                    }

                }
            }

            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onAdapterChanged(p0: ViewPager, p1: PagerAdapter?, p2: PagerAdapter?) {

            }

        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_payment -> {
                val intent = Intent(this, BillActivity::class.java)
                startActivity(intent)
            }

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_payment, menu)
        return super.onCreateOptionsMenu(menu)
    }

}




