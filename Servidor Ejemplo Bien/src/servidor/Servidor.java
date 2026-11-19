package servidor;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import gestion_login.GestionLogin;
import gestion_login.GestionLoginFichero;
import gestion_ordenes.Orden;

public class Servidor implements Closeable {

	private ServerSocket servidor;
	private GestionLogin logins;

	private Usuario user1;
	private Usuario user2;

	private ArrayList<Usuario> todos;

//	private PrintWriter pw1;
//	private PrintWriter pw2;
//	private Scanner sc1;
//	private Scanner sc2;
//	private String nick1;
//	private String nick2;

	public Servidor(int puerto) {
		try {
			servidor = new ServerSocket(puerto);// , 50, InetAddress.getByName("10.90.236.179"));
			System.out.println("[LOG] Servidor escuchando: " + servidor);
			todos = new ArrayList<Usuario>(2);
			user1 = new Usuario();
			user2 = new Usuario();
			user1.setNick("User1");
			user2.setNick("User2");
			todos.add(user1);
			todos.add(user2);
//			nick1 = "User1";
//			nick2 = "User2";
			logins = new GestionLoginFichero();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(32);
		}
	}

	public void initConnections() throws IOException {
		System.out.println("[LOG] Esperando a clientes");
//		conn1 = servidor.accept();
		user1.setConn(servidor.accept());
		System.out.println("[LOG] Cliente 1 conectado");
//		conn2 = servidor.accept();
		user2.setConn(servidor.accept());
		System.out.println("[LOG] Cliente 2 conectado");
		user1.initConns();
		user2.initConns();
	}

	public void bucleRun() {
		String linea1;
		String linea2;
		do {
			linea1 = user1.getMessage();
			System.out.println("[LOG] Cliente 1: " + linea1);
			gestionarComando(user1, linea1);
//			user1.sayMessage(respuesta1);
//			user2.sayMessage(respuesta1);
			linea2 = user2.getMessage();
			System.out.println("[LOG] Cliente 2: " + linea2);
			gestionarComando(user2, linea2);
//			user1.sayMessage(respuesta2);
//			user2.sayMessage(respuesta2);
		} while (!linea1.equals("EXIT") && !linea2.equals("EXIT"));
	}
	
	private void responderBC(String mensaje) {
		for (Usuario u : todos) {
			u.sayMessage(mensaje);
		}
	}

	private void gestionarComando(Usuario usuario, String linea) {
		Orden comando = Orden.parseOrden(linea);
		String toReturn;
		boolean isBc = false;

		if (usuario.isLogged()) {
			switch (comando.getTipo()) {
			case EXIT:
				toReturn = "EXIT";
				usuario.exit();
				break;
			case SAY:
				String msg = comando.getPar(0);
				toReturn = "HEAR " + usuario.getNick() + " " + msg;
				isBc = true;
				break;
			case SETNICK:
				usuario.setNick(comando.getPar(0));
				toReturn = "OK";
				break;
			default:
				toReturn = "ERROR";
				break;
			}
		} else {
			switch (comando.getTipo()) {
			case REGISTER:
				boolean res = registerUser(comando.getPar(0), comando.getPar(1));
				toReturn = res ? "OK" : "KO";
				break;
			case LOGIN:
				res = logins.login(comando.getPar(0), comando.getPar(1));
				if (res) {
					usuario.logIn();
					usuario.setNick(comando.getPar(0));
					toReturn = "OK";
				} else {
					toReturn = "KO";
				}
				break;
			default:
				toReturn = "ERROR";
				break;
			}
		}
		if (isBc) {
			responderBC(toReturn);
		} else {
			usuario.sayMessage(toReturn);
		}
//		return toReturn;
	}

//	private boolean setNick(String usuario, String nuevoNick) {
//		if (usuario.equals(nick1)) {
//			nick1 = nuevoNick;
//			return true;
//		}
//		if (usuario.equals(nick2)) {
//			nick2 = nuevoNick;
//			return true;
//		}
//		return false;
//	}

	private boolean registerUser(String user, String contrasenna) {
		return logins.registrar(user, contrasenna);
	}

	public void close() throws IOException {
		user1.close();
		user2.close();
		servidor.close();
	}

}
