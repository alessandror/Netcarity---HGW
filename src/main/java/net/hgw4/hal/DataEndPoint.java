package net.hgw4.hal;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * each node has a set of dataendpoints for attached sensors and actuators
 * @author AlessandroR
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
     *
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

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.ECD19088-1FB9-CF31-AD5F-FC1E86C15B12]
    // </editor-fold> 
    /**
     *
     * @return
     */
    public String getEndPointId() {
        return dataEndPointId;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.DCCACEAE-C77C-4BCC-EB6E-CCD65D574622]
    // </editor-fold> 
    /**
     *
     * @param val
     */
    public void setEndPointId(String val) {
        dataEndPointId = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.22E98DE1-C1B8-ED03-A191-D95B4ED25E6A]
    // </editor-fold> 
    /**
     *
     * @return
     */
    public String getEndPointDescr() {
        return dataEndPointDescr;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.09E7C6D8-75C6-6AD8-8974-F91E49CAA979]
    // </editor-fold> 
    /**
     *
     * @param val
     */
    public void setEndPointDescr(String val) {
        dataEndPointDescr = val;
    }
    
    /**
     *
     * @param val
     */
    public void setEndPointCommTechId(String val) {
        dataEndPointCommTechId = val;
    }
    
    /**
     *
     * @return
     */
    public String getEndPointCommTechId() {
        return dataEndPointCommTechId;
    }

    public void setEndPointCoordinates(String string) {
        dataEndPointCoordinates = string;
    }

    public void setEndPointDataRangeMax(String string) {
        dataEndPointEndPointDataRangeMax = string;
    }

    public void setEndPointDataRangeMin(String string) {
        dataEndPointEndPointDataRangeMin = string;
    }

    public void setEndPointPosition(String string) {
        dataEndPointPosition = string;
    }

    public void setEndPointPriority(String string) {
        dataEndPointPriority = string;
    }

    public String getEndPointCoordinates() {
        return dataEndPointCoordinates;
    }

    public String getEndPointDataRangeMax() {
        return dataEndPointEndPointDataRangeMax;
    }

    public String getEndPointDataRangeMin() {
        return dataEndPointEndPointDataRangeMin;
    }

    public String getEndPointPosition() {
        return dataEndPointPosition;
    }

    public String getEndPointPriority() {
        return dataEndPointPriority;
    }




}

