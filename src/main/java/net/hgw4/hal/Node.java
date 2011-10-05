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
import java.util.*;

/**
 * a node is composed of dataendpoints, each endpoint has a commtech
 * 
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

