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

    private TypeNoeud type;
    private Object valeur;
    private Arbre filsGauche;
    private Arbre filsDroit;

    private static final String DATA_BEGIN = "DATA SEGMENT\n";
    private static final String DATA_END = "DATA ENDS\n";

    private static final String CODE_BEGIN = "CODE SEGMENT\n";
    private static final String CODE_END = "CODE ENDS\n";

    private static int etq = 0;

    /**
     * Crée une feuille (arbre sans fils)
     *
     * @param type   : Le type de la donnée stockée
     * @param valeur : La donnée stockée dans ce noeud
     */
    public Arbre(TypeNoeud type, Object valeur) {
        this.type = type;
        this.valeur = valeur;
        filsDroit = null;
        filsGauche = null;
    }

    /**
     * Crée un nouveau noeud avec deux sous-noeuds
     *
     * @param type       : Le type de la donnée stockée
     * @param valeur     : La donnée stockée dans ce noeud
     * @param filsGauche : le premier fils du noeud
     * @param filsDroit  : le second fils du noeud
     */
    public Arbre(TypeNoeud type, Object valeur, Arbre filsGauche, Arbre filsDroit) {
        this.type = type;
        this.valeur = valeur;
        this.filsGauche = filsGauche;
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
        Set<String> res = new HashSet<>();
        if (type == TypeNoeud.LET){
            res.add((String)valeur);
        }
        if (filsDroit != null) res.addAll(filsDroit.getVariables());
        if (filsGauche != null) res.addAll(filsGauche.getVariables());
        return res;
    }

    /**
     * Génére le code relatif au noeud
     * @return le code générer pour ce noeud et ses fils
     */
    public String generer(){
        String res = "";
        switch (type) {
            case SEMI:
                res = filsGauche.generer();
                res += filsDroit.generer();
                break;
            case LET:
                res = filsGauche.generer();
                res += "\tmov " + valeur + ", eax\n";
                break;
            case PLUS:
                res = filsGauche.generer();
                res += "\tpush eax\n";
                res += filsDroit.generer();
                res += "\tpop ebx\n" +
                        "\tadd eax, ebx\n";
                break;
            case MOINS:
                res = filsDroit.generer();
                res += "\tpush eax\n";
                res += filsGauche.generer();
                res += "\tpop ebx\n" +
                        "\tsub eax, ebx\n";
                break;
            case MUL:
                res = filsGauche.generer();
                res += "\tpush eax\n";
                res += filsDroit.generer();
                res += "\tpop ebx\n" +
                        "\tmul eax, ebx\n";
                break;
            case DIV:
                res = filsGauche.generer();
                res += "\tpush eax\n";
                res += filsDroit.generer();
                res += "\tpop ebx\n" +
                        "\tdiv ebx, eax\n" +
                        "\tmov eax, ebx\n";
                break;
            case MOD:
                res = filsDroit.generer();
                res += "\tpush eax\n";
                res += filsGauche.generer();
                res += "\tpop ebx\n" +
                        "\tmov ecx, eax\n" +
                        "\tdiv ecx, ebx\n" +
                        "\tmul ecx, ebx\n" +
                        "\tsub eax, ecx\n";
                break;
            case LT:
                res = filsGauche.generer();
                res += "\tpush eax\n";
                res += filsDroit.generer();
                res += "\tpop ebx\n" +
                        "\tsub ebx, eax\n" +
                        "\tjl vrai_" + etq + "\n" +
                        "\tmov eax, 0\n" +
                        "\tjmp fin_" + etq + "\n" +
                        "vrai_" + etq + " :\n" +
                        "\tmov eax, 1\n" +
                        "fin_" + etq + " :\n";
                etq++;
                break;
            case GT:
                res = filsGauche.generer();
                res += "\tpush eax\n";
                res += filsDroit.generer();
                res += "\tpop ebx\n" +
                        "\tsub ebx, eax\n" +
                        "\tjg vrai_" + etq + "\n" +
                        "\tmov eax, 0\n" +
                        "\tjmp fin_" + etq + "\n" +
                        "vrai_" + etq + " :\n" +
                        "\tmov eax, 1\n" +
                        "fin_" + etq + " :\n";
                etq++;
                break;
            case LTE:
                res = filsGauche.generer();
                res += "\tpush eax\n";
                res += filsDroit.generer();
                res += "\tpop ebx\n" +
                        "\tsub ebx, eax\n" +
                        "\tjle vrai_" + etq + "\n" +
                        "\tmov eax, 0\n" +
                        "\tjmp fin_" + etq + "\n" +
                        "vrai_" + etq + " :\n" +
                        "\tmov eax, 1\n" +
                        "fin_" + etq + " :\n";
                etq++;
                break;
            case GTE:
                res = filsGauche.generer();
                res += "\tpush eax\n";
                res += filsDroit.generer();
                res += "\tpop ebx\n" +
                        "\tsub ebx, eax\n" +
                        "\tjge vrai_" + etq + "\n" +
                        "\tmov eax, 0\n" +
                        "\tjmp fin_" + etq + "\n" +
                        "vrai_" + etq + " :\n" +
                        "\tmov eax, 1\n" +
                        "fin_" + etq + " :\n";
                etq++;
                break;
            case OUTPUT:
                res = filsGauche.generer();
                res += "\tout eax\n";
                break;
            case INPUT:
                res = "\tin eax\n";
                break;
            case WHILE:
                int etq_temp = etq;
                etq++;
                res = "debut_cond_" + etq_temp + " :\n";
                res += filsGauche.generer();
                res += "\tjz sortie_" + etq_temp + "\n";
                res += filsDroit.generer();
                res += "\tjmp debut_cond_" + etq_temp + "\n" +
                        "sortie_" + etq_temp + " :\n";
                break;
            case IF:
                int etq_tmp = etq;
                etq++;
                res = filsGauche.generer();
                res += "\tjz else_" + etq_tmp + "\n";
                res += filsDroit.filsGauche.generer();  // première expression (then)
                res += "jmp sortie_" + etq_tmp + "\n";
                res += "else_" + etq_tmp + " :\n";
                res += filsDroit.filsDroit.generer();  // deuxième expression (else)
                res += "sortie_" + etq_tmp + " :\n";
                break;
            case OR:
                res = filsGauche.generer();
                res += "\tpush eax\n";
                res += filsDroit.generer();
                res += "\tpop ebx\n" +
                        "\tadd eax, ebx\n" +
                        "\tjg casOU" + etq + "\n" +
                        "\tmov eax, 0\n" +
                        "\tjmp sortieOU" + etq + "\n" +
                        "casOU" + etq + " :\n" +
                        "\tmov eax, 1\n" +
                        "sortieOU" + etq + " :\n";
                etq++;
                break;
            case AND:
                res = filsGauche.generer();
                res += "\tpush eax\n";
                res += filsDroit.generer();
                res += "\tpop ebx\n" +
                        "\tmul eax, ebx\n" +         // on évalue les booleans
                        "\tjz casET" + etq + "\n" +  // si l'un deux est faux (0), on saute pour renvoyer 0
                        "\tmov eax, 1\n" +           // sinon on renvoie vrai (1)
                        "\tjmp sortieET" + etq + "\n" +
                        "casET" + etq + " :\n" +
                        "\tmov eax, 0\n" +
                        "sortieET" + etq + " :\n";
                etq++;
                break;
            case IDENT:   // la valeur est une variable (identificateur)
            case ENTIER:  // ou un entier
                res = "\tmov eax, " + valeur + "\n";
        }
        return res;
    }

    public void toAsmFile(String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        writer.write(DATA_BEGIN);
        Set<String> variables = this.getVariables();
        for (String var : variables ){
            writer.write("\t" + var + " DD\n");
        }
        writer.write(DATA_END);
        writer.write(CODE_BEGIN);
        writer.write(generer());
        writer.write(CODE_END);

        writer.close();
    }

    public TypeNoeud getType() {
        return type;
    }

    public void setType(TypeNoeud type) {
        this.type = type;
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

}
