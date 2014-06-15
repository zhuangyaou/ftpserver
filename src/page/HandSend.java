package page;
import java.io.*;
import java.net.*;
public class HandSend extends Thread{
  	ServerSocket server;
  	Socket socket;
  public void run(){
  	try{
  		server=new ServerSocket(1026);
  	}catch(IOException e){
  		
  	}
  	while(true){
     try{
     	  socket=server.accept();
     }catch(IOException e){
     System.out.println("HandSend"+e);	
     }
  }
  	}
  public Socket getsocket(){
  		return socket;
   }
  }