/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demojug;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactory;

/**
 *
 * @author geoffroy.vibrac
 */
public class Cards {

     public static void main(String[] args) {
        try {            
            CardTerminal myReader = getDefaultReader();
            
            // je créé un objet carte null
            Card card = null;
            System.out.println("Attente de la carte");
            // attente de la carte pendant 5 secondes
            myReader.waitForCardPresent(5000);
            // si la carte est présente => on se connecte à la carte
            if (myReader.isCardPresent()){
                // connexion avec tout protocol disponible
                card = myReader.connect("*");
                if (card != null){
                    // j'affiche le protocole
                    System.out.println("Protocol de la carte : " + card.getProtocol());
                    //déconnexion de la carte
                    card.disconnect(true);
                }
            }
            
        } catch (CardException ex) {
            Logger.getLogger(Cards.class.getName()).log(Level.SEVERE, null, ex);
        }





     }


     public static CardTerminal getDefaultReader() {

        CardTerminal myReader = null;
        
        try {

            // j'instancie une fabrique par défaut = PC/SC
            TerminalFactory factory = TerminalFactory.getDefault();
            //je créé un objet représentant les lecteurs PC/SC
            CardTerminals readers = factory.terminals();
            // je peut alors instancier une liste de lecteurs
            List<CardTerminal> readersList = readers.list();
            // j'instancie un lecteur depuis son nom
            myReader = readers.getTerminal("OMNIKEY CardMan 5x21-CL 0");


        } catch (CardException ex) {
            Logger.getLogger(Readers.class.getName()).log(Level.SEVERE, null, ex);
        }

        return myReader;

    }


}
