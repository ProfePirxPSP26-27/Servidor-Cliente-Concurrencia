package servidor;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Usuario implements Closeable {
	
	private Socket conn;
	private String nick;
	private boolean logged;
	
	private PrintWriter pw;
	private Scanner sc;
	
	public void setNick(String nick) {
		this.nick = nick;
	}
	
	public String getNick() {
		return nick;
	}
	
	public void setConn(Socket conn) {
		this.conn = conn;
	}
	
	public Socket getConn() {
		return conn;
	}
	
	public void logIn() {
		logged = true;
	}
	
	public void exit() {
		logged = false;
	}
	
	public boolean isLogged() {
		return logged;
	}
	
	public void initConns() throws IOException {
		pw = new PrintWriter(conn.getOutputStream(), true);
		sc = new Scanner(conn.getInputStream());
	}

	public String getMessage() {
		return sc.nextLine();
	}
	
	public void sayMessage(String msg) {
		pw.println(msg);
	}

	@Override
	public void close() throws IOException {
		pw.close();
		sc.close();
		conn.close();
	}
	
}
