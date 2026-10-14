import javax.swing.JOptionPane;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        ProductoDAO productoDAO = new ProductoDAO();
        Inventario inventario = new Inventario();
        ArrayList<Cliente> listaClientes = new ArrayList<>();

        // 1. Carga inicial directa desde MySQL
        ArrayList<Producto> productosBD = productoDAO.listarTodos();
        for (Producto p : productosBD) {
            inventario.registrarProducto(p);
        }

        // Cliente genérico por defecto
        listaClientes.add(new Cliente("222222222222", "Consumidor Final", 0.0, true));

        int consecutivoFactura = 1;
        String opcion = "";

        do {
            String menu = "========= SISTEMA POS - GESTIÓN TOTAL =========\n"
                    + "--- INVENTARIO (CRUD CON MYSQL) ---\n"
                    + "1. Registrar nuevo producto (Create)\n"
                    + "2. Listar catálogo completo (Read)\n"
                    + "3. Buscar producto por código (Read)\n"
                    + "4. Actualizar precio o stock (Update)\n"
                    + "5. Eliminar producto de bodega (Delete)\n"
                    + "--- CLIENTES Y CAJA ---\n"
                    + "6. Registrar nuevo cliente\n"
                    + "7. Atender venta en caja (Carrito + Vueltas)\n"
                    + "--- FINANZAS ---\n"
                    + "8. Ver reporte de inversión en mercancía\n"
                    + "9. Salir del sistema\n\n"
                    + "Elija una opción (1-9):";

            opcion = JOptionPane.showInputDialog(null, menu, "Menú Principal", JOptionPane.QUESTION_MESSAGE);

            if (opcion == null) {
                opcion = "9";
            }

            switch (opcion) {

                case "1":
                    // CREATE PRODUCTO
                    try {
                        String cod = JOptionPane.showInputDialog("Ingrese código del producto (ej: 047):");
                        if (cod == null || cod.trim().isEmpty()) break;

                        String nom = JOptionPane.showInputDialog("Ingrese nombre del producto:");
                        double precVenta = Double.parseDouble(JOptionPane.showInputDialog("Precio de venta al público:"));
                        int cant = Integer.parseInt(JOptionPane.showInputDialog("Cantidad inicial en stock:"));
                        double costoProv = Double.parseDouble(JOptionPane.showInputDialog("Costo pagado al proveedor:"));

                        Producto nuevo = new Producto(cod.trim(), nom.trim(), precVenta, cant, costoProv);

                        if (productoDAO.insertar(nuevo)) {
                            inventario.registrarProducto(nuevo);
                            JOptionPane.showMessageDialog(null, "Producto '" + nom + "' guardado exitosamente en base de datos.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al guardar el producto en MySQL.", "Error BD", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Debe ingresar valores numéricos válidos en precio, costo o cantidad.", "Dato Inválido", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "2":
                    // READ (LISTAR TODO)
                    if (inventario.getProductos().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No hay productos disponibles en bodega.", "Catálogo Vacío", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    }

                    StringBuilder catalogo = new StringBuilder("=== CATÁLOGO DE PRODUCTOS EN BODEGA ===\n\n");
                    for (Producto p : inventario.getProductos()) {
                        catalogo.append("• [").append(p.getCodigo()).append("] ")
                                .append(p.getNombre())
                                .append(" | Venta: $").append(p.getPrecio())
                                .append(" | Costo: $").append(p.getCostoProveedor())
                                .append(" | Stock: ").append(p.getCantidad()).append("\n");
                    }
                    JOptionPane.showMessageDialog(null, catalogo.toString(), "Catálogo de Productos", JOptionPane.INFORMATION_MESSAGE);
                    break;

                case "3":
                    // READ (BUSCAR POR CÓDIGO)
                    String codConsulta = JOptionPane.showInputDialog("Ingrese el código del producto a buscar:");
                    if (codConsulta == null || codConsulta.trim().isEmpty()) break;

                    Producto pBuscado = inventario.buscarPorCodigo(codConsulta.trim());

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
                    if (codActualizar == null || codActualizar.trim().isEmpty()) break;

                    Producto pActualizar = inventario.buscarPorCodigo(codActualizar.trim());

                    if (pActualizar != null) {
                        String subOpcion = JOptionPane.showInputDialog("Producto: " + pActualizar.getNombre()
                                + "\n1. Modificar Precio de Venta (Actual: $" + pActualizar.getPrecio() + ")"
                                + "\n2. Agregar Stock por llegada de mercancía (Actual: " + pActualizar.getCantidad() + ")"
                                + "\nElija opción (1 o 2):");

                        try {
                            if ("1".equals(subOpcion)) {
                                double nuevoPrecio = Double.parseDouble(JOptionPane.showInputDialog("Ingrese nuevo precio de venta:"));
                                pActualizar.setPrecio(nuevoPrecio);
                                JOptionPane.showMessageDialog(null, "Precio actualizado con éxito a $" + nuevoPrecio);
                            } else if ("2".equals(subOpcion)) {
                                int unidadesNuevas = Integer.parseInt(JOptionPane.showInputDialog("¿Cuántas unidades surtió el proveedor?:"));
                                int nuevoStockTotal = pActualizar.getCantidad() + unidadesNuevas;
                                pActualizar.setCantidad(nuevoStockTotal);

                                // Actualizar en MySQL
                                productoDAO.actualizarStock(pActualizar.getCodigo(), nuevoStockTotal);
                                JOptionPane.showMessageDialog(null, "Stock actualizado a " + nuevoStockTotal + " unidades y sincronizado con MySQL.");
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
                    if (codEliminar == null || codEliminar.trim().isEmpty()) break;

                    boolean eliminadoBD = productoDAO.eliminar(codEliminar.trim());

                    if (eliminadoBD) {
                        inventario.eliminarProducto(codEliminar.trim());
                        JOptionPane.showMessageDialog(null, "Producto retirado del catálogo y de la base de datos.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo eliminar: verifique el código en MySQL.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "6":
                    // REGISTRAR CLIENTE
                    try {
                        String docCliente = JOptionPane.showInputDialog("Ingrese cédula o documento del cliente:");
                        if (docCliente == null || docCliente.trim().isEmpty()) break;

                        String nomCliente = JOptionPane.showInputDialog("Ingrese nombre completo:");
                        double saldo = Double.parseDouble(JOptionPane.showInputDialog("Saldo disponible inicial:"));

                        Cliente nuevoCliente = new Cliente(docCliente.trim(), nomCliente.trim(), saldo, true);
                        ClienteDAO clienteDAO = new ClienteDAO();
                        clienteDAO.insertar(nuevoCliente);
                        JOptionPane.showMessageDialog(null, "Cliente " + nomCliente + " registrado exitosamente.");
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Error: Ingrese un valor numérico válido en el saldo.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    break;

                case "7":
                    // ATENDER VENTA EN CAJA (CARRITO + MYSQL + CAMBIO)
                    try {
                        // 1. Identificar cliente
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

                        // 2. Carrito de compras
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

                            // Búsqueda por código o texto
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

                        if (carrito.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Venta cancelada: Carrito vacío.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }

                        // 3. Liquidación
                        double totalFactura = 0.0;
                        StringBuilder cuerpoFactura = new StringBuilder();
                        for (ItemVenta item : carrito) {
                            totalFactura += item.getSubtotal();
                            cuerpoFactura.append(item.getProducto().getNombre())
                                    .append(" x").append(item.getCantidad())
                                    .append(" = $").append(item.getSubtotal()).append("\n");
                        }

                        // 4. Pago en efectivo y cambio
                        double efectivo = Double.parseDouble(JOptionPane.showInputDialog("===== LIQUIDACIÓN DE CUENTA =====\n\n"
                                + cuerpoFactura
                                + "-----------------------------------------\n"
                                + "TOTAL A PAGAR: $" + totalFactura + "\n\n"
                                + "Ingrese dinero recibido en efectivo:"));

                        if (efectivo < totalFactura) {
                            JOptionPane.showMessageDialog(null, "Pago incompleto. El efectivo ($" + efectivo + ") no cubre el total. Faltan $" + (totalFactura - efectivo), "Venta Cancelada", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Descontar en memoria y persistir en MySQL
                            for (ItemVenta item : carrito) {
                                Producto enBodega = inventario.buscarPorCodigo(item.getProducto().getCodigo());
                                if (enBodega != null) {
                                    enBodega.reducirStock(item.getCantidad());
                                    productoDAO.actualizarStock(enBodega.getCodigo(), enBodega.getCantidad());
                                }
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
                        JOptionPane.showMessageDialog(null, "Error: Ingrese valores numéricos válidos.", "Dato Erróneo", JOptionPane.ERROR_MESSAGE);
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