package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.SubcategoriaRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.NecesidadMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad.NecesidadRequest;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.util.List;
import java.util.Random;

public class NecesidadController implements WithSimplePersistenceUnit {
  private final BeneficiarioRepository repoBeneficiarios;
  private final SubcategoriaRepository repoSubcategorias;

  public NecesidadController(BeneficiarioRepository repoBeneficiarios, SubcategoriaRepository repoSubcategorias) {
    this.repoBeneficiarios = repoBeneficiarios;
    this.repoSubcategorias = repoSubcategorias;
  }

  public void crear(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idBeneficiario = ctx.pathParam("id");
    //Cosas que recibo por Body
    NecesidadRequest necesidadDto = ctx.bodyAsClass(NecesidadRequest.class);
    String nombreSubCategoria = necesidadDto.subcategoria();

    beginTransaction();
    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
    Subcategoria subcategoria = buscarSubcategoriaPorNombre(nombreSubCategoria);
    Necesidad necesidad = NecesidadMapper.aDominio(necesidadDto, subcategoria);
    necesidad.setId(codigoSimplificado());
    beneficiario.agregarNecesidad(necesidad);
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
    String nombreSubcategoria = necesidadDto.subcategoria();

    beginTransaction();
    Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
    Necesidad necesidad = beneficiario.buscarNecesidadPorId(idNecesidad);
    Subcategoria subcategoria = buscarSubcategoriaPorNombre(nombreSubcategoria);
    NecesidadMapper.actualizarDominio(necesidad, necesidadDto, subcategoria);
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

  private Long codigoSimplificado() {
    return (long) new Random().nextInt(1000000);
  }

  private Subcategoria buscarSubcategoriaPorNombre(String nombre) {
    Subcategoria subcategoria = repoSubcategorias.buscarPorNombre(nombre);
    if (subcategoria == null)
      throw new RecursoNoEncontradoException("No existe la subcategoría: " + nombre);

    return subcategoria;
  }

}
