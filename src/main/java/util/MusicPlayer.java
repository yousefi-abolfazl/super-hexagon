package util;

/**
 * Simple dummy music player that just logs messages but doesn't actually play audio.
 * This is a simplified version to avoid the complexity of audio implementations.
 */
public class MusicPlayer {
    private static MusicPlayer instance;
    private boolean enabled = true;
    private String currentTrack = null;
    
    private MusicPlayer() {
        System.out.println("MusicPlayer initialized (dummy implementation)");
    }
    
    public static MusicPlayer getInstance() {
        if (instance == null) {
            instance = new MusicPlayer();
        }
        return instance;
    }
    
    public void play(String resourcePath) {
        if (!enabled) {
            System.out.println("Music is disabled, not playing: " + resourcePath);
            return;
        }
        
        currentTrack = resourcePath;
        System.out.println(" 🎵Playing music: " + resourcePath + " (simulated)");
    }
    
    public void stop() {
        if (currentTrack != null) {
            System.out.println("🛑 Stopping music: " + currentTrack);
            currentTrack = null;
        }
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            stop();
        } else if (currentTrack != null) {
            System.out.println("🎵 Resuming music: " + currentTrack + " (simulated)");
        }
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setVolume(float volume) {
        System.out.println("📢 Setting volume to: " + volume + " (simulated)");
    }
    
    public float getVolume() {
        return 0.5f; // Default volume
    }
}