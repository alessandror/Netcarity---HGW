/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BT;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;


/**
 * SPP profile implementation
 * 
 * @author AlessandroR
 */
public class RfComm {
    public String curBtAddr = "";
    public StreamConnection connection = null;
    public OutputStream out = null;
    public InputStream in = null;
    public int byte_read = 0;
    public byte msgRcvd[] = new byte[100];

    
    /**
     * 
     * @param btaddr
     */
    public RfComm(String btaddr) {
        curBtAddr = btaddr;
        initCom();
         
    }
    
    
    /**
     * init comm
     */
    public void initCom() {
        try {
                //btgoep://0019639C4007:6 for OBEX or btspp://00A096169D05:1;authenticate=false;encrypt=false;master=false for serial
                connection = (StreamConnection) Connector.open("btspp://" + curBtAddr + ":1");
                out = connection.openOutputStream();
                in = connection.openInputStream();
                
                } catch (IOException ex) {
                Logger.getLogger(RfComm.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    
    /**
     *  end comm
     */
    public void endCom(){
        if (in != null && out !=null){
            try {
                in.close();
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(RfComm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    /**
     *  send a message
     * @param msg
     */
    public void sndMsg(byte[] msg){
        try {
            System.out.println("--> send msg:");
            printBytes(msg);
            System.out.println("");
            out.write(msg); 
        } catch (IOException ex) {
            Logger.getLogger(RfComm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * get number of received bytes
     * @return
     */
    public int getByteRead(){
      System.out.println("--> byte read:" + byte_read); 
      System.out.println("");
      return byte_read;    
    }

    
    /**
     * receive a message
     * @return
     */
    public byte[] rcvMsg(){
        try {
          byte_read = in.read(msgRcvd);
          System.out.println("--> recv msg:");
          printBytes(msgRcvd);
          System.out.println("");
        } catch (IOException ex) {
            Logger.getLogger(RfComm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return msgRcvd;
    }
    
    public void printBytes(byte toprint[]){
        for (int i = 0; i < toprint.length; i++) {
            System.out.print(Integer.toString(toprint[i] & 0xFF, 16));
        }
    }
    
    
    
}
