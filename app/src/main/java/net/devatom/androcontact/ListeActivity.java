package net.devatom.androcontact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import net.devatom.androdatabase.DbHandler;
import net.devatom.andromodele.Personne;

import java.util.Objects;

public class ListeActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public DbHandler maDatabase = null;
    private ListView lstPersonnes;
    private Personne itemSelected;
    private ArrayAdapter<Personne> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);
        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);

        lstPersonnes = findViewById(R.id.lstPersonnes);

        //Ouverture de la base de données
        maDatabase = new DbHandler(this);
        itemSelected = null;

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item, maDatabase.getAllPersonnes());
        lstPersonnes.setAdapter(adapter);
        lstPersonnes.setOnItemClickListener(this);
        lstPersonnes.setOnItemLongClickListener(this);
        Toast.makeText(this,"Total : " + maDatabase.getNbPersonnes(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        itemSelected = (Personne) lstPersonnes.getItemAtPosition(position);
        Log.d(getString(R.string.app_name), view.toString() + "; id = " + String.valueOf(itemSelected.getId()));
        Intent myIntent;
        myIntent = new Intent(this, FormActivity.class);
        myIntent.putExtra("id", itemSelected.getId());

        startActivityForResult(myIntent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK){
            Log.d(getString(R.string.app_name), "Refresh needed");
            adapter.clear();
            adapter.addAll(maDatabase.getAllPersonnes());
            adapter.notifyDataSetChanged();
        }else{
            Log.d(getString(R.string.app_name), "Refresh unneeded");
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        itemSelected = (Personne) lstPersonnes.getItemAtPosition(position);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.del_question))
                .setMessage(getString(R.string.del_message))
                .setPositiveButton(getString(R.string.bt_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (maDatabase.deletePersonne(itemSelected.getId()) > 0){
                            Toast.makeText(getApplicationContext(), "Suppression effectuée !", Toast.LENGTH_SHORT).show();
                            adapter.remove(itemSelected);
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.bt_no), null)
                .show();
        return true;
    }
}
