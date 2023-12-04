![ICESI University Logo](https://www.icesi.edu.co/launiversidad/images/La_universidad/logo_icesi.png)

## Distributed Sorting. Software Architecture

### **Authors** ✒️

- Dylan Bermúdez Cardona - A00381287
- Santiago Escobar León - A00382203
- Daniel Montezuma Sevillano - A00382231
- Diego Fernando Mueses Zúñiga - A00382021

### **Project Objectives**

OT1. Explain the fundamental elements involved in the design of the software architecture of a computing system, including complementary aspects of the hardware architecture (processing and storage) and interconnection, and how these affect the achievement of quality attributes in the system; correctly use the standardized UML notation to specify these design elements.

This approach must recognize the responsibility assumed when designing the system architecture, in the scope of the proposed solution and the tradeoffs involved, considering professional ethics.

OT2. Derive a first version of software architecture from the analysis of given requirements, specifying it in a standardized UML notation. This analysis involves identifying architecturally significant requirements (ASR) established by stakeholders, and selecting architectural drivers based on the prioritization of said RAS.

In the process of this derivation, both the commitments (tradeoffs) and responsibilities that a software architect assumes with stakeholders and with society in general must be recognized, when prioritizing a subset of the RAS considered; also recognize the scope and impact that these have on the overall design of the system and its quality attributes.

OT3. Identify and apply architectural patterns and styles according to a prioritized set of architecturally significant requirements (RAS), mappable to quality attributes, to refine a reference architecture and obtain a specific software architecture, for systems of low or medium complexity.

Recognize that the quality of the software largely depends on the good treatment of these RAS and the relationships (interactions) between the components (re)used in the software, since desirable properties emerge from all of this (e.g., good quality attributes).

OT4. Define the global structure of the code based on the architectural design for its implementation, and carry out its deployment.

### **Project Description: Distributed Sorting System**

This project aims to design and implement a software system to address sorting challenges in large datasets through distributed sorting. The system includes a client that requests the data file name and invokes the distributed component (`dist_sorter`). The latter implements a distributed sorting strategy, combining various design patterns, with precise measurements of processing time and data transfer between nodes. Experiments are conducted by varying the number of machines and sorters, assessing performance in distributed configurations. The goal is to determine when distributing the process becomes advantageous and to evaluate the system's capacity. The results are presented in a ZIP file containing a deployment diagram, source code, compilation instructions, and a report featuring a table and comparative analysis of execution times versus data sizes, highlighting the improvements achieved in distributed performance compared to the monolithic execution.

### **Configuración** ⚙️

Para esta demostración, utilizaremos un proyecto con dos subproyectos para construir las aplicaciones Master y Worker.

Asegúrese de tener el archivo 'distributed_sorting.zip' en su computadora.

Descomprima el archivo 'distributed_sorting.zip' en su computadora.

Empaquete los directorios 'master' y 'worker' que se encuentran dentro de 'distributed_sorting' en un archivo .zip.

Para conectarse de forma remota a la computadora, ejecute el siguiente comando SSH:

```bash
ssh swarch@xhgrid# (contraseña: swarch)
```

***Siendo '#' el número de la computadora a la cual se quiere conectar.***

Ejecute el comando `ifconfig` en cada xhgrid# y tome nota (o recuerde) de la dirección IPV4 de cada computadora. Esta información será útil más adelante.

Para este ejemplo, nos conectaremos a dos computadoras para desplegar un Master y un Worker:

**Master:**
```bash
ssh swarch@xhgrid7 (contraseña: swarch)
```

**Worker:**
```bash
ssh swarch@xhgrid8 (contraseña: swarch)
```

**1. Master**

Abra la consola en xhgrid7 y cree una nueva carpeta (con un nombre de su elección) en esa ruta.

```bash
mkdir nueva_carpeta
```

En la computadora xhgrid7, vaya a /ruta/a/carpeta/creada/

Ejecute el comando `pwd` en xhgrid7 y copie la ruta.

Realice la transferencia en formato .zip del archivo `master` ubicado en la ruta de su computadora. Para hacerlo, ejecute el siguiente comando:

```bash
scp master.zip swarch@xhgrid7:/ruta/a/carpeta/creada/
```

Descomprima `master.zip` ejecutando el siguiente comando:

```bash
unzip master.zip
```

En xhgrid7, vaya a la ruta `/ruta/master/`. Luego, ejecute el siguiente comando:

```bash
gradle build
```

Vaya a `/ruta/distributed_sorting/master/build/libs/`

Ejecute el siguiente comando:

```bash
mc
```

Use las flechas del teclado para seleccionar el archivo llamado `master.jar` y presione la tecla `Enter`.

Use las flechas del teclado para seleccionar el archivo llamado `master.cfg` y presione la tecla `F4`.

Presione la tecla `i`.

Cambie la siguiente línea de código:

```bash
Master.Endpoints = default -h localhost -p 10000
```

a

```bash
Master.Endpoints = default -h IPV4_xhgrid7 -p 10000
```

Donde `IPV4_xhgrid7` indica la IPV4 obtenida previamente de la computadora xhgrid7.

Presione la tecla `Esc` y luego escriba `:x` y presione `Enter`.

Vaya a `/..` con las flechas del teclado. Presione `Enter`.

Luego, ejecute el siguiente comando:

```bash
java -jar master.jar
```

**2. Worker**

Abra la consola en xhgrid8 y cree una nueva carpeta (con un nombre de su elección) en esa ruta.

```bash
mkdir nueva_carpeta
```

En la computadora xhgrid8, vaya a /ruta/a/carpeta/creada/

Ejecute el comando `pwd` en xhgrid8 y copie la ruta.

Realice la transferencia en formato .zip del archivo `worker` ubicado en la ruta de su computadora. Para hacerlo, ejecute el siguiente comando:

```bash
scp worker.zip swarch@xhgrid8:/ruta/a/carpeta/creada/
```

Descomprima `worker.zip` ejecutando el siguiente comando:

```bash
unzip worker.zip
```

En xhgrid8, vaya a la ruta `/ruta/worker/`. Luego, ejecute el siguiente comando:

```bash
gradle build
```

Vaya a `/ruta/worker/build/libs/`

Ejecute el siguiente comando:

```bash
mc
```

Use las flechas del teclado para seleccionar el archivo llamado `worker.jar` y presione la tecla `Enter`.

Use las flechas del teclado para seleccionar el archivo llamado `worker.cfg` y presione la tecla `F4`.

Presione la tecla `i`.

Cambie la siguiente línea de código:

```bash
Worker.Endpoints = default -h localhost -p 10001
MasterInterface.Proxy = Master:default -h localhost -p 10000
```

a

```bash
Worker.Endpoints = default -h IPV4_xhgrid8 -p 10001
MasterInterface.Proxy = Master:default -h IPV4_xhgrid7 -p 10000
```

Donde `IPV4_xhgrid8` indica la IPV4 obtenida previamente de la computadora xhgrid8. Y `IPV4_xhgrid7` indica la IPV4 obtenida previamente de la computadora xhgrid7.

Presione la tecla `Esc` y luego escriba `:x` y presione `Enter`.

Vaya a `/..` con las flechas del teclado. Presione `Enter`.

Luego, ejecute el siguiente comando:

```bash
java -jar worker.jar
```

***Nota:*** El proceso de desplegar **workers** se ejecuta cuantas veces sea necesario.

### **Build With** 🛠️

<div style="text-align: left">
    <p>
        <a href="https://www.jetbrains.com/idea/" target="_blank"> <img alt="IntelliJ IDEA" src="https://cdn.svgporn.com/logos/intellij-idea.svg" height="60" width = "60"></a>
        <a href="https://www.java.com/" target="_blank"> <img alt="Java" src="https://cdn.svgporn.com/logos/java.svg" height="60" width = "60"></a>
    </p>
</div>

This project requires the following versions:

- **JDK:** 11
- **ZeroC ICE:** 3.7.6
- **Gradle:** 1.4.5
