public class Main {
    public static void main(String[] args) throws Exception {
        ApplicationContext xmlContext = new XmlApplicationContext("applicationContext.xml");
        UserController xmlController = xmlContext.getBean("userController", UserController.class);
        xmlController.createUser("John", "john@example.com");

        ApplicationContext annotationContext = new AnnotationApplicationContext("com.example");
        UserController annotationController = annotationContext.getBean("userController", UserController.class);
        annotationController.createUser("Alice", "alice@example.com");
    }
}