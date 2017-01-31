package medview.t4server;

/**
 * Simulating a call to the T4Server socket server.
 * @author Fredrik Lindahl
 */

import java.io.*;

import java.net.*;

public class T4Client
{
	public T4Client() throws IOException
	{
		Socket socket = new Socket("localhost", port);

		OutputStreamWriter writer = new OutputStreamWriter(

			socket.getOutputStream(), "UTF-8");
					
		writer.write(SAMPLE_XML);

		writer.flush();

		writer.close();

		socket.close();
	}

	private static String SAMPLE_XML =
		"<?xml version=\"1.0\" encoding=\"UTF-8\"?><T4MedView><Administrative>" +
		"<Person Name_First=\"Marita\" Name_Middle=\"Tjohej\" Name_Last=\"Nilsson\" " +
		"Personal_Number=\"5403114234\"/><Address Street=\"Angemarken 4B\" Post_Number" +
		"=\"42315\" City=\"Göteborg\" Country=\"Sweden\"/><Contact Phone_Home=\"031-423152\" " +
		"Phone_Work=\"031-7703242\" Email=\"nilsson@odontologi.gu.se\"/><Remittent Name_First=\"" +
		"Mats\" Name_Last=\"Jontell\"/></Administrative></T4MedView>";

	private static final int DEFAULT_PORT = 50000;
	
	private static int port;

	public static void main(String[] args) throws IOException
	{		
		if (args.length == 1)
		{
			try
			{
				port = Integer.parseInt(args[0]);
			}
			catch (NumberFormatException exc)
			{
				exc.printStackTrace();

				System.exit(1);
			}
		}
		else
		{
			port = DEFAULT_PORT;
		}
		
		new T4Client();
	}
}
