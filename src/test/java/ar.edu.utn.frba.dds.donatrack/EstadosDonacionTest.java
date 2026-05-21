package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.builder.BienBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.CategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.builder.SubcategoriaBuilder;
import ar.edu.utn.frba.dds.donatrack.clasificacion.Categoria;
import ar.edu.utn.frba.dds.donatrack.clasificacion.Subcategoria;
import ar.edu.utn.frba.dds.donatrack.donacion.Bien;
import ar.edu.utn.frba.dds.donatrack.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donacion.EstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donacion.Perecedero;
import ar.edu.utn.frba.dds.donatrack.donacion.TipoEstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donacion.UnidadMedida;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EstadosDonacionTest {
    @Test
    public void estadoInicialDeUnaDonacionEsEnDeposito(){
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
    public void estadoDeUnaDonacionEnTraslado(){
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

        donacion.cambiarEstado(TipoEstadoDonacion.EN_TRASLADO, null);

        assertEquals(TipoEstadoDonacion.EN_TRASLADO, donacion.getEstadoActual());
    }

    @Test
    public void estadosDeLaDonacion(){
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

        donacion.cambiarEstado(TipoEstadoDonacion.EN_TRASLADO, null);
        donacion.cambiarEstado(TipoEstadoDonacion.ENTREGADA, null);

        List<TipoEstadoDonacion> estadosEsperados = List.of(
                TipoEstadoDonacion.EN_DEPOSITO,
                TipoEstadoDonacion.EN_TRASLADO,
                TipoEstadoDonacion.ENTREGADA
        );

        assertEquals(estadosEsperados, donacion.getTiposEstado());
    }

    @Test
    public void historialDeDonacion() {
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

        donacion.cambiarEstado(TipoEstadoDonacion.ASIGNACION_REALIZADA, null);
        donacion.cambiarEstado(TipoEstadoDonacion.LISTA_PARA_ENTREGAR, null);
        donacion.cambiarEstado(TipoEstadoDonacion.EN_TRASLADO, null);
        donacion.cambiarEstado(TipoEstadoDonacion.ENTREGA_FALLIDA, "No se encontraba en el domicilio");

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
        assertEquals(estadosEsperados, donacion.getTiposEstado());
    }
}
