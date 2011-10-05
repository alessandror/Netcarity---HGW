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


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class DataQueue {
private SensorDataLogic curSensorDataLogic = null;
private CircularBuffer curCircularBuffer = null;
private Logger dataQueueLogger = null;

    public DataQueue(String associatedLogic, int lenCircularBuffer) {
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                       System.getProperty("file.separator") +
                                       "configs/log4j.properties");
        dataQueueLogger = Logger.getLogger(CommTech.class);
        dataQueueLogger.info("--> DataQueue <--");
        curSensorDataLogic = new SensorDataLogic(associatedLogic);
        curCircularBuffer = new CircularBuffer(lenCircularBuffer);
        
    }
    
    /** 
     * insert data in queue
     * @param data
     */
    public void insertData( byte[] curDataIn ){
        dataQueueLogger.info("--> insertData <--");
        //inserisco il dato comunque, logic lo gestisce in maniera autonoma
        processSensorDataLogic(curDataIn);
        curCircularBuffer.add(curDataIn);

    }

    /**
     *
     * @return JSONObject
     */
    public byte[] getData(){
        dataQueueLogger.info("--> getData <--");
        return (byte[]) curCircularBuffer.get();
    }

    private void processSensorDataLogic( byte[] curDataIn) {
        dataQueueLogger.info("--> processSensorDataLogic <--");
        curSensorDataLogic.process(curDataIn);
    }
}

