import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClienteDAO {

    // 1. Guardar cliente en MySQL
    public boolean insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (cedula, nombre, saldo, activo) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getCedula());
            ps.setString(2, cliente.getNombre());
            ps.setDouble(3, cliente.getSaldoDisponible());
            ps.setBoolean(4, cliente.getActivo());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar cliente: " + e.getMessage());
            return false;
        }
    }

    // 2. Listar todos los clientes desde MySQL
    public ArrayList<Cliente> listarTodos() {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getDouble("saldo"),
                        rs.getBoolean("activo")
                );
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

    // 3. Buscar cliente por cédula
    public Cliente buscarPorCedula(String cedula) {
        String sql = "SELECT * FROM clientes WHERE cedula = ?";
        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Cliente(
                            rs.getString("cedula"),
                            rs.getString("nombre"),
                            rs.getDouble("saldo"),
                            rs.getBoolean("activo")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }
}