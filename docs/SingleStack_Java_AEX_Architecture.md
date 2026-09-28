# 🏛️ SAP PO 7.4 / 7.5 Single-Stack (Java AEX) Architecture

A technical deep-dive into the architecture, execution pipeline, and enterprise capabilities of SAP Process Orchestration running on the Java Enterprise Edition (JEE) runtime.

---

## 1. Architectural Overview: Java Single-Stack (AEX)

SAP Process Orchestration 7.4 and 7.5 are built purely on the **Advanced Adapter Engine Extended (AEX)** Java single-stack architecture, delivering high throughput, horizontal clustering, and zero ABAP-stack dependencies.

### Core Architectural Components

1. **Java Enterprise Runtime (AS Java):**
   * Built on SAP NetWeaver AS Java 7.4 / 7.5.
   * All message pipeline processing (Routing, Mapping, Envelope handling, Serialization) executes directly in Java memory without cross-stack RFC or HTTP marshaling.
   * Managed via **SAP NetWeaver Administrator (NWA)** at `https://<po-host>:<port>/nwa`.

2. **Enterprise Services Repository (ESR):**
   * Central design-time storage for Enterprise Service definitions, Data Types, Message Types, Service Interfaces, and Graphical / Java / XSLT mappings.

3. **Integration Directory (ID):**
   * Configuration time environment configuring Integrated Configuration Objects (ICOs), Communication Channels, Value Mappings, and Communication Components.

4. **NetWeaver BPM (NW BPM) & BRM (Business Rules Management):**
   * Standards-based (BPMN 2.0 / BPEL) stateful process orchestration engine embedded inside the Java stack.
   * Business Rules Management (BRM) externalizes decision tables and business logic from interface code.

---

## 2. Integrated Configuration Object (ICO) Pipeline

In SAP PO 7.4 / 7.5 Java Single-Stack, the **Integrated Configuration (ICO)** is the atomic configuration unit that processes messages from end to end:

```
[Inbound Channel] 
       │
       ▼
[Sender Agreement & Authentication]
       │
       ▼
[Receiver Determination (XPath In-Memory Routing)]
       │
       ▼
[Interface Determination & Mapping Engine (Java / Graphical / XSLT)]
       │
       ▼
[Receiver Channel Delivery (QoS: EO / EOIO)]
```

### Key Advantages of ICOs:
* **In-Memory Processing:** Eliminates database message persistence between intermediate pipeline steps unless explicitly configured for staging/logging.
* **Granular Payload Staging & Logging:** Configurable persistence steps (e.g., `BI` - Before Inbound, `MS` - After Mapping, `AM` - After Outbound) for audit compliance and replayability.
* **High Concurrency:** Utilizes configurable Java thread pools (`messaging.system.queueParallelism`) for high-volume IDoc and Web Service integrations.

---

## 3. High-Volume Standard Adapters

SAP PO Java Single-Stack provides native AEX adapters:
* **IDoc_AAE:** High-speed inbound and outbound IDoc processing with SAP S/4HANA and ECC via transactional RFC (tRFC).
* **RFC:** Synchronous and asynchronous RFC execution via JCo (Java Connector).
* **SOAP:** Web services with WS-Security, digital signatures, and SAML tokens.
* **REST:** Exposing and consuming RESTful JSON/XML APIs with dynamic URL pattern matching and content conversion.
* **JDBC:** Database integration with atomic polling statements (`SELECT ... FOR UPDATE`) and XML statement batches.
* **File / SFTP:** Secure file transfer with File Content Conversion (FCC) and ASMA dynamic attribute handling.
* **B2B / EDI:** AS2, EDIFACT, X12 B2B protocol integration for logistics and supplier collaboration.

---

## 4. Monitoring & Administration

* **Java Message Monitor (`/pimon`):** Unified monitoring of all message flows, payload inspection, and manual message restart.
* **Communication Channel Monitor:** Real-time channel availability, ping testing, and automated external monitoring.
* **Component-Based Message Alerting (CBMA):** Automated alerting rules delivering instant alerts via email or alert consumer services during runtime failures.
