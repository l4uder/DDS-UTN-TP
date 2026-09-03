package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.NecesidadMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad.NecesidadRequest;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.javalin.http.Context;
import java.util.List;
import java.util.Random;

public class NecesidadController {
  private final BeneficiarioRepository repoBeneficiarios;

  public NecesidadController(BeneficiarioRepository repoBeneficiarios) {
    this.repoBeneficiarios = repoBeneficiarios;
  }

  public void crear(Context ctx) {
    //Cosas que recibo por URL --> Path param
    Long idBeneficiario = Long.valueOf(ctx.pathParam("id"));
    //Cosas que recibo por Body
    NecesidadRequest necesidadDto = ctx.bodyAsClass(NecesidadRequest.class);

    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
    Necesidad necesidad = NecesidadMapper.aDominio(necesidadDto);

    necesidad.setId(codigoSimplificado());
    beneficiario.agregarNecesidad(necesidad);
    repoBeneficiarios.actualizar(beneficiario);
    ctx.status(201).json(NecesidadMapper.aDto(necesidad));
  }

  public void obtenerTodos(Context ctx) {
    //Cosas que recibo por URL --> Path param
    Long idBeneficiario = Long.valueOf(ctx.pathParam("id"));

    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);

    List<Necesidad> necesidades = beneficiario.getNecesidades();
    ctx.status(200).json(NecesidadMapper.aDto(necesidades));
  }

  public void obtener(Context ctx) {
    //Cosas que recibo por URL --> Path param
    Long idBeneficiario = Long.valueOf(ctx.pathParam("id"));
    String idNecesidad = ctx.pathParam("nid");

    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
    Necesidad necesidad = beneficiario.buscarNecesidadPorId(idNecesidad);

    ctx.status(200).json(NecesidadMapper.aDto(necesidad));
  }

  public void actualizar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    Long idBeneficiario = Long.valueOf(ctx.pathParam("id"));
    String idNecesidad = ctx.pathParam("nid");
    //Cosas que recibo por Body
    NecesidadRequest necesidadDto = ctx.bodyAsClass(NecesidadRequest.class);

    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
    Necesidad necesidad = beneficiario.buscarNecesidadPorId(idNecesidad);
    NecesidadMapper.actualizarDominio(necesidad, necesidadDto);

    repoBeneficiarios.actualizar(beneficiario);
    ctx.status(200).json(NecesidadMapper.aDto(necesidad));
  }

  public void eliminar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    Long idBeneficiario = Long.valueOf(ctx.pathParam("id"));
    String idNecesidad = ctx.pathParam("nid");

    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
    beneficiario.eliminarNecesidadPorId(idNecesidad);

    repoBeneficiarios.actualizar(beneficiario);
    ctx.status(204);
  }

  //====================== FUNCIONES AUXILIARES ========================
  private Beneficiario buscarBeneficiarioPorId(Long id) {
    Beneficiario beneficiario = repoBeneficiarios.buscarPorId(id);
    if (beneficiario == null) throw new RecursoNoEncontradoException("No existe beneficiario: " + id);
    return beneficiario;
  }

  private String codigoSimplificado() {
    String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder sb = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < 6; i++) {
      int indiceAleatorio = random.nextInt(CARACTERES.length());
      sb.append(CARACTERES.charAt(indiceAleatorio));
    }
    return sb.toString();
  }

}
