package ar.edu.utn.frba.dds.donatrack.donaciones.service;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.GeneradorRankings;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.Ranking;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.algoritmos.CompatibilidadSemantica;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.algoritmos.PrioridadSubAtendidos;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.donacion.Donacion;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.BeneficiarioRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.DonacionRepository;
import ar.edu.utn.frba.dds.donatrack.donaciones.persistencia.RankingRepository;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import java.util.List;

public class AsignacionService {
  private final DonacionRepository repoDonaciones = DonacionRepository.getInstancia();
  private final BeneficiarioRepository repoBeneficiarios = BeneficiarioRepository.getInstancia();
  private final RankingRepository repoRankings = RankingRepository.getInstancia();

  public void ejecutarMatchmaking() {
    GeneradorRankings generadorRankings = new GeneradorRankings();
    generadorRankings.agregarAlgoritmo(new CompatibilidadSemantica());
    generadorRankings.agregarAlgoritmo(new PrioridadSubAtendidos());

    List<Beneficiario> beneficiarios = repoBeneficiarios.buscarTodos();
    List<Donacion> donaciones = repoDonaciones.buscarTodos();

    List<Ranking> resultados = generadorRankings.asignar(donaciones, beneficiarios);

    resultados.forEach(r -> repoRankings.guardar(r));
  }

  public List<Ranking> obtenerRankings() {
    return repoRankings.buscarTodos();
  }

  public Ranking obtenerRanking(String donacionId) {
    Donacion donacion = repoDonaciones.buscarPorId(donacionId);
    if (donacion == null) throw new RecursoNoEncontradoException("No se encontró ninguna donación con el ID: " + donacionId);

    Ranking ranking = repoRankings.buscarPorDonacion(donacion);
    if (ranking == null) throw new RecursoNoEncontradoException("La donacion " + donacionId + " no tiene un ranking generado");

    return ranking;
  }

  public Donacion confirmarAsignacion(String donacionId, String beneficiarioId) {
    Donacion donacion = repoDonaciones.buscarPorId(donacionId);
    if (donacion == null) throw new RecursoNoEncontradoException("No se encontró ninguna donación con el ID: " + donacionId);

    Beneficiario beneficiario = repoBeneficiarios.buscarPorId(beneficiarioId);
    if (beneficiario == null) throw new RecursoNoEncontradoException("No existe beneficiario con id: " + beneficiarioId);

    donacion.confirmarAsignacion(beneficiario);
    repoDonaciones.actualizar(donacion);
    repoBeneficiarios.actualizar(beneficiario);
    repoRankings.eliminar(donacionId);
    return donacion;
  }

}
