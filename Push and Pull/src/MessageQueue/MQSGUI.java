/* Sameer Valiah Pusunuru
 * 1001552473
 * DS Lab3
 */
package MessageQueue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class MQSGUI extends JFrame {
	//java awt frame for client display
	private JFrame frame;
	//Area where the messages from server and client self msgs will be displayed
	private JTextArea chatWindow;

	private JTextField portalGUI;
	
	//stream from where we will listen msgs from server
	private ObjectInputStream input;
	//stream from where we will send msgs to client
	private ObjectOutputStream output;
	//String variable to save msgs from user input
	private String message ="";
	//int variable giving out random number for client gui
	static Random rand = new Random();
	private static String usrname="MQS";
	//string variable to store msgs after decoding/encoding them
	private String msg1="";
	//int variable to keep track of the user name
	private static int count =0;
	//hard coded address of the server
	private static final String serverIP ="localhost";
	//socket for the client
	private Socket clientConnection;



	public  MQSGUI() 
	{



		//name of window
		super(usrname);
		
		
		
		//label for client window
		JLabel label = new JLabel();
		add(label,BorderLayout.NORTH);
		label.setText("DS Lab 3");

		//field for showing logical clock show
		portalGUI=new JTextField();
		portalGUI.setEditable(false);
		add(portalGUI, BorderLayout.PAGE_START);
		portalGUI.setText("!!!***Message Queue Server***!!!");
		
	

		//Text area where incoming and outgoing msgs are displayed
		chatWindow = new JTextArea();
		chatWindow.setEditable(false);
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);

	

		//setting size of jframe
		setSize(400,300);

		//setting location for window
		setLocation(0,0);

		//making the client window visible 
		setVisible(true);

		
	}





	

	




	//Initiating the client , this fuction is being called by clientMain class
	public void startClient() throws Exception
	{
		try
		{
			// fucntion to create the socket and make the necessary arrangments for connection
			connectToServer();
		
			//function to setup the streams where we can listen and talk to the server by creating streams on the socket
			setUpStreams();
			
			//the fuction which handles the communication between server and client
			whileChatting();
		}
		catch (EOFException e)
		{
			//if eroor the closing the client connection and displaying the msg
			showMessage("\n Client Terminated");
		}
		catch(IOException ei)
		{
			ei.printStackTrace();
		}
		finally 
		{
			//after above steps closing the connection on exit 
			close();
		}
	}




	//Function which creates connection for client to server
	private void connectToServer() throws IOException
	{
		//showMessage(" Trying to connect to server \n");
		//socket being created on the hard coded port value
		clientConnection = new Socket("localhost",7689);


	}
	//after connection is made setting up the communiction ports
	private void setUpStreams() throws IOException
	{
		//output stream which let us to send our msgs over server
		output = new ObjectOutputStream(clientConnection.getOutputStream());
		//making sure all the material on the output object is being sent to server
		output.flush();
		sendMessage(usrname);
		showMessage(" ***MQS up and running!!*** \n");
		//input stream which lets us hear what server is replying back to us
		input = new ObjectInputStream(clientConnection.getInputStream());
	}

	//Function handling the communication between client and server
	private void whileChatting() throws  Exception
	{
		String msg="";

		//first initiate without condition
		do 
		{
			try
			{
				//hear what server is replying
				message=(String) input.readObject();
			
				
					showMessage(" "+message);
				
			}
			catch(ClassNotFoundException e)
			{
				showMessage("\n please send the message again");
			}
		}while(!message.equalsIgnoreCase("END"));// if user inputs end in any case then the communication will be closed eventually leading to the closure of client connection
	}

	//fucntion to properly close all the client related objects like input,output stream and socket of the client
	@SuppressWarnings("deprecation")
	private void close() 
	{
		showMessage("\n Connection Closed");
		sendMessage("***"+usrname+" Connection closed ***");

		
		try 
		{
			//closing the streams
			input.close();
			output.close();
			clientConnection.close();
			//closing the thread having the clock
		
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
	}

	// sending msg to server after encoding it in htpp
	private void sendMessage(String message1)
	{
		



		try
		{



		
			//output.writeObject(message);
			//putting the string as object inside the output stream for server to recieve
			output.writeObject(message1);
			output.flush();

		}

		catch (IOException ex)
		{
			chatWindow.append("\n error in messages");
		}
	}
	//displaying the message on the GUI(chatWindow-text area) the messages are decoded
	private void showMessage(final String s)
	{
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						chatWindow.append("\n "+s);
					}
				}
				);
	}



	
	
	
	
}
