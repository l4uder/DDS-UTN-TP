package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.necesidades.Necesidad;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.NecesidadMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.necesidad.NecesidadRequest;
import ar.edu.utn.frba.dds.donatrack.shared.ExceptionHandlers.ErrorResponse;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DomainValidationException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.google.gson.JsonSyntaxException;
import io.javalin.http.Context;
import java.util.List;
import java.util.UUID;

public class NecesidadController {
  private final BeneficiarioRepository repoBeneficiarios;

  public NecesidadController() {
    this.repoBeneficiarios = BeneficiarioRepository.getInstancia();
  }

  public void crear(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idBeneficiario = ctx.pathParam("id");
      //Cosas que recibo por Body
      NecesidadRequest necesidadDto = ctx.bodyAsClass(NecesidadRequest.class);

      Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
      Necesidad necesidad = NecesidadMapper.aDominio(necesidadDto);

      necesidad.setId(UUID.randomUUID().toString());
      beneficiario.agregarNecesidad(necesidad);
      repoBeneficiarios.actualizar(beneficiario);
      ctx.status(201).json(NecesidadMapper.aDto(necesidad));
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

  public void obtenerTodos(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idBeneficiario = ctx.pathParam("id");

      Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);

      List<Necesidad> necesidades = beneficiario.getNecesidades();
      ctx.status(200).json(NecesidadMapper.aDto(necesidades));
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  public void obtener(Context ctx) {
    try {
      //Cosas que recibo por URL --> Path param
      String idBeneficiario = ctx.pathParam("id");
      String idNecesidad = ctx.pathParam("nid");

      Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
      Necesidad necesidad = beneficiario.buscarNecesidadPorId(idNecesidad);

      ctx.status(200).json(NecesidadMapper.aDto(necesidad));
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
      String idNecesidad = ctx.pathParam("nid");
      //Cosas que recibo por Body
      NecesidadRequest necesidadDto = ctx.bodyAsClass(NecesidadRequest.class);

      Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
      Necesidad necesidad = beneficiario.buscarNecesidadPorId(idNecesidad);
      NecesidadMapper.actualizarDominio(necesidad, necesidadDto);

      repoBeneficiarios.actualizar(beneficiario);
      ctx.status(200).json(NecesidadMapper.aDto(necesidad));
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
      String idNecesidad = ctx.pathParam("nid");

      Beneficiario beneficiario = buscarBeneficiarioPorId(idBeneficiario);
      beneficiario.eliminarNecesidadPorId(idNecesidad);

      repoBeneficiarios.actualizar(beneficiario);
      ctx.status(204);
    } catch (RecursoNoEncontradoException e) {
      ctx.status(404).json(new ErrorResponse(404, e.getMessage()));
    } catch (Exception e) {
      e.printStackTrace();
      ctx.status(500).json(new ErrorResponse(500, "Ocurrió un error inesperado en el servidor"));
    }
  }

  //====================== FUNCIONES AUXILIARES ========================
  private Beneficiario buscarBeneficiarioPorId(String id) {
    Beneficiario beneficiario = repoBeneficiarios.buscarPorId(id);
    if (beneficiario == null) throw new RecursoNoEncontradoException("No existe beneficiario: " + id);
    return beneficiario;
  }

}
