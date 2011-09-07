/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package HAL;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author alessandror
 */
public class SensorDataLogic {
private Logger sensorDataLogicLogger = null;
private String curAssociatedLogic = null;

    //TODO check possible logics
    public static enum PossibleLogicCases {
        checkalarm,
        checkvalue
    }

    SensorDataLogic(String associatedLogic) {
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                       System.getProperty("file.separator") +
                                       "configs/log4j.properties");
        sensorDataLogicLogger = Logger.getLogger(CommTech.class);
        sensorDataLogicLogger.info("--> SensorDataLogic <--");
        sensorDataLogicLogger.info("--> SensorDataLogic <--" + associatedLogic);
        curAssociatedLogic = associatedLogic;
    }

    void process(byte[] curDataIn ) {
        sensorDataLogicLogger.info("--> process <--");
        switch( PossibleLogicCases.valueOf( curAssociatedLogic.toLowerCase()) ){
            case checkalarm:
                 //TODO logic case
            break;
            case checkvalue:
                //TODO logic case
            break;
        }
    }
}
