package cr.ac.ucr.ecci.ci1330.IoC.Bean;

import cr.ac.ucr.ecci.ci1330.IoC.AutowiringMode;

/**
 * Dependency of a bean
 * @author María José Cubero
 */

public class Dependency {
    private String beanID;
    private String reference;
    private String name; //
    private AutowiringMode autowiringMode;

    public Dependency(String beanID, String reference) {
        this.beanID = beanID;
        this.reference = reference;
        this.autowiringMode = AutowiringMode.BYTYPE;
    }

    public Dependency(String beanID, String reference, String name) {
        this.beanID = beanID;
        this.reference = reference;
        this.name = name;
        this.autowiringMode = AutowiringMode.BYTYPE;
    }

    public Dependency(){

    }

    public String getBeanID() {
        return beanID;
    }

    public void setBeanID(String beanID) {
        this.beanID = beanID;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AutowiringMode getAutowiringMode() {
        return autowiringMode;
    }

    public void setAutowiringMode(AutowiringMode autowiringMode) {
        this.autowiringMode = autowiringMode;
    }
}
