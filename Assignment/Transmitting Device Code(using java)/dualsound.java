package sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
public class dualsound {
	 public static float SAMPLE_RATE = 44100f;
	    public static void tone(int hz1, int hz2, int msecs) 
	    //An exception indicating that a line cannot be opened because it is unavailable. 
	    //This situation arises most commonly when a requested line is already in use by another application.
	    throws LineUnavailableException 
	    {
	        tone(hz1, hz2, msecs, 1.0);
	    }

	    public static void tone(int hz1, int hz2, int msecs, double vol)
	    throws LineUnavailableException 
	    {
	        byte[] buf = new byte[2];
	        //AudioFormat(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian)
	        //Constructs an AudioFormat with a linear PCM encoding and the given parameters.
	        AudioFormat af = new AudioFormat(SAMPLE_RATE,16,1,true,true);  
	        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
	        
	        
	        //Opens the line with the specified format, causing the line to acquire any required system resources 
	        //and become operational.The implementation chooses a buffer size, which is measured in bytes 
	        //but which encompasses an integral number of sample frames. The buffer size that the system has chosen may be queried by subsequently calling DataLine.getBufferSize().
	        sdl.open(af);
	        sdl.start();
	        
	        for (int i=0; i < msecs*8; i++) {
	              double angle = i / (SAMPLE_RATE / hz1) * 2.0 * Math.PI;
	        	  double angle2 = i/(SAMPLE_RATE/hz2)*2.0*Math.PI;
	              buf[0] = (byte)(0.5*Math.sin(angle)*127.0*vol + 0.5*Math.sin(angle2) * 127.0 * vol);
	              
	              //Writes audio data to the mixer via this source data line. The requested number of bytes of data are read from the specified array, 
	              //starting at the given offset into the array, and written to the data line's buffer.
	              sdl.write(buf,0,2);
	        }
	        
	        //Drains queued data from the line by continuing data I/O until the data line's internal buffer has been emptied.
	        sdl.drain();
	        sdl.stop();
	        sdl.close();
	    }
}
