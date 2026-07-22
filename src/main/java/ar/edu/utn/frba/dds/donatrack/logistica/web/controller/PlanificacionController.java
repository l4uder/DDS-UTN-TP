package ar.edu.utn.frba.dds.donatrack.logistica.web.controller;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.coordinadores.CoordinadorRuta;
import ar.edu.utn.frba.dds.donatrack.logistica.cron.ProcesoLogistica;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.entrega.Entrega;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.planificacion.CallbackPlanificacionRequest;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import io.javalin.http.Context;

public class PlanificacionController {

  private final CoordinadorRuta coordinadorRuta;
  private final ProcesoLogistica procesoLogistica;

  public PlanificacionController(CoordinadorRuta coordinadorRuta, ProcesoLogistica procesoLogistica) {
    this.coordinadorRuta = coordinadorRuta;
    this.procesoLogistica = procesoLogistica;
  }

  public void planificar(Context ctx) {
    List<Entrega> entregas = coordinadorRuta.planificarEntregasPendientes();
    ctx.status(202).json(entregas);
  }

  public void callback(Context ctx) {
    try {
      CallbackPlanificacionRequest request = ctx.bodyAsClass(CallbackPlanificacionRequest.class);
      List<Ruta> rutas = coordinadorRuta.procesarCallback(request);
      ctx.status(201).json(rutas);
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ExceptionHandlers.ErrorResponse(400, "El body no es un JSON valido"));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ExceptionHandlers.ErrorResponse(404, e.getMessage()));
    }
  }

  public void ejecutarManual(Context ctx) {
    procesoLogistica.ejecutar();
    ctx.status(202);
  }
}