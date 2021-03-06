/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author tatthang
 */
import java.io.BufferedInputStream;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.db_helper;

public class client implements Runnable{

    private Server server ; 
    private Socket socket;
    private String username;// a unique username
    private String password;
    private DataInputStream inputstream;
    private DataOutputStream outputstream;
    private Thread thread;
    
    private String RFC; // a command message sent by a CLIENT
    
    /*************RFC Constants****************************/
    private final String HELLO_RFC = "HELLO"; // RFC = HELLO username password
    private final String HELLO_RESPONSE_ACCEPT = "ACCEPT HELLO";//RFC = ACCEPT HELLO
    private final String HELLO_RESPONSE_REJECT = "REJECT HELLO";
    /************End of RFC Constants********************/
    
    public client(Server server,Socket socket){
        this.server = server;
        this.socket = socket;
        try {
            inputstream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            outputstream = new DataOutputStream(socket.getOutputStream());
            
        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
        thread = new Thread(this);
        thread.start();
    }
    
    public client(Socket socket, String username, String password) {
        this.socket = socket;
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void run() {
        while (thread != null){
            try {
                RFC = inputstream.readLine();
            } catch (IOException ex) {
                Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            /*-------Handle the RFC------*/
            if (RFC.startsWith(HELLO_RFC))
            {
                boolean loginSuccess = DoLogin(RFC);
                if (!loginSuccess){
                    SendLoginMessageResponse(HELLO_RESPONSE_REJECT);
                    StopClient();
                }
                else
                {
                    SendLoginMessageResponse(HELLO_RESPONSE_ACCEPT);
                    this.server.addNewActiveClient(this);
                }
            }
        }
    }
    
    private void SendLoginMessageResponse(String RFC_response){
        try {
            outputstream.write(RFC_response.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void StopClient(){
        
        this.thread = null;
        
        try {
            
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean DoLogin(String RFC_command){
        java.util.StringTokenizer str = new java.util.StringTokenizer(RFC_command);
        str.nextToken();
        String username = str.nextToken();
        String password = str.nextToken();
        
        db_helper db = new db_helper();
        boolean success = db.DoLogin(username, password);
        
        return success;
    }
}
