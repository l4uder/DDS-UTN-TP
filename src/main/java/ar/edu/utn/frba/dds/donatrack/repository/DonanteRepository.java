package ar.edu.utn.frba.dds.donatrack.repository;

import ar.edu.utn.frba.dds.donatrack.donante.Donante;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DonanteRepository {

    private Map<String, Donante> donantesStore = new HashMap<>();

    public static final DonanteRepository INSTANCE = new DonanteRepository();

    private DonanteRepository() {
    }

    public void guardarDonante(Donante donante) throws PersistanceException {
        donantesStore.put(donante.getEmail(), donante);
    }

    public List<Donante> buscarTodos(){
        return donantesStore.values().stream().toList();
    }
}
