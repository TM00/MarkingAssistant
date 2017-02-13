package marking;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.concurrent.Task;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

// Nice source:     https://gist.github.com/jewelsea/1446612

public class SoundPlayer {

	public static final List<String> SUPPORTED_FILE_EXTENSIONS = Arrays.asList(".mp3", ".m4a");
	private static AudioClip audio;
	private static MediaPlayer loopPlayer;

	public static void startPlayingSound(File soundFile){

		if(audio == null){ // If sound is not playing
			Task<?> task = new Task<Object>() {

				@Override
				protected Object call() throws Exception {
					System.out.println("Running runnable");

					//System.out.println(ResourceLoader.getFile(soundFileName).toURL().toExternalForm());

					audio = new AudioClip(soundFile.toURL().toExternalForm());
					audio.setVolume(0.5f);
					audio.play();

					return null;
				}
			};
			Thread thread = new Thread(task);
			thread.start();
		}

	}

	private static boolean initializeLoopsound = false;

	public static void initializeLoopsound(File soundFile){

		final Media media = new Media("file:///" + (soundFile+"").replace("\\", "/").replaceAll(" ", "%20"));
		final MediaPlayer player = new MediaPlayer(media);
		player.setOnError(new Runnable() {
			@Override public void run() {
				System.out.println("Media error occurred: " + player.getError());
			}
		});

		loopPlayer = player;
		loopPlayer.setVolume(0.5f);
		//loopPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		initializeLoopsound = true;
	}
	public static void startLoopSound(){
		if(initializeLoopsound){
			loopPlayer.setOnEndOfMedia(new Runnable() {
				public void run() {
					System.out.println("Repeating!!!!!");
					loopPlayer.seek(Duration.ZERO);
				}
			});

			loopPlayer.play();
		}else{
			System.out.println("LoopSound not initialized!!");
		}
	}

	public static void startLoopSound(File soundFile){

		if(loopPlayer == null){


			if(!isPlaying()){
				/*if(audio == null){ // If sound is not playing
					Task<?> task = new Task<Object>() {

						@Override
						protected Object call() throws Exception {
							System.out.println("Running runnable");

							System.out.println(soundFile.toURL().toExternalForm());

							audio = new AudioClip(soundFile.toURL().toExternalForm());
							audio.setVolume(0.5f);
							audio.setCycleCount(AudioClip.INDEFINITE);
							audio.play(0);

							return null;
						}
					};
					Thread thread = new Thread(task);
					thread.start();
				}*/

				if(loopPlayer.getStatus() != MediaPlayer.Status.PLAYING){
					Task<?> task = new Task<Object>() {

						@Override
						protected Object call() throws Exception {
							System.out.println("Running runnable");


							loopPlayer.play();

							return null;
						}
					};
					Thread thread = new Thread(task);
					thread.start();
				}
			}
		}
	}

	//------------------
	// Background music 
	//------------------

	public static boolean isPlaying(){

		if(currentPlayer==null)
			return false;

		else if(currentPlayer.getStatus() == MediaPlayer.Status.PLAYING)
			return true;
		return false;
	}

	private static final List<MediaPlayer> players = new ArrayList<>();
	public static MediaPlayer currentPlayer;

	public static void startBackGroundSound(File folder){

		if(!folder.isDirectory())
			throw new RuntimeException("Not a folder");

		Task<?> task = new Task<Object>() {

			@Override
			protected Object call() throws Exception {

				for (String file : folder.list(new FilenameFilter() {
					@Override public boolean accept(File dir, String name) {
						for (String ext: SUPPORTED_FILE_EXTENSIONS) {
							if (name.endsWith(ext)) {
								return true;
							}
						}

						return false;
					}
				})) players.add(createPlayer("file:///" + (folder + "\\" + file).replace("\\", "/").replaceAll(" ", "%20")));


				currentPlayer = players.get(0);
				currentPlayer.play();
				// play each audio file in turn.
				for (int i = 0; i < players.size(); i++) {
					final MediaPlayer player     = players.get(i);
					final MediaPlayer nextPlayer = players.get((i + 1) % players.size());
					player.setOnEndOfMedia(new Runnable() {
						@Override public void run() {

							player.stop();
							currentPlayer = nextPlayer;
							currentPlayer.play();

						}
					});
				}

				return null;
			}
		};
		Thread thread = new Thread(task);
		thread.start();


	}

	public static void startBackGroundSound(File folder, double volume){

		if(!folder.isDirectory())
			throw new RuntimeException("Not a folder");

		Task<?> task = new Task<Object>() {

			@Override
			protected Object call() throws Exception {

				for (String file : folder.list(new FilenameFilter() {
					@Override public boolean accept(File dir, String name) {
						for (String ext: SUPPORTED_FILE_EXTENSIONS) {
							if (name.endsWith(ext)) {
								return true;
							}
						}

						return false;
					}
				})) players.add(createPlayer("file:///" + (folder + "\\" + file).replace("\\", "/").replaceAll(" ", "%20")));


				currentPlayer = players.get(0);
				currentPlayer.setVolume(volume);
				currentPlayer.play();
				// play each audio file in turn.
				for (int i = 0; i < players.size(); i++) {
					final MediaPlayer player     = players.get(i);
					final MediaPlayer nextPlayer = players.get((i + 1) % players.size());
					player.setOnEndOfMedia(new Runnable() {
						@Override public void run() {

							player.stop();
							currentPlayer = nextPlayer;
							currentPlayer.setVolume(volume);
							currentPlayer.play();

						}
					});
				}

				return null;
			}
		};
		Thread thread = new Thread(task);
		thread.start();


	}

	public static void skipBackGroundTrack(){

		currentPlayer.stop();

		MediaPlayer nextPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());

		currentPlayer = nextPlayer;	       
		currentPlayer.play();
	}

	public static void pauseBackGroundTrack(){

		if(currentPlayer.getStatus() == MediaPlayer.Status.PAUSED)
			currentPlayer.play();
		if(currentPlayer.getStatus() == MediaPlayer.Status.PLAYING)
			currentPlayer.pause();

		MediaPlayer nextPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());

		currentPlayer = nextPlayer;	       
		currentPlayer.play();
	}



	/** @return a MediaPlayer for the given source which will report any errors it encounters */
	private static MediaPlayer createPlayer(String mediaSource) {
		final Media media = new Media(mediaSource);
		final MediaPlayer player = new MediaPlayer(media);
		player.setOnError(new Runnable() {
			@Override public void run() {
				System.out.println("Media error occurred: " + player.getError());
			}
		});
		return player;
	}

	/**
	 * ONLY stops looping sound
	 */
	public static void stopPlayingSound(){
		//	thread.interrupt();
		System.out.println("Stopping play...");
		/*	if(audio != null){
			audio.stop();
			audio = null;
		}*/
		if(loopPlayer !=null)
			loopPlayer.stop();
	}

}
