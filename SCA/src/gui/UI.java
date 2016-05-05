package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import client.Client;
import server.Server;


public class UI extends JFrame implements ActionListener
{
	
	private static final long serialVersionUID = 1L;
	
	boolean IamServer = false;
	Client client;
	Server server;
	
	JPanel mainPanel = new JPanel();
	JPanel serverHeaderPanel = new JPanel(new FlowLayout()); 
	JPanel serverInputPanel = new JPanel(new FlowLayout()); 
	JPanel clientHeaderPanel = new JPanel(new FlowLayout()); 
	JPanel clientInputPanel = new JPanel(new FlowLayout()); 
	JPanel chatArea = new JPanel();
	
	JLabel serverLabel = new JLabel("SERVER");
	JButton startServer = new JButton("Start Server");
	
	JLabel serverPortNumber = new JLabel("PORT NUMBER");
	JTextField serverPortNumberEditor = new JTextField();
	
	JLabel clientLabel = new JLabel("CLIENT");
	JLabel clientPortNumber = new JLabel("PORT NUMBER");
	JLabel serverIP = new JLabel("SERVER IP");
	JButton connectToServer = new JButton("Connect To Server");
	JTextField clientPortNumberEditor = new JTextField();
	JTextField serIP = new JTextField();
	
	JTextArea chatTextArea = new JTextArea(); 
	JTextArea sendChatTextArea = new JTextArea();
	JButton send = new JButton("SEND");
	
	JScrollPane chatPane = new JScrollPane(chatTextArea);
	JScrollBar verticalBar = chatPane.getVerticalScrollBar();
	
	public UI()
	{
		setSize(700,500);
		setVisible(true);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		send.addActionListener(this);
		connectToServer.addActionListener(this);
		startServer.addActionListener(this);
		
		// Server components
		serverHeaderPanel.setPreferredSize(new Dimension(700,35));
		serverInputPanel.setPreferredSize(new Dimension(700,50));
		clientHeaderPanel.setPreferredSize(new Dimension(700,35));
		clientInputPanel.setPreferredSize(new Dimension(700,50));
		chatArea.setPreferredSize(new Dimension(700,200));
		
		serverPortNumberEditor.setPreferredSize(new Dimension(100,20));
		serverLabel.setPreferredSize(new Dimension(100,20));
		startServer.setPreferredSize(new Dimension(120,20));
				
		serverHeaderPanel.add(serverLabel);
		serverInputPanel.add(serverPortNumber);
		serverInputPanel.add(serverPortNumberEditor);
		serverInputPanel.add(startServer);
		
		//client components
		clientLabel.setPreferredSize(new Dimension(100,20));		
		clientPortNumber.setPreferredSize(new Dimension(100,20));
		serverIP.setPreferredSize(new Dimension(80,20));
		connectToServer.setPreferredSize(new Dimension(150,20));
		clientPortNumberEditor.setPreferredSize(new Dimension(100,20));
		serIP.setPreferredSize(new Dimension(130,20));
		
		clientHeaderPanel.add(clientLabel);
		clientInputPanel.add(clientPortNumber);
		clientInputPanel.add(clientPortNumberEditor);
		clientInputPanel.add(serverIP);
		clientInputPanel.add(serIP);
		clientInputPanel.add(connectToServer);
		
		//chat components
		chatPane.setPreferredSize(new Dimension(600,100));
		sendChatTextArea.setPreferredSize(new Dimension(400,50));
		send.setPreferredSize(new Dimension(200,50));
		
		chatTextArea.setEditable(false);
		
		chatArea.add(chatPane);
		chatArea.add(sendChatTextArea);
		chatArea.add(send);
		
		mainPanel.add(serverHeaderPanel);
		mainPanel.add(serverInputPanel);
		mainPanel.add(clientHeaderPanel);
		mainPanel.add(clientInputPanel);
		mainPanel.add(chatArea);
		
		add(mainPanel);
	}
	
	public static void main(String args[])
	{
		new UI();
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==connectToServer)		
		{
			client = new Client(serIP.getText(),Integer.parseInt(clientPortNumberEditor.getText()),this);
			client.start();
			return;
		}
		else if(e.getSource()==startServer)
		{
			server = new Server(Integer.parseInt(serverPortNumberEditor.getText()),this);	
			server.start();
			IamServer = true;
			return;
		}
		if(e.getSource()==send)
		{
			if(IamServer)
			{
				server.setToBeSent(sendChatTextArea.getText());
				sendChatTextArea.setText(null);  //reset text area
			}
			else
			{
				client.setToBeSent(sendChatTextArea.getText());
				sendChatTextArea.setText(null);  //reset text area
			}
		}

	}
	public void appendText(String text)
	{
		if( !(text==null) )
		{
			chatTextArea.append(text+"\n");
			verticalBar.setValue(100);
		}
		
	}
}