package com.example.onlinetriki

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlin.system.exitProcess

//Inicia la partida online si el fue el que creo el codigo
var isMyMove = isCodeMaker;
class OnlineMultiplayerGameActivity : AppCompatActivity() {

    //Variables para todos los componente UI
    lateinit var player1TV : TextView
    lateinit var player2TV : TextView
    lateinit var box1Btn : Button
    lateinit var box2Btn : Button
    lateinit var box3Btn : Button
    lateinit var box4Btn : Button
    lateinit var box5Btn : Button
    lateinit var box6Btn : Button
    lateinit var box7Btn : Button
    lateinit var box8Btn : Button
    lateinit var box9Btn : Button
    lateinit var resetBtn : Button
    //Variable para saber de quie  es el turno
    lateinit var turnTV : TextView

    //Puntuaciones de los jugadores
    var player1count = 0
    var player2count = 0

    //Saber que casillas ha marcado cada jugador
    var player1 = ArrayList<Int>()
    var player2 = ArrayList<Int>()
    var emptyCells = ArrayList<Int>()

    //
    var activeUser = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_multiplayer_game)
        player1TV = findViewById(R.id.idTVPlayer1)
        player2TV = findViewById(R.id.idTVPlayer2)

        box1Btn = findViewById(R.id.idBtnBox1)
        box2Btn = findViewById(R.id.idBtnBox2)
        box3Btn = findViewById(R.id.idBtnBox3)
        box4Btn = findViewById(R.id.idBtnBox4)
        box5Btn = findViewById(R.id.idBtnBox5)
        box6Btn = findViewById(R.id.idBtnBox6)
        box7Btn = findViewById(R.id.idBtnBox7)
        box8Btn = findViewById(R.id.idBtnBox8)
        box9Btn = findViewById(R.id.idBtnBox9)

        //resetBtn = findViewById(R.id.idBtnReset)
        turnTV = findViewById(R.id.idTVTurn)

        //Inicializar texto del turno
        if(isCodeMaker){
            turnTV.text="It's your turn"
        } else {
            turnTV.text="It's opponent's turn"
        }

        /*
        Boton para reiniciar removido
        resetBtn.setOnClickListener {
            reset()
        }
         */

        //Inicializar base de datos
        FirebaseDatabase.getInstance().reference.child("data").child(code).addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                var data = snapshot.value
                if(isMyMove){
                    isMyMove = false
                    moveOnline(data.toString(), isMyMove)
                } else {
                    isMyMove = true
                    moveOnline(data.toString(), isMyMove)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //reset()
                //Toast.makeText(this@OnlineMultiplayerGameActivity,"Game Reset",Toast.LENGTH_SHORT).show()

                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun moveOnline(data: String, move : Boolean){
        //val audio = MediaPlayer.create(this, R.raw.sword)
        if(move){
            var buttonSelected : Button = when(data.toInt()){
                1->box1Btn
                2->box2Btn
                3->box3Btn
                4->box4Btn
                5->box5Btn
                6->box6Btn
                7->box7Btn
                8->box8Btn
                9->box9Btn
                else -> {box1Btn}
            }
            buttonSelected.text = "O"
            turnTV.text = "It's your turn"
            buttonSelected.setTextColor(Color.parseColor("#D22BB804"))
            player2.add(data.toInt())
            emptyCells.add(data.toInt())
            //audio.start()
            //Handler().postDelayed{Runnable {audio.release()}, 200}
            buttonSelected.isEnabled = false
            checkWinner()
        }
    }

    fun checkWinner(): Int{
        //val audio = MediaPlayer.create(this, R.raw.sword)
        //Revisar si el jugador 1 gano el juego
        if((player1.contains(1) && player1.contains(2) && player1.contains(3)) ||
            (player1.contains(4) && player1.contains(5) && player1.contains(6)) ||
            (player1.contains(7) && player1.contains(8) && player1.contains(9)) ||
            (player1.contains(1) && player1.contains(4) && player1.contains(7)) ||
            (player1.contains(2) && player1.contains(5) && player1.contains(8)) ||
            (player1.contains(3) && player1.contains(6) && player1.contains(9)) ||
            (player1.contains(1) && player1.contains(5) && player1.contains(9)) ||
            (player1.contains(3) && player1.contains(5) && player1.contains(7))){
            player1count+=1
            buttonDisable()
            //audio.start()
            //disableReset()
            //Handler().postDelayed(Runnable {audio.release()},4000)
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("You have won!"+"\n\n")
            build.setNegativeButton("Exit"){dialog,which->
                //audio.release()
                reset()
                removeCode()
                exitProcess(1)
            }
            Handler().postDelayed(Runnable {build.show()}, 500)
            return 1
        } else if(
            (player2.contains(1) && player2.contains(2) && player2.contains(3)) ||
            (player2.contains(4) && player2.contains(5) && player2.contains(6)) ||
            (player2.contains(7) && player2.contains(8) && player2.contains(9)) ||
            (player2.contains(1) && player2.contains(4) && player2.contains(7)) ||
            (player2.contains(2) && player2.contains(5) && player2.contains(8)) ||
            (player2.contains(3) && player2.contains(6) && player2.contains(9)) ||
            (player2.contains(1) && player2.contains(5) && player2.contains(9)) ||
            (player2.contains(3) && player2.contains(5) && player2.contains(7))
            ){
            player2count+=1
            buttonDisable()
            //audio.start()
            //disableReset()
            //Handler().postDelayed(Runnable {audio.release()},4000)
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Over")
            build.setMessage("Opponent have won the game!")
            build.setNegativeButton("Exit"){dialog,which->
                //audio.release()
                reset()
                removeCode()
                exitProcess(1)
            }
            Handler().postDelayed(Runnable {build.show()}, 500)
            return 1
        } else if(
            (emptyCells.contains(1) && emptyCells.contains(2) && emptyCells.contains(3)) &&
            (emptyCells.contains(4) && emptyCells.contains(5) && emptyCells.contains(6)) &&
            (emptyCells.contains(7) && emptyCells.contains(8) && emptyCells.contains(9))
        ){
            val build = AlertDialog.Builder(this)
            build.setTitle("Game Draw")
            build.setMessage("Game Draw")
            build.setNegativeButton("Exit"){dialog,which->
                //audio.release()
                reset()
                removeCode()
                exitProcess(1)
            }
            build.show()
            return 1
        }
        //Si nadie ha ganado ni hay empate retornar 0
        return 0
    }

    fun playNow(buttonSelected : Button, currCell : Int){
        //val audio = MediaPLayer.create(this, R.raw.sword)
        buttonSelected.text = "X"
        emptyCells.remove(currCell)
        turnTV.text = "It's opponent's turn"
        buttonSelected.setTextColor(Color.parseColor("#EC0C0C"))
        player1.add(currCell)
        emptyCells.add(currCell)
        //audio.start()
        buttonSelected.isEnabled = false
        //Handler().postDelayed(Runnable { audio.release() }, 200)
        checkWinner()
    }

    fun reset(){
        player1.clear()
        player2.clear()
        emptyCells.clear()
        activeUser = 1
        for(i in 1..9){
            var buttonSelected : Button = when(i){
                1->box1Btn
                2->box2Btn
                3->box3Btn
                4->box4Btn
                5->box5Btn
                6->box6Btn
                7->box7Btn
                8->box8Btn
                9->box9Btn
                else -> {box1Btn}
            }
            buttonSelected.isEnabled = true
            buttonSelected.text= ""
            player1TV.text = "Player 1: $player1count"
            player2TV.text = "Player 2: $player2count"
            isMyMove = isCodeMaker
            if(isCodeMaker){
                FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
            }
        }
    }

    fun buttonDisable(){
        for(i in 1..9){
            val buttonSelected : Button = when(i){
                1->box1Btn
                2->box2Btn
                3->box3Btn
                4->box4Btn
                5->box5Btn
                6->box6Btn
                7->box7Btn
                8->box8Btn
                9->box9Btn
                else -> {box1Btn}
            }
            if(buttonSelected.isEnabled){
                buttonSelected.isEnabled = false
            }
        }
    }

    //Remover el codigo de la database de firebass
    fun removeCode(){
        if(isCodeMaker){
            FirebaseDatabase.getInstance().reference.child("codes").child(keyValue).removeValue()
        }
    }

    /*
    fun disableReset(){
        resetBtn.isEnabled = false
        Handler().postDelayed(Runnable {resetBtn.isEnabled = true}, 2000)
    }
     */

    fun updateDatabase(cellId : Int){
        FirebaseDatabase.getInstance().reference.child("data").child(code).push().setValue(cellId)
    }

    //Función que ocurre cuando un usuario oprime la flecha hacia atras
    override fun onBackPressed() {
        removeCode()
        if(isCodeMaker){
            FirebaseDatabase.getInstance().reference.child("data").child(code).removeValue()
        }
        exitProcess(0)
    }

    fun buttonClick(view: View) {
        if(isMyMove){
            val but = view as Button
            var cellOnline = 0
            when (but.id){
                R.id.idBtnBox1 ->cellOnline=1
                R.id.idBtnBox2 ->cellOnline=2
                R.id.idBtnBox3 ->cellOnline=3
                R.id.idBtnBox4 ->cellOnline=4
                R.id.idBtnBox5 ->cellOnline=5
                R.id.idBtnBox6 ->cellOnline=6
                R.id.idBtnBox7 ->cellOnline=7
                R.id.idBtnBox8 ->cellOnline=8
                R.id.idBtnBox9 ->cellOnline=9
                else -> {cellOnline=0}
            }
            playerTurn = false;
            Handler().postDelayed(Runnable {playerTurn = true}, 600)
            playNow(but, cellOnline)
            updateDatabase(cellOnline)
        } else {
            Toast.makeText(this, "Wait for your turn", Toast.LENGTH_SHORT).show()
        }
    }
}