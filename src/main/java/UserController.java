@Component("userController")
public class UserController {
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    public void createUser(String name, String email) {
        User user = new User(name, email);
        userService.registerUser(user);
    }
}