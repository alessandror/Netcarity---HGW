package net.hgw4.hal;

/**
 * define possible command messages
 * @author AlessandroR
 */
public interface PossibleMsgCmds {

    String onCmd(String val);
    String offCmd(String val);
    String onPercCmd(String val);
    String offPercCmdString(String val);


}
