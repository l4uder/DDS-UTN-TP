package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.BeneficiarioMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.util.List;

public class BeneficiarioController implements WithSimplePersistenceUnit {
  private final BeneficiarioRepository repoBeneficiarios;

  public BeneficiarioController(BeneficiarioRepository repoBeneficiarios) {
    this.repoBeneficiarios = repoBeneficiarios;
  }

  public void crear(Context ctx) {
    //Cosas que recibo por URL --> Query param
    BeneficiarioRequest beneficiarioDto = ctx.bodyAsClass(BeneficiarioRequest.class);

    Beneficiario beneficiario = BeneficiarioMapper.aDominio(beneficiarioDto);
    beginTransaction();
    repoBeneficiarios.guardar(beneficiario);
    ctx.status(201).json(BeneficiarioMapper.aDto(beneficiario));
    commitTransaction();
  }

  public void obtenerTodos(Context ctx) {
    beginTransaction();
    List<Beneficiario> beneficiarios = repoBeneficiarios.buscarTodos();
    ctx.status(200).json(BeneficiarioMapper.aDtoResumen(beneficiarios));
    commitTransaction();
  }

  public void obtener(Context ctx) {
    //Cosas que recibo por URL --> Path param
    Long idBeneficiario = Long.valueOf(ctx.pathParam("id"));
    beginTransaction();
    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);

    ctx.status(200).json(BeneficiarioMapper.aDto(beneficiario));
    commitTransaction();
  }

  public void actualizar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    Long idBeneficiario = Long.valueOf(ctx.pathParam("id"));
    //Cosas que recibo por Body
    BeneficiarioRequest beneficiarioDto = ctx.bodyAsClass(BeneficiarioRequest.class);

    beginTransaction();
    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
    BeneficiarioMapper.actualizarDominio(beneficiario, beneficiarioDto);

    repoBeneficiarios.actualizar(beneficiario);
    ctx.status(200).json(BeneficiarioMapper.aDto(beneficiario));
    commitTransaction();
  }

  public void eliminar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    Long idBeneficiario = Long.valueOf(ctx.pathParam("id"));
    beginTransaction();
    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);

    repoBeneficiarios.eliminar(beneficiario);
    commitTransaction();
    ctx.status(204);
  }

  //================= FUNCIONES AUXILIARES ===================
  private Beneficiario buscarBeneficiarioPorId(Long id) {
    Beneficiario beneficiario = repoBeneficiarios.buscarPorId(id);
    if (beneficiario == null) throw new RecursoNoEncontradoException("No existe beneficiario: " + id);
    return beneficiario;
  }

}
