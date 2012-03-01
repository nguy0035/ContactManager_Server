/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author tatthang
 */
public class db_helper {

    public boolean DoLogin(String username,String password){
        if (username.equalsIgnoreCase("tatthang") && password.equalsIgnoreCase("password"))
            return true;
        
        return false;
    }
}
