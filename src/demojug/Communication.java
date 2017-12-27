/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demojug;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

/**
 *
 * @author geoffroy.vibrac
 */
public class Communication {


    public static void main(String[] args) {
        try {
            Card myCard = getCard();
            // récupèration d'un canal de communication avec la carte
            CardChannel channel = myCard.getBasicChannel();
            // Création de la commande APDU : demande de l'UID de la carte
            // CLA : 0xFF : demande de protocol
            // INS : 0xCA : get data
            // P1, P2 : 0x00 pas de paramètre
            // Le : 256 byte maxi
            CommandAPDU commandeAPDU = new CommandAPDU((byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, 256);
            // transmission de la commande par le canal qui retourne la réponse
            ResponseAPDU reponseAPDU = channel.transmit(commandeAPDU);
            // affichage de la réponse
            System.out.println(Util.byteArrayToHexString(reponseAPDU.getBytes()));
            //déconnexion de la carte
            myCard.disconnect(true);

        } catch (CardException ex) {
            Logger.getLogger(Communication.class.getName()).log(Level.SEVERE, null, ex);
        }
      



    }



    public static Card getCard(){

        // je créé un objet carte null
        Card card = null;
        try {
            CardTerminal myReader = getDefaultReader();
            System.out.println("Attente de la carte");
            // attente de la carte pendant 5 secondes
            myReader.waitForCardPresent(5000);
            // si la carte est présente => on se connecte à la carte
            if (myReader.isCardPresent()){
                // connexion avec tout protocol disponible
                card = myReader.connect("*");
            }

        } catch (CardException ex) {
            Logger.getLogger(Cards.class.getName()).log(Level.SEVERE, null, ex);
        }

        return card;

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
