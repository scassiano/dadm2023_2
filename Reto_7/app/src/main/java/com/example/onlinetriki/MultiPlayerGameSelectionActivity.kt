package com.example.onlinetriki

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MultiPlayerGameSelectionActivity : AppCompatActivity() {
    //Variables para los botones
    lateinit var onlineBtn : Button
    lateinit var offlineBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_player_game_selection)
        onlineBtn = findViewById(R.id.idBtnOnline)
        offlineBtn = findViewById(R.id.idBtnOffline)

        //Definimos acciones on click
        onlineBtn.setOnClickListener {
            singleUser = false
            startActivity(Intent(this, OnlineCodeGeneratorActivity::class.java))

        }

        offlineBtn.setOnClickListener {
            singleUser = false
            startActivity(Intent(this, GamePlayActivity::class.java))
        }
    }
}