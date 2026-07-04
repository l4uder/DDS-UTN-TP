package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.util.ArrayList;
import java.util.List;

public class PersonaJuridica extends Donante {
  private String razonSocial;
  private TipoOrganizacion tipoOrganizacion;
  private String rubro;
  private List<Representante> representantes;

  public PersonaJuridica(String razonSocial, TipoOrganizacion tipo,
                        String rubro, Documento documento,
                        List<Representante> representantes, List<MedioContacto> contactos) {
    super(documento, contactos);
    if (documento.getTipoDocumento() != TipoDocumento.CUIT) {
      throw new DomainValidationException("La persona juridica debe tener un CUIT");
    }
    this.razonSocial = razonSocial;
    this.tipoOrganizacion = tipo;
    this.rubro = rubro;
    this.representantes = representantes == null
                            ? new ArrayList<>() : new ArrayList<>(representantes);
  }

  public String getRazonSocial() {
    return razonSocial;
  }

  public TipoOrganizacion getTipoOrganizacion() {
    return tipoOrganizacion;
  }

  public String getRubro() {
    return rubro;
  }

  public List<Representante> getRepresentantes() {
    return representantes;
  }

  public void agregarRepresentante(Representante representante) {
    representantes.add(representante);
  }

  @Override
  public String toString() {
    return "PersonaJuridica{"
        + "razonSocial: " + razonSocial
        + ", rubro: " + rubro
        + '}';
  }
}