public class User {
    private String username;
    private String password;
    private int highScore_2048, highScore_Senku;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.highScore_2048 = 0;
        this.highScore_Senku = 0;
    }

// Getters and setters


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getHighScore_2048() {
        return highScore_2048;
    }

    public void setHighScore_2048(int highScore_2048) {
        this.highScore_2048 = highScore_2048;
    }

    public int getHighScore_Senku() {
        return highScore_Senku;
    }

    public void setHighScore_Senku(int highScore_Senku) {
        this.highScore_Senku = highScore_Senku;
    }
}
