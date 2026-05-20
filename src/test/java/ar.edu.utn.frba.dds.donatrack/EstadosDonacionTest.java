package ar.edu.utn.frba.dds.donatrack;

import ar.edu.utn.frba.dds.donatrack.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donacion.EstadoDonacion;
import ar.edu.utn.frba.dds.donatrack.donacion.TipoEstadoDonacion;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EstadosDonacionTest {
    @Test
    public void estadoInicialDeUnaDonacionEsEnDeposito(){
        Donacion donacion = new Donacion("varias sillas y mesas", null);

        assertEquals(donacion.getEstadoActual(), TipoEstadoDonacion.EN_DEPOSITO);
    }

    @Test
    public void estadoDeUnaDonacionEnTraslado(){
        Donacion donacion = new Donacion("varias sillas y mesas", null);

        donacion.cambiarEstado(new EstadoDonacion(TipoEstadoDonacion.EN_TRASLADO));

        assertEquals(donacion.getEstadoActual(), TipoEstadoDonacion.EN_TRASLADO);
    }

    @Test
    public void estadosDeLaDonacion(){
        Donacion donacion = new Donacion("varias sillas y mesas", null);

        donacion.cambiarEstado(new EstadoDonacion(TipoEstadoDonacion.EN_TRASLADO));
        donacion.cambiarEstado(new EstadoDonacion(TipoEstadoDonacion.ENTREGADA));

        List<TipoEstadoDonacion> estadosEsperados = List.of(
                TipoEstadoDonacion.EN_DEPOSITO,
                TipoEstadoDonacion.EN_TRASLADO,
                TipoEstadoDonacion.ENTREGADA
        );

        assertEquals(donacion.getHistorialEstados(), estadosEsperados);
    }
}
