package com.example.World2D;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GamePanel(this));
        sp = getSharedPreferences("DATA", MODE_PRIVATE);
        
         //This give the user a message concerning the game controls in the beginning of the game!
        AlertDialog.Builder controls = new AlertDialog.Builder(this);
        controls.setTitle("Controls");

        controls
                .setMessage("Welcome to OtopiaGameplay \n\n" +
                        "Game Controls \n\n" +
                        "- Press ↑ to jump upwards. \n" +
                        "- Press ↓ to crouch to the floor. \n" +
                        "- Press ← to move left. \n" +
                        "- Press → to move right \n")
                .setCancelable(false)
                .setPositiveButton("Got it!",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                    }
                });

// create alert dialog
        AlertDialog alertDialog = controls.create();

        controls.show();
    }
}
