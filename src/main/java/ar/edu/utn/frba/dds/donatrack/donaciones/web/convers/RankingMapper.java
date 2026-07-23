package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.Ranking;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.asignacion.RankingResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RankingMapper {

  public static RankingResponse aDto(Ranking ranking) {
    return new RankingResponse(
        ranking.getId(),
        ranking.getFechaGeneracion(),
        DonacionMapper.aDto(ranking.getDonacion()),
        CandidatoMapper.aDto(ranking.getCandidatos()));
  }

}