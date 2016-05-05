package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import gui.UI;

public class Server implements Runnable
{
	ServerSocket serverSocket;
	int portNumber;
	UI GUI;
	boolean link = true;
	String ToBeSent="NoEvent";
	volatile Thread th;
	String threadName = "ServerThread";
	Socket clientSocket;
	PrintWriter serverOutput;
	BufferedReader serverInput;
	
	public Server(int portNumber, UI GUI)
	{
		this.portNumber = portNumber;
		this.GUI = GUI;
	}
	
	public void startServer()
	{
		try 
		{
			serverSocket = new ServerSocket(portNumber);
			//serverSocket.setSoTimeout(30000);
		} 
		catch (IOException e) 
		{		
			e.getMessage();
		}
		System.out.println("Server Socket has been open on port "+serverSocket.getLocalPort());
		try 
		{
			System.out.println("Trying to establish a connection with client...");
			clientSocket = serverSocket.accept();	
			System.out.println("connection established with client");
		} 
		catch (IOException e) 
		{
			System.out.println("Failed to find client to connect");
			e.getMessage();
		}
		
	}
	public void writeToClient(String outputText)
	{

		try 
		{
			serverOutput = new PrintWriter(clientSocket.getOutputStream(),true);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		if( (outputText.equals("NoEvent")) || (outputText.equals("")))
		{
			outputText = "NoEvent";
			serverOutput.println(outputText);
		}
		else
		{
			serverOutput.println(outputText);
			GUI.appendText(outputText);
			setToBeSent("");  //reset text to be sent
		}
		
	}
	
	public void readFromClient()
	{
		String Input = "";
		try 
		{
			serverInput= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			Input = serverInput.readLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if( !(Input.equals("NoEvent")) && !(Input.equals("")))
		{
			GUI.appendText(Input);
		}   //else ignore

	}
	public void endConnection()
	{
		try 
		{
			serverOutput.close();
			serverInput.close();
			clientSocket.close();
		} 
		catch (IOException e) 
		{
			
			e.getMessage();
		}
		th=null;
	}
	public void setToBeSent(String ToBeSent)
	{
		this.ToBeSent = ToBeSent;
	}
	public String getToBeSent()
	{
		return ToBeSent;
	}
	public void start()
	{
		if(th==null)
		{
			th = new Thread(this,threadName);
			th.start();
		}
	}
	@Override
	public void run() 
	{
		startServer();
		
		while(link)
		{
			writeToClient(getToBeSent());
			readFromClient();
		}
		endConnection();
	}
	
}
