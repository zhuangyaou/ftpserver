package page;

import java.io.*;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
  public class HandleFile extends JFrame { 
  	File dir;	
  	public HandleFile(){
  	  	
  	  }
 File[] HandAllFile(){	
 	File[] filename=null; 
    	filename=dir.listFiles();
    	return filename;
    }
  public File up(){
  	dir=dir.getParentFile();
  	return dir;   
  }
public void setCurrentDir(String directory){
	this.dir=new File(directory);
}
public String getCurrentDir(){
		return this.dir.toString();
}
public void getDown(String name){
dir=new File(dir.getAbsolutePath().concat("\\"+name.substring(3)));
  }
  public static void main(String args[]){
  	String[] filename;
  	HandleFile a=new HandleFile();
  }
  }