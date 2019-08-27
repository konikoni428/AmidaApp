package com.masahiro.amidaapp

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.masahiro.amidaapp.ui.main.PageViewModel
import com.masahiro.amidaapp.ui.main.SectionsPagerAdapter
import java.util.*
import kotlin.random.Random

class ResultActivity : AppCompatActivity() {
    companion object{
        const val START_DATASET = "com.masahiro.amidaapp.start_dataset"
        const val FINISH_DATASET = "com.masahiro.amidaapp.finish_dataset"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startDataset = intent.getStringArrayExtra(START_DATASET) ?: arrayOf()
        val finishDataset = intent.getStringArrayExtra(FINISH_DATASET) ?: arrayOf()

        val viewModel = ViewModelProviders.of(this).get(PageViewModel::class.java)

        viewModel.startDataset = startDataset
        viewModel.finishDataset = finishDataset

        viewModel.amidaHorizontalSize = startDataset.size * 3
        viewModel.amidaVerticalSize = startDataset.size

//        val amidaView = AmidaView(this)
//        setContentView(amidaView)

        setContentView(R.layout.activity_result)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, startDataset)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)



//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }
}