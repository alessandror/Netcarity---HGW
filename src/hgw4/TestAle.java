
package hgw4;

import HAL.*;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author AlessandroR
 */
public final class TestAle implements Observer {
    private Hal curHal;
    private Logger testAleLogger;
    private TempSens0 tempKitchen;
    private TempSens0 tempBedroom;
    private JSONObject curDataTempKitchenSens;
    private JSONObject curDataTempBedroomSens;
    private JSONObject curDataTempSerialSens;
    private JSONObject curDataZigBee;
    private JSONObject curWebCamRsp=null;
    private JSONObject curWebCamRsp1;
    private SerialComm curSerialComm;

    public TimerTask task0;
    public Timer timer0;
    public TimerTask task1;
    public Timer timer1;
    public TimerTask task2;
    public Timer timer2;
    private int cmd = 0;
    private int toggle_flag = 0;
    private int timerSleep = 100; //default timer sleep
    private Timer timer3;
    private Timer timer4;
    private Timer_testIpCamButton task3;
    //private final Timer_getDataFromZigbee task4;
    private String data;
    public BufferedWriter out=null;
    public EventHandler curEventHandlerTestAle = null;
    
    public TestAle()  {
        PropertyConfigurator.configure( System.getProperty("user.dir") +
                                      System.getProperty("file.separator") +
                                      "configs"+ System.getProperty("file.separator") +"log4j.properties");
        testAleLogger = Logger.getLogger(TestAle.class.getName());
        testAleLogger.info("--> TestAle <--");
        
        curHal = new Hal();
        
        //createTempKitchenSensor();
        //createTempBedroomSensor();

        //create and start sych eventhandler sending refernce to observable class
        curHal.startInternalEventHandler();
        //configure this class to receive events
        setExternalEventHandler();
        
        //test sensori
        curHal.startAllDataCollecting();
        timer0 = new Timer("poll data from temp sensor");
        task0 = new Timer_getDataFromSerial();
        timer0.schedule(task0,1000,1000);
        
//        timer0 = new Timer("poll data from fake sensors");
//        task0 = new Timer_getDataFromFakeSensors();
//        timer0.schedule(task0,1000,1000);
        
        
        //test attuatore
        //curHal.startAllDataCommander();

        //test cicle barionet relay_1
//        timer0 = new Timer("test barionet relay_1");
//        task0 = new Timer_setDataForActuators();
//        timer0.schedule(task0,1000,1000);


        /*
        try {
            //test log zigbee
            //out = new BufferedWriter(new FileWriter("/home/alessandror/zigbeedata.txt", true))
            out = new BufferedWriter(new FileWriter(new File("/home/alessandror/zigbeedata.txt")));;
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(TestAle.class.getName()).log(Level.SEVERE, null, ex);
        }
            timer4 = new Timer("poll data from znet");
            task4 = new Timer_getDataFromZigbee();
            timer4.schedule(task4,5000,5000);
         */
    }

    public void setExternalEventHandler(){
         curHal.startExternalEventHandler(this);
    }
    
    public void update(Observable obs, Object x) {
      testAleLogger.info("update(" + obs + "," + x + ");");
    }


    public class Timer_testIpCamButton extends TimerTask {
            public void run() {
            testAleLogger.info("TestAle TimerTask Timer_testIpCamButton() ");
            testIpCamButton();
            }
    }

    public class Timer_getDataFromFakeSensors extends TimerTask {
            public void run() {
                testAleLogger.info("TestAle TimerTask getDataFromFakeSensors() ");
                getDataFromSensors();
            }
    }
    
    public class Timer_setDataForActuators extends TimerTask {
            public void run() {
            testAleLogger.info("TestAle TimerTask setDataForActuators() ");
            setDataForActuators();
            }
    }
    
    public class Timer_getDataFromSerial extends TimerTask {
            public void run() {
            testAleLogger.info("TestAle TimerTask getDataFromSerial() ");
                getDataFromSerial();
            }
    }
    public class Timer_getDataFromZigbee extends TimerTask {
        public void run() {
            testAleLogger.info("TestAle TimerTask getDataFromZigbee() ");
                getDataFromZigbee();
            }
    }

    private void getDataFromZigbee() {
        testAleLogger.info("TestAle in getDataFromZigbee() ");
        curDataZigBee =  curHal.pollSensor("0001", "0001");
        try {
             testAleLogger.info("sensor:0001");
             testAleLogger.info("endpoint:0001");
             testAleLogger.info("msgid:" + curDataZigBee.getString("msgid"));
             testAleLogger.info("timestamp:" + curDataZigBee.getString("timestamp"));
             testAleLogger.info("data:" + curDataZigBee.getString("data") );
            try {
                out.write(curDataZigBee.getString("data")+"\n");
                out.flush();
            } catch (IOException ex) {
                testAleLogger.error(ex);
            }
        } catch (JSONException ex) {
            testAleLogger.error(ex);
        }
    }
    
    
    
