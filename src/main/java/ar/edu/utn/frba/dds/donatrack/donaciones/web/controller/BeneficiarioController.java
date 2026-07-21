package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.BeneficiarioMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.ContactoMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;

public class BeneficiarioController {
  private final BeneficiarioRepository repoBeneficiarios;

  public BeneficiarioController() {
    this.repoBeneficiarios = BeneficiarioRepository.getInstancia();
  }

  public void crear(Context ctx) {
    try {
      //Cosas que recibo por URL --> Query param
      BeneficiarioRequest beneficiarioDto = ctx.bodyAsClass(BeneficiarioRequest.class);

      Beneficiario beneficiario = BeneficiarioMapper.aDominio(beneficiarioDto);

      repoBeneficiarios.guardar(beneficiario);
      ctx.status(201).json(BeneficiarioMapper.aDto(beneficiario));
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
      List<Beneficiario> beneficiarios = repoBeneficiarios.buscarTodos();
      ctx.status(200).json(beneficiarios.stream().map(BeneficiarioMapper::aDtoResumen).toList());
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
      String idBeneficiario = ctx.pathParam("id");

      Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);

      ctx.status(200).json(BeneficiarioMapper.aDto(beneficiario));
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
      String idBeneficiario = ctx.pathParam("id");
      //Cosas que recibo por Body
      BeneficiarioRequest beneficiarioDto = ctx.bodyAsClass(BeneficiarioRequest.class);

      Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
      BeneficiarioMapper.actualizarDominio(beneficiario, beneficiarioDto);

      repoBeneficiarios.actualizar(beneficiario);
      ctx.json(BeneficiarioMapper.aDto(beneficiario));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void eliminar(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idBeneficiario = ctx.pathParam("id");

      Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);

      repoBeneficiarios.eliminar(idBeneficiario);
      ctx.status(204);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  //================= FUNCIONES AUXILIARES ===================
  private Beneficiario buscarBeneficiarioPorId(String id) {
    Beneficiario beneficiario = repoBeneficiarios.buscarPorId(id);
    if (beneficiario == null) throw new RecursoNoEncontradoException("No existe beneficiario: " + id);
    return beneficiario;
  }

}
