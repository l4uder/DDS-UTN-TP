package ar.edu.utn.frba.dds.donatrack.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.dominio.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.dominio.mediocontacto.MedioContacto;
import java.util.ArrayList;
import java.util.List;

public class PersonaJuridica extends Donante {
  private String razonSocial;
  private TipoOrganizacion tipoOrganizacion;
  private String rubro;
  private Documento documento;
  private List<Representante> representantes;

  public PersonaJuridica(
      String razonSocial,
      TipoOrganizacion tipo,
      String rubro,
      Documento documento,
      List<Representante> representantes,
      MedioContacto medioContPred,
      List<MedioContacto> contactosSecundarios) {
    super(medioContPred, contactosSecundarios);
    if (documento.getTipoDocumento() != TipoDocumento.CUIT) {
      throw new DomainValidationException("La persona juridica debe tener un CUIT");
    }
    this.razonSocial = razonSocial;
    this.tipoOrganizacion = tipo;
    this.documento = documento;
    this.rubro = rubro;
    this.representantes = representantes == null
        ? new ArrayList<>()
        : new ArrayList<>(representantes);
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

  public Documento getDocumento() {
    return documento;
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