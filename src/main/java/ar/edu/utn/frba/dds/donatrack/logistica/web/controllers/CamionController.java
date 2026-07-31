package ar.edu.utn.frba.dds.donatrack.logistica.web.controllers;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Gps;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.GpsRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.web.convers.CamionMapper;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.camion.ActualizarCamionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.camion.CamionRequest;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.javalin.http.Context;
import java.util.List;

public class CamionController {
  private final CamionRepository repoCamiones;
  private final GpsRepository repoGps;

  public CamionController(CamionRepository repoCamiones, GpsRepository repoGps) {
    this.repoCamiones = repoCamiones;
    this.repoGps = repoGps;
  }

  public void crear(Context ctx) {
    CamionRequest camionDto = ctx.bodyAsClass(CamionRequest.class);

    Camion camion = CamionMapper.aDominio(camionDto);

    repoCamiones.guardar(camion);
    ctx.status(201).json(CamionMapper.aDto(camion));
  }

  public void obtenerTodos(Context ctx) {
    List<Camion> camiones = repoCamiones.buscarTodos();

    ctx.status(200).json(CamionMapper.aDto(camiones));
  }

  public void obtener(Context ctx) {
    String patente = ctx.pathParam("patente");

    Camion camion = buscarCamionPorPatente(patente);

    ctx.status(200).json(CamionMapper.aDto(camion));
  }

  //pasamos de un put a patch
  public void actualizar(Context ctx) {
    String patente = ctx.pathParam("patente");
    ActualizarCamionRequest request = ctx.bodyAsClass(ActualizarCamionRequest.class);

    Camion camion = buscarCamionPorPatente(patente);
    Gps gps = buscarGpsPorImei(request.gpsImei());
    CamionMapper.actualizarDesdeRequest(camion, gps, request);

    repoCamiones.actualizar(camion);
    ctx.status(200).json(CamionMapper.aDto(camion));
  }

  public void eliminar(Context ctx) {
    String patente = ctx.pathParam("patente");

    Camion camion = buscarCamionPorPatente(patente);
    repoCamiones.eliminar(camion);
    ctx.status(204);
  }

  //================ FUNCIONES AUXILIARES ======================
  private Camion buscarCamionPorPatente(String patente) {
    Camion camion = repoCamiones.buscarPorPatente(patente);
    if (camion == null) {
      throw new RecursoNoEncontradoException("No existe el camión, con patente: " + patente);
    }
    return camion;
  }

  private Gps buscarGpsPorImei(String imei) {
    if (imei == null) {
      return null;
    }
    Gps gps = repoGps.buscarPorId(imei);
    if (gps == null) {
      throw new RecursoNoEncontradoException("No existe el gps, con imei: " + imei);
    }
    return gps;
  }

}