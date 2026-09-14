package ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.entrega.RegistroEntrega;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.Genero;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Representante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.TipoOrganizacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.persona.Humana;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.tipodonantes.juridica.Juridica;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.MedioContacto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Donante {
  @Setter
  private String id;
  private Documento documento;
  private List<RegistroEntrega> entregas;

  protected Donante(Documento documento) {
    checkDatos(documento);
    this.documento = documento;
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

  public abstract String getTipoPersona();

  protected abstract List<MedioContacto> getContactosPrincipales();

  protected abstract List<MedioContacto> getContactos();

  protected void actualizarDatosBase(Documento documento) {
    checkDatos(documento);
    this.documento = documento;
  }

}