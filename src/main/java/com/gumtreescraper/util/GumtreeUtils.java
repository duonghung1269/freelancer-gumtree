/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gumtreescraper.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author SGSCDHDX
 */
public class GumtreeUtils {
    private static final Logger LOG = Logger.getLogger(GumtreeUtils.class);
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    
    private GumtreeUtils() {}
    
    public static Date convertStringToDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        
        try {
            Date d = sdf.parse(dateStr);
            return d;
        } catch (ParseException ex) {
            LOG.error("Parse date error: " + ex);
        }
        
        return null;
    }
    
    public static String convertDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        return sdf.format(date);
    }
    
}
