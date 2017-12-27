package demojug;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
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
import javax.swing.JFrame;

/**
*
* @author mree
*/
public class ReadMifare {

   public ReadMifare() {

       try {

           CardTerminal terminal = null;

           // show the list of available terminals
           TerminalFactory factory = TerminalFactory.getDefault();
           CardTerminals terminals = factory.terminals();

           String readerName = "";

                   terminal = terminals.getTerminal("OMNIKEY CardMan 5x21-CL 0");
         

           // Establish a connection with the card
           System.out.println("Attente de la carte");
           terminal.waitForCardPresent(0);

           Card card = terminal.connect("T=1");
           CardChannel channel = card.getBasicChannel();

           // Start with something simple, read UID, kinda like Hello World!
           byte[] baReadUID = new byte[5];

           baReadUID = new byte[]{(byte) 0xFF, (byte) 0xCA, (byte) 0x00,
                                  (byte) 0x00, (byte) 0x00};

           //System.out.println("UID: " + send(baReadUID, channel));
           // If successfull, the output will end with 9000

           // OK, now, the real work
           // Get Serial Number
           // Load key
           byte[] baLoadKey = new byte[12];

           baLoadKey = new byte[]{(byte) 0xFF, (byte) 0x82, (byte) 0x20,
                                     (byte) 0x00, (byte) 0x06, (byte) 0xFF,
                                     (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                                     (byte) 0xFF, (byte) 0xFF};

           System.out.println("LOAD KEY: " + send(baLoadKey, channel));
           // If successfull, will output 9000

                                   // chargement de la clef D3h F7h D3h F7h D3h F7
                                   // Load Key CLA = FF,
                                   // INS = 82,
                                   // Key structure = 0x20 pour non volatile memory
                                   // Key number 0x00->0x1F
                                   // taille de la clef : 6 octets
                                   
           baLoadKey = new byte[]{(byte) 0xFF, (byte) 0x82, (byte) 0x20, (byte) 0x1C, (byte) 0x06,
                                   // la clef
                                   (byte) 0xA0,(byte) 0xA1, (byte) 0xA2,
                                   (byte) 0xA3,(byte) 0xA4, (byte) 0xA5};
                                     

           //System.out.println("LOAD KEY: " + send(baLoadKey, channel));

           // Authenticate
           byte[] baAuth = new byte[7];

           //FF 86 00 00 05 01 00 04 60 00

           baAuth = new byte[]{(byte) 0xFF, (byte) 0x86, (byte) 0x00, (byte) 0x00, (byte) 0x05,
                               (byte) 0x01, (byte) 0x00, (byte) 0x04, (byte) 0x61, (byte) 0x00};
                               // Version 0x01, 0x00, BLOCK number, Key type 0x60 ou 0x61, Key number

           System.out.println("AUTHENTICATE: " + send(baAuth, channel));
           // If successfull, will output 9000

           // Read Serial
           byte[] baRead = new byte[6];

           baRead = new byte[]{(byte) 0xFF, (byte) 0xB0, (byte) 0x00,
                               (byte) 0x04, (byte) 0x10};

           ResponseAPDU blockValue =  channel.transmit(new CommandAPDU(baRead));
           System.out.println("READ: " + Util.byteArrayToHexString(blockValue.getData()));
           System.out.println("READ: " + new String(blockValue.getData(),"ISO-8859-1"));



           // Read Serial
           byte[] baWrite = new byte[21];

           baWrite = new byte[]{(byte) 0xFF, (byte) 0xD6, (byte) 0x00,
                               (byte) 0x04, (byte) 0x10
                               // datas//48 65 6c 6c 6f 20 54 6f 75 72 73 20 4a 75 67 21
                        , (byte) 0x42, (byte) 0x79, (byte) 0x65, (byte) 0x20, (byte) 0x54, (byte) 0x6F, (byte) 0x75, (byte) 0x72,
                          (byte) 0x73, (byte) 0x20, (byte) 0x4A, (byte) 0x75, (byte) 0x67, (byte) 0x20, (byte) 0x21, (byte) 0x20
           };

           //System.out.println("WRITE: " + send(baWrite, channel));

           
//           JFrame fenetre = new JFrame("Merci le jug");
//           fenetre.setSize(800,600); // on définit la taille
//           fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //mode de fermeture
//           fenetre.setVisible(true);  //on rend la frame visible

           JFrame fenetre = new FrameJUG(new String(blockValue.getData(),"ISO-8859-1"));
           fenetre.setVisible(true);


       } catch (Exception ex) {
           ex.printStackTrace();
       }
   }

   public String send(byte[] cmd, CardChannel channel) {

       String res = "";

       byte[] baResp = new byte[258];
       ByteBuffer bufCmd = ByteBuffer.wrap(cmd);
       ByteBuffer bufResp = ByteBuffer.wrap(baResp);

       // output = The length of the received response APDU
       int output = 0;

       try {

           output = channel.transmit(bufCmd, bufResp);

       } catch (CardException ex) {
           ex.printStackTrace();
       }

       for (int i = 0; i < output; i++) {
           res += String.format("%02X", baResp[i]);
           // The result is formatted as a hexadecimal integer
       }
       
       return res;
   }

   public static void main(String[] args) {
       new ReadMifare();
   }
}