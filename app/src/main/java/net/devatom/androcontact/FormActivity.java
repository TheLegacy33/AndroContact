package net.devatom.androcontact;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import net.devatom.androdatabase.DbHandler;
import net.devatom.andromodele.Personne;

import java.util.Objects;

public class FormActivity extends Activity implements View.OnClickListener {

    private int idPersonne = 0;
    private Personne maPersonne;

    private Spinner spCivilite;
    private EditText ttNom, ttPrenom, ttEmail;
    private Button btSave, btCancel;

    public DbHandler maDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        Objects.requireNonNull(getActionBar()).setDisplayHomeAsUpEnabled(true);

        //Lien avec les champs
        spCivilite = findViewById(R.id.cbCivilite);
        ttNom = findViewById(R.id.ttNom);
        ttPrenom = findViewById(R.id.ttPrenom);
        ttEmail = findViewById(R.id.ttEmail);
        btSave = findViewById(R.id.btSave);
        btCancel = findViewById(R.id.btCancel);

        btSave.setOnClickListener(this);
        btCancel.setOnClickListener(this);

        //Remplissage de la liste des civilités
        ArrayAdapter<CharSequence> data =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.civilites,
                        android.R.layout.simple_spinner_item
                );
        data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCivilite.setAdapter(data);

        //Ouverture de la base de données
        maDatabase = new DbHandler(this);

        Bundle extras = getIntent().getExtras();
        if  (extras == null){
            //Je viens pour ajouter une Personne
            spCivilite.setSelection(0);
            ttNom.setText("");
            ttPrenom.setText("");
            ttEmail.setText("");
            maPersonne = new Personne();

        }else if (extras.getInt("id", idPersonne) != 0){
            //Je viens pour récupérer les informations d'une Personne
            idPersonne = extras.getInt("id");
            maPersonne = maDatabase.getPersonne(idPersonne);
            spCivilite.setSelection(data.getPosition(maPersonne.getCivilite()));
            ttNom.setText(maPersonne.getNom());
            ttPrenom.setText(maPersonne.getPrenom());
            ttEmail.setText(maPersonne.getEmail());
        }
    }

    @Override
    protected void onDestroy() {
        maDatabase.closeDb();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btSave)){
            if (spCivilite.getSelectedItemPosition() > 0 && ttNom.getText().length() != 0 && ttPrenom.getText().length() != 0 && ttEmail.getText().length() != 0){
                maPersonne.setCivilite(spCivilite.getSelectedItem().toString());
                maPersonne.setNom(ttNom.getText().toString());
                maPersonne.setPrenom(ttPrenom.getText().toString());
                maPersonne.setEmail(ttEmail.getText().toString());

                if (idPersonne != 0){
                    if (maDatabase.updatePersonne(maPersonne) > 0){
                        Toast.makeText(this, "Enregistrement effectué", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (maDatabase.personneExists(maPersonne.getNom().trim(), maPersonne.getPrenom().trim()) > 0){
                        Toast.makeText(this, "Cette personne existe déjà !", Toast.LENGTH_SHORT).show();
                        return ;
                    }else {
                        if (maDatabase.addPersonne(maPersonne) > 0) {
                            Toast.makeText(this, "Ajout effectué", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                setResult(RESULT_OK);
                finish();
            }else{
                Toast.makeText(this, "Veuillez renseigner les champs !", Toast.LENGTH_SHORT).show();
            }
        }else if (v.equals(btCancel)){
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
