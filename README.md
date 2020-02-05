# Smart Thermostat
This repository contains the **Microservices-based IoT-backend application** and the **Web app** for a **smart thermostat**. The project consists of a **microcontroller** talking to an IoT Hub via **MQTT** (VerneMQ), sending **temperature** and **humidity data**. The IoT Hub publishes the received data (**events**) to **Kafka** and the API Gateway subscribes to the broker and delivers the measurements to the web application via **WebSocket**. A chart is displayed to the user showing the measurements of the microcontroller in **real-time**. It also displays the data of a tilt sensor and a button which can toggle a fan (connected to the microcontroller) on/off by sending a **command** to the device. The communication basically looks like the following: 

**Web App <-via WebSocket-> API Gateway <-via Kafka-> IoT Hub <-via VerneMQ-> Microcontroller**

If you have any questions about the application, you'd like to know how to run it/interact with it or you wanna see the C code and the microcontroller (ATmega328 + ESP8266) used then feel free to contact me via [mmaresch.com](http://mmaresch.com).

# Dependencies
Thanks to everyone contributing to any of the following projects:
- Any Spring project
- Kafka
- Lombok
- Reactor
- Paho
- Angular
- Angular Material
- RxJS
- Chart.js
