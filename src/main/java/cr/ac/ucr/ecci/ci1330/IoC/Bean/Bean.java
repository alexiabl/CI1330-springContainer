package cr.ac.ucr.ecci.ci1330.IoC.Bean;

import cr.ac.ucr.ecci.ci1330.IoC.AutowiringMode;
import cr.ac.ucr.ecci.ci1330.IoC.ScopeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean
 * @author Alexia Borchgrevink
 * @author María José Cubero
 * @author Renato Mainieri
 */
public class Bean {
    private String id;
    private String className;
    private String initMethod;
    private String destructMethod;
    private Object beanInstance;
    private AutowiringMode autowiringMode;
    private ScopeType scopeType;
    private List<Dependency> constructorDependencies;
    private List<Dependency> setterDependencies;
    private boolean lazy;

    public Bean(){
        this.initMethod = "initMethod";
        this.destructMethod = "destructMethod";
        this.autowiringMode = AutowiringMode.BYTYPE;
        this.scopeType = ScopeType.SINGLETON;
        this.constructorDependencies= new ArrayList<>();
        this.setterDependencies= new ArrayList<>();
        this.lazy= false;
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

    public Object getBeanInstance() { return beanInstance; }

    public void setBeanInstance(Object beanInstance) {
        this.beanInstance = beanInstance;
    }

    public AutowiringMode getAutowiringMode() { return autowiringMode; }

    public void setAutowiringMode(AutowiringMode autowiringMode) { this.autowiringMode = autowiringMode; }

    public ScopeType getScopeType() { return scopeType; }

    public void setScopeType(ScopeType scopeType) { this.scopeType = scopeType; }

    public List<Dependency> getConstructorDependencies() {
        return constructorDependencies;
    }

    public void setConstructorDependencies(List<Dependency> constructorDependencies) {
        this.constructorDependencies = constructorDependencies;
    }

    public List<Dependency> getSetterDependencies() {
        return setterDependencies;
    }

    public void setSetterDependencies(List<Dependency> setterDependencies) {
        this.setterDependencies = setterDependencies;
    }

    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    public boolean hasDependencies(){
        if(constructorDependencies.size()> 0 || setterDependencies.size() >0){
            return true;
        }
        return false;
    }
}
