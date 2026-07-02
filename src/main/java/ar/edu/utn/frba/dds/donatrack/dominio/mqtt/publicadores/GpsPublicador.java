package ar.edu.utn.frba.dds.donatrack.dominio.mqtt.publicadores;

import ar.edu.utn.frba.dds.donatrack.dominio.mqtt.GpsMensaje;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.google.gson.Gson;
import java.util.Scanner;

public class GpsPublicador {

  public static void main(String[] args) {
    String brokerUrl = "tcp://broker.hivemq.com:1883";
    Gson conversor = new Gson();
    Scanner teclado = new Scanner(System.in);
    System.out.print("Ingrese el ID del gps: ");
    String idDispositivo = teclado.nextLine();
    System.out.print("Ingrese la latitud: ");
    String latitud = teclado.nextLine();
    System.out.print("Ingrese la longitud: ");
    String longitud = teclado.nextLine();
    String topic = "g7/dds/vehiculos/gps/ubicaciones/" + idDispositivo;
    System.out.println("se enviara mensajes al topic: "+ topic);

    try {
      MqttClient client = new MqttClient(brokerUrl, idDispositivo);
      client.connect();

      GpsMensaje miUbicacion = new GpsMensaje(idDispositivo, latitud, longitud);

      String mensajeEnviar = conversor.toJson(miUbicacion);

      MqttMessage message = new MqttMessage(mensajeEnviar.getBytes());
      message.setQos(1);
      client.publish(topic, message);

      System.out.println("Enviado: " + mensajeEnviar);
      client.disconnect();
    } catch (Exception e) {
      System.out.println("Error al enviar el mensaje: " + e.getMessage());
    }
  }

}