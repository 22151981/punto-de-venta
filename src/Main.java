public class Main {

    static void main(String[] args) {

        Producto agua = new Producto ("P001", "Agua Litro. ", 4000, 10);

        System.out.println( "El agua fue creado exitosamente en la memoria");
        System.out.println( "Precio del Producto: " + agua.getPrecio() );

    }
}
