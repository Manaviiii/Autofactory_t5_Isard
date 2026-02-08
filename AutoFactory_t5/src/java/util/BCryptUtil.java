package util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {
    
    private static final int DEFAULT_ROUNDS = 10;
    
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(DEFAULT_ROUNDS));
    }
    
    public static boolean checkPassword(String password, String hash) {
        if (password == null || hash == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(password, hash);
        } catch (Exception e) {
            System.err.println("Error verificant password: " + e.getMessage());
            return false;
        }
    }
    
    public static void main(String[] args) {
        System.out.println("Hash de 'admin': " + hashPassword("admin"));
        System.out.println("Hash de 'user1': " + hashPassword("user1"));
        System.out.println("Hash de 'password': " + hashPassword("password"));
    }
}
