package main.java.cr.ac.ucr.ecci.ci1330.SpringContainer;


public class Bean<T>{
    String id;
    String className;
    String scope;
    String autowiringMode;
    String initMethod;
    String destructMethod;
    T beanInstance;

    public Bean(String id, String className, String scope, String autowiringMode, String initMethod, String destructMethod) {
        this.id = id;
        this.className = className;
        this.scope = scope;
        this.autowiringMode = autowiringMode;
        this.initMethod = initMethod;
        this.destructMethod = destructMethod;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAutowiringMode() {
        return autowiringMode;
    }

    public void setAutowiringMode(String autowiringMode) {
        this.autowiringMode = autowiringMode;
    }

    public String getInitMethod() {
        return initMethod;
    }

    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }

    public String getDestructMethod() {
        return destructMethod;
    }

    public void setDestructMethod(String destructMethod) {
        this.destructMethod = destructMethod;
    }

    public T getBeanInstance() {
        return beanInstance;
    }

    public void setBeanInstance(T beanInstance) {
        this.beanInstance = beanInstance;
    }
}
