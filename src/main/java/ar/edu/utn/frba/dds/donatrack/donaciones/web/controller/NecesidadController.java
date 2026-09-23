package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.NecesidadMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad.NecesidadRequest;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.util.List;
import java.util.Random;

public class NecesidadController implements WithSimplePersistenceUnit {
  private final BeneficiarioRepository repoBeneficiarios;

  public NecesidadController(BeneficiarioRepository repoBeneficiarios) {
    this.repoBeneficiarios = repoBeneficiarios;
  }

  public void crear(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idBeneficiario = ctx.pathParam("id");
    //Cosas que recibo por Body
    NecesidadRequest necesidadDto = ctx.bodyAsClass(NecesidadRequest.class);

    beginTransaction();
    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
    Necesidad necesidad = NecesidadMapper.aDominio(necesidadDto);
    beneficiario.agregarNecesidad(necesidad);
    persist(necesidad);
    repoBeneficiarios.actualizar(beneficiario);
    commitTransaction();

    ctx.status(201).json("Necesidad creada correctamente");
  }

  public void obtenerTodos(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idBeneficiario = ctx.pathParam("id");

    beginTransaction();
    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
    List<Necesidad> necesidades = beneficiario.getNecesidades();
    ctx.status(200).json(NecesidadMapper.aDto(necesidades));
    commitTransaction();
  }

  public void obtener(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idBeneficiario = ctx.pathParam("id");
    String idNecesidad = ctx.pathParam("nid");

    beginTransaction();
    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
    Necesidad necesidad = beneficiario.buscarNecesidadPorId(idNecesidad);
    ctx.status(200).json(NecesidadMapper.aDto(necesidad));
    commitTransaction();
  }

  public void actualizar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idBeneficiario = ctx.pathParam("id");
    String idNecesidad = ctx.pathParam("nid");
    //Cosas que recibo por Body
    NecesidadRequest necesidadDto = ctx.bodyAsClass(NecesidadRequest.class);

    beginTransaction();
    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
    Necesidad necesidad = beneficiario.buscarNecesidadPorId(idNecesidad);
    NecesidadMapper.actualizarDominio(necesidad, necesidadDto);
    repoBeneficiarios.actualizar(beneficiario);
    commitTransaction();

    ctx.status(200).json("se ha actualizado correctamente");
  }

  public void eliminar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idBeneficiario = ctx.pathParam("id");
    String idNecesidad = ctx.pathParam("nid");

    beginTransaction();
    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
    beneficiario.eliminarNecesidadPorId(idNecesidad);
    repoBeneficiarios.actualizar(beneficiario);
    commitTransaction();

    ctx.status(204);
  }

  //====================== FUNCIONES AUXILIARES ========================
  private Beneficiario buscarBeneficiarioPorId(String id) {
    Beneficiario beneficiario = repoBeneficiarios.buscarPorId(Long.valueOf(id));
    if (beneficiario == null) throw new RecursoNoEncontradoException("No existe beneficiario: " + id);
    return beneficiario;
  }

}
