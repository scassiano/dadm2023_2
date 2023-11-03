package com.example.onlinetriki

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


//Variable para saber si es singleplayer o multiplayer
var singleUser = false

class MainActivity : AppCompatActivity() {

    //Variables para los botones
    lateinit var singlePlayerBtn : Button
    lateinit var multiPlayerBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Inicializar botones
        singlePlayerBtn = findViewById(R.id.idBtnSinglePlayer)
        multiPlayerBtn = findViewById(R.id.idBtnMultiPlayer)

        //Acciones al clickear, llevarlo a las actividades
        singlePlayerBtn.setOnClickListener{
            singleUser = true
            startActivity(Intent(this, GamePlayActivity::class.java))

        }

        multiPlayerBtn.setOnClickListener {
            singleUser = false
            startActivity(Intent(this, MultiPlayerGameSelectionActivity::class.java))

        }
    }
}