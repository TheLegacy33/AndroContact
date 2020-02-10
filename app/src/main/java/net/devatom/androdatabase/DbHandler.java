package net.devatom.androdatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.devatom.andromodele.Personne;

import java.util.List;

public class DbHandler extends SQLiteOpenHelper {
    // Version de la base de données
    private static final int DATABASE_VERSION = 3;

    // Nom de la base de données
    private static final String DATABASE_NAME = "androContact.db";

    public DbHandler(Context pContext) {
        super(pContext, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(getDatabaseName(), pContext.getDatabasePath(getDatabaseName()).toString());
    }

    // Création des tables
    @Override
    public void onCreate(SQLiteDatabase pDatabase) {
        DbPersonne.createTable(pDatabase);
    }

    //Mise à jour des tables
    @Override
    public void onUpgrade(SQLiteDatabase pDatabase, int oldVersion, int newVersion) {
        DbPersonne.dropTable(pDatabase);
        DbPersonne.createTable(pDatabase);
    }

    public int getDbVersion(){
        return this.getReadableDatabase().getVersion();
    }

    /**
     * CRUD(Create, Read, Update, Delete)
     */

    public int addPersonne(Personne pPersonne) {
        return (int) DbPersonne.insertPersonne(getWritableDatabase(), pPersonne);
    }

    public Personne getPersonne(int pId){
        return DbPersonne.getPersonne(getReadableDatabase(), pId);
    }

    public List<Personne> getAllPersonnes() {
        return DbPersonne.getAll(getReadableDatabase());
    }

    public int updatePersonne(Personne pPersonne) {
        return DbPersonne.updatePersonne(getWritableDatabase(), pPersonne);
    }

    public int deletePersonne(int pId) {
        return DbPersonne.deletePersonne(getWritableDatabase(), pId);
    }

    public int getNbPersonnes() {
        return DbPersonne.count(getReadableDatabase());
    }

    public int personneExists(String pNom, String pPrenom){
        return DbPersonne.exists(getReadableDatabase(), pNom, pPrenom);
    }

    public void closeDb(){
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen()){
            db.close();
        }
    }
}