package main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import gestion_menus.Menu;

public class MainCliente {
	
	public static final String HELP = "Bad usage"
			+ "Usage: java -jar Cliente.jar <ip_server> <port>\n"
			+ "    where <ip_client> is a valid ip or hostname"
			+ "    where <port> is an integer from 1 to 65537";

	public static void main(String[] args) throws IOException {
		int puerto = 40000;
		InetAddress ip = InetAddress.getLocalHost();
		if (args.length > 0) {
			try {
				puerto = Integer.parseInt(args[0]);
				ip = InetAddress.getByName(args[1]);
			} catch (RuntimeException e) {
				System.err.println(HELP);
				System.exit(23);
			}
		}
		
		Menu m = new Menu(new Socket(ip, puerto));
		boolean estaLoggeado = false;
		boolean repetir = true;
		
		while (repetir) {
			if (!estaLoggeado) {
				System.out.println("DEBES LOGEARTE PARA PODER USAR LA APLICACIÓN");
				int res = m.registerLogin();
				estaLoggeado = res == Menu.LOGIN_CORRECT;
			} else {
				boolean salir = !m.menu();
				if (salir) {
					repetir = m.siONo();
				}
			}
		}
		
		m.close();
	}
}
