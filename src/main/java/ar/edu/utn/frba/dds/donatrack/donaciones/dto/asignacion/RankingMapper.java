package ar.edu.utn.frba.dds.donatrack.donaciones.dto.asignacion;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.Ranking;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import java.util.ArrayList;
import java.util.List;

public class RankingMapper {

  private RankingMapper() {
  }

  public static RankingResponse aResponse(Ranking ranking) {
    List<Beneficiario> beneficiarios = ranking.getCandidatos();
    List<CandidatoDto> candidatos = new ArrayList<>();
    for (int i = 0; i < beneficiarios.size(); i++) {
      Beneficiario beneficiario = beneficiarios.get(i);
      candidatos.add(new CandidatoDto(
          i + 1,
          beneficiario.getId(),
          beneficiario.getRazonSocial(),
          beneficiario.getDireccion()));
    }
    return new RankingResponse(
        ranking.getDonacionId(),
        ranking.getFechaGeneracion(),
        candidatos);
  }

}
