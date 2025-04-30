public class BeanDefinition {
    private String id;
    private Class<?> clazz;
    private ConstructorArgs constructorArgs;
    private PropertyValues propertyValues;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Class<?> getClazz() { return clazz; }
    public void setClazz(Class<?> clazz) { this.clazz = clazz; }
    public ConstructorArgs getConstructorArgs() { return constructorArgs; }
    public void setConstructorArgs(ConstructorArgs constructorArgs) { this.constructorArgs = constructorArgs; }
    public PropertyValues getPropertyValues() { return propertyValues; }
    public void setPropertyValues(PropertyValues propertyValues) { this.propertyValues = propertyValues; }
}