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
