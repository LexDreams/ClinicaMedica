
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conexionMySQL {
    
    private static Connection conexionUnica;
	
	private conexionMySQL(String direccionIp, String nombreBD, 
			String usuarioBD, String passwordBD) throws SQLException{
		
		//cargar driver
		cargarControlador();
		
		//obtener la conexion		
		String url = "jdbc:mysql://" + "127.0.0.1" + "/" + "clinica_medica1";
		
		conexionUnica = DriverManager.getConnection(url,usuarioBD,passwordBD);
	}
	
	private void cargarControlador() throws SQLException{		
			DriverManager.registerDriver (new com.mysql.jdbc.Driver());		
	}	
	
	public static Connection getConexionUnica(String direccionIp, 
			String nombreBD, String usuarioBD, String passwordBD) throws SQLException{
		
		if(conexionUnica == null){			
			System.out.println("La conexion es nula, hay que crearla una vez");
			new conexionMySQL(direccionIp, nombreBD, usuarioBD, passwordBD);			
		}
		
		System.out.println("Su Base De Datos Esta Conectada");
		return conexionUnica;
	}
    
}
