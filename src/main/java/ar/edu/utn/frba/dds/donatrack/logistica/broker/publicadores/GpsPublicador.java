package ar.edu.utn.frba.dds.donatrack.logistica.broker.publicadores;

import ar.edu.utn.frba.dds.donatrack.logistica.broker.dto.GpsMensaje;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class GpsPublicador {

  public static void main(String[] args) {
    String brokerUrl = "tcp://broker.hivemq.com:1883";
    ObjectMapper conversor = new ObjectMapper();
    conversor.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    Scanner teclado = new Scanner(System.in);

    System.out.print("Ingrese el ID del gps por ejemplo: gpsA o gpsB: ");
    String idDispositivo = teclado.nextLine();
    String topic = "g7/dds/vehiculos/gps/ubicaciones/" + idDispositivo;
    System.out.print("ArchivoJson: ");
    String nombreArchivo = teclado.nextLine();
    String rutaArchivo = "src/main/resources/coordenadasCamiones/" + nombreArchivo;

    Path path = Paths.get(rutaArchivo);
    if (!existeArchivo(path)) {
      System.out.println("ERROR: No se encontró el archivo en la ruta: " + rutaArchivo);
      return;
    }
    System.out.println("se enviara mensajes al topic: " + topic);
    try {
      String contenidoJson = new String(Files.readAllBytes(path));
      List<GpsMensaje> mensajes = conversor.readValue(contenidoJson,
          new TypeReference<List<GpsMensaje>>() {});

      System.out.println("Archivo de coordenadas cargada exitosamente, con: " + mensajes.size()
                    + " coordenadas");

      MqttClient client = new MqttClient(brokerUrl, idDispositivo);
      client.connect();

      for (GpsMensaje mensaje : mensajes) {
        //agregamos id que tomamos por consola
        mensaje.setId(idDispositivo);

        String mensajeEnviar = conversor.writeValueAsString(mensaje);
        System.out.println("Enviando: " + mensajeEnviar);

        MqttMessage message = new MqttMessage(mensajeEnviar.getBytes());
        message.setQos(1);
        client.publish(topic, message);
        pasanSeg(4);
      }

      System.out.println("Ruta finalizada.");
      client.disconnect();
    } catch (Exception e) {
      System.out.println("Error al enviar el mensaje: " + e.getMessage());
    } finally {
      teclado.close();
    }
  }

  //=============Funciones auxiliares=================
  private static Boolean existeArchivo(Path path) {
    return Files.exists(path);
  }

  private static void pasanSeg(Integer segundos) {
    try {
      Thread.sleep(segundos * 1000);
    } catch (InterruptedException e) {
      System.out.println("error al esperar segundos " + e.getMessage());
    }
  }

}