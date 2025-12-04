package com.bignerbranch.android.zd2_v3_zykova

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_panel_icon)

        val imgInputData: ImageView = findViewById(R.id.discussionsImage)
        val imgDataList: ImageView = findViewById(R.id.discussionsImage2)
        val imgExit: ImageView = findViewById(R.id.discussionsImage3)

        imgInputData.setOnClickListener {
            val intent = Intent(this, FragmentInputActivity::class.java)
            startActivity(intent)
        }

        imgDataList.setOnClickListener {
            val intent = Intent(this, DataListActivity::class.java)
            startActivity(intent)
        }

        imgExit.setOnClickListener {
            finishAffinity()
        }
    }
}