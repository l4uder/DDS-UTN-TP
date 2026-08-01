package ar.edu.utn.frba.dds.donatrack.logistica.web.controllers;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta.Chofer;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.ruta.Ruta;
import ar.edu.utn.frba.dds.donatrack.logistica.web.convers.ChoferMapper;
import ar.edu.utn.frba.dds.donatrack.logistica.web.convers.RutaMapper;
import ar.edu.utn.frba.dds.donatrack.logistica.web.dto.chofer.ChoferDto;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.RutaRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.web.integracion.ConectorDonacionesApi;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import io.javalin.http.Context;

import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import java.util.List;

public class RutaController {
  private final RutaRepository repoRutas;
  private final ConectorDonacionesApi donacionesBridge;

  public RutaController(RutaRepository repository, ConectorDonacionesApi donacionesClient) {
    this.repoRutas = repository;
    this.donacionesBridge = donacionesClient;
  }

  public void obtenerTodas(Context ctx) {
    List<Ruta> rutas = repoRutas.buscarTodas();

    ctx.status(200).json(RutaMapper.aDto(rutas));
  }

  public void obtener(Context ctx) {
    String idRuta = ctx.pathParam("id");

    Ruta ruta = buscarRutaPorId(idRuta);
    ctx.status(200).json(RutaMapper.aDto(ruta));
  }

  public void asignarChofer(Context ctx) {
    //Cosas que recibo por URL
    String idRuta = ctx.pathParam("id");
    //Cosas que recibo por Body
    ChoferDto choferDto = ctx.bodyAsClass(ChoferDto.class);

    Chofer chofer = ChoferMapper.aDominio(choferDto);
    Ruta ruta = buscarRutaPorId(idRuta);

    ruta.asignarChofer(chofer);
    repoRutas.actualizar(ruta);
    ctx.status(200).json(RutaMapper.aDto(ruta));
  }

  public void iniciar(Context ctx) {
    String idRuta = ctx.pathParam("id");

    Ruta ruta = buscarRutaPorId(idRuta);
    ruta.iniciarRecorrido();
    String linkMapa = ruta.getCamion().getLinkSeguimiento();
    List<DonacionEnTransito> donaciones = ruta.getEntregasOrdenadas().stream().flatMap(e -> e.getDonaciones().stream()).toList();
    comunicarAlasDonacionesQueEstanEnCamino(donaciones, linkMapa);
    repoRutas.actualizar(ruta);
    ctx.status(200).json(RutaMapper.aDto(ruta));
  }

  //================= FUNCIONES AUXILIARES ======================
  public Ruta buscarRutaPorId(String id) {
    Ruta ruta = repoRutas.buscarPorId(id);
    if (ruta == null) throw new RecursoNoEncontradoException("Ruta no encontrada: " + id);
    return ruta;
  }

  public void comunicarAlasDonacionesQueEstanEnCamino(List<DonacionEnTransito> donaciones, String linkMapa) {
    donaciones.forEach(d ->
        donacionesBridge.marcarDonacionEnCamino(d.getId(), linkMapa)
    );
  }

}