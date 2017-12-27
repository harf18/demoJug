/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demojug;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactory;

/**
 *
 * @author geoffroy.vibrac
 */
public class Readers {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {

            // j'instancie une fabrique par défaut = PC/SC
            TerminalFactory factory = TerminalFactory.getDefault();
            //je créé un objet représentant les lecteurs PC/SC
            CardTerminals readers = factory.terminals();
            // je peut alors instancier une liste de lecteurs
            List<CardTerminal> readersList = readers.list();
            // je parcours la liste avec la methode getName() pour récupérer les noms des lecteurs
            for (CardTerminal ct : readersList){
                System.out.println(ct.getName());                      
            }
            // j'instancie un lecteur depuis son nom
            CardTerminal myReader = readers.getTerminal("OMNIKEY CardMan 5x21-CL 0");


        } catch (CardException ex) {
            Logger.getLogger(Readers.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
