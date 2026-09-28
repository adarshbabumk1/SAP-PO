# 🏛️ SAP PO 7.5: Integrated Configuration (ICO) Architecture & Governance Guide

**Role:** Tech Lead - SAP Integration  
**Focus:** High-Performance Integrated Configuration Object (ICO) Design in Single-Stack Java AEX  
**Target Platform:** SAP NetWeaver Process Orchestration 7.5

---

## 1. Integrated Configuration (ICO) Processing Pipeline

In Single-Stack SAP PO 7.5 (Java AEX), the Integrated Configuration Object (ICO) eliminates cross-stack ABAP-to-Java marshaling by executing the entire pipeline in the Java VM memory:

```
[ Inbound Adapter Channel (e.g. IDoc_AAE / File / REST) ]
                      │
                      ▼
 1. Inbound Processing (Security, Authorization, Header Extraction)
                      │
                      ▼
 2. Routing Determination (In-Memory XPath Evaluation)
                      │
                      ▼
 3. Interface Determination (Graphical Mapping / Java UDF Execution)
                      │
                      ▼
 4. Outbound Processing (Receiver Communication Channel Delivery)
```

---

## 2. Staging & Logging Configuration (Best Practices)

To balance troubleshooting visibility with Java memory consumption and database disk I/O, configure staging steps selectively in the **Advanced Settings** tab of the ICO:

| Stage Code | Description | Recommended DEV/QA Setting | Recommended PRD Setting |
| :--- | :--- | :--- | :--- |
| **BI** | Before Inbound Processing | None | None |
| **MS** | Before Mapping (Store Payload) | Store on Error | Store on Error |
| **AM** | After Mapping (Transformed XML)| Store Always | Store on Error |
| **VO** | Before Outbound Processing | None | None |

* **Tech Lead Tip:** Enabling `Store Always` across all stages in production leads to severe XI Message DB table growth (`BC_MSG` and `BC_MSG_LOG`) and causes JVM garbage collection pauses.

---

## 3. High-Volume Adapter Thread Pool Governance

In NetWeaver Administrator (`http://<po-host>:<port>/nwa`):
* Configure dedicated worker threads for high-volume channels under **Java System Properties** (`com.sap.aii.adapter.idoc.ra`, `com.sap.aii.adapter.file.ra`).
* Use **Channel Independent Clustering** to allow multiple server nodes to process queue chunks concurrently without lock contention.