    public void getDataFromSerial(){
        testAleLogger.info("------------------ Serial Temp Sensor ------------------------");
        curDataTempSerialSens =  curHal.pollSensor("0003", "0001");
        
        try {
           if (curDataTempSerialSens != null){
            testAleLogger.info("sensor:0003");
            testAleLogger.info("endpoint:0001");
            testAleLogger.info("msgid:" + curDataTempSerialSens.getString("msgid"));
            testAleLogger.info("timestamp:" + curDataTempSerialSens.getString("timestamp"));

            String value = curDataTempSerialSens.getString("data");
                if (value.equalsIgnoreCase("noData")){
                    testAleLogger.info("data:" +curDataTempSerialSens.getString("data"));
                }else{

                    float curTemp = (float) 0.00;
                    int i00 = Integer.valueOf(value, 16).intValue();
                    curTemp = (float)i00;
                    float curTempValue = (float) 0.00;
                    //curTempValue = (float) ((float) -0.08 * curTemp + 68.0);
                    curTempValue = (float) ((float) -0.08 * curTemp + 66.7);
                    testAleLogger.info("data:" + Float.toString(Math.abs(curTempValue)));
                }
           }else{
               testAleLogger.info("---> no data from real serial temp sens  <---");
           }

        } catch (JSONException ex) {
            testAleLogger.error(ex);
        }
        System.out.println(curDataTempSerialSens.toString());
    }

    public void getDataFromSensors(){
       testAleLogger.info("------------------Sensor 1------------------------");
       curDataTempKitchenSens =  curHal.pollSensor("0001", "0001");
       testAleLogger.info("print temp data ");
       try {
           if (curDataTempKitchenSens != null){
            testAleLogger.info("sensor:0001");
            testAleLogger.info("endpoint:0001");
            testAleLogger.info("msgid:" + curDataTempKitchenSens.getString("msgid"));
            testAleLogger.info("timestamp:" + curDataTempKitchenSens.getString("timestamp"));
            testAleLogger.info("data:" + curDataTempKitchenSens.getString("data"));
           
           }else{
               testAleLogger.info("---> no data from fake sens 0 <---");
           }
           
        } catch (JSONException ex) {
            testAleLogger.error(ex);
        }
        testAleLogger.info(curDataTempKitchenSens.toString());
        
       curDataTempBedroomSens =  curHal.pollSensor("0001", "0002");
       testAleLogger.info("------------------Sensor 2------------------------");
       testAleLogger.info("print temp data ");
       try {
           if (curDataTempBedroomSens != null){
            testAleLogger.info("sensor:0001");
            testAleLogger.info("endpoint:0002");
            testAleLogger.info("msgid:" + curDataTempBedroomSens.getString("msgid"));
            testAleLogger.info("timestamp:" + curDataTempBedroomSens.getString("timestamp"));
            testAleLogger.info("data:" + curDataTempBedroomSens.getString("data"));
           
           }else{
               testAleLogger.info("---> no data from fake sens 1 <---");
           }
           
        } catch (JSONException ex) {
            testAleLogger.error(ex);
        }
        System.out.println(curDataTempBedroomSens.toString());
    }
    
    public void setDataForActuators(){
 
         testAleLogger.info("---> start test barionet ");
        //try {
            
            JSONObject commandResult = null;
            //simulo un relè toggle
            if (toggle_flag == 0) {
                cmd = 0;
                toggle_flag = 1;
            } else {
                cmd = 1;
                toggle_flag = 0;
            }

           
            testAleLogger.info("test barionet->>msg");
            testAleLogger.info("command:" + cmd);
            commandResult = curHal.exeCmd("0001", "0001", cmd);
            testAleLogger.info("test barionet->>msgresult");
            testAleLogger.info(commandResult);
            /*
            if (commandResult != null){
                System.out.println("<---msgid:" + commandResult.getString("msgid"));
                System.out.println("<---timestamp:" + commandResult.getString("timestamp"));
                System.out.println("<---value:" + commandResult.getString("value"));
            }else{
                System.out.println("no resp by now!");
            }
            
        } catch (JSONException ex) {
            Logger.getLogger(TestAle.class.getName()).log(Level.SEVERE, null, ex);
        }*/
      testAleLogger.info("--> end test barionet ");
    }
    
    public void testIpCamButton(){

        testAleLogger.info("---> start test ipcam ");

        //check status
        try {
            curWebCamRsp1 = curHal.pollSensor("0002", "0001");
            testAleLogger.info("test ipcam poll sensor rsp: " + curWebCamRsp1.getString("data"));
        
            if (curWebCamRsp1.getString("data").equalsIgnoreCase("1") == true) {
                testAleLogger.info("--> RING! <-- ");
                //reset status
                curWebCamRsp = curHal.exeCmd("0002", "0001", 0); //0 = on basta madare on per fare ciclo di reset del rele
                //System.out.println("test ipcam exe cmd rsp: " + curWebCamRsp.toString() );
            }
            
        } catch (JSONException ex) {
            testAleLogger.error(ex);
        }

       testAleLogger.info("<--- end test ipcam");
        
    }
    
    /**
     * crea il fake sensor
     */
    public void createTempKitchenSensor() {
      testAleLogger.info("createTempKitchenSensor() ");
        tempKitchen = new TempSens0(60000,"0001");
    }
    
    private void createTempBedroomSensor() {
        testAleLogger.info("createTempBedroomSensor() ");
        tempBedroom = new TempSens0(60001,"0002");
    
    }

}
