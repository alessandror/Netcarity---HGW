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

import com.rapplogic.xbee.api.XBeeException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.rapplogic.xbee.api.ApiId;
import com.rapplogic.xbee.api.AtCommand;
import com.rapplogic.xbee.api.AtCommandResponse;
import com.rapplogic.xbee.api.XBee;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRxResponse;
import com.rapplogic.xbee.util.ByteUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class ZNetComm {

    private final static Logger log = Logger.getLogger(ZNetComm.class);
    public byte[] curData = new byte[100];
    private List curZNetNodes;
    private final String curPort;
    private final int curBaud;
    private Thread thread0;
    
    public ZNetComm(String port, String Baud)  {
         PropertyConfigurator.configure(System.getProperty("user.dir") +
                         System.getProperty("file.separator") +
                         "configs"+ System.getProperty("file.separator") +"log4j.properties");
        log.info("in ZNetComm ");
       
        curPort = port;
        curBaud = Integer.parseInt(Baud);
        for (int i0=0 ;i0<100;i0++){
            curData[i0] =(byte) 0xFF;
        }
        thread0 = new Thread(new ZnetRcvrThread(),"ZnetRcvrThread");
        thread0.start();
        
    }

    public class ZnetRcvrThread implements Runnable {

        @Override
        public void run (){
            XBee xbee = new XBee();
            try {
                // replace with the com port of your receiving XBee (typically your end device)
                //[ALE]
                xbee.open(curPort, curBaud); //[ALE]
            } catch (XBeeException ex) {
                log.error(ex);
            }

            while (true) {

                try {
                    // we wait here until a packet is received.
                    XBeeResponse response = xbee.getResponse();

                    log.info("received response " + response.toString());

                    if (response.getApiId() == ApiId.ZNET_RX_RESPONSE) {
                        // we received a packet from ZNetSenderTest.java
                        ZNetRxResponse rx = (ZNetRxResponse) response;
                        curData =  makeByteArray( rx.getData() );
                        log.info("Received RX packet, option is " + rx.getOption() + ", sender 64 address is " + ByteUtils.toBase16(rx.getRemoteAddress64().getAddress()) + ", remote 16-bit address is " + ByteUtils.toBase16(rx.getRemoteAddress16().getAddress()) + ", data is " + ByteUtils.toBase16(rx.getData()));

                        // optionally we may want to get the signal strength (RSSI) of the last hop.
                        // keep in mind if you have routers in your network, this will be the signal of the last hop.
                        AtCommand at = new AtCommand("DB");
                        xbee.sendAsynchronous(at);
                        XBeeResponse atResponse = xbee.getResponse();

                        if (atResponse.getApiId() == ApiId.AT_RESPONSE) {
                            // remember rssi is a negative db value
                            log.info("RSSI of last response is " + -((AtCommandResponse) atResponse).getValue()[0]);
                        } else {
                            // we didn't get an AT response
                            log.info("expected RSSI, but received " + atResponse.toString());
                        }
                    } else {
                        log.debug("received unexpected packet " + response.toString());
                    }

                } catch (Exception e) {
                    log.error(e);
                }
//TODO: meccanismo di stop dei thread di basso livello
//                finally {
//                    xbee.close();
//                }
           }
           
        }
    }
    

    byte[] getData() {
        log.info("curdata: " + curData);
        return curData;
    }

    private byte[] makeByteArray(int[] data ) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(data);

        byte[] array = byteBuffer.array();

        return array;
    }

}
