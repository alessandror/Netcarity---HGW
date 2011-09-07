package HAL;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author alessandror
 */
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

