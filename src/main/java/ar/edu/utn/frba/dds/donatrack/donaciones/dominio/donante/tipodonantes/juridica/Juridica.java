package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "donantes_juridicas")
@PrimaryKeyJoinColumn(name = "id_donante")
public class Juridica extends Donante {
  @Column(name = "razon_social")
  private String razonSocial;
  @Column(name = "tipo_organizacion")
  @Enumerated(EnumType.STRING)
  private TipoOrganizacion tipoOrganizacion;
  @Column(name = "rubro")
  private String rubro;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_persona_juridica")
  private List<Representante> representantes;

  public Juridica(String razonSocial, Documento documento, TipoOrganizacion tipo,
                  String rubro, List<Representante> representantes) {
    super(documento, "JURIDICA");
    checkDatos(razonSocial, documento, representantes);
    this.razonSocial = razonSocial;
    this.tipoOrganizacion = tipo == null ? TipoOrganizacion.SIN_ESPECIFICAR : tipo;
    this.rubro = rubro;
    this.representantes = new ArrayList<>(representantes);
  }

  private void checkDatos(String razonSocial, Documento documento, List<Representante> representantes) {
    if (razonSocial == null || razonSocial.isBlank()) {
      throw new DominioException("El campo 'razon_social' es obligatorio, en la persona jurídica");
    }
    if (documento == null) {
      throw new DominioException("El campo 'documento' es obligatorio, en la persona jurídica, opciones posibles: " + TipoDocumento.valoresPosiblesJuridica());
    }
    if (!TipoDocumento.valoresPosiblesJuridica().contains(documento.getTipoDocumento())) {
      throw new DominioException("El campo 'documento' por ser Jurídica, solo puede ser: " + TipoDocumento.valoresPosiblesJuridica());
    }
    if (representantes == null || representantes.isEmpty()) {
      throw new DominioException("El campo 'representantes' es obligatorio, en la persona jurídica");
    }
  }

  @Override
  public String getNombreCompleto() {
    return getRazonSocial();
  }

  @Override
  public String getTipo() {
    return "JURIDICA";
  }

  @Override
  public List<MedioContacto> getContactos() {
    return this.representantes.stream().flatMap(r -> r.getContactos().stream()).toList();
  }

  @Override
  public List<MedioContacto> getContactosPrincipales() {
    return this.representantes.stream().flatMap(r -> r.getContactosPrincipales().stream()).toList();
  }

  public void actualizarDatos(String razonSocial, Documento documento, TipoOrganizacion tipo,
                              String rubro, List<Representante> representantes) {
    super.actualizarDatosBase(documento);
    checkDatos(razonSocial, documento, representantes);
    this.razonSocial = razonSocial;
    this.tipoOrganizacion = tipo == null ? TipoOrganizacion.SIN_ESPECIFICAR : tipo;
    this.rubro = rubro;
    this.representantes = new ArrayList<>(representantes);
  }

}