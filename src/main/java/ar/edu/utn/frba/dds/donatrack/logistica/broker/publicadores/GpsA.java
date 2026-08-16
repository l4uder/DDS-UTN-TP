package ar.edu.utn.frba.dds.donatrack.logistica.broker.publicadores;

import ar.edu.utn.frba.dds.donatrack.logistica.broker.dto.GpsMensaje;
import ar.edu.utn.frba.dds.donatrack.shared.GsonConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class GpsA {
  private final static String brokerUrl = "tcp://broker.hivemq.com:1883";
  private final static String topic = "Donatrack/g7/dds/gps/ubicaciones/GpsA";

  public static void main(String[] args) {
    MqttClient cliente = null;
    Gson conversor = GsonConfig.crear();
    Path path = Paths.get("src/main/resources/coordenadasCamiones/gpsA.json");
    if (!Files.exists(path)) throw new RuntimeException("El archivo no existe");
    System.out.println("se enviara mensajes al topic: " + topic);

    try {
      List<GpsMensaje> mensajes = conversor.fromJson(Files.newBufferedReader(path), new TypeToken<List<GpsMensaje>>(){}.getType());
      System.out.println("Archivo de coordenadas cargada exitosamente, con: " + mensajes.size() + " coordenadas");

      cliente = new MqttClient(brokerUrl, "Donatrack-GPS-A");
      cliente.connect();

      for (GpsMensaje mensaje : mensajes) {
        String mensajeEnviar = conversor.toJson(mensaje);
        System.out.println("Enviando: " + mensajeEnviar);
        cliente.publish(topic, new MqttMessage(mensajeEnviar.getBytes()));
        pasanSeg(4);
      }

      System.out.println("Ruta finalizada.");
      cliente.disconnect();
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    } finally {
      if (cliente != null && cliente.isConnected()) {
        try {
          cliente.disconnect();
        } catch (Exception e) {
          System.err.println("No se pudo desconectar limpiamente: " + e.getMessage());
        }
      }
    }
  }

  //=============Funciones auxiliares=================
  private static void pasanSeg(Integer segundos) {
    try {
      Thread.sleep(segundos * 1000);
    } catch (InterruptedException e) {
      System.out.println("error al esperar segundos " + e.getMessage());
      Thread.currentThread().interrupt();
    }
  }

}