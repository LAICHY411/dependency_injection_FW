public class XmlApplicationContext implements ApplicationContext {
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
    private Map<String, Object> singletonBeans = new HashMap<>();

    public XmlApplicationContext(String xmlConfigPath) throws Exception {
        parseXmlConfig(xmlConfigPath);
        createBeans();
    }

    private void parseXmlConfig(String xmlConfigPath) throws Exception {}

    private void createBeans() throws Exception {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitions.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            Object bean = createBeanInstance(beanDefinition);
            populateBean(bean, beanDefinition);
            singletonBeans.put(beanName, bean);
        }
    }

    private Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
        if (beanDefinition.getConstructorArgs() != null && !beanDefinition.getConstructorArgs().getArgs().isEmpty()) {
            return createBeanWithConstructorArgs(beanDefinition);
        }
        return beanDefinition.getClazz().newInstance();
    }

    private Object createBeanWithConstructorArgs(BeanDefinition beanDefinition) throws Exception {
        Class<?> clazz = beanDefinition.getClazz();
        ConstructorArgs constructorArgs = beanDefinition.getConstructorArgs();
        Class<?>[] paramTypes = new Class[constructorArgs.getArgs().size()];
        Object[] paramValues = new Object[constructorArgs.getArgs().size()];

        for (int i = 0; i < constructorArgs.getArgs().size(); i++) {
            ConstructorArg arg = constructorArgs.getArgs().get(i);
            if (arg.getRef() != null) {
                paramTypes[i] = Object.class;
                paramValues[i] = getBean(arg.getRef());
            } else {
                paramTypes[i] = Class.forName(arg.getType());
                paramValues[i] = convertValue(arg.getValue(), paramTypes[i]);
            }
        }

        Constructor<?> constructor = clazz.getConstructor(paramTypes);
        return constructor.newInstance(paramValues);
    }

    private void populateBean(Object bean, BeanDefinition beanDefinition) throws Exception {
        if (beanDefinition.getPropertyValues() == null) return;

        for (PropertyValue property : beanDefinition.getPropertyValues().getProperties()) {
            String propertyName = property.getName();
            Field field = bean.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);

            if (property.getRef() != null) {
                field.set(bean, getBean(property.getRef()));
            } else {
                field.set(bean, convertValue(property.getValue(), field.getType()));
            }
        }
    }

    private Object convertValue(String value, Class<?> targetType) {
        return value;
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        return singletonBeans.get(beanName);
    }

    @Override
    public <T> T getBean(Class<T> clazz) throws Exception {
        for (Object bean : singletonBeans.values()) {
            if (clazz.isInstance(bean)) {
                return clazz.cast(bean);
            }
        }
        throw new Exception("No bean found for type: " + clazz.getName());
    }

    @Override
    public <T> T getBean(String beanName, Class<T> clazz) throws Exception {
        Object bean = getBean(beanName);
        if (clazz.isInstance(bean)) {
            return clazz.cast(bean);
        }
        throw new Exception("Bean with name '" + beanName + "' is not of type: " + clazz.getName());
    }
}