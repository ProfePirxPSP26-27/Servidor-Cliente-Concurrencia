package gestion_login;

public interface GestionLogin {
	
	boolean registrar(String usuario, String contrasenna);
	
	boolean login(String usuario, String contrasenna);
	
	boolean borrarUsuario(String usuario, String contrasenna);

}
