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
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoEntregadaRequest;
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoErrorEntregaRequest;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoInicioRutaRequest;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DonacionController {
  private final DonacionRepository repoDonaciones;
  private final DonanteRepository repoDonantes;

  public DonacionController() {
    this.repoDonaciones = DonacionRepository.getInstancia();
    this.repoDonantes = DonanteRepository.getInstancia();
  }

  public void crear(Context ctx) {
    try {
      //Cosas que recibo por Body
      DonacionRequest request = ctx.bodyAsClass(DonacionRequest.class);
      List<String> idDonantes = request.donanteIds();
      List<BienDto> bienesDto = request.bienes();

      List<Donante> donantes = idDonantes.stream().map(id -> buscarDonantePorId(id)).toList();
      Donacion donacion = DonacionMapper.aDominio(bienesDto, donantes);

      repoDonaciones.guardar(donacion);
      ctx.status(201).json(DonacionMapper.aDto(donacion));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void obtenerTodos(Context ctx) {
    try {
      //Cosas que recibo por URL --> Query param
      String estadoDonacion = ctx.queryParam("estado");

      TipoEstadoDonacion estado = aEstadoDonacion(estadoDonacion);

      List<Donacion> donaciones = estado == null ? repoDonaciones.buscarTodos() : repoDonaciones.buscarTodoPorEstado(estado);
      ctx.status(200).json(DonacionMapper.aDto(donaciones));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void obtener(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idDonacion = ctx.pathParam("id");

      Donacion donacion = buscarDonacionPorId(idDonacion);

      ctx.status(200).json(DonacionMapper.aDto(donacion));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void actualizar(Context ctx) {
    try {
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
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void eliminar(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idDonacion = ctx.pathParam("id");

      Donacion donacion = buscarDonacionPorId(idDonacion);

      repoDonaciones.eliminar(idDonacion);
      ctx.status(204);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void cambiarEstadoADeposito(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idDonacion = ctx.pathParam("id");

      Donacion donacion = buscarDonacionPorId(idDonacion);

      donacion.confirmarRecepcionDeposito();
      repoDonaciones.guardar(donacion);
      ctx.status(200).json(DonacionMapper.aDto(donacion));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void cambiarEstadoAVencida(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idDonacion = ctx.pathParam("id");

      Donacion donacion = buscarDonacionPorId(idDonacion);

      donacion.marcarVencida();
      repoDonaciones.guardar(donacion);
      ctx.status(200).json(DonacionMapper.aDto(donacion));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void cambiarEstadoALista(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idDonacion = ctx.pathParam("id");

      Donacion donacion = buscarDonacionPorId(idDonacion);

      donacion.confirmarRuta();
      repoDonaciones.guardar(donacion);
      ctx.status(200).json(DonacionMapper.aDto(donacion));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void cambiarEstadoAEntregada(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idDonacion = ctx.pathParam("id");
      //Cosas que recibo por Body
      CambioEstadoEntregadaRequest request = ctx.bodyAsClass(CambioEstadoEntregadaRequest.class);
      String idCamion = request.camionId();

      Donacion donacion = buscarDonacionPorId(idDonacion);

      donacion.confirmarEntrega();
      AppEventBus.getInstance().post(new EventoEntregaExitosa(donacion, LocalDate.now(), idCamion));
      repoDonaciones.guardar(donacion);
      ctx.status(200).json(DonacionMapper.aDto(donacion));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void cambiarEstadoAErrorEntrega(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idDonacion = ctx.pathParam("id");
      //Cosas que recibo por Body
      CambioEstadoErrorEntregaRequest request = ctx.bodyAsClass(CambioEstadoErrorEntregaRequest.class);
      String observacion = request.observacion();

      Donacion donacion = buscarDonacionPorId(idDonacion);

      donacion.notificarEntregaFallida(observacion);
      AppEventBus.getInstance().post(new EventoEntregaFallida(observacion, donacion, LocalDate.now()));
      repoDonaciones.guardar(donacion);
      ctx.status(200).json(DonacionMapper.aDto(donacion));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void cambiarEstadoAEnTraslado(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idDonacion = ctx.pathParam("id");
      //Cosas que recibo por Body
      CambioEstadoInicioRutaRequest request = ctx.bodyAsClass(CambioEstadoInicioRutaRequest.class);
      String mapa = request.linkMapa();

      Donacion donacion = buscarDonacionPorId(idDonacion);

      donacion.confirmarTrasladoEnCurso();
      AppEventBus.getInstance().post(new EventoInicioDeRuta(donacion, LocalDate.now(), mapa));
      repoDonaciones.guardar(donacion);
      ctx.status(200).json(DonacionMapper.aDto(donacion));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (CambioDeEstadoNoPermitidoException e) {
      ctx.status(409).json(new ErrorResponse(409, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void historialEstados(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idDonacion = ctx.pathParam("id");

      Donacion donacion = buscarDonacionPorId(idDonacion);

      List<EstadoDonacion> historialEstados = donacion.getHistorialEstados();
      ctx.status(200).json(EstadoDonacionMapper.aDto(historialEstados));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  //================ FUNCIONES AUXILIARES ===============
  private TipoEstadoDonacion aEstadoDonacion(String estado) {
    if (estado == null) return null;
    try {
      return TipoEstadoDonacion.valueOf(estado.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DomainValidationException("No se conoce el estado de donación: " + estado);
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
