package gestion_menus;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Menu implements Closeable {

	public static final int REGISTER_CORRECT = 10001;
	public static final int REGISTER_INCORRECT = 10002;
	public static final int LOGIN_CORRECT = 20001;
	public static final int LOGIN_INCORRECT = 20002;

	public static final String REGISTRAR = "REGISTER %s %s";
	public static final String LOGIN = "LOGIN %s %s";
	public static final String SAY = "SAY %s";
	public static final String SETNICK = "SETNICK %s";

	private Scanner scStdin;
	private Socket conn;
	private PrintWriter pw;
	// private Scanner scConn;
	private Thread hiloEsc;
	private Thread hiloCons;
	Queue<String> colaMensajes;

	public Menu(Socket conn) throws IOException {
		this.conn = conn;
		scStdin = new Scanner(System.in);
		// scConn = new Scanner(conn.getInputStream());
		pw = new PrintWriter(conn.getOutputStream(), true);
		colaMensajes = new ConcurrentLinkedQueue<String>();
		hiloEsc = new HiloEscuchador(conn, colaMensajes);
		hiloEsc.start();
		hiloCons = new HiloConsumidor(colaMensajes);
		hiloCons.start();
	}

	public int registerLogin() {
		System.out.println("\t1. Registrarse");
		System.out.println("\t2. Iniciar sesión");
		System.out.print(">>> ");
		String op = scStdin.nextLine();
		String user = null;
		String pass = null;

		if ("12".contains(op)) {
			System.out.print("Nombre usuario: ");
			user = scStdin.nextLine();
			System.out.print("Contraseña: ");
			pass = scStdin.nextLine();
			switch (op) {
			case "1":
				pw.println(String.format(REGISTRAR, user, pass));
				return getOkays() ? REGISTER_CORRECT : REGISTER_INCORRECT;
				// return waitForResp() ? REGISTER_CORRECT : REGISTER_INCORRECT;
			case "2":
				pw.println(String.format(LOGIN, user, pass));
				return getOkays() ? LOGIN_CORRECT : LOGIN_INCORRECT;
//					return waitForResp() ? LOGIN_CORRECT : LOGIN_INCORRECT;
			default:
				System.out.println("Opción inválida");
				break;
			}
		}

		return 0;
	}

	public boolean menu() {
		System.out.println("\t1. Decir mensaje");
		System.out.println("\t2. Cambiar nick");
		System.out.println("\t3. Salir");
		System.out.print(">>> ");
		String op = scStdin.nextLine();

		switch (op) {
		case "1":
			System.out.print("Msg: ");
			String msg = scStdin.nextLine();
			pw.println(String.format(SAY, msg));
			break;
		case "2":
			System.out.print("Nuevo nick: ");
			String nick = scStdin.nextLine();
			pw.println(String.format(SETNICK, nick));
			break;
		case "3":
			pw.println("EXIT");
//			System.out.println(scConn.nextLine());
			return false;
		default:
			System.out.println("Orden inválida");
			break;
		}

		return true;
	}

	public boolean siONo() {
		System.out.print("¿Continuar? (S/n): ");
		return scStdin.nextLine().toLowerCase().charAt(0) != 'n';
	}

	@Override
	public void close() throws IOException {
		scStdin.close();
//		scConn.close();
//		scConn = null;
		pw.close();
		conn.close();
		hiloEsc.interrupt();
		hiloCons.interrupt();
	}

	public boolean getOkays() {
		//System.out.println("GET OKAYS");
		synchronized (colaMensajes) {
			//System.out.println("SYNCROOOO");
			try {
				//System.out.println("WE WAIT");
				colaMensajes.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (colaMensajes.contains("OK")) {
				colaMensajes.remove("OK");
				colaMensajes.notify();
				return true;
			}
			colaMensajes.remove("KO");
			colaMensajes.notifyAll();
		}
		//System.out.println("PUES ME LO SALTO");
		return false;
	}

}
