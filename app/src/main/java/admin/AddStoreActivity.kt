package admin

import `interface`.StoreState
import actions.Actions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.androidappfinalproject.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_admin_add_store.*
import kotlinx.android.synthetic.main.bottom_nav_bar_admin.*
import models.Store
import models.Stores
import state.AddStore
import state.StoreList
import kotlin.properties.Delegates

class AddStoreActivity : AppCompatActivity() {

    private val predefinedStores: MutableList<Store> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_store)
        buildPredefinedStores()
        showStoreList(emptyList())
        bottomNavViewBarAdmin.onNavigationItemSelectedListener = mOnNavigationItemSelectedListener

    }


    var currentState by Delegates.observable<StoreState>(
        StoreList(
            emptyList()
        ), { _, old, new ->
            renderViewState(new, old)
        })


    private fun buildPredefinedStores() {
        predefinedStores.add(Store("West Haven"))
    }

    private fun renderViewState(newState: StoreState, oldState: StoreState) {
        when (newState) {
            is StoreList -> showStoreList(newState.store)
            is AddStore -> showAddStoreView(predefinedStores)
        }
        when (oldState) {
            is StoreList -> hideStoreList()
            is AddStore -> hideAddStoreView()
        }
    }

    private fun showStoreList(sandwiches: List<Store>) {
        storeList.visibility = View.VISIBLE
        add_store_button.setOnClickListener {
            currentState = currentState.consumeAction(Actions.AddStoreClicked())
        }
    }


    private fun showAddStoreView(predefinedStore: List<Store>) {
        add_store_container.visibility = View.VISIBLE
        predefined_store_listview.adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, predefinedStore)
    }


    private fun hideStoreList() {
        storeList.visibility = View.GONE
        hide_store_button.setOnClickListener {
            val selectedStore = predefinedStores
            currentState =
                currentState.consumeAction(Actions.PredefinedStoreSelected(selectedStore))
        }
    }

    private fun hideAddStoreView() {
        add_store_container.visibility = View.GONE
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.admin_add_beer -> {
                    val intent = Intent(this, AddBeerActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.admin_profile -> {
                    val intent2 = Intent(this, ProfileAdminActivity::class.java)
                    startActivity(intent2)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.admin_store -> {
                    val intent2 = Intent(this, AddStoreActivity::class.java)
                    startActivity(intent2)
                    return@OnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
}