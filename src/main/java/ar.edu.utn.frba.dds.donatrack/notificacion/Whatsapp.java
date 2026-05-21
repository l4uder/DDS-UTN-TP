package ar.edu.utn.frba.dds.donatrack.notificacion;

import ar.edu.utn.frba.dds.donatrack.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donante.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donante.TipoContacto;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicioWhatsapp.ServicioWhatsapp;

import java.util.List;

public class Whatsapp implements Notificador {
    private ServicioWhatsapp servicioWhatsapp;

    public Whatsapp(ServicioWhatsapp servicioWhatsapp) {
        this.servicioWhatsapp = servicioWhatsapp;
    }

    @Override
    public void notificar(Donante donante, String mensaje) {
        List<MedioContacto> mediosDeContacto = donante.getMediosContacto(TipoContacto.WHATSAPP);
        List<String> numerosAEnviar = mediosDeContacto.stream().map(MedioContacto::getDetalle).toList();

        numerosAEnviar.forEach(numero-> servicioWhatsapp.notificar(numero, mensaje));
    }
}
