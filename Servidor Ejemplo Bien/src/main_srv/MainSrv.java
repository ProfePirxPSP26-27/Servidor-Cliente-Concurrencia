package main_srv;

import java.io.IOException;

import servidor.Servidor;

public class MainSrv {
	
	public static String HELP = "Invalid value for parameter <port> \"%s\"\n"
			+ "Usage: java -jar Servidor.jar <port>\n"
			+ "    where <port> is an integer from 1 to 65537";

	public static void main(String[] args) {
		int puerto = 40000;
		if (args.length > 0) {
			try {
				puerto = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println(String.format(HELP, args[0]));
				System.exit(23);
			}
		}
		Servidor srv = new Servidor(puerto);
		
		try {
			srv.initConnections();
//			srv.pedirNombres();
			srv.bucleRun();
		} catch (IOException e) {
			System.err.println("ERROR FATAL");
			e.printStackTrace();
		} finally {
			try {
				srv.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			System.out.println("[LOG] Goodbye!");
		}
	}

}
