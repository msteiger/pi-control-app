package msteiger.de.picontrolapp;

/**
 * Created by msteiger on 31.12.2017.
 */

public class PiConfig {
    public static PiConfig instance = new PiConfig();

    private PiConfig() {
        //
    }

    public String getTargetUrl() {
        return "http://msteiger-pc:8080/";
    }
}
