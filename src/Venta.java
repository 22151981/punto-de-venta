public class Venta {

    private String numeroFactura;
    private Cliente cliente;
    private Producto producto;
    private int cantidad;
    private double total;

    public Venta(String numeroFactura, Cliente cliente, Producto producto, int cantidad) {
        this.numeroFactura = numeroFactura;
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.total = producto.getPrecio() * cantidad;
    }

    public String getNumeroFactura() {
        return this.numeroFactura;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public Producto getProducto() {
        return this.producto;
    }

    public int getCantidad() {
        return this.cantidad;
    }

    public double getTotal() {
        return this.total;
    }

public void procesarVenta() {

        if ( !this.cliente.getActivo()) {
            System.out.println("No se puede realizar la venta: cliente inactivo.");
            return;
        }
        if (this.cantidad <= 0) {
        System.out.println("No se puede realizar la venta: cantidad inválida.");
        return;
    }
        if (this.producto.getCantidad() < this.cantidad) {
        System.out.println("No se puede realizar la venta: stock insuficiente.");
        return;
    }
        if (this.cliente.getSaldoDisponible() < this.total) {
            System.out.println("No se puede realizar la venta: saldo insuficiente.");
            return;
        }

//transaccion exitosa, se descuenta el stock y el saldo del cliente
        this.producto.reducirStock(this.cantidad);
        this.cliente.descontarSaldo(this.total);
        System.out.println("Venta procesada exitosamente. Total a pagar: $" + this.total);

    // Mensaje de despedida al cliente
    System.out.println("¡Gracias por su compra, " + this.cliente.getNombre() + "! Que vuelva pronto, feliz día.");

    }

}
