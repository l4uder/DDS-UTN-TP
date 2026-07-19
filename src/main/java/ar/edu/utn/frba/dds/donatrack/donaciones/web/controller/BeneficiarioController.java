package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.beneficiario.BeneficiarioResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.comun.ContactoMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;

public class BeneficiarioController {

  private final BeneficiarioRepository repository = BeneficiarioRepository.getInstancia();

  public void listar(Context ctx) {
    List<BeneficiarioResponse> beneficiarios = repository.buscarTodos().stream()
        .map(BeneficiarioMapper::aResponse)
        .toList();
    ctx.json(beneficiarios);
  }

  public void obtener(Context ctx) {
    try {
      ctx.json(BeneficiarioMapper.aResponse(repository.obtenerPorId(ctx.pathParam("id"))));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void crear(Context ctx) {
    try {
      BeneficiarioRequest request = ctx.bodyAsClass(BeneficiarioRequest.class);
      Beneficiario creado = BeneficiarioMapper.aDominio(request);
      repository.guardarBeneficiario(creado);
      ctx.status(201).json(BeneficiarioMapper.aResponse(creado));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    }
  }

  public void actualizar(Context ctx) {
    try {
      Beneficiario beneficiario = repository.obtenerPorId(ctx.pathParam("id"));
      BeneficiarioRequest request = ctx.bodyAsClass(BeneficiarioRequest.class);
      BeneficiarioMapper.validar(request);
      beneficiario.actualizarDatos(request.razonSocial(),
          request.direccion(),
          ContactoMapper.aDominio(request.contactos()));
      repository.guardarBeneficiario(beneficiario);
      ctx.json(BeneficiarioMapper.aResponse(beneficiario));
    } catch (JsonSyntaxException e) {
      ctx.status(400).json(new ErrorResponse(400, "El body no es un JSON valido"));
    } catch (DomainValidationException e) {
      ctx.status(400).json(new ErrorResponse(400, e.getMessage()));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }

  public void eliminar(Context ctx) {
    try {
      repository.obtenerPorId(ctx.pathParam("id"));
      repository.eliminar(ctx.pathParam("id"));
      ctx.status(204);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    }
  }
}
