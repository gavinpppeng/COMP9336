package sound;

import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class codingsound {
	public static float SAMPLE_RATE = 44100f;
	public static void Hammingcoding(String str) throws LineUnavailableException {
		char[] word = str.toCharArray();
		String binary;
		String eight_bits_binary = null;
		String[] coding_bits;
		ArrayList<Integer> transmit_bits = new ArrayList<Integer>();    //transmit bits
		transmit_bits.add(64);
		transmit_bits.add(66);
		for(int i = 0; i<word.length; i++) {
			int ascii_num = word[i];
			binary = Integer.toBinaryString(ascii_num);
			// change to ascii binary
			eight_bits_binary = String.format("%08d",Integer.valueOf(binary)); 
			System.out.println(eight_bits_binary);
			
			//Hamming coding
			coding_bits = eight_bits_binary.split("");
			//check bits
			//XX1X101X0111
			//12 bits = 8 data bits + 4 check bits
			//XXX1: 1,3,5,7,9,11
			int check_1 = even_one(Integer.valueOf(coding_bits[0]) + Integer.valueOf(coding_bits[1]) + Integer.valueOf(coding_bits[3]) + Integer.valueOf(coding_bits[4]) + Integer.valueOf(coding_bits[6]));
			//XX1X: 2,3,6,7,10,11
			int check_2 = even_one(Integer.valueOf(coding_bits[0]) + Integer.valueOf(coding_bits[2]) + Integer.valueOf(coding_bits[3]) + Integer.valueOf(coding_bits[5]) + Integer.valueOf(coding_bits[6]));
			//X1XX: 4,5,6,7,12
			int check_3 = even_one(Integer.valueOf(coding_bits[1]) + Integer.valueOf(coding_bits[2]) + Integer.valueOf(coding_bits[3]) + Integer.valueOf(coding_bits[7]));
			//1XXX:8,9,10,11,12
			int check_4 = even_one(Integer.valueOf(coding_bits[4]) + Integer.valueOf(coding_bits[5]) + Integer.valueOf(coding_bits[6]) + Integer.valueOf(coding_bits[7]));
			
			ArrayList sendingbits = new ArrayList<Integer>();
			
			sendingbits.add(check_1);
			//Task 5 set an error, each error will be added/reduced 1
			//sendingbits.add(check_1-1);
			
			sendingbits.add(check_2);
			//sendingbits.add(check_2+1);
			
			sendingbits.add(Integer.valueOf(coding_bits[0]));
			//sendingbits.add(Integer.valueOf(coding_bits[0])+1);
			
			sendingbits.add(check_3);
			//sendingbits.add(check_3+1);
			
			sendingbits.add(Integer.valueOf(coding_bits[1]));
			//sendingbits.add(Integer.valueOf(coding_bits[1])-1);
			
			sendingbits.add(Integer.valueOf(coding_bits[2]));
			//sendingbits.add(Integer.valueOf(coding_bits[2])+1);
			
			sendingbits.add(Integer.valueOf(coding_bits[3]));
			//sendingbits.add(Integer.valueOf(coding_bits[3])+1);
			
			sendingbits.add(check_4);
			//sendingbits.add(check_4-1);
			
			sendingbits.add(Integer.valueOf(coding_bits[4]));
			//sendingbits.add(Integer.valueOf(coding_bits[4])+1);
			
			//sendingbits.add(Integer.valueOf(coding_bits[5]));
			sendingbits.add(Integer.valueOf(coding_bits[5])+1);
			
			sendingbits.add(Integer.valueOf(coding_bits[6]));
			//sendingbits.add(Integer.valueOf(coding_bits[6])+1);
			
			sendingbits.add(Integer.valueOf(coding_bits[7]));	
			//sendingbits.add(Integer.valueOf(coding_bits[7])-1);
			
			System.out.println("The sending bits are: "+ sendingbits);
			
        	int highBit = 32 * (int)sendingbits.get(0) + 16 * (int)sendingbits.get(1) + 8 * (int)sendingbits.get(2) + 4 *(int) sendingbits.get(3) + 2 * (int)sendingbits.get(4) + (int)sendingbits.get(5);
        	int lowBit = 32 * (int)sendingbits.get(6) + 16 * (int)sendingbits.get(7) + 8 * (int)sendingbits.get(8) + 4 *(int) sendingbits.get(9) + 2 * (int)sendingbits.get(10) + (int)sendingbits.get(11);
        	System.out.println(String.valueOf(highBit) + "-" + String.valueOf(lowBit));
        	transmit_bits.add(highBit);
        	transmit_bits.add(lowBit);
			
		}
		transmit_bits.add(65);
//		for(int i=0; i<transmit_bits.size();i++) {
	//		System.out.println("The transmit array has:" + String.valueOf(transmit_bits.get(i)));
	//	}
		sound(transmit_bits, 1500, 1);
		
	}
	
	public static int even_one(int num) {
		if (num%2 == 0) {
			return 0;
		}
		else {
			return 1;
		}
	}
	
	public static void sound(ArrayList<Integer> bits, int msec, double vol) throws LineUnavailableException{
    	byte[] buf = new byte[2];
        AudioFormat af = new AudioFormat(SAMPLE_RATE, 16, 1, true, true);     
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        long second = System.currentTimeMillis();
        String bits_sound;
        String[] coding_bit = null;
        for (int j = 0; j < bits.size(); j ++ ) {
        	System.out.println("The transmit array has: " + String.valueOf(bits.get(j)));
        	int hz = 400 + 100 * bits.get(j);
        	/*
        	if (bits.get(j) > 0) {
        	String bin_bits = Integer.toBinaryString(bits.get(j));
        	bits_sound = String.format("%08d",Integer.valueOf(bin_bits));
        	System.out.println("The bits are: "+ bits_sound);
        	coding_bit = bits_sound.split("");
        	int a = 1600 + 100 * (Integer.valueOf(coding_bit[0]));
        	System.out.println(String.valueOf(a));
        	}
        	*/
    		for (int i = 0; i < msec * 4; i ++ ) {
	           	 double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
	           	 buf[0] = (byte)(Math.sin(angle) * 127.0 * vol );
                 sdl.write(buf, 0, 2);
           }
        }
 
        sdl.drain();
        long end = System.currentTimeMillis();
        sdl.stop();
        sdl.close();
	}
}
