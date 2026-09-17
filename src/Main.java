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
                    // ATENDER VENTA EN CAJA - CARRITO MULTI-PRODUCTO
                    try {
                        // 1. Identificación del cliente
                        String docBuscar = JOptionPane.showInputDialog("Cédula del cliente (deje en blanco para Consumidor Final):");
                        Cliente clienteVenta = new Cliente("222222222222", "Consumidor Final", 0.0, true);

                        if (docBuscar != null && !docBuscar.trim().isEmpty()) {
                            for (Cliente c : listaClientes) {
                                if (c.getCedula().equalsIgnoreCase(docBuscar.trim())) {
                                    clienteVenta = c;
                                    break;
                                }
                            }
                        }

                        // 2. Carrito temporal de compras
                        ArrayList<ItemVenta> carrito = new ArrayList<>();
                        boolean seguirComprando = true;

                        do {
                            String codVenta = JOptionPane.showInputDialog("--- CAJA: CARRITO DE COMPRAS ---\n"
                                    + "Cliente: " + clienteVenta.getNombre() + "\n"
                                    + "Productos en carrito: " + carrito.size() + "\n\n"
                                    + "Ingrese CÓDIGO o NOMBRE del producto:");

                            if (codVenta == null || codVenta.trim().isEmpty()) {
                                break;
                            }

                            // Búsqueda por código o coincidencia en nombre
                            Producto pVenta = inventario.buscarPorCodigo(codVenta.trim());
                            if (pVenta == null) {
                                for (Producto p : inventario.getProductos()) {
                                    if (p.getNombre().toLowerCase().contains(codVenta.trim().toLowerCase())) {
                                        pVenta = p;
                                        break;
                                    }
                                }
                            }

                            if (pVenta == null) {
                                JOptionPane.showMessageDialog(null, "Producto no encontrado en inventario.", "Error", JOptionPane.ERROR_MESSAGE);
                            } else {
                                int cantPedida = Integer.parseInt(JOptionPane.showInputDialog("Producto: " + pVenta.getNombre()
                                        + "\nPrecio: $" + pVenta.getPrecio()
                                        + "\nStock Disponible: " + pVenta.getCantidad()
                                        + "\n\n¿Cuántas unidades lleva?"));

                                // Validar que la cantidad pedida (más lo que ya tenga en carrito) no supere el stock
                                int yaEnCarrito = 0;
                                for (ItemVenta item : carrito) {
                                    if (item.getProducto().getCodigo().equals(pVenta.getCodigo())) {
                                        yaEnCarrito += item.getCantidad();
                                    }
                                }

                                if (cantPedida <= 0) {
                                    JOptionPane.showMessageDialog(null, "Cantidad inválida.", "Aviso", JOptionPane.WARNING_MESSAGE);
                                } else if ((yaEnCarrito + cantPedida) > pVenta.getCantidad()) {
                                    JOptionPane.showMessageDialog(null, "Stock insuficiente. Máximo disponible: " + (pVenta.getCantidad() - yaEnCarrito), "Aviso", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    carrito.add(new ItemVenta(pVenta, cantPedida));
                                    JOptionPane.showMessageDialog(null, cantPedida + "x " + pVenta.getNombre() + " agregado(s) al carrito.");
                                }
                            }

                            int respuesta = JOptionPane.showConfirmDialog(null, "¿Desea registrar otro producto para este cliente?", "Seguir comprando", JOptionPane.YES_NO_OPTION);
                            seguirComprando = (respuesta == JOptionPane.YES_OPTION);

                        } while (seguirComprando);

                        // Si no agregó nada, cancelamos
                        if (carrito.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Venta cancelada: No se ingresaron productos al carrito.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }

                        // 3. Cálculo del total general
                        double totalFactura = 0.0;
                        String cuerpoFactura = "";
                        for (ItemVenta item : carrito) {
                            totalFactura += item.getSubtotal();
                            cuerpoFactura += item.getProducto().getNombre() + " x" + item.getCantidad() + " = $" + item.getSubtotal() + "\n";
                        }

                        // 4. Cobro en efectivo y cambio
                        double efectivo = Double.parseDouble(JOptionPane.showInputDialog("===== LIQUIDACIÓN DE CUENTA =====\n\n"
                                + cuerpoFactura
                                + "-----------------------------------------\n"
                                + "TOTAL A PAGAR: $" + totalFactura + "\n\n"
                                + "Ingrese dinero recibido en efectivo:"));

                        if (efectivo < totalFactura) {
                            JOptionPane.showMessageDialog(null, "Pago incompleto. El efectivo ($" + efectivo + ") no alcanza. Faltan $" + (totalFactura - efectivo), "Venta Cancelada", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Transacción exitosa: descontamos stock real de cada producto vendido
                            for (ItemVenta item : carrito) {
                                item.getProducto().reducirStock(item.getCantidad());
                            }

                            double cambio = efectivo - totalFactura;
                            String numFactura = "FAC-" + String.format("%03d", consecutivoFactura++);

                            String tirilla = "=========================================\n"
                                    + "               FACTURA DE VENTA\n"
                                    + "=========================================\n"
                                    + "No. Factura: " + numFactura + "\n"
                                    + "Cliente: " + clienteVenta.getNombre() + " (" + clienteVenta.getCedula() + ")\n"
                                    + "-----------------------------------------\n"
                                    + cuerpoFactura
                                    + "-----------------------------------------\n"
                                    + "TOTAL:              $" + totalFactura + "\n"
                                    + "Efectivo Recibido:  $" + efectivo + "\n"
                                    + "CAMBIO (VUELTAS):   $" + cambio + "\n"
                                    + "-----------------------------------------\n"
                                    + "¡Gracias por su compra! Vuelva pronto.\n"
                                    + "=========================================";

                            JOptionPane.showMessageDialog(null, tirilla, "Recibo Generado", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Debe ingresar valores numéricos válidos.", "Dato Erróneo", JOptionPane.ERROR_MESSAGE);
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