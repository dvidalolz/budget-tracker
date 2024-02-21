package users.internal.user;

public interface UserRepository {
    
    public User findByUserName(String userName);

}
