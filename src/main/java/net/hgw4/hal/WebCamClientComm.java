/*
Copyright 2011 Alex Redaelli

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package net.hgw4.hal;

/**
 * manages http commands for axis webcam
 * 
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONException;
import org.json.JSONObject;

//10.0.0.6:8080

public class WebCamClientComm implements PossibleMsgCmds {

    private String ip = null;
    private Logger WebCamClientCommLogger;
    
    public WebCamClientComm(String ipWebcam){
       PropertyConfigurator.configure(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "configs/log4j.properties");
       WebCamClientCommLogger = Logger.getLogger(Hal.class);
       WebCamClientCommLogger.info("--> WebCamClientComm <--");
       ip = ipWebcam;

    }

    @Override
    public String onCmd(String val) {
        setPort(1,1);
        setPort(1,0); //for enabling the call button
        return "ok";
    }

    @Override
    public String offCmd(String val) {
        setPort(1,1);
        setPort(1,0);
        return "ok";
    }

    @Override
    public String onPercCmd(String val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String offPercCmdString(String val) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public JSONObject sendCmd(int curCmd) {
        JSONObject response = null;
            String  curCommandStatus = new String();
            switch(curCmd){
                case 0: //on
                    curCommandStatus = onCmd("");
                break;
                case 1: //off
                     curCommandStatus = offCmd("");
                break;
                case 2: //onperc
                     curCommandStatus = "no-operative";
                break;
                case 3: //offperc
                     curCommandStatus = "no-operative";
                break;
            }
          //generate json response
          String msgRaw = new String("{"+
                                     "'value':'" + curCommandStatus +
                                      "'}");
          
            try {
                response = new JSONObject(msgRaw);
            } catch (JSONException ex) {
                WebCamClientCommLogger.error(ex);
            }
              return response;
    }


    public byte[] getData() {
        byte[] result;

        int buttonStatus = checkPortStatus(1);

        return intToByteArray(buttonStatus);
    }


/**
 * check if input 1 is pressed == 1 ring button
 * @param port
 * @return
 */
    public int checkPortStatus(int port) {
        
        //http://test:test@10.0.0.6:8080/axis-cgi/io/input.cgi?check=1

        try {
            String curport = Integer.toString(port);
            URL url = new URL("http://" + ip + "/axis-cgi/io/input.cgi?check=" + curport.toString()); //reply with input1=1 o 0
            String userPassword = "root" + ":" + "netcarity.7x1";
            String encoding = new sun.misc.BASE64Encoder().encode (userPassword.getBytes());
            URLConnection uc = url.openConnection();
            uc.setRequestProperty ("Authorization", "Basic " + encoding);
            InputStream content = (InputStream)uc.getInputStream();
            BufferedReader in   =  new BufferedReader (new InputStreamReader (content));

            String str;
            str = in.readLine();
                if (str.contentEquals("input1=1")){
                        return 1; //high
                }else if(str.contentEquals("input1=0")){

                        return 0; //low
                }
            /*
            while ((str = in.readLine()) != null) { // str is one line of text; readLine() strips the newline character(s)
                WebCamClientCommLogger.info("while checkPortStatus  "   );
                         
                if (str.contentEquals("input1=1")){
                        return 1; //high
                }else if(str.contentEquals("input1=0")){
                        return 0; //low
                }
                
            }
            */

            in.close();

        } catch (MalformedURLException ex) {
            WebCamClientCommLogger.error(ex);
        } catch (IOException ex) {
            WebCamClientCommLogger.error(ex);
        }
        return -1;
    }

    
    /**
     * resets status of ring button relè
     * @param port
     * @param status
     */
    public void setPort(int port,int status){
        try {
            String curPort = Integer.toString(port);
            String curStatus = new String();
           if (status ==1 ){
               curStatus ="/"; //active
           }else{
               curStatus ="\\"; //inactive
           }
            URL url = new URL("http://" + ip + "/axis-cgi/io/output.cgi?action=" + curPort + ":" + curStatus);
            String userPassword = "root" + ":" + "netcarity.7x1";
            String encoding = new sun.misc.BASE64Encoder().encode (userPassword.getBytes());
            URLConnection uc = url.openConnection();
            uc.setRequestProperty ("Authorization", "Basic " + encoding);

            InputStream content = (InputStream)uc.getInputStream();
            BufferedReader in   =  new BufferedReader (new InputStreamReader (content));
            String str;
            str = in.readLine();
            in.close();
 
        } catch (MalformedURLException ex) {
             WebCamClientCommLogger.error(ex);
        } catch (IOException ex) {
             WebCamClientCommLogger.error(ex);
        }

    }

    public  byte[] intToByteArray(int value) {
            byte[] b = new byte[4];
            for (int i = 0; i < 4; i++) {
                int offset = (b.length - 1 - i) * 8;
                b[i] = (byte) ((value >>> offset) & 0xFF);
            }
            return b;
        }

    
}
