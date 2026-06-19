package ar.edu.utn.frba.dds.donatrack.donante;

import ar.edu.utn.frba.dds.donatrack.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.medioContacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.medioContacto.MedioContacto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class Donante {
    protected MedioContacto medioDeContactoPred;
    protected Set<MedioContacto> mediosDeContacto;
    protected List<RegistroEntrega> entregas = new ArrayList<>();

    public Donante(MedioContacto medioDeContacto, List<MedioContacto> mediosDeContacto) {
        if (medioDeContacto == null) {
            throw new DomainValidationException("El medio de contacto principal no puede ser null");
        }
        this.mediosDeContacto = new HashSet<>(mediosDeContacto);
        this.mediosDeContacto.add(medioDeContacto);
        this.medioDeContactoPred = medioDeContacto;

        if (this.mediosDeContacto.stream().noneMatch(el -> el instanceof CorreoDeContato)) {
            throw new DomainValidationException("Al menos un medio de contacto debe ser un correo");
        }
        if (this.mediosDeContacto.stream().filter(el -> el instanceof CorreoDeContato).toList().size() > 1) {
            throw new DomainValidationException("No puede haber mas de un correo como medio de contacto");
        }
    }

    public void cambiarContactoPred(MedioContacto contacto) {
        if (contacto == null) {
            throw new DomainValidationException("El medio de contacto principal no puede ser null");
        }
        var mediosDeContactoTest = new HashSet<>(mediosDeContacto);
        mediosDeContactoTest.add(contacto);
        if (this.mediosDeContacto.stream().filter(el -> el instanceof CorreoDeContato).toList().size() > 1) {
            throw new DomainValidationException("No puede haber mas de un correo como medio de contacto");
        }
        medioDeContactoPred = contacto;
        mediosDeContacto = mediosDeContactoTest;
    }

    public List<MedioContacto> getMediosContacto() {
        return mediosDeContacto.stream().toList();
    }

    public void agregarContactoSecundario(MedioContacto contacto) {
        this.mediosDeContacto.add(contacto);
    }

    public MedioContacto getMedioDeContactoPred() {
        return medioDeContactoPred;
    }

    public Set<MedioContacto> getMediosDeContacto() {
        return mediosDeContacto;
    }

    public String getEmail() {
        var correoOpt = mediosDeContacto.stream().dropWhile(el -> !(el instanceof CorreoDeContato)).findFirst();

        if (correoOpt.isEmpty()) {
            throw new IllegalStateException("Donante no tiene correo como medio de contacto");
        }
        return ((CorreoDeContato) correoOpt.get()).getCorreo();
    }
}
