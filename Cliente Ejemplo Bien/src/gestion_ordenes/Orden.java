package gestion_ordenes;

import java.util.Arrays;

public class Orden {

	public enum Tipo {
		EXIT, OK, KO, REGISTER(2), LOGIN(2),
		SAY(1, true), SETNICK(1), HEAR(2, true);

		private int parNum;
		private boolean rest;

		private Tipo() {
		}

		private Tipo(int parNum) {
			this.parNum = parNum;
		}
		
		private Tipo(int parNum, boolean rest) {
			this.parNum = parNum;
			this.rest = rest;
		}
		
		public int getParNum() {
			return parNum;
		}
		
		public boolean validate(String pars) {
			return parNum == pars.split(" ").length || (rest && parNum <= pars.split(" ").length);
		}
	}

	private String pars;
	private Tipo tipo;

	private Orden(Tipo tipo, String pars) {
		this.tipo = tipo;
		this.pars = pars;
	}

	public static Orden parseOrden(String orden) {
		String comm = orden.split(" ")[0].toUpperCase();
		Tipo t = Tipo.valueOf(comm);
		String resto = "";
		for (int i = 1; i < orden.split(" ").length; i++)
			resto += orden.split(" ")[i] + " ";
		resto = resto.trim();
		if (!t.validate(resto)) throw new RuntimeException("Orden inválida");
		return new Orden(t, resto);
	}

	@Override
	public String toString() {
		return tipo.toString() + " " + pars;
	}
	
	public String getPars() {
		return pars;
	}
	
	public String getPar(int i) {
		String[] pspli = pars.split(" ");
		if (!tipo.rest && i < tipo.parNum) {
			return pspli[i];
		} else if (i < tipo.parNum - 1) {
			return pspli[i];
		} else if (i == tipo.parNum - 1) {
			return String.join(" ", Arrays.copyOfRange(pspli, i, pspli.length));
		}
		
		throw new ArrayIndexOutOfBoundsException();
	}
	
	public Tipo getTipo() {
		return tipo;
	}

	public static void main(String[] args) {
		Orden a = parseOrden("HEAR pepito juanito dice que todo muy bien");
		System.out.println(a);
		System.out.println(a.getPar(0));
		System.out.println(a.getPar(1));
	}

}
