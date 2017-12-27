/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demojug;

/**
 *
 * @author geoffroy.vibrac
 */
public class Util {

    public static String byteArrayToHexString(byte in[]) {

        byte ch = 0x00;

        int i = 0;

        if (in == null || in.length <= 0)

            return null;


        String pseudo[] = {"0", "1", "2","3", "4", "5", "6", "7", "8","9", "A", "B", "C", "D", "E","F"};

        StringBuffer out = new StringBuffer(in.length * 2);
        while (i < in.length) {

            ch = (byte) (in[i] & 0xF0);
            ch = (byte) (ch >>> 4);
            ch = (byte) (ch & 0x0F);
            out.append(pseudo[ (int) ch]);
            ch = (byte) (in[i] & 0x0F);
            out.append(pseudo[ (int) ch]);
            i++;
        }

        String rslt = new String(out);

        return rslt;

    }

}
