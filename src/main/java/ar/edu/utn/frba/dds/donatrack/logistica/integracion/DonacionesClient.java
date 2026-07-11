package ar.edu.utn.frba.dds.donatrack.logistica.integracion;

import ar.edu.utn.frba.dds.donatrack.logistica.config.DonacionesServiceConfig;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.BeneficiarioDTO;
import ar.edu.utn.frba.dds.donatrack.logistica.dto.externo.DonacionAsignadaDTO;
import ar.edu.utn.frba.dds.donatrack.shared.GsonConfig;
import ar.edu.utn.frba.dds.donatrack.shared.dto.CambioEstadoRequest;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.ServicioExternoException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class DonacionesClient {

  private static final String ESTADO_ASIGNACION_REALIZADA = "ASIGNACION_REALIZADA";
  private static final Type LISTA_DONACIONES_REMOTAS =
      new TypeToken<List<DonacionRemotaResponse>>() {}.getType();

  private final HttpClient httpClient;
  private final String baseUrl;
  private final Gson gson;

  public DonacionesClient() {
    this(
        HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build(),
        DonacionesServiceConfig.BASE_URL,
        GsonConfig.crear()
    );
  }

  public DonacionesClient(HttpClient httpClient, String baseUrl, Gson gson) {
    this.httpClient = httpClient;
    this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    this.gson = gson;
  }

  public List<DonacionAsignadaDTO> buscarDonacionesAsignadas() {
    String url = baseUrl + "/donaciones?estado=" + ESTADO_ASIGNACION_REALIZADA;
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .header("Accept", "application/json")
        .build();

    HttpResponse<String> response = enviar(request, "buscar donaciones asignadas");
    if (response.statusCode() != 200) {
      throw new ServicioExternoException(
          "Error al buscar donaciones asignadas: HTTP " + response.statusCode()
              + " — " + response.body());
    }

    List<DonacionRemotaResponse> remotas = gson.fromJson(response.body(), LISTA_DONACIONES_REMOTAS);
    if (remotas == null) {
      return List.of();
    }
    return remotas.stream().map(this::aDonacionAsignada).toList();
  }

  public void cambiarEstadoDonacion(String donacionId, String estado) {
    String url = baseUrl + "/donaciones/" + donacionId + "/estados";
    String body = gson.toJson(new CambioEstadoRequest(estado, null));

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .POST(HttpRequest.BodyPublishers.ofString(body))
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .build();

    HttpResponse<String> response = enviar(request, "cambiar estado de donación " + donacionId);
    int status = response.statusCode();
    if (status < 200 || status >= 300) {
      throw new ServicioExternoException(
          "Error al cambiar estado de donación " + donacionId + ": HTTP " + status
              + " — " + response.body());
    }
  }

  private HttpResponse<String> enviar(HttpRequest request, String operacion) {
    try {
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException e) {
      throw new ServicioExternoException(
          "Error de red al " + operacion + " en servicio de donaciones", e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ServicioExternoException(
          "Llamada interrumpida al " + operacion + " en servicio de donaciones", e);
    }
  }

  private DonacionAsignadaDTO aDonacionAsignada(DonacionRemotaResponse remota) {
    DonacionRemotaResponse.BeneficiarioRemotoResponse beneficiarioRemoto = remota.beneficiario();
    if (beneficiarioRemoto == null) {
      throw new ServicioExternoException(
          "Donación asignada sin beneficiario: id=" + remota.id());
    }
    BeneficiarioDTO beneficiario = new BeneficiarioDTO(
        beneficiarioRemoto.id(),
        beneficiarioRemoto.razonSocial(),
        beneficiarioRemoto.direccion()
    );
    return new DonacionAsignadaDTO(remota.id(), remota.descripcion(), beneficiario);
  }
}
