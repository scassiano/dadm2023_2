package com.example.androidtictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Un objeto que representa el estado interno del juego
    private TicTacToeGame mGame;

    //Para acceder a los botones del triki que se muestran en pantalla
    private Button mBoardButtons[];

    //Para acceder a el boton de new game
    private Button mNewGameButton;

    //Para acceder al texto que se mostrara en pantalla
    private TextView mInfoTextView;

    //Saber cuando el juego ha terminado
    private boolean mGameOver;

    //Variable para alternar quein juega al inicio (-1 CPU, 1 Player)
    private int mBegin;

    //Funcion para iniciar un nuevo juego
    private void startNewGame() {

        //Limpiar el tablero a nivel logico
        mGame.clearBoard();

        //Volver invisible el New Game Button
        mNewGameButton.setVisibility(View.INVISIBLE);

        //Volver el gameover false
        mGameOver = false;

        // Reiniciar los botones a nivel grafico y darles un listener para las acciones del usuario
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
        }

        mNewGameButton.setOnClickListener(new NewGameClickListener());

        if (mBegin == 1){
            // Mencionar que el humano va primero
            mInfoTextView.setText(R.string.first_human);
        } else {
            //Turno de la computadora y luego dejar texto para saber que es turno del humano
            mInfoTextView.setText(R.string.turn_computer);
            int move = mGame.getComputerMove();
            setMove(TicTacToeGame.COMPUTER_PLAYER, move);

            //Turno humano
            mInfoTextView.setText(R.string.turn_human);
        }
    }

    // Maneja los clicks que se reciben a los botones del tablero
    private class ButtonClickListener implements View.OnClickListener {
        int location;
        public ButtonClickListener(int location) {
            this.location = location;
        }
        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled() && !mGameOver) {
                setMove(TicTacToeGame.HUMAN_PLAYER, location);
// If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.getComputerMove();
                    setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }
                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_human);
                }
                else if (winner == 1) {
                    mInfoTextView.setText(R.string.result_tie);
                    mGameOver = true;
                    //Volver Visible el New Game Button
                    mNewGameButton.setVisibility(View.VISIBLE);
                    mBegin = mBegin * -1;
                }
                else if (winner == 2) {
                    mInfoTextView.setText(R.string.result_human_wins);
                    mGameOver = true;
                    //Volver Visible el New Game Button
                    mNewGameButton.setVisibility(View.VISIBLE);
                    //Cambiar la persona que inicia la proxima ronda
                    mBegin = mBegin * -1;
                }
                else {
                    mInfoTextView.setText(R.string.result_computer_wins);
                    mGameOver = true;
                    //Volver Visible el New Game Button
                    mNewGameButton.setVisibility(View.VISIBLE);
                    mBegin = mBegin * -1;
                }
            }
        }
    }

    private class NewGameClickListener implements View.OnClickListener {
        public NewGameClickListener() {}
        public void onClick(View view) {
            startNewGame();
        }
    }

    private void setMove(char player, int location) {
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);
        mBoardButtons[location].setText(String.valueOf(player));
        if (player == TicTacToeGame.HUMAN_PLAYER)
            mBoardButtons[location].setTextColor(Color.rgb(0, 200, 0));
        else
            mBoardButtons[location].setTextColor(Color.rgb(200, 0, 0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBoardButtons = new Button[TicTacToeGame.BOARD_SIZE];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);
        mInfoTextView = (TextView) findViewById(R.id.information);
        mNewGameButton = (Button) findViewById(R.id.newgame);
        mBegin = 1; //La primera vez inicia el jugador humano

        mGame = new TicTacToeGame();
        startNewGame();
    }

}

