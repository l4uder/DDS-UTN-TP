package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.Donante;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.Documento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donante.documento.TipoDocumento;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.mediocontacto.CorreoDeContato;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.builder.PersonaHumanaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.CategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.SubcategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Bien;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.EstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.bien.UnidadMedida;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.CambioDeEstadoNoPermitidoException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EstadosDonacionTest {
  private Subcategoria fideos;
  private Bien fideosLucetti;

  private Donante donantePrueba;

  @BeforeEach
  public void configuracionInicial() {
    fideos = new SubcategoriaBuilder()
        .conNombre("Fideos secos")
        .conCategoria(new CategoriaBuilder().conNombre("Alimentos").build())
        .build();

    fideosLucetti = new BienBuilder()
        .conDescripcion("Fideos Lucchetti")
        .conCantidad(4)
        .conSubcategoria(fideos)
        .conFechaVencimiento(LocalDate.now().plusMonths(6))
        .conUnidad(UnidadMedida.UNIDADES)
        .buildPerecedero();

    donantePrueba = new PersonaHumanaBuilder()
        .conNombre("Juan")
        .conApellido("Pérez")
        .conDocumento(new Documento(TipoDocumento.DNI, "12345678"))
        .conAgregarContacto(new CorreoDeContato("juan@prueba.com", true))
        .conDireccion("alguna dirección")
        .build();
  }

  @Test
  public void estadoInicialDeUnaDonacionEsEnDeposito() {
    Donacion donacion = new Donacion(List.of(fideosLucetti), List.of(donantePrueba));

    assertEquals(TipoEstadoDonacion.EN_DEPOSITO, donacion.getEstadoActual());
  }

  @Test
  public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeEnDeposito() {
    Donacion donacion = new Donacion(List.of(fideosLucetti), List.of(donantePrueba));

    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarTrasladoEnCurso);
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRuta);
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarEntrega);
    assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.notificarEntregaFallida("razon"));
    assertEquals(TipoEstadoDonacion.EN_DEPOSITO, donacion.getEstadoActual());
  }

  @Test
  public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeAsignacionRealizada() {
    Donacion donacion = new Donacion(List.of(fideosLucetti), List.of(donantePrueba));

    donacion.confirmarAsignacion(new Beneficiario("razon", "direccion", List.of(new CorreoDeContato("esperanza@prueba.com", true))));

    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarTrasladoEnCurso);
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRecepcionDeposito);
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarEntrega);
    assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.notificarEntregaFallida("razon"));
    assertEquals(TipoEstadoDonacion.ASIGNACION_REALIZADA, donacion.getEstadoActual());
  }

  @Test
  public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeListaParaEntregar() {
    Donacion donacion = new Donacion(List.of(fideosLucetti), List.of(donantePrueba));

    donacion.confirmarAsignacion(new Beneficiario("razon", "direccion", List.of(new CorreoDeContato("esperanza@prueba.com", true))));
    donacion.confirmarRuta();

    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarEntrega);
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRecepcionDeposito);
    assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.confirmarAsignacion(new Beneficiario("razon", "direccion", List.of(new CorreoDeContato("esperanza@prueba.com", true)))));
    assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.notificarEntregaFallida("razon"));
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::marcarVencida);
    assertEquals(TipoEstadoDonacion.LISTA_PARA_ENTREGAR, donacion.getEstadoActual());
  }

  @Test
  public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeEnViaje() {
    Donacion donacion = new Donacion(List.of(fideosLucetti), List.of(donantePrueba));

    donacion.confirmarAsignacion(new Beneficiario("razon", "direccion", List.of(new CorreoDeContato("esperanza@prueba.com", true))));
    donacion.confirmarRuta();
    donacion.confirmarTrasladoEnCurso();

    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRuta);
    assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.confirmarAsignacion(new Beneficiario("razon", "direccion", List.of(new CorreoDeContato("nombreBeneficiario@gmail.com", true)))));
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::marcarVencida);
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRecepcionDeposito);

    assertEquals(TipoEstadoDonacion.EN_TRASLADO, donacion.getEstadoActual());
  }

  @Test
  public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeEntregaFallida() {
    Donacion donacion = new Donacion(List.of(fideosLucetti), List.of(donantePrueba));

    donacion.confirmarAsignacion(new Beneficiario("razon", "direccion", List.of(new CorreoDeContato("esperanza@prueba.com", true))));
    donacion.confirmarRuta();
    donacion.confirmarTrasladoEnCurso();
    donacion.notificarEntregaFallida("No se encontraba en el domicilio");

    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRuta);
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarTrasladoEnCurso);
    assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.confirmarAsignacion(new Beneficiario("razon", "direccion", List.of(new CorreoDeContato("esperanza@prueba.com", true)))));
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::marcarVencida);

    assertEquals(TipoEstadoDonacion.ENTREGA_FALLIDA, donacion.getEstadoActual());
  }

  @Test
  public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeEntregaRealizada() {
    Donacion donacion = new Donacion(List.of(fideosLucetti), List.of(donantePrueba));

    donacion.confirmarAsignacion(new Beneficiario("razon", "direccion", List.of(new CorreoDeContato("esperanza@prueba.com", true))));
    donacion.confirmarRuta();
    donacion.confirmarTrasladoEnCurso();
    donacion.confirmarEntrega();

    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRuta);
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarTrasladoEnCurso);
    assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.confirmarAsignacion(new Beneficiario("razon", "direccion", List.of(new CorreoDeContato("esperanza@prueba.com", true)))));
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::marcarVencida);
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRecepcionDeposito);
    assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.notificarEntregaFallida("razon"));

    assertEquals(TipoEstadoDonacion.ENTREGADA, donacion.getEstadoActual());
  }

  @Test
  public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeVencida() {
    Donacion donacion = new Donacion(List.of(fideosLucetti), List.of(donantePrueba));

    donacion.marcarVencida();

    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRuta);
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarTrasladoEnCurso);
    assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.confirmarAsignacion(new Beneficiario("razon", "direccion", List.of(new CorreoDeContato("esperanza@prueba.com", true)))));
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarEntrega);
    assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRecepcionDeposito);
    assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.notificarEntregaFallida("razon"));

    assertEquals(TipoEstadoDonacion.VENCIDA, donacion.getEstadoActual());
  }

  @Test
  public void entregaExitosa() {
    Donacion donacion = new Donacion(List.of(fideosLucetti), List.of(donantePrueba));

    donacion.confirmarAsignacion(new Beneficiario("razon", "direccion", List.of(new CorreoDeContato("esperanza@prueba.com", true))));
    donacion.confirmarRuta();
    donacion.confirmarTrasladoEnCurso();
    donacion.confirmarEntrega();

    List<TipoEstadoDonacion> estadosEsperados = List.of(
            TipoEstadoDonacion.EN_DEPOSITO,
            TipoEstadoDonacion.ASIGNACION_REALIZADA,
            TipoEstadoDonacion.LISTA_PARA_ENTREGAR,
            TipoEstadoDonacion.EN_TRASLADO,
            TipoEstadoDonacion.ENTREGADA
    );

    //donacion.getHistorialEstados().forEach(e ->
    //        System.out.println(e.getFecha() + " | " + e.getTipoEstado() + " | " + e.getDetalle()));
    assertEquals(estadosEsperados, donacion.getHistorialEstados().stream().map(EstadoDonacion::getTipoEstado).toList());
  }

  @Test
  public void entregaFallida() {
    Donacion donacion = new Donacion(List.of(fideosLucetti), List.of(donantePrueba));

    donacion.confirmarAsignacion(new Beneficiario("razon", "direccion", List.of(new CorreoDeContato("esperanza@prueba.com", true))));
    donacion.confirmarRuta();
    donacion.confirmarTrasladoEnCurso();
    donacion.notificarEntregaFallida("No se encontraba en el domicilio");

    List<TipoEstadoDonacion> estadosEsperados = List.of(
            TipoEstadoDonacion.EN_DEPOSITO,
            TipoEstadoDonacion.ASIGNACION_REALIZADA,
            TipoEstadoDonacion.LISTA_PARA_ENTREGAR,
            TipoEstadoDonacion.EN_TRASLADO,
            TipoEstadoDonacion.ENTREGA_FALLIDA
    );

    //donacion.getHistorialEstados().forEach(e ->
    //        System.out.println(e.getFecha() + " | " + e.getTipoEstado() + " | " + e.getDetalle()));
    assertEquals(estadosEsperados, donacion.getHistorialEstados().stream().map(EstadoDonacion::getTipoEstado).toList());
  }

  @Test
  public void donacionVencida() {
    Donacion donacion = new Donacion(List.of(fideosLucetti), List.of(donantePrueba));

    donacion.marcarVencida();

    List<TipoEstadoDonacion> estadosEsperados = List.of(
            TipoEstadoDonacion.EN_DEPOSITO,
            TipoEstadoDonacion.VENCIDA
    );

    //donacion.getHistorialEstados().forEach(e ->
    //        System.out.println(e.getFecha() + " | " + e.getTipoEstado() + " | " + e.getDetalle()));
    assertEquals(estadosEsperados, donacion.getHistorialEstados().stream().map(EstadoDonacion::getTipoEstado).toList());
  }
}
