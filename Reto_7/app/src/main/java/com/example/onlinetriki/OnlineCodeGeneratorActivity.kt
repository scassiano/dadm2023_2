package com.example.onlinetriki

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.postDelayed
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


var isCodeMaker = false;
var code = "null"
var codeFound = false
var checkTemp = true
var keyValue : String = "null"
//CAMBIOS

class OnlineCodeGeneratorActivity : AppCompatActivity() {

    //Variables para componentes de UI
    lateinit var headTV : TextView
    lateinit var codeEdt : EditText
    lateinit var createCodeBtn : Button
    lateinit var joinCodeBtn : Button
    lateinit var loadingPB : ProgressBar
    lateinit var createdGamesHeadTV : TextView
    lateinit var currGamesTV : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_code_generator)
        headTV = findViewById(R.id.idTVHead)
        codeEdt = findViewById(R.id.idEdtCode)
        createCodeBtn = findViewById(R.id.idBtnCreate)
        joinCodeBtn = findViewById(R.id.idBtnJoin)
        loadingPB = findViewById(R.id.idPBLoading)
        createdGamesHeadTV = findViewById(R.id.idTVCGC)
        currGamesTV = findViewById((R.id.idTVListCodes))



        //Funcion que actualiza la lista de codigos disponibles
        FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var textCurrGames = ""
                //Se crean dos listas
                //Una con los codigos ya creados
                //Otra con los juegos creados y ya empezados
                //Al usuario solo se le mostraran juegos ya creados y no empezados
                var juegosCreados = mutableListOf<String>()

                //Codigos ya creados en la base de datos
                snapshot.children.forEach {
                    juegosCreados.add(it.value.toString())
                }

                FirebaseDatabase.getInstance().reference.child("data").addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach{
                            juegosCreados.remove(it.key.toString())
                        }
                        juegosCreados.forEach{
                            textCurrGames = textCurrGames + it.toString() + "\n\n"
                        }
                        if(textCurrGames.isNotEmpty()){
                            currGamesTV.text = textCurrGames
                        }else{
                            currGamesTV.text = "There are no games currently created. Create a new one!"
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        createCodeBtn.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = codeEdt.text.toString()
            createCodeBtn.visibility = View.GONE
            joinCodeBtn.visibility = View.GONE
            headTV.visibility = View.GONE
            codeEdt.visibility = View.GONE
            createdGamesHeadTV.visibility = View.GONE
            currGamesTV.visibility = View.GONE
            loadingPB.visibility = View.VISIBLE

            if(code!="null" && code!=""){
                isCodeMaker = true
                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot){
                        //Si el check es true entonces ese valor ya existe
                        var check = isValueAvaliable(snapshot, code)
                        Handler().postDelayed({
                            if (check == true) {
                                createCodeBtn.visibility = View.VISIBLE
                                joinCodeBtn.visibility = View.VISIBLE
                                codeEdt.visibility = View.VISIBLE
                                headTV.visibility = View.VISIBLE
                                createdGamesHeadTV.visibility = View.VISIBLE
                                currGamesTV.visibility = View.VISIBLE
                                loadingPB.visibility = View.GONE
                            } else {
                                FirebaseDatabase.getInstance().reference.child("codes").push()
                                    .setValue(code)
                                isValueAvaliable(snapshot, code)
                                checkTemp = false
                                Handler().postDelayed({
                                    accepted()
                                    Toast.makeText(
                                        this@OnlineCodeGeneratorActivity,
                                        "Please don't go back",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }, 300)
                            }
                        }, 2000)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }else{
                createCodeBtn.visibility = View.VISIBLE
                joinCodeBtn.visibility = View.VISIBLE
                headTV.visibility = View.VISIBLE
                codeEdt.visibility = View.VISIBLE
                createdGamesHeadTV.visibility = View.VISIBLE
                currGamesTV.visibility = View.VISIBLE
                loadingPB.visibility = View.GONE

                Toast.makeText(this,"Please enter a valid code", Toast.LENGTH_SHORT).show()
            }
        }

        joinCodeBtn.setOnClickListener {
            code = "null"
            codeFound = false
            checkTemp = true
            keyValue = "null"
            code = codeEdt.text.toString()
            if(code != "null" && code!=""){
                createCodeBtn.visibility = View.GONE
                joinCodeBtn.visibility = View.GONE
                codeEdt.visibility = View.GONE
                headTV.visibility = View.GONE
                createdGamesHeadTV.visibility = View.GONE
                currGamesTV.visibility = View.GONE
                loadingPB.visibility = View.VISIBLE

                FirebaseDatabase.getInstance().reference.child("codes").addValueEventListener(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var data : Boolean = isValueAvaliable(snapshot, code)
                        Handler().postDelayed({
                            if(data==true){
                                codeFound=true
                                accepted()
                                createCodeBtn.visibility = View.VISIBLE
                                joinCodeBtn.visibility = View.VISIBLE
                                headTV.visibility = View.VISIBLE
                                codeEdt.visibility = View.VISIBLE
                                createdGamesHeadTV.visibility = View.VISIBLE
                                currGamesTV.visibility = View.VISIBLE
                                loadingPB.visibility = View.GONE
                            }else{
                                createCodeBtn.visibility = View.VISIBLE
                                joinCodeBtn.visibility = View.VISIBLE
                                headTV.visibility = View.VISIBLE
                                codeEdt.visibility = View.VISIBLE
                                createdGamesHeadTV.visibility = View.VISIBLE
                                currGamesTV.visibility = View.VISIBLE
                                loadingPB.visibility = View.GONE
                                Toast.makeText(this@OnlineCodeGeneratorActivity,"Invalid Code",Toast.LENGTH_SHORT).show()
                            }
                        },2000)
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })


            }else{
                Toast.makeText(this,"Please enter a valid code",Toast.LENGTH_SHORT).show()
            }

        }
    }

    //Funcion cuando el otro jugador acepta el juego
    fun accepted (){
        startActivity(Intent(this,OnlineMultiplayerGameActivity::class.java))
        createCodeBtn.visibility = View.VISIBLE
        joinCodeBtn.visibility = View.VISIBLE
        codeEdt.visibility = View.VISIBLE
        headTV.visibility = View.VISIBLE
        createdGamesHeadTV.visibility = View.VISIBLE
        currGamesTV.visibility = View.VISIBLE
        loadingPB.visibility = View.GONE
    }

    //
    fun isValueAvaliable(snapshot: DataSnapshot, code: String): Boolean{
        var data = snapshot.children
        data.forEach{
            var value = it.getValue().toString()
            if(value==code){
                keyValue = it.key.toString()
                return true
            }
        }
        return false

    }
}

