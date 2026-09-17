public class Venta {

    private String numeroFactura;
    private Cliente cliente;
    private Producto producto;
    private int cantidad;
    private double total;
    private double gananciaTotal;
    private double efectivoRecibido;
    private double cambio;

    public Venta(String numeroFactura, Cliente cliente, Producto producto, int cantidad) {
        this.numeroFactura = numeroFactura;
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
        this.total = producto.getPrecio() * cantidad;
        this.gananciaTotal = producto.calcularGananciaUnitaria() * cantidad;
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

    public double getGananciaTotal() {
        return this.gananciaTotal;
    }

    public double getEfectivoRecibido() {
        return this.efectivoRecibido;
    }

    public double getCambio() {
        return this.cambio;
    }

    // Calcula el cambio y valida si el billete o dinero alcanza
    public double calcularCambio(double efectivoRecibido) {
        if (efectivoRecibido < this.total) {
            System.out.println("Error: El efectivo recibido ($" + efectivoRecibido + ") no cubre el total ($" + this.total + ").");
            return -1;
        }
        this.efectivoRecibido = efectivoRecibido;
        this.cambio = efectivoRecibido - this.total;
        System.out.println("Cambio a devolver: $" + this.cambio);
        return this.cambio;
    }

    public void procesarVenta() {
        if (!this.cliente.getActivo()) {
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

        // Transacción exitosa: se descuenta el stock en bodega
        this.producto.reducirStock(this.cantidad);
        System.out.println("Venta procesada exitosamente. Factura: " + this.numeroFactura);
        System.out.println("¡Gracias por su compra, " + this.cliente.getNombre() + "! Que vuelva pronto, feliz día.");
    }
}