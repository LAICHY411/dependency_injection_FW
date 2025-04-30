public class AnnotationApplicationContext implements ApplicationContext {
    private Map<String, Object> beans = new HashMap<>();
    private Map<String, Class<?>> beanClasses = new HashMap<>();

    public AnnotationApplicationContext(String basePackage) throws Exception {
        scanComponents(basePackage);
        createBeans();
        autowireBeans();
    }

    private void scanComponents(String basePackage) throws Exception {}

    private void createBeans() throws Exception {
        for (Map.Entry<String, Class<?>> entry : beanClasses.entrySet()) {
            String beanName = entry.getKey();
            Class<?> clazz = entry.getValue();

            Constructor<?>[] constructors = clazz.getConstructors();
            if (constructors.length == 1 && constructors[0].isAnnotationPresent(Autowired.class)) {
                Constructor<?> constructor = constructors[0];
                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];

                for (int i = 0; i < paramTypes.length; i++) {
                    params[i] = getBean(paramTypes[i]);
                }

                beans.put(beanName, constructor.newInstance(params));
            } else {
                beans.put(beanName, clazz.newInstance());
            }
        }
    }

    private void autowireBeans() throws Exception {
        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    Qualifier qualifier = field.getAnnotation(Qualifier.class);
                    Object dependency = qualifier != null ?
                            getBean(qualifier.value()) : getBean(field.getType());
                    field.set(bean, dependency);
                }
            }

            for (Method method : bean.getClass().getMethods()) {
                if (method.isAnnotationPresent(Autowired.class) &&
                        method.getName().startsWith("set") &&
                        method.getParameterCount() == 1) {
                    Class<?> paramType = method.getParameterTypes()[0];
                    Qualifier qualifier = method.getAnnotation(Qualifier.class);
                    Object dependency = qualifier != null ?
                            getBean(qualifier.value()) : getBean(paramType);
                    method.invoke(bean, dependency);
                }
            }
        }
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        return beans.get(beanName);
    }

    @Override
    public <T> T getBean(Class<T> clazz) throws Exception {
        for (Object bean : beans.values()) {
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