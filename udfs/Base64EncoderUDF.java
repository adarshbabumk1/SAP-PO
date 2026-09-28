package com.enterprise.sap.pi.udf;

import com.sap.aii.mappingtool.tf7.rt.Container;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * SAP PO 7.5 Java User-Defined Function (UDF): Base64 Encoder / Decoder.
 * 
 * Execution Type: Single Value
 * 
 * @author Adarsh (SAP Integration Architect)
 */
public class Base64EncoderUDF {

    /**
     * Encodes raw text into Base64 format
     */
    public String encodeBase64(String input, Container container) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        return Base64.getEncoder().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes Base64 encoded string into UTF-8 text
     */
    public String decodeBase64(String base64Input, Container container) {
        if (base64Input == null || base64Input.isEmpty()) {
            return "";
        }
        try {
            byte[] decoded = Base64.getDecoder().decode(base64Input);
            return new String(decoded, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            container.getTrace().addWarning("Invalid Base64 string: " + ex.getMessage());
            return base64Input;
        }
    }
}
