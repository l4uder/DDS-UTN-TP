package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.EstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoEntregaExitosa;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoEntregaFallida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.eventos.EventoInicioDeRuta;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.BienMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.DonacionMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.EstadoDonacionMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.bien.BienDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.DonacionRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.notificacion.AppEventBus;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.EnTrasladoDonacionDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.EntregadaDonacionDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.ErrorEntregaDonacionDto;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BodyException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.javalin.http.Context;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class DonacionController {
  private final DonacionRepository repoDonaciones;
  private final DonanteRepository repoDonantes;

  public DonacionController(DonacionRepository repoDonaciones, DonanteRepository repoDonantes) {
    this.repoDonaciones = repoDonaciones;
    this.repoDonantes = repoDonantes;
  }

  public void crear(Context ctx) {
    //Cosas que recibo por Body
    DonacionRequest request = ctx.bodyAsClass(DonacionRequest.class);
    List<String> idDonantes = request.donanteIds();
    List<BienDto> bienesDto = request.bienes();

    List<Donante> donantes = idDonantes.stream().map(this::buscarDonantePorId).toList();
    Donacion donacion = DonacionMapper.aDominio(bienesDto, donantes);

    repoDonaciones.guardar(donacion);
    ctx.status(201).json(DonacionMapper.aDto(donacion));
  }

  public void obtenerTodos(Context ctx) {
    //Cosas que recibo por URL --> Query param
    String estadoDonacion = ctx.queryParam("estado");

    TipoEstadoDonacion estado = aTipoEstadoDonacion(estadoDonacion);

    List<Donacion> donaciones = estado == null ? repoDonaciones.buscarTodos() : repoDonaciones.buscarTodoPorEstado(estado);
    ctx.status(200).json(DonacionMapper.aDto(donaciones));
  }

  public void obtener(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");

    Donacion donacion = buscarDonacionPorId(idDonacion);

    ctx.status(200).json(DonacionMapper.aDto(donacion));
  }

  public void actualizar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");
    //Cosas que recibo por Body
    DonacionRequest request = ctx.bodyAsClass(DonacionRequest.class);
    List<BienDto> bienesDto = request.bienes();

    Donacion donacion = buscarDonacionPorId(idDonacion);
    List<Bien> bienes = BienMapper.aDominio(bienesDto);

    donacion.reemplazarBienes(bienes);
    repoDonaciones.actualizar(donacion);
    ctx.status(200).json(DonacionMapper.aDto(donacion));
  }

  public void eliminar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");

    Donacion donacion = buscarDonacionPorId(idDonacion);

    repoDonaciones.eliminar(donacion);
    ctx.status(204);
  }

  public void historialEstados(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");

    Donacion donacion = buscarDonacionPorId(idDonacion);

    List<EstadoDonacion> historialEstados = donacion.getHistorialEstados();
    ctx.status(200).json(EstadoDonacionMapper.aDto(historialEstados));
  }

  public void donacionDevueltaADeposito(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");

    Donacion donacion = buscarDonacionPorId(idDonacion);

    donacion.confirmarRecepcionDeposito();
    repoDonaciones.actualizar(donacion);
    ctx.status(200).json(DonacionMapper.aDto(donacion));
  }

  public void donacionEnTraslado(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");
    //Cosas que recibo por Body
    EnTrasladoDonacionDto request = ctx.bodyAsClass(EnTrasladoDonacionDto.class);
    if (request == null || request.linkMapa() == null || request.linkMapa().isBlank()) throw new BodyException("El body no tiene el link del mapa");
    String mapa = request.linkMapa();

    Donacion donacion = buscarDonacionPorId(idDonacion);

    donacion.confirmarTrasladoEnCurso();
    AppEventBus.getInstance().post(new EventoInicioDeRuta(donacion, LocalDate.now(), mapa));
    repoDonaciones.actualizar(donacion);
    ctx.status(200).json(DonacionMapper.aDto(donacion));
  }

  public void donacionVencida(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");

    Donacion donacion = buscarDonacionPorId(idDonacion);

    donacion.marcarVencida();
    repoDonaciones.actualizar(donacion);
    ctx.status(200).json(DonacionMapper.aDto(donacion));
  }

  public void donacionListaParaEntregar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");

    Donacion donacion = buscarDonacionPorId(idDonacion);

    donacion.confirmarRuta();
    repoDonaciones.actualizar(donacion);
    ctx.status(200).json(DonacionMapper.aDto(donacion));
  }

  public void donacionEntregada(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");
    //Cosas que recibo por Body
    EntregadaDonacionDto request = ctx.bodyAsClass(EntregadaDonacionDto.class);
    if (request == null || request.camionId() == null || request.camionId().isBlank()) throw new BodyException("El body no tiene el id del camion");
    String idCamion = request.camionId();

    Donacion donacion = buscarDonacionPorId(idDonacion);

    donacion.confirmarEntrega();
    AppEventBus.getInstance().post(new EventoEntregaExitosa(donacion, LocalDate.now(), idCamion));
    repoDonaciones.actualizar(donacion);
    ctx.status(200).json(DonacionMapper.aDto(donacion));
  }

  public void donacionEntregaFallida(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");
    //Cosas que recibo por Body
    ErrorEntregaDonacionDto request = ctx.bodyAsClass(ErrorEntregaDonacionDto.class);
    if (request == null || request.observacion() == null || request.observacion().isBlank()) throw new BodyException("El body no tiene la observacion del error");
    String observacion = request.observacion();

    Donacion donacion = buscarDonacionPorId(idDonacion);

    donacion.notificarEntregaFallida(observacion);
    AppEventBus.getInstance().post(new EventoEntregaFallida(observacion, donacion, LocalDate.now()));
    repoDonaciones.actualizar(donacion);
    ctx.status(200).json(DonacionMapper.aDto(donacion));
  }

  //================ FUNCIONES AUXILIARES ===============
  private TipoEstadoDonacion aTipoEstadoDonacion(String estado) {
    if (estado == null) return null;
    try {
      return TipoEstadoDonacion.valueOf(estado.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DominioException("El estado de donación: " + estado + " no existe, debe ser: " + Arrays.toString(TipoEstadoDonacion.values()));
    }
  }

  private Donante buscarDonantePorId(String id) {
    Donante donante = repoDonantes.buscarPorId(id);
    if (donante == null) throw new RecursoNoEncontradoException("No existe donante: " + id);
    return donante;
  }

  private Donacion buscarDonacionPorId(String id) {
    Donacion donacion = repoDonaciones.buscarPorId(id);
    if (donacion == null) throw new RecursoNoEncontradoException("No existe donación: " + id);
    return donacion;
  }

}
