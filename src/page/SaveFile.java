package page;

import java.io.*;
import java.net.Socket;

  public class SaveFile extends Thread{
  	DataOutputStream output;
  	DataInputStream input;
  	String filename;
  	Socket SaveSocket=null;
  	public SaveFile(String file,Socket socket){
  		SaveSocket=socket;
  		filename=file;
  	}
  public void run(){
  	int len;
  	try{
  	input=new DataInputStream(SaveSocket.getInputStream());
  	  output=new DataOutputStream(new FileOutputStream(new File(filename)));
  	   byte[] buf=new byte[2048];
  	     while((len=input.read(buf))!=-1){
  	     	  output.write(buf,0,len);
  	     }
  	     input.close();
  	     output.close();
  	     System.out.println("up end!!!");
  	     
  	}catch(IOException e){
  		
  	}
  }
  
  }