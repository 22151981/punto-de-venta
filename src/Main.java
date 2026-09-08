public class Main {
    public static void main(String[] args) {

        // 1. Instancias base
        Producto agua = new Producto("P001", "Agua Mineral Litro", 4000.0, 10);
        Cliente cliente = new Cliente("123456", "Javier Layton", 99000.0, true);

        System.out.println("=== ESTADO INICIAL ===");
        System.out.println("Producto: " + agua.getNombre() + " | Stock: " + agua.getCantidad() + " | Precio: $" + agua.getPrecio());
        System.out.println("Cliente: " + cliente.getNombre() + " | Saldo: $" + cliente.getSaldoDisponible());

        // 2. Transacción coordinada por Venta
        int cantidadAComprar = 3;
        Venta venta1 = new Venta("FAC-001", cliente, agua, cantidadAComprar);

        System.out.println("\n--- PROCESANDO COMPRA ---");
        System.out.println("Factura: " + venta1.getNumeroFactura());
        System.out.println("Unidades solicitadas: " + venta1.getCantidad());
        System.out.println("Total a pagar: $" + venta1.getTotal());

        // 3. Venta se encarga de TODO el negocio (validar, descontar y proteger)
        venta1.procesarVenta();

        System.out.println("\n=== ESTADO FINAL ===");
        System.out.println("Stock restante en inventario: " + agua.getCantidad());
        System.out.println("Saldo restante del cliente: $" + cliente.getSaldoDisponible());
    }
}