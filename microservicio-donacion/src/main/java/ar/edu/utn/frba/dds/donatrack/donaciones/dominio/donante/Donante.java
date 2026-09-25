package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.entrega.RegistroEntrega;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "donantes")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Donante {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_donante")
  private Long id;
  @Embedded
  private Documento documento;
  @Column(name = "tipo_donante")
  private String tipo;
  @OneToMany(mappedBy = "donante")
  private List<RegistroEntrega> entregas;

  protected Donante(Documento documento, String tipo) {
    checkDatos(documento);
    this.documento = documento;
    this.tipo = tipo;
    this.entregas = new ArrayList<>();
  }

  private void checkDatos(Documento documento) {
    if (documento == null) {
      throw new DominioException("El documento no puede ser null");
    }
  }

  public abstract String getNombreCompleto();

  public RegistroEntrega getUltimaEntrega() {
    //return this.entregas.stream().max(Comparator.comparing(r -> r.getFecha())).orElse(null);
    if (this.entregas.isEmpty()) return null;

    return this.entregas.get(this.entregas.size() - 1);
  }

  public void recibirNotificacion(String mensaje) {
    List<MedioContacto> contactos = getContactosPrincipales();
    contactos.forEach(c -> c.enviarMensaje(mensaje));
  }

  public void recibirNotificacionImportante(String mensaje) {
    List<MedioContacto> contactos = getContactos();
    contactos.forEach(c -> c.enviarMensaje(mensaje));
  }

  public boolean estaAusentePorMasDe(Integer dias) {
    RegistroEntrega ultima = this.getUltimaEntrega();
    if (ultima == null) return false; // A los nuevos No los vamos a considerar como ausentes

    LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
    return ultima.getFecha().isBefore(fechaLimite);
  }

  protected abstract List<MedioContacto> getContactosPrincipales();

  protected abstract List<MedioContacto> getContactos();

  protected void actualizarDatosBase(Documento documento) {
    checkDatos(documento);
    this.documento = documento;
  }

}