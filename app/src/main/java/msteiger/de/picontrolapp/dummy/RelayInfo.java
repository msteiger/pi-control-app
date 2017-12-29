package msteiger.de.picontrolapp.dummy;

import java.util.ArrayList;
import java.util.List;

import msteiger.de.picontrolapp.dummy.TriggerTime;

/**
 * TODO: describe
 */
public class RelayInfo {

    private String id;
    private String name;
    private int gpioPin;
    private List<TriggerTime> triggers = new ArrayList<>();

    private RelayInfo() {
    }

    public  RelayInfo(String id, int gpioPin) {
        this.id = id;
        this.gpioPin = gpioPin;
    }

    public  RelayInfo(String id, int gpioPin, String name) {
        this.id = id;
        this.gpioPin = gpioPin;
        this.name = name;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the gpioPin
     */
    public int getGpioPin() {
        return gpioPin;
    }
    /**
     * @param gpioPin the gpioPin to set
     */
    public void setGpioPin(int gpioPin) {
        this.gpioPin = gpioPin;
    }
    /**
     * @return the triggers
     */
    public List<TriggerTime> getTriggers() {
        return triggers;
    }
    /**
     * @param triggers the triggers to set
     */
    public void setTriggers(List<TriggerTime> triggers) {
        this.triggers = triggers;
    }
}
