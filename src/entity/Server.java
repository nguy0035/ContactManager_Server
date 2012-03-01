/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author tatthang
 */
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;

public class Server implements Runnable{
    private int SERVER_STATE;
    private int SERVER_STATE_RUNNING = 0;
    private int SERVER_STATE_STOP = 1;
    
    private ServerSocket serverSocket;
    private Socket socket;
    
    private int LISTENED_PORT = 5555;
    
    private ArrayList<client> active_users = new ArrayList<client>();
    
    public Server(){
        try
        {
            serverSocket = new ServerSocket(LISTENED_PORT);
        }catch(IOException e)
        {
            
        }
        
        
    }
    @Override
    public void run() {
        
        while (SERVER_STATE==SERVER_STATE_RUNNING){
            try{
                socket = serverSocket.accept();
                client authenticating_client = new client(socket);
            }
            catch(IOException e){
                
            }
        }
        
    }
    
    
}
