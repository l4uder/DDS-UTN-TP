package ar.edu.utn.frba.dds.donatrack.donaciones.web.controller;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Categoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.eventos.EventoVencida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.EstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.eventos.EventoEntregaExitosa;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.eventos.EventoEntregaFallida;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.eventos.EventoInicioDeRuta;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.CategoriaRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonanteRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.SubcategoriaRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.BienMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.DonacionMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.convers.EstadoDonacionMapper;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.bien.BienDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.DonacionRequest;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.comunicaciones.DispatcherEventos;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.EnTrasladoDonacionDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.EntregadaDonacionDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.donacion.ErrorEntregaDonacionDto;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.BodyException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.DominioException;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import java.util.Arrays;
import java.util.List;

public class DonacionController implements WithSimplePersistenceUnit {
  private final DonacionRepository repoDonaciones;
  private final DonanteRepository repoDonantes;
  private final SubcategoriaRepository repoSubcategorias;
  private final CategoriaRepository repoCategorias;

  public DonacionController(DonacionRepository repoDonaciones, DonanteRepository repoDonantes, SubcategoriaRepository repoSubcategorias, CategoriaRepository repoCategorias) {
    this.repoDonaciones = repoDonaciones;
    this.repoDonantes = repoDonantes;
    this.repoSubcategorias = repoSubcategorias;
    this.repoCategorias = repoCategorias;
  }

  // [----] -> [En deposito]
  public void crear(Context ctx) {
    //Cosas que recibo por Body
    DonacionRequest request = ctx.bodyAsClass(DonacionRequest.class);
    if (request.donantesId() == null) throw new BodyException("Bad Request, necesita: 'donantes_id'");
    if (request.bienes() == null) throw new BodyException("Bad Request, necesita: 'bienes' ");
    List<String> idDonantes = request.donantesId();
    List<BienDto> bienesDto = request.bienes();

    beginTransaction();
    List<Donante> donantes = idDonantes.stream().map(this::buscarDonantePorId).toList();
    Donacion donacion = DonacionMapper.aDominio(bienesDto, donantes);

    //Valida e hidrata las subcategorias y categorias con las existentes en la db
    //Si se quiere crear una donacion con una (sub)categoria no existente, crearlas previamente
    for (Bien bien : donacion.getBienes()) {
      if (bien.getSubcategoria() != null) {
        Subcategoria subDelMapper = bien.getSubcategoria();
        Categoria catDelMapper = subDelMapper.getCategoria();

        //Valida Categoría
        Categoria catReal = null;
        if (catDelMapper != null && catDelMapper.getNombre() != null) {
          catReal = repoCategorias.buscarPorNombre(catDelMapper.getNombre());
          if (catReal == null) {
            throw new BodyException("Bad Request: La categoría '" + catDelMapper.getNombre() + "' no existe.");
          }
        }

        //Valida Subcategoría
        Subcategoria subReal = repoSubcategorias.buscarPorNombre(subDelMapper.getNombre());
        if (subReal == null) {
          throw new BodyException("Bad Request: La subcategoría '" + subDelMapper.getNombre() + "' no existe.");
        }

        //Valida coherencia jerárquica (que la subcategoría realmente pertenezca a la categoría enviada)
        if (catReal != null && subReal.getCategoria() != null
            && !subReal.getCategoria().getId().equals(catReal.getId())) {
          throw new BodyException("Bad Request: La subcategoría '" + subReal.getNombre()
              + "' no pertenece a la categoría '" + catReal.getNombre() + "'.");
        }

        bien.setSubcategoria(subReal);
      }
    }

    repoDonaciones.guardar(donacion);
    commitTransaction();

    ctx.status(201).json("Donación creada correctamente");
  }

  public void obtenerTodos(Context ctx) {
    //Cosas que recibo por URL --> Query param
    String estadoDonacion = ctx.queryParam("estado");

    TipoEstadoDonacion estado = aTipoEstadoDonacion(estadoDonacion);

    beginTransaction();
    List<Donacion> donaciones = estado == null ? repoDonaciones.buscarTodos() : repoDonaciones.buscarTodoPorEstado(estado);
    ctx.status(200).json(DonacionMapper.aDto(donaciones));
    commitTransaction();
  }

  public void obtener(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");

    beginTransaction();
    Donacion donacion = buscarDonacionPorId(idDonacion);
    ctx.status(200).json(DonacionMapper.aDto(donacion));
    commitTransaction();
  }

  public void actualizar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");
    //Cosas que recibo por Body
    DonacionRequest request = ctx.bodyAsClass(DonacionRequest.class);
    if (request.bienes() == null) throw new BodyException("Bad Request, necesita: 'bienes'");
    List<BienDto> bienesDto = request.bienes();

