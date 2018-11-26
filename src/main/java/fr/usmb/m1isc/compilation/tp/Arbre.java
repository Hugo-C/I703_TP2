package fr.usmb.m1isc.compilation.tp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Arbre binaire qui contient une donnée quelconque
 */
public class Arbre {

    private Object valeur;
    private Arbre filsGauche;
    private Arbre filsDroit;

    private static final String DATA_BEGIN = "DATA SEGMENT\n";
    private static final String DATA_END = "DATA ENDS\n";

    private static final String CODE_BEGIN = "CODE SEGMENT\n";
    private static final String CODE_END = "CODE ENDS\n";

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

    public Set<String> getVariables(){
        Set<String> res = new HashSet<String>();
        if (valeur == "let"){
            res.add((String)filsGauche.valeur);
        }
        if (filsDroit != null) res.addAll(filsDroit.getVariables());
        if (filsGauche != null) res.addAll(filsGauche.getVariables());
        return res;
    }

    public String generer(){
        String res = "";
        if (valeur.getClass() == String.class) {
            switch ((String) valeur) {
                case ";":
                    res = filsGauche.generer();
                    res += filsDroit.generer();
                    break;
                case "let":
                    res = filsDroit.generer();
                    res += "\tmov "+ filsGauche.valeur +", eax\n";
                    break;
                case "+":
                    res = filsGauche.generer();
                    res += "\tpush eax\n";
                    res += filsDroit.generer();
                    res += "\tpop ebx\n\tadd eax, ebx\n";
                    break;
                case "*":
                    res = filsGauche.generer();
                    res += "\tpush eax\n";
                    res += filsDroit.generer();
                    res += "\tpop ebx\n\tmul eax, ebx\n";
                    break;
                case "/":
                    res = filsGauche.generer();
                    res += "\tpush eax\n";
                    res += filsDroit.generer();
                    res += "\tpop ebx\n\tdiv ebx, eax\n\tmov eax, ebx\n";
                    break;
                    default:
                        res = "\tmov eax, " + valeur + "\n";
            }
        }
        else if (valeur.getClass() == Integer.class){
            res = "\tmov eax, "+valeur+"\n";
        }
        return res;
    }

    public void toAsmFile(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        writer.write(DATA_BEGIN);
        Set<String> varibales = this.getVariables();
        for (String var : varibales ){
            writer.write("\t" + var + " DD\n");
        }
        writer.write(DATA_END);
        writer.write(CODE_BEGIN);
        writer.write(generer());
        writer.write(CODE_END);

        writer.close();
    }
}
