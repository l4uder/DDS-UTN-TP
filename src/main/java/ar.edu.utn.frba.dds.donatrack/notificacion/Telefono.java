package ar.edu.utn.frba.dds.donatrack.notificacion;

import ar.edu.utn.frba.dds.donatrack.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donante.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.donante.tipoContacto;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicioSms.ServicioSMS;
import ar.edu.utn.frba.dds.donatrack.notificacion.servicioWhatsapp.ServicioWhatsapp;

import java.util.List;

public class Telefono implements Notificador{
    private ServicioSMS servicioSMS;

    public Telefono(ServicioSMS servicioSMS) {
        this.servicioSMS = servicioSMS;
    }

    @Override
    public void notificar(Donante donante, String mensaje) {
        List<MedioContacto> mediosDeContacto = donante.getMediosContacto(tipoContacto.TELEFONO);
        List<String> numerosAEnviar = mediosDeContacto.stream().map(MedioContacto::getDetalle).toList();

        numerosAEnviar.forEach(numero-> servicioSMS.notificar(numero, mensaje));
    }
}
