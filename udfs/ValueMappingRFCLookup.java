package com.enterprise.sap.pi.udf;

import com.sap.aii.mapping.lookup.*;
import com.sap.aii.mappingtool.tf7.rt.Container;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * SAP PO 7.5 Java User-Defined Function (UDF): Synchronous RFC Lookup.
 * 
 * Execution Type: Single Value / Context
 * 
 * Performs real-time lookup into SAP ERP using JCo / SystemAccessor
 * through a configured RFC Communication Channel in Integration Directory.
 * 
 * @author Adarsh (SAP Integration Architect)
 */
public class ValueMappingRFCLookup {

    /**
     * Executes RFC lookup using SystemAccessor
     * 
     * @param channelName Communication Channel name configured in ID
     * @param businessSystem Target Business System (e.g. ERP_CLNT100)
     * @param customerNumber Parameter to query
     * @param container Mapping container runtime context
     * @return Resulting field value from RFC output
     */
    public String lookupCustomerCountry(String channelName, String businessSystem, String customerNumber, Container container) {
        String resultCountry = "UNKNOWN";
        SystemAccessor accessor = null;

        try {
            // 1. Determine Communication Channel Service
            Channel channel = LookupService.getChannel(businessSystem, channelName);
            accessor = LookupService.getSystemAccessor(channel);

            // 2. Build RFC XML Request Payload (RFC: BAPI_CUSTOMER_GETDETAIL2)
            String rfcXmlRequest = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<rfc:BAPI_CUSTOMER_GETDETAIL2 xmlns:rfc=\"urn:sap-com:document:sap:rfc:functions\">"
                + "<CUSTOMERNO>" + customerNumber + "</CUSTOMERNO>"
                + "</rfc:BAPI_CUSTOMER_GETDETAIL2>";

            InputStream inputStream = new ByteArrayInputStream(rfcXmlRequest.getBytes(StandardCharsets.UTF_8));
            Payload requestPayload = LookupService.getXmlPayload(inputStream);

            // 3. Execute Synchronous RFC Call
            Payload responsePayload = accessor.call(requestPayload);
            InputStream responseStream = responsePayload.getContent();

            // 4. Parse response stream (Simplified XML text search for demo)
            java.util.Scanner s = new java.util.Scanner(responseStream).useDelimiter("\\A");
            String responseStr = s.hasNext() ? s.next() : "";

            if (responseStr.contains("<COUNTRY>")) {
                int start = responseStr.indexOf("<COUNTRY>") + "<COUNTRY>".length();
                int end = responseStr.indexOf("</COUNTRY>");
                if (start < end) {
                    resultCountry = responseStr.substring(start, end).trim();
                }
            }

        } catch (Exception ex) {
            container.getTrace().addWarning("RFC Lookup error: " + ex.getMessage());
        } finally {
            if (accessor != null) {
                try {
                    accessor.close();
                } catch (LookupException ignored) {}
            }
        }

        return resultCountry;
    }
}
