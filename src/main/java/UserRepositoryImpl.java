@Component("userRepository")
public class UserRepositoryImpl implements UserRepository {
    public void save(User user) {
        System.out.println("Saving user: " + user);
    }
}