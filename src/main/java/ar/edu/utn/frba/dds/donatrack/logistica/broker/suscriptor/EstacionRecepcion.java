package ar.edu.utn.frba.dds.donatrack.logistica.broker.suscriptor;

import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Camion;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Coordenada;
import ar.edu.utn.frba.dds.donatrack.logistica.dominio.camion.Gps;
import ar.edu.utn.frba.dds.donatrack.logistica.broker.dto.GpsMensaje;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.CamionRepository;
import ar.edu.utn.frba.dds.donatrack.logistica.persistencia.GpsRepository;
import ar.edu.utn.frba.dds.donatrack.shared.GsonConfig;
import ar.edu.utn.frba.dds.donatrack.shared.excepciones.RecursoNoEncontradoException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class EstacionRecepcion {
  private final String brokerUrl =  "tcp://broker.hivemq.com:1883";
  private MqttClient cliente;
  private Gson conversor;
  private CamionRepository repoCamiones;
  private GpsRepository repoGps;

  public EstacionRecepcion(CamionRepository camionRepository, GpsRepository gpsRepository) {
    this.repoCamiones = camionRepository;
    this.repoGps = gpsRepository;
    this.cliente = null;
    //this.conversor = new ObjectMapper();
    //conversor.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    //conversor.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.conversor = GsonConfig.crear();
  }

  public void conectar() {
    try {
      cliente = new MqttClient(brokerUrl, "Donatrack-Receptor-DDS-G7");

      cliente.setCallback(new MqttCallback() {
        public void connectionLost(Throwable cause) {
          System.out.println("Se perdió la conexión con el broker: " + cause.getMessage());
        }

        public void messageArrived(String topic, MqttMessage message) {
          try {
            String mensajeRecibido = new String(message.getPayload());
            System.out.println("llego nuevo mensaje: " + mensajeRecibido);
            GpsMensaje datos = conversor.fromJson(mensajeRecibido, GpsMensaje.class);
            Camion camion = repoCamiones.buscarPorGps(datos.imei());
            if (camion == null) throw new RecursoNoEncontradoException("No existe camión con el GPS " + datos.imei());
            Gps gps = repoGps.buscarPorId(datos.imei());
            if (gps == null) throw new RecursoNoEncontradoException("No existe GPS con el imei " + datos.imei());
            camion.agregarCoordenada(new Coordenada(datos.latitud(), datos.longitud()));
            gps.actualizarEstado(datos.nivelBateria());
            repoCamiones.actualizar(camion);
            repoGps.actualizar(gps);
          } catch (Exception e) {
            System.out.println("Error procesando un mensaje del topic " + topic);
            System.out.println("Motivo: " + e.getMessage());
          }
        }

        public void deliveryComplete(IMqttDeliveryToken token) {}
      });

      MqttConnectOptions options = new MqttConnectOptions();
      options.setCleanSession(true);

      cliente.connect(options);
      System.out.println("Conectado al broker exitosamente.");

    } catch (MqttException e) {
      System.out.println("Error al conectarse");
      System.err.println("Error al conectar: " + e.getMessage());
    }
  }

  public void suscribir(String topic) {
    try {
      if (cliente != null && cliente.isConnected()) {
        cliente.subscribe(topic);
        System.out.println("Suscrito al topic: " + topic);
      } else {
        System.out.println("No se puede suscribir: el cliente no está conectado.");
      }
    } catch (MqttException e) {
      throw new RuntimeException("Error al suscribirse: " + e.getMessage(), e);
    }
  }

  public void desconectar() {
    try {
      if (cliente != null && cliente.isConnected()) {
        cliente.disconnect();
        System.out.println("Desconectado limpiamente del broker.");
      }
    } catch (MqttException e) {
      throw new RuntimeException("Error al cerrar el broker " + e.getMessage(), e);
    }
  }

}
