package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.beneficiario.EntidadBeneficiaria;
import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.CategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.SubcategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donacion.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EstadosDonacionTest {
    @Test
    public void estadoInicialDeUnaDonacionEsEnDeposito() {
        Subcategoria fideos = new SubcategoriaBuilder()
                .conNombre("Fideos secos")
                .conCategoria(new CategoriaBuilder().conNombre("Alimentos").build())
                .build();

        Perecedero fideosLucetti = new BienBuilder()
                .conDescripcion("Fideos Lucchetti")
                .conCantidad(4)
                .conSubcategoria(fideos)
                .buildPerecedero(UnidadMedida.UNIDADES);

        Donacion donacion = new Donacion(List.of(fideosLucetti));

        assertEquals(TipoEstadoDonacion.EN_DEPOSITO, donacion.getEstadoActual());
    }

    @Test
    public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeEnDeposito() {
        Subcategoria fideos = new SubcategoriaBuilder()
                .conNombre("Fideos secos")
                .conCategoria(new CategoriaBuilder().conNombre("Alimentos").build())
                .build();

        Perecedero fideosLucetti = new BienBuilder()
                .conDescripcion("Fideos Lucchetti")
                .conCantidad(4)
                .conSubcategoria(fideos)
                .buildPerecedero(UnidadMedida.UNIDADES);

        Donacion donacion = new Donacion(List.of(fideosLucetti));

        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarTrasladoEnCurso);
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRuta);
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarEntrega);
        assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.notificarEntregaFallida("razon"));
        assertEquals(TipoEstadoDonacion.EN_DEPOSITO, donacion.getEstadoActual());
    }

    @Test
    public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeAsignacionRealizada() {
        Subcategoria fideos = new SubcategoriaBuilder()
                .conNombre("Fideos secos")
                .conCategoria(new CategoriaBuilder().conNombre("Alimentos").build())
                .build();

        Perecedero fideosLucetti = new BienBuilder()
                .conDescripcion("Fideos Lucchetti")
                .conCantidad(4)
                .conSubcategoria(fideos)
                .buildPerecedero(UnidadMedida.UNIDADES);

        Donacion donacion = new Donacion(List.of(fideosLucetti));

        donacion.confirmarAsignacion(new EntidadBeneficiaria("razon", "direccion", List.of()));

        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarTrasladoEnCurso);
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRecepcionDeposito);
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarEntrega);
        assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.notificarEntregaFallida("razon"));
        assertEquals(TipoEstadoDonacion.ASIGNACION_REALIZADA, donacion.getEstadoActual());
    }

    @Test
    public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeListaParaEntregar() {
        Subcategoria fideos = new SubcategoriaBuilder()
                .conNombre("Fideos secos")
                .conCategoria(new CategoriaBuilder().conNombre("Alimentos").build())
                .build();

        Perecedero fideosLucetti = new BienBuilder()
                .conDescripcion("Fideos Lucchetti")
                .conCantidad(4)
                .conSubcategoria(fideos)
                .buildPerecedero(UnidadMedida.UNIDADES);

        Donacion donacion = new Donacion(List.of(fideosLucetti));

        donacion.confirmarAsignacion(new EntidadBeneficiaria("razon", "direccion", List.of()));
        donacion.confirmarRuta();

        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarEntrega);
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRecepcionDeposito);
        assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.confirmarAsignacion(new EntidadBeneficiaria("razon", "direccion", List.of())));
        assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.notificarEntregaFallida("razon"));
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::marcarVencida);
        assertEquals(TipoEstadoDonacion.LISTA_PARA_ENTREGAR, donacion.getEstadoActual());
    }

    @Test
    public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeEnViaje() {
        Subcategoria fideos = new SubcategoriaBuilder()
                .conNombre("Fideos secos")
                .conCategoria(new CategoriaBuilder().conNombre("Alimentos").build())
                .build();

        Perecedero fideosLucetti = new BienBuilder()
                .conDescripcion("Fideos Lucchetti")
                .conCantidad(4)
                .conSubcategoria(fideos)
                .buildPerecedero(UnidadMedida.UNIDADES);

        Donacion donacion = new Donacion(List.of(fideosLucetti));

        donacion.confirmarAsignacion(new EntidadBeneficiaria("razon", "direccion", List.of()));
        donacion.confirmarRuta();
        donacion.confirmarTrasladoEnCurso();

        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRuta);
        assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.confirmarAsignacion(new EntidadBeneficiaria("razon", "direccion", List.of())));
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::marcarVencida);
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRecepcionDeposito);

        assertEquals(TipoEstadoDonacion.EN_TRASLADO, donacion.getEstadoActual());
    }

    @Test
    public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeEntregaFallida() {
        Subcategoria fideos = new SubcategoriaBuilder()
                .conNombre("Fideos secos")
                .conCategoria(new CategoriaBuilder().conNombre("Alimentos").build())
                .build();

        Perecedero fideosLucetti = new BienBuilder()
                .conDescripcion("Fideos Lucchetti")
                .conCantidad(4)
                .conSubcategoria(fideos)
                .buildPerecedero(UnidadMedida.UNIDADES);

        Donacion donacion = new Donacion(List.of(fideosLucetti));

        donacion.confirmarAsignacion(new EntidadBeneficiaria("razon", "direccion", List.of()));
        donacion.confirmarRuta();
        donacion.confirmarTrasladoEnCurso();
        donacion.notificarEntregaFallida("No se encontraba en el domicilio");

        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRuta);
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarTrasladoEnCurso);
        assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.confirmarAsignacion(new EntidadBeneficiaria("razon", "direccion", List.of())));
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::marcarVencida);

        assertEquals(TipoEstadoDonacion.ENTREGA_FALLIDA, donacion.getEstadoActual());
    }

    @Test
    public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeEntregaRealizada() {
        Subcategoria fideos = new SubcategoriaBuilder()
                .conNombre("Fideos secos")
                .conCategoria(new CategoriaBuilder().conNombre("Alimentos").build())
                .build();

        Perecedero fideosLucetti = new BienBuilder()
                .conDescripcion("Fideos Lucchetti")
                .conCantidad(4)
                .conSubcategoria(fideos)
                .buildPerecedero(UnidadMedida.UNIDADES);

        Donacion donacion = new Donacion(List.of(fideosLucetti));

        donacion.confirmarAsignacion(new EntidadBeneficiaria("razon", "direccion", List.of()));
        donacion.confirmarRuta();
        donacion.confirmarTrasladoEnCurso();
        donacion.confirmarEntrega();

        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRuta);
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarTrasladoEnCurso);
        assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.confirmarAsignacion(new EntidadBeneficiaria("razon", "direccion", List.of())));
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::marcarVencida);
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRecepcionDeposito);
        assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.notificarEntregaFallida("razon"));

        assertEquals(TipoEstadoDonacion.ENTREGADA, donacion.getEstadoActual());
    }

    @Test
    public void cambiosDeEstadoInvalidosDebenLanzarExcepcionDesdeVencida() {
        Subcategoria fideos = new SubcategoriaBuilder()
                .conNombre("Fideos secos")
                .conCategoria(new CategoriaBuilder().conNombre("Alimentos").build())
                .build();

        Perecedero fideosLucetti = new BienBuilder()
                .conDescripcion("Fideos Lucchetti")
                .conCantidad(4)
                .conSubcategoria(fideos)
                .buildPerecedero(UnidadMedida.UNIDADES);

        Donacion donacion = new Donacion(List.of(fideosLucetti));

        donacion.marcarVencida();

        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRuta);
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarTrasladoEnCurso);
        assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.confirmarAsignacion(new EntidadBeneficiaria("razon", "direccion", List.of())));
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarEntrega);
        assertThrows(CambioDeEstadoNoPermitidoException.class, donacion::confirmarRecepcionDeposito);
        assertThrows(CambioDeEstadoNoPermitidoException.class, () -> donacion.notificarEntregaFallida("razon"));

        assertEquals(TipoEstadoDonacion.VENCIDA, donacion.getEstadoActual());
    }

    @Test
    public void entregaExitosa() {
        Subcategoria fideos = new SubcategoriaBuilder()
                .conNombre("Fideos secos")
                .conCategoria(new CategoriaBuilder().conNombre("Alimentos").build())
                .build();

        Perecedero fideosLucetti = new BienBuilder()
                .conDescripcion("Fideos Lucchetti")
                .conCantidad(4)
                .conSubcategoria(fideos)
                .buildPerecedero(UnidadMedida.UNIDADES);

        Donacion donacion = new Donacion(List.of(fideosLucetti));

        donacion.confirmarAsignacion(new EntidadBeneficiaria("razon", "direccion", List.of()));
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

        donacion.getHistorialEstados().forEach(e ->
                System.out.println(e.getFecha() + " | " + e.getTipoEstado() + " | " + e.getDetalle())
        );
        assertEquals(estadosEsperados, donacion.getHistorialEstados().stream().map(EstadoDonacion::getTipoEstado).toList());
    }

    @Test
    public void entregaFallida() {
        Subcategoria fideos = new SubcategoriaBuilder()
                .conNombre("Fideos secos")
                .conCategoria(new CategoriaBuilder().conNombre("Alimentos").build())
                .build();

        Perecedero fideosLucetti = new BienBuilder()
                .conDescripcion("Fideos Lucchetti")
                .conCantidad(4)
                .conSubcategoria(fideos)
                .buildPerecedero(UnidadMedida.UNIDADES);

        Donacion donacion = new Donacion(List.of(fideosLucetti));

        donacion.confirmarAsignacion(new EntidadBeneficiaria("razon", "direccion", List.of()));
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

        donacion.getHistorialEstados().forEach(e ->
                System.out.println(e.getFecha() + " | " + e.getTipoEstado() + " | " + e.getDetalle())
        );
        assertEquals(estadosEsperados, donacion.getHistorialEstados().stream().map(EstadoDonacion::getTipoEstado).toList());
    }

    @Test
    public void donacionVencida() {
        Subcategoria fideos = new SubcategoriaBuilder()
                .conNombre("Fideos secos")
                .conCategoria(new CategoriaBuilder().conNombre("Alimentos").build())
                .build();

        Perecedero fideosLucetti = new BienBuilder()
                .conDescripcion("Fideos Lucchetti")
                .conCantidad(4)
                .conSubcategoria(fideos)
                .buildPerecedero(UnidadMedida.UNIDADES);

        Donacion donacion = new Donacion(List.of(fideosLucetti));

        donacion.marcarVencida();

        List<TipoEstadoDonacion> estadosEsperados = List.of(
                TipoEstadoDonacion.EN_DEPOSITO,
                TipoEstadoDonacion.VENCIDA
        );

        donacion.getHistorialEstados().forEach(e ->
                System.out.println(e.getFecha() + " | " + e.getTipoEstado() + " | " + e.getDetalle())
        );
        assertEquals(estadosEsperados, donacion.getHistorialEstados().stream().map(EstadoDonacion::getTipoEstado).toList());
    }
}
