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

/**
 * each node has a set of dataendpoints for attached sensors and actuators
 * 
 */
public class DataEndPoint {
    /**
     *
     */
    public CommTech curCommTech;
    private Logger dataEndPointLogger = null;
    private String dataEndPointCommTechId = null;
    private String dataEndPointDescr = null;
    private String dataEndPointId = null;
    private String dataEndPointPosition = null;
    private String dataEndPointCoordinates = null;
    private String dataEndPointEndPointDataRangeMin = null;
    private String dataEndPointEndPointDataRangeMax = null;
    private String dataEndPointPriority = null;
    
    /**
     * create end point
     * @param val
     */
    public DataEndPoint(CommTech val) {
        PropertyConfigurator.configure(System.getProperty("user.dir") +
                                     System.getProperty("file.separator") +
                                     "configs/log4j.properties");
        dataEndPointLogger = Logger.getLogger(DataEndPoint.class);
        dataEndPointLogger.info("--> DataEndPoint <--");

        this.curCommTech = val;
    }

    
    /**
     * get endpoint id
     * @return dataEndPointId
     */
    public String getEndPointId() {
        return dataEndPointId;
    }


    /**
     * set end point id
     * @param val
     */
    public void setEndPointId(String val) {
        dataEndPointId = val;
    }


    /**
     * get end point description
     * @return dataEndPointDescr
     */
    public String getEndPointDescr() {
        return dataEndPointDescr;
    }

    /**
     * set end point description
     * @param val
     */
    public void setEndPointDescr(String val) {
        dataEndPointDescr = val;
    }
    
    /**
     * set end poitn commtechid
     * @param val
     */
    public void setEndPointCommTechId(String val) {
        dataEndPointCommTechId = val;
    }
    
    /**
     * get end point comme tech id
     * @return dataEndPointCommTechId
     */
    public String getEndPointCommTechId() {
        return dataEndPointCommTechId;
    }
    
    /**
     * set end point coordinates
     * @param string 
     */
    public void setEndPointCoordinates(String string) {
        dataEndPointCoordinates = string;
    }

    /** set end poinr data range max
     * 
     * @param string 
     */
    public void setEndPointDataRangeMax(String string) {
        dataEndPointEndPointDataRangeMax = string;
    }
    
    /**
     * set end poinr data range min
     * @param string 
     */
    public void setEndPointDataRangeMin(String string) {
        dataEndPointEndPointDataRangeMin = string;
    }

    /**
     * set end point position
     * @param string 
     */
    public void setEndPointPosition(String string) {
        dataEndPointPosition = string;
    }

    /**
     * set end point priority 
     * @param string 
     */
    public void setEndPointPriority(String string) {
        dataEndPointPriority = string;
    }
    
    /**
     * get end poitn coordinates
     * @return dataEndPointCoordinates
     */
    public String getEndPointCoordinates() {
        return dataEndPointCoordinates;
    }
    
    /**
     * get ens point data range max
     * @return dataEndPointEndPointDataRangeMax
     */
    public String getEndPointDataRangeMax() {
        return dataEndPointEndPointDataRangeMax;
    }

    /**
     * get endpoitn data range min
     * @return dataEndPointEndPointDataRangeMin
     */
    public String getEndPointDataRangeMin() {
        return dataEndPointEndPointDataRangeMin;
    }
    
    /**
     * get end point position
     * @return dataEndPointPosition
     */
    public String getEndPointPosition() {
        return dataEndPointPosition;
    }
    
    /**
     * get end point priority
     * @return dataEndPointPriority
     */
    public String getEndPointPriority() {
        return dataEndPointPriority;
    }




}

