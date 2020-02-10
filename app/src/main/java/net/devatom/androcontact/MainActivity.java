package net.devatom.androcontact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button btListe, btAjout, btClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Définition du controle des actions sur les boutons
        btListe = findViewById(R.id.btListe);
        btAjout = findViewById(R.id.btAdd);
        btClose = findViewById(R.id.btClose);

        btListe.setOnClickListener(this);
        btAjout.setOnClickListener(this);
        btClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String message = "";
        Intent myIntent = null;
        if (v.equals(btListe)){
            message = "Click sur liste";
            myIntent = new Intent(this, ListeActivity.class);
        }else if (v.equals(btAjout)){
            message = "Click sur ajout";
            myIntent = new Intent(this, FormActivity.class);
        }else if (v.equals(btClose)){
            finish();
        }
        startActivity(myIntent);
        Log.d(getString(R.string.app_name), message);
    }
}
