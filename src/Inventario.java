import java.util.ArrayList;

public class Inventario {

    // 1. Atributo: Simula la tabla 'productos'
    private ArrayList<Producto> productos;

    // 2. Constructor: Inicializa la lista vacía
    public Inventario() {
        this.productos = new ArrayList<>();
    }

    // ==========================================
    // MÉTODOS CRUD
    // ==========================================

    // C - CREATE: Registrar producto si el código no existe
    public void registrarProducto(Producto nuevo) {
        if (buscarPorCodigo(nuevo.getCodigo()) != null) {
            System.out.println("Error: Ya existe un producto con el código " + nuevo.getCodigo());
            return;
        }
        this.productos.add(nuevo);
        System.out.println("Producto registrado: " + nuevo.getNombre() + " | Código: " + nuevo.getCodigo());
    }

    // R - READ (Por ID): Busca un producto específico por su código
    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : this.productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    // R - READ (Todos): Lista el catálogo completo
    public void listarProductos() {
        System.out.println("=== LISTA DE PRODUCTOS EN INVENTARIO ===");
        if (this.productos.isEmpty()) {
            System.out.println("No hay productos en el inventario.");
            return;
        }

        System.out.println("\n========== CATÁLOGO DE PRODUCTOS ==========");
        for (Producto p : this.productos) {
            System.out.println("Código: " + p.getCodigo() +
                    " | Nombre: " + p.getNombre() +
                    " | Precio: $" + p.getPrecio() +
                    " | Stock: " + p.getCantidad());
        }
        System.out.println("===========================================");
        System.out.println("Total de productos en inventario: " + this.productos.size());
    }

    // U - UPDATE: Actualiza precio y stock de un producto existente
    public void actualizarProducto(String codigo, double nuevoPrecio, int nuevoStock) {
        Producto encontrado = buscarPorCodigo(codigo);

        if (encontrado == null) {
            System.out.println("Error: No se puede actualizar. El producto con código " + codigo + " no existe.");
            return;
        }

        encontrado.setPrecio(nuevoPrecio);
        encontrado.setCantidad(nuevoStock);
        System.out.println("Producto " + codigo + " (" + encontrado.getNombre() + ") actualizado correctamente.");
    }
    // D - DELETE: Elimina un producto por su código
    public void eliminarProducto(String codigo) {
        Producto encontrado = buscarPorCodigo(codigo);

        if (encontrado == null) {
            System.out.println("Error: No se puede eliminar. El producto con código " + codigo + " no existe.");
            return;
        }

        this.productos.remove(encontrado);
        System.out.println("Producto " + codigo + " (" + encontrado.getNombre() + ") eliminado del inventario con éxito.");
    }
}