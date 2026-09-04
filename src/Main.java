public class Main {
    public static void main(String[] args) {

        // 1. Instanciamos los dos objetos
        Producto agua = new Producto("P001", "Agua Mineral Litro", 4000.0, 10);
        Cliente cliente = new Cliente("123456", "Javier Layton", 1000.0, true);

        System.out.println("=== ESTADO INICIAL ===");
        System.out.println("Producto: " + agua.getNombre() + " | Stock: " + agua.getCantidad() + " | Precio: $" + agua.getPrecio());
        System.out.println("Cliente: " + cliente.getNombre() + " | Saldo: $" + cliente.getSaldoDisponible());

        // 2. Definimos la compra
        int cantidadAComprar = 3;
        double totalAPagar = agua.getPrecio() * cantidadAComprar;

        System.out.println("\n--- PROCESANDO COMPRA ---");
        System.out.println("Unidades a comprar: " + cantidadAComprar);
        System.out.println("Total a pagar: $" + totalAPagar);

        // 3. Ejecutamos los métodos de negocio de cada objeto
        if (totalAPagar > cliente.getSaldoDisponible()) {
            System.out.println("No se puede vender: no te alcanza el dinero.");
        } else {
            agua.reducirStock(cantidadAComprar);
            cliente.descontarSaldo(totalAPagar);
        }
        System.out.println("\n=== ESTADO FINAL TRAS LA COMPRA ===");
        System.out.println("Stock restante en inventario: " + agua.getCantidad());
        System.out.println("Saldo restante del cliente: $" + cliente.getSaldoDisponible());
    }
}