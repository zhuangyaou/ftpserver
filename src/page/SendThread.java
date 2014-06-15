
package page;

import java.net.*;
import java.awt.*;
import java.io.*;

import javax.swing.JTextArea;



public class SendThread extends Thread{
	static int MAXSIZE=4096;//每次发送4K
	Socket socket;
	DataInputStream input=null;
    DataOutputStream output=null;
	File file;
	int len=0;
	long filelen;
	JTextArea Display;

public SendThread(Socket send,File selectFile,JTextArea d){
		socket=send;
		this.set(selectFile);
		Display=d;
	}
  public void set(File a){
  	this.file=a;
  }
	public long getFileLen(){
		return filelen;
	}
	public long getFileLong(){
		return filelen;
	}
  public void run(){
  	  try{
  	    output=new DataOutputStream(socket.getOutputStream());
  		DataInputStream in=new DataInputStream(new FileInputStream(file));
        Display.append("发送到："+socket.getInetAddress()+"开始\n");
	    Display.setCaretPosition(Display.getText().length());
		Display.append(socket.getInetAddress()+"正下载文件"+file.getName()+"\n");
		Display.setCaretPosition(Display.getText().length());
  	    byte[] buf=new byte[MAXSIZE];
  	  while((len=in.read(buf))!=-1){ 
  	  output.write(buf,0,len);
  	    output.flush();
  	        }
  	  in.close();
		Display.append(socket.getInetAddress()+"下载已完成\n");
	    Display.setCaretPosition(Display.getText().length());
  	    output.close(); 	 
  	}catch(IOException e){
 		Display.append("发送过程"+socket.getInetAddress()+"错误\n"+e);
	    Display.setCaretPosition(Display.getText().length());
  	}
  }
}
