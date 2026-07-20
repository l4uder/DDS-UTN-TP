package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.Ranking;
import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.asignacion.CandidatoDto;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.asignacion.RankingResponse;
import java.util.ArrayList;
import java.util.List;

public class RankingMapper {

  private RankingMapper() {}

  public static RankingResponse aDto(Ranking ranking) {
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
        ranking.getDonacion().getId(),
        ranking.getFechaGeneracion(),
        candidatos);
  }

}
