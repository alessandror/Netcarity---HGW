package HAL;

/**
 * manages the creation of message id for sensor, actuators and events
 * @author AlessandroR
 */
public class MessageIDManager {
private int sensorMsgIdValueCounter = 0;
private int actuatorMsgIdValueCounter = 0;
private int eventMsgIdValueCounter = 0;


    /**
     * 
     * @param init
     */
    MessageIDManager() {
        

    }

    public int getSensorMsgId(){
        if (sensorMsgIdValueCounter > Integer.MAX_VALUE )sensorMsgIdValueCounter=0;
        sensorMsgIdValueCounter+=1;
        return sensorMsgIdValueCounter;
    }

    public int getActuatorMsgId(){
        if (actuatorMsgIdValueCounter > Integer.MAX_VALUE )actuatorMsgIdValueCounter=0;
        actuatorMsgIdValueCounter+=1;
        return actuatorMsgIdValueCounter;
    }

    public int getEventMsgId(){
        if (eventMsgIdValueCounter > Integer.MAX_VALUE )eventMsgIdValueCounter=0;
        eventMsgIdValueCounter+=1;
        return eventMsgIdValueCounter;
    }

    public void setSensorMsgId(int val){
        sensorMsgIdValueCounter = val;
    }

    public void setActuatorMsgId(int val){
        actuatorMsgIdValueCounter = val;
    }

    public void setEventMsgId(int val){
        eventMsgIdValueCounter = val;
    }
}