    beginTransaction();
    Donacion donacion = buscarDonacionPorId(idDonacion);
    List<Bien> bienes = BienMapper.aDominio(bienesDto);
    donacion.actualizarBienes(bienes);
    repoDonaciones.actualizar(donacion);
    commitTransaction();
    ctx.status(200).json("Donación actualizada correctamente");
  }

  public void eliminar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");

    beginTransaction();
    Donacion donacion = buscarDonacionPorId(idDonacion);
    repoDonaciones.eliminar(donacion);
    commitTransaction();

    ctx.status(204);
  }

  public void historialEstados(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");

    beginTransaction();
    Donacion donacion = buscarDonacionPorId(idDonacion);
    List<EstadoDonacion> historialEstados = donacion.getHistorialEstados();
    ctx.status(200).json(EstadoDonacionMapper.aDto(historialEstados));
    commitTransaction();
  }

  // [Asignación Realizada] -> [Lista Para Entregar]
  public void donacionListaParaEntregar(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");

    beginTransaction();
    Donacion donacion = buscarDonacionPorId(idDonacion);
    donacion.listaParaEntregar();
    repoDonaciones.actualizar(donacion);
    commitTransaction();

    ctx.status(200).json("la donación esta lista para ser entregada");
  }

  // [Lista Para Entregar] -> [En Traslado]
  public void donacionEnCamino(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");
    //Cosas que recibo por Body
    EnTrasladoDonacionDto request = ctx.bodyAsClass(EnTrasladoDonacionDto.class);
    if (request.linkMapa() == null) throw new BodyException("Bad Request, necesita: 'link_mapa' ");
    String mapa = request.linkMapa();

    beginTransaction();
    Donacion donacion = buscarDonacionPorId(idDonacion);
    donacion.enCamino();
    repoDonaciones.actualizar(donacion);
    DispatcherEventos.getInstancia().publicar(new EventoInicioDeRuta(donacion.getBeneficiario(), donacion.getDonantes(), donacion.getDescripcion(), mapa));
    commitTransaction();

    ctx.status(200).json("la donación se encuentra en traslado");
  }

  // [En Traslado] -> [Entregada] FIN
  public void donacionEntregada(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");
    //Cosas que recibo por Body
    EntregadaDonacionDto request = ctx.bodyAsClass(EntregadaDonacionDto.class);
    if (request.linkComprobanteEntrega() == null) throw new BodyException("Bad Request, necesita: 'link_comprobante_entrega' ");
    String comprobante = request.linkComprobanteEntrega();

    beginTransaction();
    Donacion donacion = buscarDonacionPorId(idDonacion);
    donacion.entregada();
    repoDonaciones.actualizar(donacion);
    DispatcherEventos.getInstancia().publicar(new EventoEntregaExitosa(donacion.getBeneficiario(), donacion.getDonantes(), donacion.getDescripcion(), comprobante));
    commitTransaction();

    ctx.status(200).json("la donación fue entregada");
  }

  // [En Traslado] -> [Entregada Fallida]
  public void donacionEntregaFallida(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");
    //Cosas que recibo por Body
    ErrorEntregaDonacionDto request = ctx.bodyAsClass(ErrorEntregaDonacionDto.class);
    if (request.observacion() == null) throw new BodyException("Bad Request, necesita: 'observacion' del error");
    String observacion = request.observacion();

    beginTransaction();
    Donacion donacion = buscarDonacionPorId(idDonacion);
    donacion.errorAlEntregar(observacion);
    repoDonaciones.actualizar(donacion);
    DispatcherEventos.getInstancia().publicar(new EventoEntregaFallida(donacion, observacion));
    commitTransaction();

    ctx.status(200).json("la donación No pudo ser entregada");
  }

  // [Entrega Fallida] -> [En Deposito]
  public void donacionDevueltaADeposito(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");

    beginTransaction();
    Donacion donacion = buscarDonacionPorId(idDonacion);
    donacion.retornarADeposito();
    repoDonaciones.actualizar(donacion);
    commitTransaction();

    ctx.status(200).json("la donación fue devuelta a deposito");
  }

  // [En Deposito] -> [Vencida] FIN
  public void donacionVencida(Context ctx) {
    //Cosas que recibo por URL --> Path param
    String idDonacion = ctx.pathParam("id");

    beginTransaction();
    Donacion donacion = buscarDonacionPorId(idDonacion);
    donacion.vencida();
    repoDonaciones.actualizar(donacion);
    DispatcherEventos.getInstancia().publicar(new EventoVencida(donacion.getDonantes()));
    commitTransaction();

    ctx.status(200).json("la donación se venció");
  }

  //================ FUNCIONES AUXILIARES ===============
  private TipoEstadoDonacion aTipoEstadoDonacion(String estado) {
    if (estado == null) return null;
    try {
      return TipoEstadoDonacion.valueOf(estado.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new DominioException("El estado de donación: " + estado + " no existe, debe ser: " + Arrays.toString(TipoEstadoDonacion.values()));
    }
  }

  private Donante buscarDonantePorId(String id) {
    Donante donante = repoDonantes.buscarPorId(Long.valueOf(id));
    if (donante == null) throw new RecursoNoEncontradoException("No existe donante: " + id);
    return donante;
  }

  private Donacion buscarDonacionPorId(String id) {
    Donacion donacion = repoDonaciones.buscarPorId(Long.valueOf(id));
    if (donacion == null) throw new RecursoNoEncontradoException("No existe donación: " + id);
    return donacion;
  }

}
