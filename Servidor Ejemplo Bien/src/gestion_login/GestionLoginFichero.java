package gestion_login;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class GestionLoginFichero implements GestionLogin {
	
	private static String FORMATO_ALMACEN = "%s %s";
	private static String RUTA_DEF = "files/pwds";
	
	private File ficheroLogin;

	public GestionLoginFichero(File ficheroLogin) throws IOException {
		this.ficheroLogin = ficheroLogin;
		if (!this.ficheroLogin.exists()) this.ficheroLogin.createNewFile();
	}
	
	public GestionLoginFichero() throws IOException {
		this(new File(RUTA_DEF));
	}

	private boolean userExists(String user) {
		Scanner sc = null;
		try {
			sc = new Scanner(ficheroLogin);
			while (sc.hasNextLine()) {
				if (sc.nextLine().split(" ")[0].equals(user)) return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (sc != null) sc.close();
		}
		
		return false;
	}
	
	@Override
	public boolean registrar(String usuario, String contrasenna) {
		if (!userExists(usuario)) {
			PrintWriter pw = null;
			try {
				pw = new PrintWriter(new FileOutputStream(ficheroLogin, true));
				pw.println(String.format(FORMATO_ALMACEN, usuario, contrasenna));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (pw != null) {
					pw.close();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean login(String usuario, String contrasenna) {
		if (!userExists(usuario)) return false;
		Scanner sc = null;
		try {
			sc = new Scanner(ficheroLogin);
			while (sc.hasNextLine()) {
				String lin = sc.nextLine();
				String user = lin.substring(0, lin.indexOf(' '));
				String pass = lin.substring(lin.indexOf(' ') + 1);
				if (user.equals(usuario) && pass.equals(contrasenna)) return true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (sc != null) sc.close();
		}
		
		return false;
	}

	@Override
	public boolean borrarUsuario(String usuario, String contrasenna) {
		// TODO Auto-generated method stub
		return false;
	}

}
