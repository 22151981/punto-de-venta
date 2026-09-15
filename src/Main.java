import javax.swing.JOptionPane;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        Inventario inventario = new Inventario();
        ArrayList<Cliente> listaClientes = new ArrayList<>();

        // Datos iniciales para pruebas rápidas
        inventario.registrarProducto(new Producto("P001", "Agua Mineral Litro", 4000.0, 10, 2000.0));
        inventario.registrarProducto(new Producto("P002", "Galletas Festival", 1500.0, 20, 800.0));
        listaClientes.add(new Cliente("123456", "Javier Layton", 50000.0, true));

        int consecutivoFactura = 1;
        String opcion = "";

        do {
            String menu = "========= SISTEMA POS - GESTIÓN TOTAL =========\n"
                    + "--- INVENTARIO (CRUD) ---\n"
                    + "1. Registrar nuevo producto (Create)\n"
                    + "2. Listar catálogo completo (Read)\n"
                    + "3. Buscar producto por código (Read)\n"
                    + "4. Actualizar precio o stock (Update)\n"
                    + "5. Eliminar producto de bodega (Delete)\n"
                    + "--- CLIENTES Y CAJA ---\n"
                    + "6. Registrar nuevo cliente\n"
                    + "7. Atender venta en caja\n"
                    + "--- FINANZAS ---\n"
                    + "8. Ver reporte de inversión en mercancía\n"
                    + "9. Salir del sistema\n\n"
                    + "Elija una opción (1-9):";

            opcion = JOptionPane.showInputDialog(null, menu, "Menú Principal", JOptionPane.QUESTION_MESSAGE);

            if (opcion == null) {
                opcion = "9"; // Si cierran la ventana con la X o Cancelar, salir limpio
            }

            switch (opcion) {

                case "1":
                    // CREATE PRODUCTO
                    try {
                        String cod = JOptionPane.showInputDialog("Ingrese código del producto (ej: P003):");
                        String nom = JOptionPane.showInputDialog("Ingrese nombre del producto:");
                        double precVenta = Double.parseDouble(JOptionPane.showInputDialog("Precio de venta al público:"));
                        int cant = Integer.parseInt(JOptionPane.showInputDialog("Cantidad inicial en stock:"));
                        double costoProv = Double.parseDouble(JOptionPane.showInputDialog("Costo pagado al proveedor:"));

                        inventario.registrarProducto(new Producto(cod, nom, precVenta, cant, costoProv));
                        JOptionPane.showMessageDialog(null, "Producto '" + nom + "' registrado en inventario.");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Debe ingresar valores numéricos válidos en precio, costo o cantidad.", "Dato Inválido", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "2":
                    // READ (LISTAR TODOS EN VENTANA)
                    String catalogo = "=== CATÁLOGO DE PRODUCTOS EN BODEGA ===\n\n";
                    for (Producto p : inventario.getProductos()) {
                        catalogo += "• [" + p.getCodigo() + "] " + p.getNombre()
                                + " | Venta: $" + p.getPrecio()
                                + " | Costo: $" + p.getCostoProveedor()
                                + " | Stock: " + p.getCantidad() + "\n";
                    }
                    JOptionPane.showMessageDialog(null, catalogo, "Catálogo de Productos", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case "3":
                    // READ (BUSCAR POR CÓDIGO)
                    String codConsulta = JOptionPane.showInputDialog("Ingrese el código del producto a buscar:");
                    Producto pBuscado = inventario.buscarPorCodigo(codConsulta);

                    if (pBuscado != null) {
                        String ficha = "=== FICHA TÉCNICA DEL PRODUCTO ===\n"
                                + "Código: " + pBuscado.getCodigo() + "\n"
                                + "Nombre: " + pBuscado.getNombre() + "\n"
                                + "Precio de Venta: $" + pBuscado.getPrecio() + "\n"
                                + "Costo Proveedor: $" + pBuscado.getCostoProveedor() + "\n"
                                + "Ganancia Unitaria: $" + pBuscado.calcularGananciaUnitaria() + "\n"
                                + "Stock disponible: " + pBuscado.getCantidad() + " unidades";
                        JOptionPane.showMessageDialog(null, ficha, "Producto Encontrado", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No existe producto con código: " + codConsulta, "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                    break;

                case "4":
                    // UPDATE PRODUCTO
                    String codActualizar = JOptionPane.showInputDialog("Ingrese código del producto a actualizar:");
                    Producto pActualizar = inventario.buscarPorCodigo(codActualizar);

                    if (pActualizar != null) {
                        String subOpcion = JOptionPane.showInputDialog("Producto: " + pActualizar.getNombre()
                                + "\n1. Modificar Precio de Venta (Actual: $" + pActualizar.getPrecio() + ")"
                                + "\n2. Agregar Stock por llegada de distribuidor (Actual: " + pActualizar.getCantidad() + ")"
                                + "\nElija opción (1 o 2):");

                        try {
                            if ("1".equals(subOpcion)) {
                                double nuevoPrecio = Double.parseDouble(JOptionPane.showInputDialog("Ingrese nuevo precio de venta:"));
                                pActualizar.setPrecio(nuevoPrecio);
                                JOptionPane.showMessageDialog(null, "Precio actualizado con éxito a $" + nuevoPrecio);
                            } else if ("2".equals(subOpcion)) {
                                int unidadesNuevas = Integer.parseInt(JOptionPane.showInputDialog("¿Cuántas unidades surtió el proveedor?:"));
                                pActualizar.setCantidad(pActualizar.getCantidad() + unidadesNuevas);
                                JOptionPane.showMessageDialog(null, "Stock actualizado a " + pActualizar.getCantidad() + " unidades.");
                            }
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Error: Debe ingresar un valor numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Producto no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "5":
                    // DELETE PRODUCTO
                    String codEliminar = JOptionPane.showInputDialog("Ingrese código del producto a retirar:");
                    boolean eliminado = inventario.eliminarProducto(codEliminar);

                    if (eliminado) {
                        JOptionPane.showMessageDialog(null, "Producto retirado del catálogo exitosamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar: el código no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "6":
                    // REGISTRAR CLIENTE
                    try {
                        String docCliente = JOptionPane.showInputDialog("Ingrese cédula o documento del cliente:");
                        String nomCliente = JOptionPane.showInputDialog("Ingrese nombre completo:");
                        double saldo = Double.parseDouble(JOptionPane.showInputDialog("Saldo disponible inicial:"));

                        listaClientes.add(new Cliente(docCliente, nomCliente, saldo, true));
                        JOptionPane.showMessageDialog(null, "Cliente " + nomCliente + " registrado exitosamente.");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Ingrese un valor numérico válido en el saldo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "7":
                    // ATENDER VENTA EN CAJA
                    String docBuscar = JOptionPane.showInputDialog("Ingrese cédula del cliente que va a comprar:");
                    Cliente clienteVenta = null;

                    for (Cliente c : listaClientes) {
                        // Si en Cliente.java usas getCedula() o getDocumento(), aquí se evalúa:
                        if (c.getCedula().equals(docBuscar)) {
                            clienteVenta = c;
                            break;
                        }
                    }

                    if (clienteVenta == null) {
                        JOptionPane.showMessageDialog(null, "Cliente no encontrado. Regístrelo primero en la opción 6.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        break;
                    }

                    String codVenta = JOptionPane.showInputDialog("--- CAJA REGISTRADORA ---\nCliente: " + clienteVenta.getNombre() + "\nIngrese código del producto:");
                    Producto pVenta = inventario.buscarPorCodigo(codVenta);

                    if (pVenta == null) {
                        JOptionPane.showMessageDialog(null, "El producto no existe en bodega.", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    }

                    try {
                        int cantVenta = Integer.parseInt(JOptionPane.showInputDialog("Producto: " + pVenta.getNombre()
                                + "\nPrecio Unitario: $" + pVenta.getPrecio()
                                + "\nStock Disponible: " + pVenta.getCantidad()
                                + "\n\n¿Cuántas unidades desea comprar?"));

                        String numFactura = "FAC-" + String.format("%03d", consecutivoFactura);
                        Venta venta = new Venta(numFactura, clienteVenta, pVenta, cantVenta);
                        venta.procesarVenta();
                        consecutivoFactura++;

                        String resumen = "=== FACTURA GENERADA: " + numFactura + " ===\n"
                                + "Cliente: " + clienteVenta.getNombre() + "\n"
                                + "Producto: " + pVenta.getNombre() + "\n"
                                + "Unidades: " + cantVenta + "\n"
                                + "Total Pagado: $" + venta.getTotal() + "\n"
                                + "Stock Restante: " + pVenta.getCantidad() + "\n"
                                + "Saldo Restante Cliente: $" + clienteVenta.getSaldoDisponible();

                        JOptionPane.showMessageDialog(null, resumen, "Venta Exitosa", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Debe ingresar una cantidad entera válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "8":
                    // REPORTE FINANCIERO
                    double inversion = inventario.calcularInversionTotal();
                    JOptionPane.showMessageDialog(null, "=== BALANCE GENERAL DE BODEGA ===\n\nCapital total invertido en mercancía: $" + inversion, "Finanzas", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case "9":
                    JOptionPane.showMessageDialog(null, "Cerrando el sistema POS. ¡Buen turno!");
                    break;

                default:
                    JOptionPane.showMessageDialog(null, "Opción inválida. Seleccione un número del 1 al 9.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    break;
            }

        } while (!opcion.equals("9"));
    }
}