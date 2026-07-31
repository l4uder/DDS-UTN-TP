package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.generadorrankings.Ranking;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.asignacion.RankingResponse;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.asignacion.RankingResumenResponse;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RankingMapper {

  public static RankingResponse aDto(Ranking ranking) {
    return new RankingResponse(
        ranking.getId(),
        ranking.getFechaGeneracion(),
        DonacionMapper.aDto(ranking.getDonacion()),
        CandidatoMapper.aDto(ranking.getCandidatos())
    );
  }

  public static RankingResumenResponse aDtoResumen(Ranking ranking) {
    return new RankingResumenResponse(
        ranking.getId(),
        ranking.getFechaGeneracion()
    );
  }

  public static List<RankingResumenResponse> aDtoResumen(List<Ranking> rankings) {
    return rankings.stream().map(RankingMapper::aDtoResumen).toList();
  }

}