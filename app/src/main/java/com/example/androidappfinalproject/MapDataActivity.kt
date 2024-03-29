package com.example.androidappfinalproject

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MapDataActivity : AppCompatActivity() {

//--------------------------------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val searchAddress = intent.getStringExtra("storeAddress")
        Toast.makeText(this, "Location: $searchAddress", Toast.LENGTH_SHORT).show()
    //Pushes holder address here and puts it into google maps
        val searchAddress1 = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=$searchAddress"))
        startActivity(searchAddress1)
    }
}
