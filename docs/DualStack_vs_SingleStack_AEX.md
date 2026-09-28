# 🏛️ SAP PI Dual-Stack vs. SAP PO Single-Stack (Java AEX) Architecture

A technical deep-dive into the architectural evolution of SAP NetWeaver Process Integration and Process Orchestration.

---

## 1. Historical Context: Dual-Stack PI (XI 3.0 up to PI 7.31)

Dual-Stack systems comprised two tightly coupled runtime engines running on the same host or cluster:
1. **ABAP Stack (Integration Engine - IE):**
   * Handled message pipeline processing: Inbound receiver determination, interface determination, mapping execution, and message archiving.
   * Native ABAP monitoring via transaction `SXMB_MONI`.
   * Cross-Component Business Process Management (ccBPM) running on the ABAP Business Workflow engine.
2. **Java Stack (Adapter Engine - AE):**
   * Hosted non-SAP connectivity: File, JDBC, JMS, Mail, and third-party adapters.
   * Monitored via the J2EE Engine and Runtime Workbench (RWB).
   * **Major bottleneck:** Cross-stack communication between ABAP and Java required internal HTTP/RFC marshaling and unmarshaling, consuming significant CPU and memory.

---

## 2. Modernization: SAP PO 7.5 Single-Stack (Java AEX)

Introduced to eliminate cross-stack overhead and provide linear horizontal scalability:
* **Advanced Adapter Engine Extended (AEX):** All pipeline execution steps (Routing, Mapping, Queuing) run purely in-memory on the Java VM.
* **Integrated Configuration Object (ICO):** Replaced 4 classic directory objects (Sender Agreement, Receiver Determination, Interface Determination, Receiver Agreement) with a single atomic configuration.
* **NetWeaver BPM (NW BPM):** Replaced ccBPM with a standard BPEL/BPMN Java process orchestration engine.
* **NetWeaver BRM (Business Rules Management):** Enabled business rule tables externalized from code.
