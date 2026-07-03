package ar.edu.utn.frba.dds.donatrack.dominio.mqtt.suscriptor;

import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Camion;
import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Coordenada;
import ar.edu.utn.frba.dds.donatrack.dominio.logistica.Gps;
import ar.edu.utn.frba.dds.donatrack.dominio.mqtt.GpsMensaje;
import ar.edu.utn.frba.dds.donatrack.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.persistencia.GpsRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class EstacionRecepcion {
  private MqttClient client;
  private String brokerUrl;
  private String clientId;
  private ObjectMapper conversor;
  private CamionRepository repoCamiones;
  private GpsRepository repoGps;

  public EstacionRecepcion(CamionRepository camionRepository, GpsRepository gpsRepository) {
    this.client = null;
    this.brokerUrl = "tcp://broker.hivemq.com:1883";
    this.clientId = "Estacion-dds-g7" + System.currentTimeMillis();
    this.conversor = new ObjectMapper();
    conversor.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    conversor.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.repoCamiones = camionRepository;
    this.repoGps = gpsRepository;
  }

  public void conectar() {
    try {
      client = new MqttClient(brokerUrl, clientId);

      // Se configuran los eventos (qué hacer cuando llegan cosas)
      client.setCallback(new MqttCallback() {
        public void connectionLost(Throwable cause) {
          System.out.println("Se perdió la conexión con el broker: " + cause.getMessage());
        }

        public void messageArrived(String topic, MqttMessage message) {
          try {
            String mensajeRecibido = new String(message.getPayload());
            GpsMensaje datos = conversor.readValue(mensajeRecibido, GpsMensaje.class);
            System.out.println("llego nuevo mensaje | gpsId: " + datos.getId()
                + " | bateria: " + datos.getNivelBateria()
                + " | Latitud: " + datos.getLatitud()
                + " | Longitud: " + datos.getLongitud());
            Camion camion = repoCamiones.buscarCamionPorGps(datos.getId());
            camion.agregarCoordenada(new Coordenada(datos.getLatitud(), datos.getLongitud()));
            Gps gps = repoGps.buscarPorId(datos.getId());
            gps.actualizarEstado(datos.getNivelBateria());
          } catch (Exception e) {
            System.out.println("Error procesando un mensaje del topic " + topic);
            System.out.println("Motivo: " + e.getMessage());
          }
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
