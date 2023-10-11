package com.example.androidtictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Un objeto que representa el estado interno del juego
    private TicTacToeGame mGame;

    //Para acceder al texto que se mostrara en pantalla
    private TextView mInfoTextView;

    //Saber cuando el juego ha terminado
    private boolean mGameOver;

    //Variable para alternar quein juega al inicio (-1 CPU, 1 Player)
    private int mBegin;

    //Variable para saber cuantas victorias y empates hay
    private int mWinsAndroid = 0;
    private int mWinsHuman = 0;
    private int mTies = 0;

    //Variables para acceder a los textos asociados a las victorias y empates
    private TextView mWinsAndroidTextView;
    private TextView mWinsHumanTextView;
    private TextView mTiesTextView;

    //Constantes para identificar cajas de dialogo
    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_RESET_SCORE_ID = 1;
    static final int DIALOG_ABOUT_ID = 2;

    //Acceso al view que representa el tablero
    private BoardView mBoardView;

    //Variables para reproductores de sonidos
    MediaPlayer mHumanMediaPlayer;
    MediaPlayer mComputerMediaPlayer;


    //Variable para almacenar datos en las preferencias
    private SharedPreferences mPrefs;

    //Funcion para iniciar un nuevo juego
    private void startNewGame() {

        //Limpiar el tablero a nivel logico
        mGame.clearBoard();

        //Redibujar la view del tablero
        mBoardView.invalidate();

        //Volver el gameover false
        mGameOver = false;

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


    private boolean setMove(char player, int location) {
        if (mGame.setMove(player, location)) {
            if (player == TicTacToeGame.HUMAN_PLAYER) {
                //Sonido del jugador
                mHumanMediaPlayer.start();
            } else {
                mComputerMediaPlayer.start();
            }

            mBoardView.invalidate(); // Redraw the board
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Activar el toolbar para que aparezca arriba de la aplicacion
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Crear el tablero logico y darselo al boardview para que pueda
        //Actualizarse segun lo que ocurra
        mGame = new TicTacToeGame();
        mBoardView = findViewById(R.id.board);
        mBoardView.setGame(mGame);
        // Listen for touches on the board
        mBoardView.setOnTouchListener(mTouchListener);

        //Inicializar la variable de preferencias
        //El primer argumento es el nombre del archivo de preferencias
        mPrefs = getSharedPreferences("ttt_prefs",MODE_PRIVATE);

        mInfoTextView = findViewById(R.id.information);
        mWinsAndroidTextView = findViewById(R.id.num_android_points);
        mWinsHumanTextView = findViewById(R.id.num_human_points);
        mTiesTextView = findViewById(R.id.num_tie_points);

        mPrefs = getSharedPreferences("ttt_prefs",MODE_PRIVATE);
        //Restaurar la puntuacion obtenida previamente
        // (0 en caso de no tener datos)
        mWinsHuman = mPrefs.getInt("mWinsHuman", 0);
        mWinsAndroid = mPrefs.getInt("mWinsAndroid",0);
        mTies = mPrefs.getInt("mTies",0);

        if (savedInstanceState == null){
            mBegin = 1; //La primera vez inicia el jugador humano
            startNewGame();
        } else {
            //Restaurar estado del juego
            mGame.setBoardState(savedInstanceState.getCharArray("board"));
            mGameOver = savedInstanceState.getBoolean("mGameOver");
            mInfoTextView.setText(savedInstanceState.getCharSequence("info"));
            mBegin = savedInstanceState.getInt("mBegin");
        }

        displayScores();
    }

    private void displayScores() {
        mWinsHumanTextView.setText(Integer.toString(mWinsHuman));
        mWinsAndroidTextView.setText(Integer.toString(mWinsAndroid));
        mTiesTextView.setText(Integer.toString(mTies));
    }

    //Creacion del menu de opciones
    @Override
    @SuppressLint("RestrictedApi")
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);

        //Hacer que los iconos de las opciones si se muestren
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }

        return true;
    }

    //Cuando el menu se selecciona
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //id del item seleccionado del menu
        int id = item.getItemId();
        if (id == R.id.new_game){
            //Iniciar un nuevo juego
            startNewGame();
            return true;
        } else if (id == R.id.ai_difficulty) {
            showDialog(DIALOG_DIFFICULTY_ID);
            return true;
        } else if (id == R.id.reset_score) {
            showDialog(DIALOG_RESET_SCORE_ID);
            return true;
        } else if (id == R.id.about) {
            showDialog(DIALOG_ABOUT_ID);
            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putCharArray("board", mGame.getBoardState());
        outState.putBoolean("mGameOver", mGameOver);
        outState.putInt("mWinsHuman",Integer.valueOf(mWinsHuman));
        outState.putInt("mWinsAndroid",Integer.valueOf(mWinsAndroid));
        outState.putInt("mTies",Integer.valueOf(mTies));
        outState.putInt("mBegin", mBegin);
        outState.putCharSequence("info", mInfoTextView.getText());
    }



    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            case DIALOG_DIFFICULTY_ID:

                builder.setTitle(R.string.difficulty_choose);

                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_easy),
                        getResources().getString(R.string.difficulty_harder),
                        getResources().getString(R.string.difficulty_expert)};


                //La primera opcion que debe estar seleccionada es expert (corresponde a 2)
                //Por ello se asigna a la variable selected
                int selected = 2;

                builder.setSingleChoiceItems(levels, selected,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss(); // Close dialog

                                //Se asigna el nivel de dificultad elegido
                                switch (item){
                                    case 0:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
                                        break;
                                    case 1:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
                                        break;
                                    case 2:
                                        mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
                                        break;
                                }

                                // Display the selected difficulty level
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();
                            }
                });
                dialog = builder.create();

                break;

            case DIALOG_RESET_SCORE_ID:
                // Create the quit confirmation dialog
                builder.setMessage(R.string.reset_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mWinsHuman = 0;
                                mWinsAndroid = 0;
                                mTies = 0;

                                mWinsHumanTextView.setText(String.valueOf(mWinsHuman));
                                mWinsAndroidTextView.setText(String.valueOf(mWinsAndroid));
                                mTiesTextView.setText(String.valueOf(mTies));
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;

            case DIALOG_ABOUT_ID:
                Context context = getApplicationContext();
                LayoutInflater inflater = LayoutInflater.from(context);
                View layout = inflater.inflate(R.layout.about_dialog, null);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();
                break;
        }
        return dialog;
    }

    // Listen for touches on the board
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            // Determine which cell was touched
            int col = (int) event.getX() / mBoardView.getBoardCellWidth();
            int row = (int) event.getY() / mBoardView.getBoardCellHeight();
            int pos = row * 3 + col;
            if (!mGameOver && setMove(TicTacToeGame.HUMAN_PLAYER, pos)){
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
                    //Cambiar la persona que inicia la proxima ronda
                    mBegin = mBegin * -1;
                    //Actualizar contadores de empates
                    mTies = mTies + 1;
                    mTiesTextView.setText(String.valueOf(mTies));
                }
                else if (winner == 2) {
                    mInfoTextView.setText(R.string.result_human_wins);
                    mGameOver = true;
                    //Cambiar la persona que inicia la proxima ronda
                    mBegin = mBegin * -1;
                    //Actualizar contadores de victorias humanas
                    mWinsHuman = mWinsHuman + 1;
                    mWinsHumanTextView.setText(String.valueOf(mWinsHuman));
                }
                else {
                    mInfoTextView.setText(R.string.result_computer_wins);
                    mGameOver = true;
                    //Cambiar la persona que inicia la proxima ronda
                    mBegin = mBegin * -1;
                    //Actualizar contadores de victorias android
                    mWinsAndroid = mWinsAndroid + 1;
                    mWinsAndroidTextView.setText(String.valueOf(mWinsAndroid));
                }
            }

            // So we aren't notified of continued events when finger is moved
            return false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        mHumanMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sword);
        mComputerMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.swish);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHumanMediaPlayer.release();
        mComputerMediaPlayer.release();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //Almacenar los puntajes para tenerlos de forma persistente
        SharedPreferences.Editor ed = mPrefs.edit();
        ed.putInt("mWinsHuman",mWinsHuman);
        ed.putInt("mWinsAndroid",mWinsAndroid);
        ed.putInt("mTies",mTies);
        //Sobreescribe los datos previamente guardados con los nuevos
        ed.commit();
    }


}

