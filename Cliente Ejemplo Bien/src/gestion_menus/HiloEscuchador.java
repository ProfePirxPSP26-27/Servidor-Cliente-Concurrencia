package gestion_menus;

import java.io.IOException;
import java.net.Socket;
import java.util.Queue;
import java.util.Scanner;

public class HiloEscuchador extends Thread {

	private Scanner scConn;
	private Socket conn;
	private Queue<String> cola;

	public HiloEscuchador(Socket conn, Queue<String> cola) {
		this.conn = conn;
		this.cola = cola;
		try {
			this.scConn = new Scanner(conn.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (!conn.isClosed()) {

			while (scConn.hasNextLine()) {
				synchronized (cola) {
					System.out.println("OMNOMNOMNOM");
					cola.add(scConn.nextLine());
					System.out.println("COLA: " + cola.toString());
					cola.notifyAll();
				}
			}
		}
//		synchronized (cola) {
//			try {
//				System.out.println("HILO ESPERANDO");
//				cola.wait();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
}