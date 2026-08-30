public class Producto {

    //Atributos (caracteristicas del producto)
    private String codigo;
    private String nombre;
    private double precio;
    private int cantidad;

    //Constructor (para crear el producto con datos iniciales)
    public Producto( String codigo, String nombre, double precio, int cantidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    //Getters y Setters (leer y cambiar datos)
    public double getPrecio() {
        return this.precio;
    }


        public String getNombre() {
                return this.nombre;
        }


        public String getCodigo (){
            return  this.codigo;
    }
    public int getCantidad (){
        return  this.cantidad;
    }

            public void setPrecio(double nuevoPrecio) {

                if (nuevoPrecio > 0) {
                    this.precio = nuevoPrecio;
                } else {
                    System.out.println("Error de precio - debe ser mayor a 0 .... ");

                }
            }

                public void setNombre(String nuevoNombre) {

                    if (nuevoNombre != null && !nuevoNombre.trim().isEmpty()) {
                        this.nombre = nuevoNombre;
                    } else {
                        System.out.println("Error: El NOmbre no es valido, este espacio no debe estar vacio .... ");

                    }
                }
    // --- MÉTODOS DE NEGOCIO (son las Acciones)
                public void  reducirStock( int cantidadVendida) {
                    if (cantidadVendida <= 0) {
                        System.out.println(" Error: La cantidad a vender debe ser mayor a 0. ");
                    } else if (cantidadVendida > this.cantidad) {
                        System.out.println("Error: Stock insuficiente. solo quedan: " + this.cantidad + " unidades de " + this.nombre);

                    } else {
                        this.cantidad -= cantidadVendida;

                            System.out.println("Venta exitosa. Stock restante de " + this.nombre + ":" + this.cantidad);

                        }

                    }

    }



