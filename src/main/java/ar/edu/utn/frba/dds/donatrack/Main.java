package ar.edu.utn.frba.dds.donatrack;

public class Main {
  public static void main(String[] args) {
    System.out.println("=========================================");
    System.out.println("SISTEMA DONATRACK - Tareas Programadas");
    System.out.println("=========================================");
    System.out.println("Este archivo contiene múltiples procesos.");
    System.out.println("Por favor, ejecútelo usando el parámetro -cp:");
    System.out.println("y no java -jar ");
    System.out.println("Para beneficiariosAusentes: java -cp donatrack.jar ar.edu.utn.frba.dds.donatrack.cron.AusenciaBeneficiario");
    System.out.println("Para proceso matchmaking: java -cp donatrack.jar ar.edu.utn.frba.dds.donatrack.cron.ProcesoMatchmaking");
    System.out.println("=========================================");
  }
}
