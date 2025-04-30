public interface ApplicationContext {
    Object getBean(String beanName) throws Exception;
    <T> T getBean(Class<T> clazz) throws Exception;
    <T> T getBean(String beanName, Class<T> clazz) throws Exception;
}