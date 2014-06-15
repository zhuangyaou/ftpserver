package page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.*;

public class FtpServer extends JFrame implements ActionListener, Runnable {
	ServerSocket server, SendSocket;

	Socket mySocket = null, Send = null;

	Thread serverThread = null;

	JTextField enterport;

	JLabel label;

	JButton comeIn;

	JButton exitButton;

	JButton chooseButton;

	JPanel contain;

	JPanel contain1;

	JTextArea playArea;

	HandSend Hand;

	Hashtable UserList = null;

	public FtpServer() {
		super("FTP服务器");
		Container container = getContentPane();
		playArea = new JTextArea();
		enterport = new JTextField(20);
		playArea.setBackground(Color.WHITE);
	//	label = new JLabel("HIT-ICES");
		comeIn = new JButton("开始服务");
		exitButton = new JButton("退出");
		chooseButton = new JButton("服务路径");
		contain = new JPanel();
		contain1 = new JPanel();
		exitButton.addActionListener(this);
		chooseButton.addActionListener(this);
		comeIn.addActionListener(this);
		container.setLayout(new BorderLayout());
		container.add(contain1, BorderLayout.NORTH);
		contain1.add(enterport);
		contain1.add(chooseButton);
		container.add(contain, BorderLayout.SOUTH);
	//	contain.add(label);
		contain.add(exitButton);
		contain.add(comeIn);
		container.add(new JScrollPane(playArea), BorderLayout.CENTER);
		setSize(500, 400);
		setVisible(true);
		this.validate();
		enterport.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {

			}

			public void mouseReleased(MouseEvent arg0) {

			}

			public void mouseEntered(MouseEvent arg0) {

			}

			public void mouseExited(MouseEvent arg0) {

			}

		});

	}

	public void filechoose() {
		String newdir;
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		File fileName = filechooser.getSelectedFile();
		int result = filechooser.showOpenDialog(filechooser);
		File dir = filechooser.getCurrentDirectory();
		newdir = dir.toString();
	//	playArea.append(newdir);
		newdir = newdir.replace('\\', '/');
		enterport.setText(newdir);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == comeIn) {
			if (serverThread == null) {
				serverThread = new Thread(this);
				serverThread.start();
			
				playArea.append("Ftp service is started !\n");
				playArea.append("Work port :1025 \n");
			}
		} else if (e.getSource() == chooseButton) {
			this.filechoose();
		} else if (e.getSource() == exitButton) {
			this.end();
		}
	}

	public void end() {
		System.exit(0);
	}

	public void run() {
		InetAddress Address = null;
		try {
			UserList = new Hashtable();
			server = new ServerSocket(1025);
			Hand = new HandSend();
			Hand.start();
			while (true) {
				mySocket = server.accept();// establish a tcp connection;
				Address = mySocket.getInetAddress();
				//System.out.println(String.valueOf(Address));
				playArea.setCaretPosition(playArea.getText().length());
				if (mySocket != null) {
					Thread myThread = new ServerThread(enterport.getText(),
							mySocket, Hand,playArea);// 为该用户创建一个线程
					myThread.start();
					playArea.append("Connection from:"
							+ mySocket.getInetAddress() + "\n");
				}
			}

		} catch (IOException ee) {
		}
	}

	public static void main(String args[]) {
		FtpServer a = new FtpServer();
		a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

class ServerThread extends Thread {

	static String password = "123";

	static String pass = "111";

	DataOutputStream output = null;

	DataInputStream input = null;

	PrintStream outstream;

	String[] allfile = new String[100];

	Socket mySocket = null;

	HandSend Send;

	int len, count = 0;

	File file;

	HandleFile GetFile;

	byte[] buf;

	String[] filename;

	String dir;

	Hashtable DownList = null;

	InetAddress UserIp;

	SendThread thread;

	DataOutputStream OutputStream;
	
	int port;
	
	JTextArea Display;

	public ServerThread(String count, Socket my, HandSend send,JTextArea Dis)
			throws IOException {
		mySocket = my;
		Send = send;
       DownList=new Hashtable();
		dir = count.concat("/");
		GetFile = new HandleFile();
        Display=Dis;
		try {
			output = new DataOutputStream(mySocket.getOutputStream());
			input = new DataInputStream(mySocket.getInputStream());
		} catch (IOException e) {

		}
	}

	public void run() {
		Socket currentsocket=null;//防止传输冲突
		String msg = null;
		GetFile.setCurrentDir(dir);
		try {
			msg = input.readUTF();
		} catch (IOException e) {

		}
		if (msg.equals(password) || msg.equals(pass)) {
			if (msg.equals(pass)) {
				try {
					output.writeUTF("匿名");
				} catch (IOException e) {

				}
			}
			while (true) {
				try {
                  UserIp=mySocket.getInetAddress();
					if (mySocket.isClosed()) {
						break;
					}
				//	System.out.println(GetFile.getCurrentDir());
					msg = input.readUTF();
				//	System.out.println(msg);
					if (msg.startsWith("文件夹")) {
						GetFile.getDown(msg);
						File[] filename = GetFile.HandAllFile();

						for (int i = 0; i < filename.length; i++) {
							if (filename[i].isFile()) {
								output.writeUTF("文件名：1"+filename[i].getName()); // 把当前路径下面所有文件发送给客户端
								output.flush();
							} else {
								output.writeUTF("文件名：0"+ filename[i].getName());
							    output.flush();
							}
						}
					}
					if (msg.startsWith("文件")) {
						File[] filename = GetFile.HandAllFile();
						// System.out.println(GetFile.getCurrentDir());
						// ServerThread
						// my=(ServerThread)UserList.get(this.user);
						for (int i = 0; i < filename.length; i++) {
							if (filename[i].isFile()) {
								output.writeUTF("文件名：1"+filename[i].getName()); // 把当前路径下面所有文件发送给客户端
								output.flush();
							} else {
								output.writeUTF("文件名：0"+ filename[i].getName());
							   output.flush();
							}
						}

					} else if (msg.startsWith("向上")) {
						File file;
						file = GetFile.up();
						File[] filename = GetFile.HandAllFile();
						// System.out.println(GetFile.getCurrentDir());
						for (int i = 0; i < filename.length; i++) {
							if (filename[i].isFile()) {
								output.writeUTF("文件名：1"+filename[i].getName()); // 把当前路径下面所有文件发送给客户端
								output.flush();
							} else {
								output.writeUTF("文件名：0"+ filename[i].getName());
							    output.flush();
							}
						}
					} else if (msg.startsWith("下载")) {
						
						Display.append(msg);
						Display.append("一下载请求\n");
					    Display.setCaretPosition(Display.getText().length());
						String sendFile = GetFile.getCurrentDir().concat(
								"//" + msg.substring(2));
						Display.append(sendFile);
						File DownFile=new File(sendFile);
						
						if (Send.socket!=null) {
							
							Display.append("发送已创建\n");
						    Display.setCaretPosition(Display.getText().length());
							thread = new SendThread(Send.socket, DownFile,Display);
							thread.start();
							output.writeUTF("内容"+DownFile.length());
							output.flush();
						}
					} else if (msg.startsWith("上传")) {
						String file = GetFile.getCurrentDir()
								.replace('\\', '/').concat(
										"/" + msg.substring(2));
						System.out.println(file);
						if (Send.socket != null) {
							Display.append("文件上传从\n"+Send.socket.getInetAddress());
						    Display.setCaretPosition(Display.getText().length());
							currentsocket=Send.socket;
							SaveFile save = new SaveFile(file, Send.socket);
							save.start();
						}
					}
				} catch (IOException e) {
					Display.append("用户"+UserIp+"已经离开\n");
					 Display.setCaretPosition(Display.getText().length());
					break;

				}
			}
		} else {
			try {
				output.writeUTF("拒绝");
			} catch (IOException ee) {

			}
		}

	}


}
