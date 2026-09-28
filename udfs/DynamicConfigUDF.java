package com.enterprise.sap.pi.udf;

import com.sap.aii.mapping.api.DynamicConfiguration;
import com.sap.aii.mapping.api.DynamicConfigurationKey;
import com.sap.aii.mapping.api.TransformationInput;
import com.sap.aii.mappingtool.tf7.rt.Container;

/**
 * SAP PO 7.5 Java User-Defined Function (UDF): Dynamic Configuration Modifier.
 * 
 * Execution Type: Single Value / Simple
 * 
 * Sets Adapter-Specific Message Attributes (ASMA) dynamically:
 * - Dynamic Target File Name (e.g. Orders_20260928_120000.xml)
 * - Target File Directory Path
 * - Email Subject
 * 
 * @author Adarsh (Tech Lead - SAP Integration)
 */
public class DynamicConfigUDF {

    private static final String NAMESPACE_FILE = "http://sap.com/xi/XI/System/File";
    private static final String NAMESPACE_MAIL = "http://sap.com/xi/XI/System/Mail";

    /**
     * Sets target filename in ASMA header
     */
    public String setTargetFileName(String fileNamePrefix, String orderNumber, Container container) {
        try {
            // Retrieve DynamicConfiguration from mapping context
            DynamicConfiguration conf = (DynamicConfiguration) container
                .getTransformationParameters()
                .get(TransformationInput.DYNAMIC_CONFIGURATION);

            if (conf != null) {
                DynamicConfigurationKey keyFileName = DynamicConfigurationKey.create(NAMESPACE_FILE, "FileName");
                
                String dynamicName = fileNamePrefix + "_" + orderNumber + "_" + System.currentTimeMillis() + ".xml";
                conf.put(keyFileName, dynamicName);

                // Also set dynamic email subject if needed
                DynamicConfigurationKey keySubject = DynamicConfigurationKey.create(NAMESPACE_MAIL, "Subject");
                conf.put(keySubject, "Automated Notification: Order Processed " + orderNumber);
            }
        } catch (Exception ex) {
            // Return trace warning rather than terminating mapping
            container.getTrace().addWarning("Failed to set ASMA FileName: " + ex.getMessage());
        }

        return orderNumber; // Return original value to keep mapping pipeline intact
    }
}
