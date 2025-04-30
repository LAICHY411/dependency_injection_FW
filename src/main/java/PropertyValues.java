public class PropertyValues {
    private List<PropertyValue> properties = new ArrayList<>();
    public void addProperty(PropertyValue property) { properties.add(property); }
    public List<PropertyValue> getProperties() { return properties; }
}