package DAG;

import java.util.Arrays;

public class Block
{
	private int mote_ID;
	private int timestamp;
	private int[] measures;
	private int[] prevHash1; 
	private int[] prevHash2;
	private int[] hash;
	private int nonce;
	

	/**
	 * Constructor to be used when reading Blocks
	 * 
	 * @param m
	 * @param mt
	 * @param h1
	 * @param h2
	 * @param hash
	 * @param n
	 */
	public Block(int m, int t, int[] measures, int[] h1, int[] h2, int[] hash, int n)
	{
		this.mote_ID = m;
		this.timestamp = t;
		this.measures = measures;
		this.prevHash1 = h1;
		this.prevHash2 = h2;
		this.hash = hash;
		this.nonce = n;
	}
	
	/**
	 * Constructor to be used when reading Blocks
	 * 
	 * @param m
	 * @param mt
	 * @param h1
	 * @param h2
	 * @param hash
	 * @param n
	 */
	public Block(int m, int t, int[] measures, String h1, String h2, String hash, int n)
	{
		this.mote_ID = m;
		this.timestamp = t;
		this.measures = measures;
		this.prevHash1 = Block.stringToIntArray(h1);
		this.prevHash2 = Block.stringToIntArray(h2);
		this.hash = Block.stringToIntArray(hash);
		this.nonce = n;
	}
	
	
	/**
	 * Generates the 2 Genesis Blocks to initialise the DAG
	 */
	public static Block[] generateGenesisBlocks()
	{
		int[] genesisHash11 = stringToIntArray("Genesis 1a");
		int[] genesisHash12 = stringToIntArray("Genesis 1b");
		int[] genesisHash13 = stringToIntArray("Genesis Hash 1");
		int[] genesisHash21 = stringToIntArray("Genesis 2a");
		int[] genesisHash22 = stringToIntArray("Genesis 2b");
		int[] genesisHash23 = stringToIntArray("Genesis Hash 2");
		
		Block g1 = new Block(0, 0, new int[] {0}, genesisHash11, genesisHash12, genesisHash13, 0);
		Block g2 = new Block(0, 0, new int[] {0}, genesisHash21, genesisHash22, genesisHash23, 0);
		return new Block[] {g1, g2};
	}
	
	
	/**
	 * Motes are unable to send String over the Radio Channel
	 * Hence we need to convert String -> int[]
	 * 
	 * @param s The string to convert
	 * @return an int array representing the input String
	 */
	static int[] stringToIntArray(String s)
	{
		int i = 0;
		int[] out = new int[s.length()];
		for(char c : s.toCharArray())
		{
			out[i] = Character.getNumericValue(c);
			i++;
		}
		return out;
	}
	
//	private void computeHash(int difficulty)
//	{
//		String baseHash = 
//				this.mote_ID 
//				+ this.measuresTimestamps.toString()
//				+ this.prevHash1
//				+ this.prevHash2;
//		this.hash = this.hashIt(baseHash + this.nonce);
//		
//		//ToDo
//		//You must implement a way to compute the hash
////		String zeros = "00000000000000000".substring(0, difficulty);
////		while (!this.hash.substring(0, difficulty).equals(zeros));
////		{
////			this.nonce++;
////			this.hash = this.hashIt(baseHash + this.nonce);
////		}
//	}
//	
//	
//	private String hashIt(String stringToHash)
//	{
//		//Compute the hash for the stringToHash
//		System.out.println("TO BE IMPLEMENTED... \nTHIS IS CAUSING AN INFINITE LOOP!!!\n\n");
//		return stringToHash;
//	}
//	


	public int getMote_ID()
	{
		return mote_ID;
	}


	public void setMote_ID(int mote_ID)
	{
		this.mote_ID = mote_ID;
	}
	

	public int[] getPrevHash1()
	{
		return prevHash1;
	}


	public void setPrevHash1(int[] prevHash1)
	{
		this.prevHash1 = prevHash1;
	}


	public int[] getPrevHash2()
	{
		return prevHash2;
	}


	public void setPrevHash2(int[] prevHash2)
	{
		this.prevHash2 = prevHash2;
	}


	public int[] getHash()
	{
		return hash;
	}


	public void setHash(int[] hash)
	{
		this.hash = hash;
	}


	public int getNonce()
	{
		return nonce;
	}


	public void setNonce(int nonce)
	{
		this.nonce = nonce;
	}


	public int getTimestamp()
	{
		return timestamp;
	}


	public void setTimestamp(int timestamp)
	{
		this.timestamp = timestamp;
	}


	public int[] getMeasures()
	{
		return measures;
	}


	public void setMeasures(int[] measures)
	{
		this.measures = measures;
	}
}
