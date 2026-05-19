package ar.edu.utn.frba.dds.donatrack.donacion;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

public class Donacion {
  private UUID id_Donacion;
  private String descripcion;
  private List<Bien> bienes;
  private EstadoDonacion estado;
  private List<HistorialEstado> historialEstados;

  public Donacion(String descripcion, List<Bien> bienes) {
    this.id_Donacion = UUID.randomUUID();
    this.estado = EstadoDonacion.EN_DEPOSITO;
    this.bienes = bienes;
    this.historialEstados = new ArrayList<>();
    this.historialEstados.add(new HistorialEstado(this.estado, descripcion));
  }

  public void cambiarEstado(EstadoDonacion estado){
    this.estado = estado;
  }
}