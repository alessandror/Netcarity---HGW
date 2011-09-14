/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.hgw4.bt;

/**
 * testbed for cardGuard's devices
 * @author AlessandroR
 */
public class testCardGuard {
    
    public static void main(String[] args) {
       CardGuard_oxypro curDev =  new CardGuard_oxypro("00A096169D05");
       
       byte[] msgRtn0 = curDev.devInfo();
       byte[] msgRtn1 = curDev.devStatus();
       byte[] msgRtn2 = curDev.startOxyProTest();
       curDev.stopOxyProTest();
       
    }
}
