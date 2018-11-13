package fr.usmb.m1isc.compilation.tp;

/**
 * Arbre binaire qui contient une donnée quelconque
 */
public class Arbre {

    private Object valeur;
    private Arbre filsGauche;
    private Arbre filsDroit;

    /**
     * Crée une feuille (arbre sans fils)
     *
     * @param valeur : La donnée stockée dans ce noeud
     */
    public Arbre(Object valeur) {
        this.valeur = valeur;
        filsDroit = null;
        filsGauche = null;
    }

    /**
     * Crée un nouveau noeud avec deux sous-noeuds
     *
     * @param valeur     : La donnée stockée dans ce noeud
     * @param filsGauche : le premier fils du noeud
     * @param filsDroit  : le second fils du noeud
     */
    public Arbre(Object valeur, Arbre filsGauche, Arbre filsDroit) {
        this.valeur = valeur;
        this.filsGauche = filsGauche;
        this.filsDroit = filsDroit;
    }


    public Object getValeur() {
        return valeur;
    }

    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }

    public Arbre getFilsGauche() {
        return filsGauche;
    }

    public void setFilsGauche(Arbre filsGauche) {
        this.filsGauche = filsGauche;
    }

    public Arbre getFilsDroit() {
        return filsDroit;
    }

    public void setFilsDroit(Arbre filsDroit) {
        this.filsDroit = filsDroit;
    }

    /**
     * Affiche l'arbre et tous les noeuds qu'il contient
     *
     * @return L'arbre sous la forme d'un String
     */
    @Override
    public String toString() {
        String res = valeur.toString();
        if (filsGauche != null) {
            res = "(" + res + " " + filsGauche.toString();
            if (filsDroit != null)
                res += " " + filsDroit.toString();
            res += ")";
        }
        return res;
    }
}
