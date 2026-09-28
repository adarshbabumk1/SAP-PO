# 🏛️ SAP Process Orchestration (PO/PI 7.5) Legacy Reference & UDF Library

[![SAP PO 7.5](https://img.shields.io/badge/SAP-Process_Orchestration_7.5-004D40?style=for-the-badge&logo=sap&logoColor=white)](https://help.sap.com/docs/SAP_NETWEAVER_750)
[![Java NetWeaver](https://img.shields.io/badge/Java-NetWeaver_AEX-007396?style=for-the-badge&logo=java&logoColor=white)](https://java.com/)
[![ESR & ID](https://img.shields.io/badge/Architecture-ESR_%26_ID-1A237E?style=for-the-badge)](docs/)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)

A comprehensive architectural reference, code library, and technical archive for **SAP Process Orchestration (SAP PO 7.5 Single-Stack AEX)** and legacy **SAP Process Integration (Dual-Stack PI)**.

Essential for maintaining legacy landscapes and planning smooth modernizations to **SAP BTP Integration Suite**.

---

## 📑 Table of Contents

1. [Architectural Anatomy: Dual-Stack vs. Single-Stack AEX](#-architectural-anatomy)
2. [Production Java User Defined Function (UDF) Library](#-production-java-udf-library)
3. [File Content Conversion (FCC) Mastery](#-file-content-conversion-fcc-mastery)
4. [Integrated Configuration (ICO) Architecture](#-integrated-configuration-ico-architecture)
5. [Monitoring & Troubleshooting (SXMB_MONI & NWA)](#-monitoring--troubleshooting)

---

## 🏛️ Architectural Anatomy

SAP Process Orchestration evolved from a complex Dual-Stack system (ABAP + Java) into the performant Single-Stack Java **Advanced Adapter Engine Extended (AEX)**:

```mermaid
flowchart TB
    subgraph DUAL["Historical SAP PI Dual-Stack (7.0 - 7.31)"]
        direction TB
        IE["ABAP Integration Engine\n• Pipeline Processing (SXMB_MONI)\n• ccBPM (Business Process Mgmt)\n• IDoc & RFC Direct Inbound"]
        AE["Java Adapter Engine (J2EE)\n• Non-SAP Adapters (File, JDBC, JMS)\n• Adapter Framework Modules"]
        IE <-->|XI Protocol (tRFC / HTTP)| AE
    end

    subgraph SINGLE["Modern SAP PO 7.5 Single-Stack (Java AEX)"]
        direction TB
        AEX["Advanced Adapter Engine Extended (AEX)\n• High-Throughput Java Messaging Service\n• Integrated Configuration Objects (ICO)\n• Java Message Mappings & UDFs"]
        NWBPM["NetWeaver BPM (Java BPEL Engine)"]
        AEX <--> NWBPM
    end

    DUAL -.->|Deprecated / Out of Maintenance| SINGLE
```

---

## ☕ Production Java User Defined Function (UDF) Library

Located in [`udfs/`](udfs/):

* **[`DynamicConfigUDF.java`](udfs/DynamicConfigUDF.java)**:
  * Uses Adapter-Specific Message Attributes (ASMA) to dynamically read or override target filenames, directory paths, and email subjects at runtime via Java `DynamicConfiguration`.
* **[`ValueMappingRFCLookup.java`](udfs/ValueMappingRFCLookup.java)**:
  * Executes a synchronous RFC call directly into SAP backend tables (e.g. `T001`, `KNA1`) using `com.sap.aii.mapping.lookup.SystemAccessor`.
* **[`Base64EncoderUDF.java`](udfs/Base64EncoderUDF.java)**:
  * Encodes or decodes binary payloads and attachments within graphical message mappings.

---

## 📄 File Content Conversion (FCC) Mastery

The File/FTP adapter's File Content Conversion engine converts flat files (CSV, fixed length, hierarchical records) into XML.

* **[`CSV_to_XML_FCC_Parameters.txt`](fcc/CSV_to_XML_FCC_Parameters.txt)**: Comma and tab-delimited conversion parameters with header rows, field names, and quote-escaping rules.
* **[`FixedLength_FCC_Parameters.txt`](fcc/FixedLength_FCC_Parameters.txt)**: Multi-record type fixed-length positional conversion (Header, Item, Trailer).

---

## 🔧 Integrated Configuration (ICO) Architecture

Single-Stack SAP PO 7.5 relies on **Integrated Configuration Objects (ICOs)** instead of separate Sender Agreements, Receiver Determinations, Interface Determinations, and Receiver Agreements:

1. **Inbound Processing**: Sender Communication Channel + Sender Agreement security.
2. **Receivers**: XPath routing conditions to determine destination business systems.
3. **Receiver Interfaces**: Message Mappings and Operation Mappings executed in memory.
4. **Outbound Processing**: Dedicated Receiver Communication Channels with specific transport protocols.
