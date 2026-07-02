package ar.edu.utn.frba.dds.donatrack.dominio.mqtt.suscriptor;

import ar.edu.utn.frba.dds.donatrack.dominio.mqtt.GpsMensaje;
import org.eclipse.paho.client.mqttv3.*;
import com.google.gson.Gson;

public class EstacionRecepcion {
  private MqttClient client;
  private String brokerUrl;
  private String clientId;
  private Gson gson;

  public EstacionRecepcion() {
    this.client = null;
    this.brokerUrl = "tcp://broker.hivemq.com:1883";
    this.clientId = "Estacion-dds-g7" + System.currentTimeMillis();
    this.gson = new Gson();
  }

  public void conectar() {
    try {
      client = new MqttClient(brokerUrl, clientId);

      // Se configuran los eventos (qué hacer cuando llegan cosas)
      client.setCallback(new MqttCallback() {
        public void connectionLost(Throwable cause) {
          System.out.println("Se perdió la conexión con el broker: " + cause.getMessage());
        }

        public void messageArrived(String topic, MqttMessage message) throws Exception {
          String mensajeRecibido = new String(message.getPayload());
          GpsMensaje datos = gson.fromJson(mensajeRecibido, GpsMensaje.class);
          System.out.println("llego nuevo mensaje: " + datos.getId() +
              " | Lat: " + datos.getLatitud() +
              " | Lon: " + datos.getLongitud());
        }

        public void deliveryComplete(IMqttDeliveryToken token) {}
      });

      MqttConnectOptions options = new MqttConnectOptions();
      options.setCleanSession(true);

      client.connect(options);
      System.out.println("Conectado al broker exitosamente.");

    } catch (MqttException e) {
      System.out.println("Error al conectarse");
      System.err.println("Error al conectar: " + e.getMessage());
    }
  }

  // 2. Fase de Suscripción
  public void suscribir(String topic) {
    try {
      if (client != null && client.isConnected()) {
        client.subscribe(topic);
        System.out.println("Suscrito al topic: " + topic);
      } else {
        System.out.println("No se puede suscribir: el cliente no está conectado.");
      }
    } catch (MqttException e) {
      System.err.println("Error al suscribirse: " + e.getMessage());
    }
  }

  public void desconectar() {
    try {
      if (client != null && client.isConnected()) {
        client.disconnect();
        System.out.println("Desconectado limpiamente del broker.");
      }
    } catch (MqttException e) {
      System.out.println("Error al cerrar el broker.");
      e.printStackTrace();
    }
  }
}
