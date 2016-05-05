package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import gui.UI;

public class Client implements Runnable
{
	Socket clientSocket;
	
	PrintWriter clientOutput;
	BufferedReader clientInput;
	volatile Thread th;
	String threadName = "ClientThread";
	UI GUI;
	String IP;
	int portNumber;
	boolean link = true;
	String ToBeSent="";
	
	public Client(String IP, int portNumber,UI GUI)
	{
		this.IP = IP;
		this.portNumber = portNumber;
		this.GUI = GUI;
		
	}
	public void connectToServer(String IP, int portNumber)
	{
		try 
		{
			clientSocket = new Socket(IP,portNumber);

		} 
		catch (UnknownHostException e) 
		{	
			e.getMessage();
		} 
		catch (IOException e)
		{
			
			e.getMessage();
		}
		System.out.println("Managed to connect to server "+ clientSocket.getRemoteSocketAddress());

		
	}
	
	public void writeToServer(String outputText)
	{
		try 
		{
			clientOutput = new PrintWriter(clientSocket.getOutputStream(),true);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		if((outputText.equals("NoEvent")) || (outputText.equals("")))
		{
			outputText = "NoEvent";
			clientOutput.println(outputText);
		}
		else
		{
			clientOutput.println(outputText);
			GUI.appendText(outputText);
			setToBeSent("");  //reset text to be sent
		}
	}
	public void readFromServer()
	{
		String Input = "";
		try 
		{
			clientInput = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			Input = clientInput.readLine();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		if( !(Input.equals("NoEvent")) && !(Input.equals("")) )
		{
			GUI.appendText(Input);
		}   //else ignore

	}
	
	public void endConnection()
	{
		try 
		{
			clientOutput.close();
			clientInput.close();
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
		connectToServer(IP,portNumber);
		
		while(link)
		{
			readFromServer();
			writeToServer(getToBeSent());		
		}
		endConnection();
		
	}
}
