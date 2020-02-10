package net.devatom.andromodele;

public class Personne {
    private int id;
    private String civilite, nom, prenom, email;

    public Personne(){
        this.id = 0;
        this.civilite = "";
        this.nom = "";
        this.prenom = "";
        this.email = "";
    }

    public void setId(int pId) {
        this.id = pId;
    }

    public void setCivilite(String pCivilite) {
        this.civilite = pCivilite;
    }

    public void setEmail(String pEmail) {
        this.email = pEmail.trim();
    }

    public void setNom(String pNom) { this.nom = pNom.trim(); }

    public void setPrenom(String pPrenom) {
        this.prenom = pPrenom.trim();
    }

    public int getId() {
        return id;
    }

    public String getCivilite() {
        return civilite;
    }

    public String getEmail() {
        return email;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return getNom() + " " + getPrenom() + " : " + getEmail();
    }
}
