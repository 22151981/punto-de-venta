public class Cliente {

  //Atributos clientes

    private String cedula;
    private String nombre;
    private double saldoDisponible;
    private boolean activo;

    public Cliente(String cedula, String nombre, double saldoDisponible, boolean activo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.saldoDisponible = saldoDisponible;
        this.activo = activo;

    }

    public String getCedula() {
        return cedula;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null && !nombre.trim().isEmpty()) {
            this.nombre = nombre;
        } else {
            System.out.println("Error: El nombre no puede estar vacío.");
        }
    }

    public double getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(double saldoDisponible) {

        if (saldoDisponible >= 0) {
            this.saldoDisponible = saldoDisponible;
        } else {
            System.out.println("Error ... El saldo no puede ser negativo .... ");
    }
    }
    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


}



