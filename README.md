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

### **Build With** 🛠️

<div style="text-align: left">
    <p>
        <a href="https://www.jetbrains.com/idea/" target="_blank"> <img alt="IntelliJ IDEA" src="https://cdn.svgporn.com/logos/intellij-idea.svg" height="60" width = "60"></a>
        <a href="https://www.java.com/" target="_blank"> <img alt="Java" src="https://cdn.svgporn.com/logos/java.svg" height="60" width = "60"></a>
    </p>
</div>

This project requires the following versions:

- **jdk**: 20
