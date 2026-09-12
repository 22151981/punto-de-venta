public class Main {
    public static void main(String[] args) {

        // 1. CARGA INICIAL DEL SISTEMA
        // Creamos la bodega central de la tienda
        Inventario inventario = new Inventario();

        // Registramos productos en el catálogo central
        inventario.registrarProducto(new Producto("P001", "Agua Mineral Litro", 4000.0, 10));
        inventario.registrarProducto(new Producto("P002", "Galletas Festival", 1500.0, 20));
        inventario.registrarProducto(new Producto("P003", "Jugo Hit Mora", 3200.0, 5));

        // Registramos al cliente que se acerca a la caja
        Cliente javier = new Cliente("123456", "Javier Layton", 50000.0, true);

        // Mostramos el estado inicial del catálogo
        inventario.listarProductos();

        // 2. SIMULACIÓN DE LA COMPRA EN CAJA
        // El cajero no tiene el producto físico en código; solo recibe un código y una cantidad
        String codigoBuscado = "P001";
        int cantidadSolicitada = 3;

        System.out.println("\n--- INICIANDO ATENCIÓN EN CAJA ---");
        System.out.println("Cliente: " + javier.getNombre());
        System.out.println("Código consultado: " + codigoBuscado);

        // 3. CONSULTA AL CRUD (READ)
        Producto productoEncontrado = inventario.buscarPorCodigo(codigoBuscado);

        // Cláusula de guarda: si el producto no existe en bodega, no hay venta
        if (productoEncontrado == null) {
            System.out.println("Operación cancelada: El producto con código " + codigoBuscado + " no existe en el sistema.");
            return;
        }

        // 4. TRANSACCIÓN MEDIADA
        // Si existe, le entregamos la referencia del producto encontrado a la Venta
        Venta venta1 = new Venta("FAC-001", javier, productoEncontrado, cantidadSolicitada);

        // Venta ejecuta sus filtros (stock, saldo, cliente activo) y descuenta
        venta1.procesarVenta();

        // 5. COMPROBACIÓN DEL INVENTARIO CENTRAL
        // Verificamos si la venta modificó el stock real dentro de la lista del inventario
        System.out.println("\n--- ESTADO DEL INVENTARIO TRAS LA VENTA ---");
        inventario.listarProductos();
    }
}