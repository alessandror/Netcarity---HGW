/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package BT;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ALessandroR
 */
public class CardGuard_oxypro {
    //00A096169D05 found name OxyPro
    //service SPP slave found btspp://00A096169D05:1;authenticate=false;encrypt=false;master=false
    
    public RfComm curRfComm = null;
    public int byteRead = 0;
    public byte msg_in[] = new byte[25];
    public byte msg_out[] = new byte[2];
    
    public CardGuard_oxypro(String oxyBtAddr){
        curRfComm = new RfComm(oxyBtAddr);
        
    }
   
    public int getByteRead(){
        return byteRead;
    }
    
    public byte[] devInfo(){
        
        msg_out[0]= 0x20;
        msg_out[1]= 0x20;
        
        return sendCmd();

   }
    public byte[] devStatus(){
        
        msg_out[0]= 0x51;
        msg_out[1]= 0x51;
        
        return sendCmd();
   }
    
    public byte[] startOxyProTest(){
        
        msg_out[0]= 0x40;
        msg_out[1]= 0x40;
        
        return sendCmd();
        
    }
    
    public void stopOxyProTest(){
        //stop test
        msg_out[0]= 0x41;
        msg_out[1]= 0x41;
        
        curRfComm.sndMsg(msg_out);
    
        //power off
        msg_out[0]= 0x52;
        msg_out[1]= 0x52;
        
        curRfComm.sndMsg(msg_out);
    
    }
    
    
    public byte[] sendCmd(){
        
        curRfComm.sndMsg(msg_out);
        
        try {

            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(CardGuard_oxypro.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        msg_in = curRfComm.rcvMsg();
        byteRead = curRfComm.getByteRead();
        if (byteRead > 0){
            if( checkAckSendMsg(msg_in,msg_out[0])){
                return msg_in;
            }else{
                curRfComm.endCom();
                return null;
            }
        }else{
                curRfComm.endCom();
                return null;
        }
        
    }
    public boolean checkAckSendMsg(byte msg[], byte cmd){
        if (Integer.toString(msg[0] & 0xFF, 16).equalsIgnoreCase("fc")){
            if(Integer.toString(msg[1] & 0xFF, 16).equalsIgnoreCase(Integer.toString(cmd & 0xFF, 16))){
                return true;
            }else{
                return false;
            }
            
        }else{
            return false;
        }
    }
    
}
