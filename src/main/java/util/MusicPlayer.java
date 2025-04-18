package util;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * MusicPlayer implementation that actually plays audio using javax.sound API.
 */
public class MusicPlayer {
    private static MusicPlayer instance;
    private boolean enabled = true;
    private String currentTrack = null;
    private Clip clip;
    private float currentVolume = 0.5f;
    
    private MusicPlayer() {
        System.out.println("MusicPlayer initialized");
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
        
        // اگر قبلاً آهنگی پخش می‌شد، متوقفش کن
        stop();
        
        currentTrack = resourcePath;
        
        try {
            // دسترسی به فایل صوتی از منابع برنامه
            InputStream audioSrc = getClass().getResourceAsStream(resourcePath);
            if (audioSrc == null) {
                System.err.println("Could not find audio resource: " + resourcePath);
                return;
            }
            
            BufferedInputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
            
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            
            // تنظیم صدا
            setVolume(currentVolume);
            
            // پخش به صورت تکراری
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            
            System.out.println("🎵 Playing music: " + resourcePath);
        } catch (Exception e) {
            System.err.println("Failed to play music: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
            System.out.println("🛑 Stopping music: " + currentTrack);
        }
        currentTrack = null;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            stop();
        } else if (currentTrack != null) {
            play(currentTrack);
            System.out.println("🎵 Resuming music: " + currentTrack);
        }
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public void setVolume(float volume) {
        this.currentVolume = volume;
        System.out.println("📢 Setting volume to: " + volume);
        
        if (clip != null) {
            try {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // تبدیل مقیاس خطی (0 تا 1) به دسیبل
                float dB = (volume <= 0.0f) ? -80.0f : (float) (20.0 * Math.log10(volume));
                gainControl.setValue(dB);
            } catch (Exception e) {
                System.err.println("Failed to set volume: " + e.getMessage());
            }
        }
    }
    
    public float getVolume() {
        return currentVolume;
    }
}