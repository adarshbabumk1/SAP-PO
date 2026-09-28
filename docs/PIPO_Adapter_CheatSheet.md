# 🔌 SAP PO 7.5 Adapter Configuration Cheat-Sheet

Comprehensive reference parameters for standard Java AEX adapters.

---

## 1. IDoc_AAE Adapter
* **Sender:** Inbound IDocs sent from SAP ECC / S/4HANA via Transaction `SM59` (TCP/IP RFC destination pointing to AEX Program ID) and `WE21` (Transactional RFC Port).
* **Receiver:** IDocs sent to SAP ECC / S/4HANA via RFC destination configured in NetWeaver Administrator (NWA Destination).
* **Quality of Service (QoS):** `EO` (Exactly Once) guaranteed by SAP Transactional RFC (tRFC) mechanism.

---

## 2. RFC Adapter
* **Sender:** Registers a JCo Server with SAP Gateway to receive synchronous RFC (`EOIO` / `BE`) or asynchronous tRFC calls.
* **Receiver:** Calls remote BAPIs or Function Modules via configured RFC connection. Supports Advanced Mode for custom connection pool sizing.

---

## 3. JDBC Adapter
* **Sender (Polling):**
  * `Query SQL Statement`: `SELECT order_id, status FROM orders WHERE status = 'PENDING'`
  * `Update SQL Statement`: `UPDATE orders SET status = 'PROCESSING' WHERE status = 'PENDING'`
  * Ensures lock/update atomicity to prevent double-polling across cluster nodes.
* **Receiver:**
  * Supported XML document structures: `<statement><dbTableName action="INSERT|UPDATE|SELECT|DELETE">`.

---

## 4. REST Adapter (Polling & Exposing)
* Supports custom URL patterns: `/api/v1/customers/{customerId}`
* Authentication: Basic, OAuth 2.0 Client Credentials, API Key.
* Automatic JSON <-> XML conversion with strip wrapper root element options.
