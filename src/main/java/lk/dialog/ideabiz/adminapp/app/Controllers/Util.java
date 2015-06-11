package lk.dialog.ideabiz.adminapp.app.Controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Malinda on 6/11/2015.
 */
public class Util {

    public static List<String> IPs = null;
    public static Properties confProperties = null;

    public static void listIP() throws Exception {
        IPs = new ArrayList<String>();

        if (confProperties == null)
            loadProperties();

        String IPstr = confProperties.getProperty("conf.allowedIP");

        if (IPstr == null)
            return;

        IPs = Arrays.asList(IPstr.split(","));
    }

    public static void loadProperties() {
        try {
            InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties");
            confProperties = new Properties();
            confProperties.load(input);
        } catch (IOException e) {
            System.out.println("Error Loading Conf");
            e.printStackTrace();
        }
    }

    public static boolean validateIP(String IP) throws Exception {

        if (IPs == null)
            listIP();

        if (IPs == null || IP == null)
            return true;

        IP = IP.toLowerCase();
        for (String str : IPs) {
            if (str.trim().toLowerCase().contains(IP))
                return true;
        }
        return false;

    }

    public static String validateMSISDN(String msisdn) throws Exception {
        if (msisdn == null)
            throw new Exception("Wrong MSISDN format");

        msisdn = msisdn.replace("tel:", "");

        if (msisdn.startsWith("+"))
            msisdn = msisdn.substring(1, msisdn.length());

        if (msisdn.startsWith("00"))
            msisdn = msisdn.substring(2, msisdn.length());

        if (msisdn.startsWith("0"))
            msisdn = msisdn.substring(1, msisdn.length());


        if (msisdn.startsWith("7"))
            msisdn = "94" + msisdn;

        if (msisdn.length() != 11)
            throw new Exception("Wrong MSISDN format");

        if (!msisdn.startsWith("94"))
            throw new Exception("Wrong MSISDN format");


        return msisdn;
    }
}
