import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Configuración de los datos de conexión a MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/pos_tienda?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USUARIO = "root"; // Tu usuario de MySQL (por defecto es root)
    private static final String CONTRASENA = "admin";  // Si tu MySQL tiene clave, ponla aquí (si usas XAMPP suele ir vacía "")

    public static Connection obtenerConexion() {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("-> Conexión a la base de datos 'pos_tienda' establecida con éxito.");
        } catch (SQLException e) {
            System.out.println("-> Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }

    // Método main exclusivo para probar si la conexión funciona
    public static void main(String[] args) {
        Connection test = obtenerConexion();
        if (test != null) {
            System.out.println("¡Todo listo! Java y MySQL están comunicándose a la perfección.");
            try {
                test.close(); // Cerramos la conexión de prueba
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Revisa el usuario, la contraseña o que el servicio de MySQL esté encendido.");
        }
    }
}