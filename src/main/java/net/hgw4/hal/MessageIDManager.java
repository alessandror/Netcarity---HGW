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
 * manages the creation of message id for sensor, actuators and events
 * 
 */
public class MessageIDManager {
private int sensorMsgIdValueCounter = 0;
private int actuatorMsgIdValueCounter = 0;
private int eventMsgIdValueCounter = 0;

    MessageIDManager() {
        

    }
    /**
     * get sensor msg id
     * @return sensorMsgIdValueCounter
     */
    public int getSensorMsgId(){
        if (sensorMsgIdValueCounter > Integer.MAX_VALUE )sensorMsgIdValueCounter=0;
        sensorMsgIdValueCounter+=1;
        return sensorMsgIdValueCounter;
    }
    
    /**
     * get actuator msg id
     * @return actuatorMsgIdValueCounter
     */
    public int getActuatorMsgId(){
        if (actuatorMsgIdValueCounter > Integer.MAX_VALUE )actuatorMsgIdValueCounter=0;
        actuatorMsgIdValueCounter+=1;
        return actuatorMsgIdValueCounter;
    }
    
    /**
     * get event ,msg id
     * @return eventMsgIdValueCounter
     */
    public int getEventMsgId(){
        if (eventMsgIdValueCounter > Integer.MAX_VALUE )eventMsgIdValueCounter=0;
        eventMsgIdValueCounter+=1;
        return eventMsgIdValueCounter;
    }
    
    /**
     * set sensor msg id
     * @param id
     */
    public void setSensorMsgId(int val){
        sensorMsgIdValueCounter = val;
    }
    
    /**
     * set actuator msg id
     * @param id 
     */
    public void setActuatorMsgId(int val){
        actuatorMsgIdValueCounter = val;
    }
    
    /**
     * set event msg id 
     * @param val 
     */
    public void setEventMsgId(int val){
        eventMsgIdValueCounter = val;
    }
}
