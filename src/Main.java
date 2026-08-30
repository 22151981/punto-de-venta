public class Main {

    public static void main(String[] args) {

        Producto agua = new Producto("P001", "Agua Litro", 4000.0, 10);

        System.out.println("--- PRUEBAS DE INVENTARIO ---");
        System.out.println("Stock inicial: " + agua.getCantidad());

        // Prueba 1: Intentar vender una cantidad negativa o cero
        System.out.println("\n1. Intentando vender -2 unidades:");
        agua.reducirStock(-2);

        // Prueba 2: Intentar vender más de lo disponible (quedan 10 y pedimos 12)
        System.out.println("\n2. Intentando vender 12 unidades:");
        agua.reducirStock(12);

        // Prueba 3: Venta exitosa (vendemos 3 de las 10)
        System.out.println("\n3. Intentando vender 3 unidades:");
        agua.reducirStock(3);

        // Prueba 4: Segunda venta exitosa (vendemos 4 de las 7 restantes)
        System.out.println("\n4. Intentando vender 4 unidades:");
        agua.reducirStock(4);

        System.out.println("\nStock final verificado con Getter: " + agua.getCantidad());
    }

}
