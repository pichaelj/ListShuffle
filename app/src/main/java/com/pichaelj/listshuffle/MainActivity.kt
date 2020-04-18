package com.pichaelj.listshuffle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

        // Setup action bar
        //
        NavigationUI.setupActionBarWithNavController(
            this,
            findNavController(R.id.nav_host_fragment))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navCtrlr = findNavController(R.id.nav_host_fragment)
        return navCtrlr.navigateUp()
    }
}
