package HAL;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import javax.comm.*;
import java.io.*;

/**
 * init real serial on pc hardware
 * @author AlessandroR
 */
public class SerialComm {
    private static final int BUF0 = 1024;
    private static final int BUF1 = 1024; 
    private static final int LEVEL0 = 50; //len of desired message > 3*max pdu
    private String curPort = null;
    private int curBaud = 9600;//default
    private Logger SerialCommLogger;
    public byte[] bufOut = new byte[BUF0];
    public byte[] bufIn = new byte[BUF0];
    public byte[] bufInTot = new byte[BUF1];
    public byte[] bufInToReturn = new byte[BUF1];

    private CommPort commPort;
    private int lenBufIn = 0;
    private int numMemebersListDataBufferIn = 0;
    private Thread thread0;
    private Thread thread1;
    
    public SerialComm(String port, String Baud){
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "configs/log4j.properties");
        SerialCommLogger = Logger.getLogger(SerialComm.class);
        SerialCommLogger.info("--> SerialComm <--");
        curPort = port;
        curBaud = Integer.parseInt(Baud);
        try {
            openPort();
        } catch (Exception ex) {
            SerialCommLogger.error(ex);
        }
    }
    
    public void openPort() throws Exception{

        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(curPort);
        
        if ( portIdentifier.isCurrentlyOwned() ){
            SerialCommLogger.info("Error: Port is currently in use");
        }else{
             commPort = portIdentifier.open(this.getClass().getName(),2000);

            if (commPort instanceof SerialPort ){
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(curBaud,
                                               SerialPort.DATABITS_8,
                                               SerialPort.STOPBITS_1,
                                               SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();

                thread0 = new Thread(new RcvDataThread(in),"SerialRcvThread");
                thread1 = new Thread(new SndDataThread(out),"SerialSendThread");
                thread0.start();
                thread1.start();
                
            }else{
                SerialCommLogger.info("Error: Only serial ports are handled ");
            }
        }
}
   
    public void closePort(){
        commPort.close();
    }
    
    public class RcvDataThread implements Runnable {
         InputStream in;
         
        public RcvDataThread ( InputStream in ){
            this.in = in;
        }

        @Override
        public void run (){
            lenBufIn = -1;
                while (true){
                    try {
                        fillBufferIn(in);
                        Thread.sleep(10);
                        //SerialCommLogger.info("ret val: " +ret);
                    } catch (InterruptedException ex) {
                        SerialCommLogger.error(ex);
                    }
                    //SerialCommLogger.info("ret val: " +ret);
                }
        }
    }

    public void fillBufferIn(InputStream in){
        int i0=0;
        try{
          if((lenBufIn = in.read(bufIn)) > 0){
           //SerialCommLogger.info("lenBufIn :  " + lenBufIn);
           //SerialCommLogger.info("numMemebersListDataBufferIn :  " + numMemebersListDataBufferIn);
              for ( i0 = numMemebersListDataBufferIn; i0 < numMemebersListDataBufferIn + lenBufIn; i0++ ){
                  
                  bufInTot[i0]=bufIn[i0-numMemebersListDataBufferIn];
                  
                  //SerialCommLogger.info("i0: " + i0);
                  String curdata = new String(bufInTot,numMemebersListDataBufferIn,(i0),"UTF-8");
                  //SerialCommLogger.info(" data:  " + curdata);
              }
              numMemebersListDataBufferIn = i0 ;

              if(numMemebersListDataBufferIn  > LEVEL0){
                String curdata = new String(bufInTot,0,(numMemebersListDataBufferIn),"UTF-8");
                //SerialCommLogger.info("tot data:  " + curdata);
                
                //data copy
                for (int i1 = 0;i1< numMemebersListDataBufferIn ;i1 ++){
                    bufInToReturn[i1]=bufInTot[i1];
                }
                numMemebersListDataBufferIn = 0;
              }
          }
        }catch ( IOException e ){
            e.printStackTrace();
        }
    }

    
    public class SndDataThread implements Runnable {
         OutputStream out;

    public SndDataThread ( OutputStream out ){
         this.out = out;
    }


        @Override
        public void run (){
            try{
               if(bufOut != null){
               this.out.write(bufOut);
               }
            }
            catch ( IOException e ){
                e.printStackTrace();
            }
            bufOut = null;
        }
    }


    public byte[] getData(){
        
        return bufInToReturn;
        
    }
    public void sendData(byte[] data){
        bufOut = data;
    }

    public int getLenInBuf(){
        return numMemebersListDataBufferIn;
    }
  
}
