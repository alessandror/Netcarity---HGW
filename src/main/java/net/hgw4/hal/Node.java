package net.hgw4.hal;
import java.util.*;




/**
 * a node is composed of dataendpoints, each endpoint has a commtech
 * @author AlessandroR
 */
public class Node {


    private String nodeId;


    private String nodeType;

 
    private String nodeDescription;
   
    /**
     * list of nodes
     */
    public ArrayList<DataEndPoint> dataEndPointsList;
        
  
    /**
     *
     */
    public Node() {
        this.dataEndPointsList = new ArrayList();
    }


    /**
     *
     * @return
     */
    public String getNodeDescription() {
        return this.nodeDescription;
    }

 
    /**
     *
     * @param val
     */
    public void setNodeDescription(String val) {
        this.nodeDescription = val;
    }

  
    /**
     *
     * @return
     */
    public String getNodeId() {
        return this.nodeId;
    }

 
    /**
     *
     * @param val
     */
    public void setNodeId(String val) {
        this.nodeId = val;
    }


    /**
     *
     * @return
     */
    public String getNodeType() {
        return this.nodeType;
    }


    public void setNodeType (String val) {
        this.nodeType = val;
    }

  
    /**
     *
     * @return
     */
    public List<DataEndPoint> getEndPointsList() {
        return dataEndPointsList;
    }

    /**
     *
     * @param val
     */
    public void setEndPointList(DataEndPoint val) {
        dataEndPointsList.add(val);
    }

}

