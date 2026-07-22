package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class PersonaJuridica extends Donante {
  private String razonSocial;
  private TipoOrganizacion tipoOrganizacion;
  private String rubro;
  private List<Representante> representantes;

  public PersonaJuridica(String razonSocial, TipoOrganizacion tipo, String rubro,
                         Documento documento, List<Representante> representantes,
                         List<MedioContacto> contactos) {
    super(documento, contactos);
    checkDatos(razonSocial, documento);
    this.razonSocial = razonSocial;
    this.tipoOrganizacion = tipo == null ? TipoOrganizacion.SIN_ESPECIFICAR : tipo;
    this.rubro = rubro;
    this.representantes = representantes != null ? new ArrayList<>(representantes) : new ArrayList<>();
  }

  private void checkDatos(String razonSocial, Documento documento) {
    if (razonSocial == null || razonSocial.isBlank()) {
      throw new DomainValidationException("El campo 'razonSocial' es obligatorio");
    }
    if (documento.getTipoDocumento() != TipoDocumento.CUIT) {
      throw new DomainValidationException("El campo 'documento' por ser Jurídica, solo puede ser CUIT");
    }
  }

  public void agregarRepresentante(Representante representante) {
    representantes.add(representante);
  }

  @Override
  public TipoDonante getTipo() {
    return TipoDonante.JURIDICA;
  }

  @Override
  public String getNombreCompleto() {
    return getRazonSocial();
  }

  public void actualizarDatos(String razonSocial, TipoOrganizacion tipo, String rubro,
                              Documento documento, List<Representante> representantes,
                              List<MedioContacto> contactos) {
    super.actualizarDatosBase(documento, contactos);
    checkDatos(razonSocial, documento);
    this.razonSocial = razonSocial;
    this.tipoOrganizacion = tipo == null ? TipoOrganizacion.SIN_ESPECIFICAR : tipo;
    this.rubro = rubro;
    this.representantes = representantes != null ? new ArrayList<>(representantes) : new ArrayList<>();
  }

  @Override
  public String toString() {
    return "PersonaJuridica{" + "razonSocial: " + razonSocial + ", rubro: " + rubro + '}';
  }

}