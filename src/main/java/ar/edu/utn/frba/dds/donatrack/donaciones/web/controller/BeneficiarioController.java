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
      BeneficiarioRequest request = ctx.bodyAsClass(BeneficiarioRequest.class);
      Beneficiario beneficiario = BeneficiarioMapper.aDominio(request);
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
      String idBeneficiario = ctx.pathParam("id");
      Beneficiario beneficiario = repoBeneficiarios.buscarPorId(idBeneficiario);
      if (beneficiario == null) throw new RecursoNoEncontradoException("El beneficiario no existe: " + idBeneficiario);
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
      String idBeneficiario = ctx.pathParam("id");
      Beneficiario beneficiario = repoBeneficiarios.buscarPorId(idBeneficiario);
      if (beneficiario == null) throw new RecursoNoEncontradoException("El beneficiario no existe: " + idBeneficiario);
      BeneficiarioRequest request = ctx.bodyAsClass(BeneficiarioRequest.class);
      beneficiario.actualizacionParcial(
          request.razonSocial(),
          request.direccion(),
          request.contactos() == null ? null : ContactoMapper.aDominio(request.contactos()));
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
      String idBeneficiario = ctx.pathParam("id");
      Beneficiario beneficiario = repoBeneficiarios.buscarPorId(idBeneficiario);
      if (beneficiario == null) throw new RecursoNoEncontradoException("El beneficiario no existe: " + idBeneficiario);
      repoBeneficiarios.eliminar(idBeneficiario);
      ctx.status(204);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

}
