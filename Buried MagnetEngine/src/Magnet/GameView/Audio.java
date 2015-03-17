package Magnet.GameView;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio {
	
	private AudioInputStream audio;
	private Clip clip;
	
	public Audio(){
		audio = null;
	}
	
	public void loadClip(String path){
		try {
			audio = AudioSystem.getAudioInputStream(new BufferedInputStream(Audio.class.getResourceAsStream(path)));
		} catch (UnsupportedAudioFileException| IOException e) {
			e.printStackTrace();
		}
		
		try {
			clip = AudioSystem.getClip();
			clip.open(audio);
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start(boolean loop){
		clip.setFramePosition(0);
	    clip.start();
	    if(loop)clip.loop(clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop(){
		clip.stop();
	}
	
	public Clip getClip(){
		return clip;
	}
	
	public boolean isPlaying(){
		return clip.isRunning();
	}

}
