package net.devatom.androdatabase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.devatom.andromodele.Personne;

import java.util.ArrayList;
import java.util.List;

abstract class DbPersonne {

    private static final String TABLE_NAME = "personne";

    private static final String FIELD_ID = "pers_id";
    private static final String FIELD_CIVILITE = "pers_civilite";
    private static final String FIELD_NOM = "pers_nom";
    private static final String FIELD_PRENOM = "pers_prenom";
    private static final String FIELD_EMAIL = "pers_email";

    private static String SQLQuery = "";

    private static final String LOGTAG = "DbAndroContact";

    static void createTable(SQLiteDatabase pDatabase) {
        Log.d(LOGTAG, "Création de la table " + TABLE_NAME);

        SQLQuery = "CREATE TABLE personne (" +
                FIELD_ID + " INT NOT NULL PRIMARY KEY, " +
                FIELD_CIVILITE + " VARCHAR(5) NOT NULL," +
                FIELD_NOM + " VARCHAR(50) NOT NULL," +
                FIELD_PRENOM + " VARCHAR(50) NOT NULL," +
                FIELD_EMAIL + " VARCHAR(250) NOT NULL" +
                ")";
        pDatabase.execSQL(SQLQuery);
    }

    static void dropTable(SQLiteDatabase pDatabase){
        Log.d(LOGTAG, "Suppression de la table " + TABLE_NAME);

        SQLQuery = "DROP TABLE " + TABLE_NAME;
        pDatabase.execSQL(SQLQuery);
    }

    static long insertPersonne(SQLiteDatabase pDatabase, Personne pPersonne) {
        ContentValues values = new ContentValues();
        values.put(FIELD_ID, getNewId(pDatabase));
        values.put(FIELD_CIVILITE, pPersonne.getCivilite());
        values.put(FIELD_NOM, pPersonne.getNom());
        values.put(FIELD_PRENOM, pPersonne.getPrenom());
        values.put(FIELD_EMAIL, pPersonne.getEmail());

        return pDatabase.insert(TABLE_NAME, null, values);
    }

    static int updatePersonne(SQLiteDatabase pDatabase, Personne pPersonne) {
        ContentValues values = new ContentValues();
        values.put(FIELD_CIVILITE, pPersonne.getCivilite());
        values.put(FIELD_NOM, pPersonne.getNom());
        values.put(FIELD_PRENOM, pPersonne.getPrenom());
        values.put(FIELD_EMAIL, pPersonne.getEmail());

        return pDatabase.update(TABLE_NAME, values, FIELD_ID + " = " + pPersonne.getId(), null);
    }

    static int deletePersonne(SQLiteDatabase pDatabase, int pId) {
        return pDatabase.delete(TABLE_NAME, FIELD_ID + " = " + pId, null);
    }

    static Personne getPersonne(SQLiteDatabase pDatabase, int pId) {
        Personne unePersonne = null;
        //SQLQuery = "SELECT " + FIELD_ID + ", " + FIELD_CIVILITE + ", " + FIELD_NOM + ", " + FIELD_PRENOM + ", " + FIELD_EMAIL + " ";
        //SQLQuery += "FROM " + TABLE_NAME + " WHERE " + FIELD_ID + " = " + pId;
        //Cursor SQLResult = pDatabase.rawQuery(SQLQuery, null);
        Cursor SQLResult = pDatabase.query(TABLE_NAME, null, FIELD_ID + " = " + pId, null, null, null, null);
        if (SQLResult != null){
            SQLResult.moveToFirst();
            unePersonne = new Personne();
            unePersonne.setId(SQLResult.getInt(0));
            unePersonne.setCivilite(SQLResult.getString(1).trim());
            unePersonne.setNom(SQLResult.getString(2).trim());
            unePersonne.setPrenom(SQLResult.getString(3));
            unePersonne.setEmail(SQLResult.getString(4));
            SQLResult.close();
        }
        return unePersonne;
    }

    static int exists(SQLiteDatabase pDatabase, String pNom, String pPrenom){
        int retVal = 0;
        SQLQuery = "SELECT COUNT(" + FIELD_ID + ") FROM personne ";
        SQLQuery += "WHERE UPPER(" + FIELD_NOM + ") = '" + pNom.toUpperCase() + "' ";
        SQLQuery += "AND UPPER(" + FIELD_PRENOM + ") = '" + pPrenom.toUpperCase() + "'";

        Cursor SQLResult = pDatabase.rawQuery(SQLQuery, null);
        if (SQLResult != null){
            SQLResult.moveToFirst();
            retVal = SQLResult.getInt(0);
            SQLResult.close();
        }
        return retVal;
    }

    static List<Personne> getAll(SQLiteDatabase pDatabase) {
        ArrayList<Personne> personnesList = new ArrayList<>();
        Cursor SQLResult = pDatabase.query(TABLE_NAME, null, null, null, null, null, FIELD_NOM + ',' + FIELD_PRENOM);

        if (SQLResult.moveToFirst()) {
            do {
                Personne unePersonne = new Personne();
                unePersonne.setId(SQLResult.getInt(0));
                unePersonne.setCivilite(SQLResult.getString(1).trim());
                unePersonne.setNom(SQLResult.getString(2).trim());
                unePersonne.setPrenom(SQLResult.getString(3));
                unePersonne.setEmail(SQLResult.getString(4));
                personnesList.add(unePersonne);
            } while (SQLResult.moveToNext());
        }
        SQLResult.close();
        return personnesList;
    }

    private static int getNewId(SQLiteDatabase pDatabase){
        Cursor SQLResult = pDatabase.rawQuery("SELECT IFNULL(MAX(" + FIELD_ID + "), 0) + 1 FROM " + TABLE_NAME, null);
        int retVal = 0;
        if (SQLResult != null){
            SQLResult.moveToFirst();
            retVal = SQLResult.getInt(0);
            SQLResult.close();
        }
        Log.d(LOGTAG, "New Id Personne : " + String.valueOf(retVal));
        return retVal;
    }

    static int count(SQLiteDatabase pDatabase) {
        int retVal = 0;
        Cursor SQLResult = pDatabase.rawQuery("SELECT COUNT(" + FIELD_ID + ") FROM " + TABLE_NAME, null);
        if (SQLResult != null){
            SQLResult.moveToFirst();
            retVal = SQLResult.getInt(0);
            SQLResult.close();
        }
        return retVal;
    }
}
