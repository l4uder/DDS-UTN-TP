package ar.edu.utn.frba.dds.donatrack.logistica.web.integracion;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.Beneficiario;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.beneficiario.DonacionEnTransito;
import ar.edu.utn.frba.dds.donatrack.shared.ConfiguracionEntorno;
import ar.edu.utn.frba.dds.donatrack.shared.GsonConfig;
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

public class ConectorDonacionesApi {
  private static final String TIPO_ESTADO_REALIZADA = "ASIGNACION_REALIZADA";
  private static final Type LISTA_DONACIONES_REMOTAS =
      new TypeToken<List<DonacionRemotaResponse>>() {
      }.getType();
  private final HttpClient httpClient;
  private final String baseUrl;
  private final Gson gson;

  public ConectorDonacionesApi() {
    this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    this.baseUrl = ConfiguracionEntorno.getInstance().elegirUrlDonaciones("http://localhost:7070");
    this.gson = GsonConfig.crear();
  }

  public List<DonacionEnTransito> buscarDonacionesAsignadas() {
    String url = baseUrl + "/donaciones?estado=" + TIPO_ESTADO_REALIZADA;
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .GET()
        .header("Accept", "application/json")
        .build();

    HttpResponse<String> response = enviar(request, "buscar donaciones asignadas");
    if (!esExitoso(response)) {
      throw new ServicioExternoException("Error al buscar donaciones asignadas: HTTP " + response.statusCode() + " — " + response.body());
    }

    List<DonacionRemotaResponse> remotas = gson.fromJson(response.body(), LISTA_DONACIONES_REMOTAS);

    return aDominio(remotas);
  }

  //activa el paso 3 de la donacion (Lista Para Entregar)
  public void marcarDonacionListaParaEntregar(String donacionId) {
    cambiarEstadoDonacion(donacionId, null, buildUrl(donacionId, "lista-para-entregar"));
  }
  //activa el paso 4 de la donacion (En Traslado)
  public void marcarDonacionEnCamino(String donacionId, String linkMapa) {
    CambioEstadoInicioRutaRequest body = new CambioEstadoInicioRutaRequest(linkMapa);
    cambiarEstadoDonacion(donacionId, gson.toJson(body), buildUrl(donacionId, "en-camino"));
  }
  //activa el paso 5 de la donacion (Entregada)
  public void marcarDonacionEntregaExitosa(String donacionId, String patente) {
    CambioEstadoEntregadaRequest body = new CambioEstadoEntregadaRequest(patente);
    cambiarEstadoDonacion(donacionId, gson.toJson(body), buildUrl(donacionId, "entregada"));
  }
  //activa el paso 5B de la donacion (Error Al Entregar)
  public void marcarDonacionErrorEntrega(String donacionId, String motivo) {
    CambioEstadoErrorEntregaRequest body = new CambioEstadoErrorEntregaRequest(motivo);
    cambiarEstadoDonacion(donacionId, gson.toJson(body), buildUrl(donacionId, "error-entrega"));
  }
  //activa el paso 6B de la donacion (Devuelta A Deposito)
  public void marcarDonacionVueltaDeposito(String donacionId) {
    cambiarEstadoDonacion(donacionId, null, buildUrl(donacionId, "vuelta-deposito"));
  }

  //===================== FUNCIONES AUXILIARES =======================
  private String buildUrl(String donacionId, String path){
    return baseUrl + "/donaciones/" + donacionId + "/" + path;
  }

  private void cambiarEstadoDonacion(String donacionId, String body, String url) {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .method("PATCH", (body==null) ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body))
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .build();

    HttpResponse<String> response = enviar(request, "cambiar estado de donación " + donacionId);
    if (!esExitoso(response)) {
      throw new ServicioExternoException("Error al cambiar estado de donación " + donacionId + ": HTTP " + response.statusCode() + " — " + response.body());
    }
  }

  private HttpResponse<String> enviar(HttpRequest request, String operacion) {
    try {
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException e) {
      throw new ServicioExternoException("Error de red al " + operacion + " en servicio de donaciones", e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ServicioExternoException("Llamada interrumpida al " + operacion + " en servicio de donaciones", e);
    }
  }

  private boolean esExitoso(HttpResponse<String> response) {
    int status = response.statusCode();
    return status >= 200 && status < 300;
  }

  private DonacionEnTransito aDominio(DonacionRemotaResponse remota) {
    DonacionRemotaResponse.BeneficiarioRemotoResponse beneficiarioRemoto = remota.beneficiario();
    if (beneficiarioRemoto == null) {
      throw new ServicioExternoException( "Donación asignada sin beneficiario: id=" + remota.id());
    }
    Beneficiario beneficiario = new Beneficiario(
        beneficiarioRemoto.id(),
        beneficiarioRemoto.razonSocial(),
        beneficiarioRemoto.direccion()
    );
    return new DonacionEnTransito(remota.id(), remota.descripcion(), beneficiario);
  }

  private List<DonacionEnTransito> aDominio(List<DonacionRemotaResponse> remotas) {
    if (remotas == null) return List.of();
    return remotas.stream().map(this::aDominio).toList();
  }

}
