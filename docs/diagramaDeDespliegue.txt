@startuml
title Diagrama de Despliegue

actor Admin as admin
actor Donante as donante
actor Beneficiario as beneficiario

rectangle {
node "Cliente" as cliente

node "Microservicio Donaciones" as ms_donaciones
node "Microservicio Logistica" as ms_logistica
}

node "Gps" as gps

cloud "Whatsapp" as whatsapp
cloud "CorreoElectronico" as correo
cloud "Sms" as sms

cloud "GPS Broker" as gps_broker

admin --> cliente
donante --> cliente
beneficiario --> cliente
gps_broker --> ms_logistica

cliente --> ms_donaciones : https
cliente --> ms_logistica : https

ms_donaciones <--> ms_logistica : http

gps --> gps_broker : mqtt

ms_donaciones --> whatsapp : https
ms_donaciones --> correo : smtp
ms_donaciones --> sms : https

@enduml