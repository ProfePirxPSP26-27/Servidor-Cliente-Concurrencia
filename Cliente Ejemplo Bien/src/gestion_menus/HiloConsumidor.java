package gestion_menus;

import java.util.Queue;

import gestion_ordenes.Orden;

public class HiloConsumidor extends Thread {
	
	private Queue<String> cola;
	private static final String FORMATO_MSG = "[%s] %s";

	public HiloConsumidor(Queue<String> cola) {
		this.cola = cola;
	}
	
	@Override
	public void run() {
		while (true) {
			if (!cola.isEmpty() && cola.peek().startsWith("HEAR")) {
				Orden o = Orden.parseOrden(cola.poll());
				System.out.println(String.format(FORMATO_MSG, o.getPar(0), o.getPar(1)));
			}
		}
	}

}
