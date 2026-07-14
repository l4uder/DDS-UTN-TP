package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.implementacion;

public class ProveedorClienteCorreo {
    private static ClienteCorreo instancia;

    public static void inicializar(ClienteCorreo motorDeCorreo) {
        instancia = motorDeCorreo;
    }

    public static ClienteCorreo getInstancia() {
        if (instancia == null) {
            throw new IllegalStateException("¡Error! El motor de correos no fue inicializado. Debe llamar a ProveedorClienteCorreo.inicializar() al arrancar el sistema.");
        }
        return instancia;
    }
}