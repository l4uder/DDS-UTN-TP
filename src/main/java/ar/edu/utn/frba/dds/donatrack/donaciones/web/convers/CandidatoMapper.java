package ar.edu.utn.frba.dds.donatrack.donaciones.web.convers;

import ar.edu.utn.frba.dds.donatrack.donaciones.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.donaciones.web.dto.candidato.CandidatoDto;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CandidatoMapper {

  public static List<CandidatoDto> aDto(List<Beneficiario> beneficiarios) {
    List<CandidatoDto> candidatos = new ArrayList<>();

    for (int i = 0; i < beneficiarios.size(); i++) {
      Beneficiario beneficiario = beneficiarios.get(i);
      candidatos.add(new CandidatoDto(i+1, BeneficiarioMapper.aDtoResumen(beneficiario)));
    }
    return candidatos;
  }

}
