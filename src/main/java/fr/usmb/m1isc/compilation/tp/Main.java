package fr.usmb.m1isc.compilation.tp;

import java.io.FileReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {
        LexicalAnalyzer yy;
        if (args.length > 0) {
            yy = new LexicalAnalyzer(new FileReader(args[0]));
        } else {
            yy = new LexicalAnalyzer(new InputStreamReader(System.in));
        }
        @SuppressWarnings("deprecation")
        parser p = new parser(yy);
        Arbre res = (Arbre) p.parse().value;  // on récupère l'arbre abstrait généré par cup
        System.out.println(res);
        System.out.println("Done");

        if(args.length <= 1){
            System.err.println("Veuillez rentrer un fichier de sortie");
        } else {
            res.toAsmFile(args[1]);
        }
    }
}
