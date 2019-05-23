package DAG;

import java.util.Arrays;

public class Block
{
	private int mote_ID;
	private long timestamp;
	private int[] measures;
	private long prevHash1; 
	private long prevHash2;
	private long hash;
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
	public Block(int m, long t, int[] measures, long h1, long h2, long hash, int n)
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
	public Block(int m, long t, int[] measures, String h1, String h2, String hash, int n)
	{
		this.mote_ID = m;
		this.timestamp = t;
		this.measures = measures;
		this.prevHash1 = Block.stringToShortArray(h1);
		this.prevHash2 = Block.stringToShortArray(h2);
		this.hash = Block.stringToShortArray(hash);
		this.nonce = n;
	}

	
	/**
	 * Generates the 2 Genesis Blocks to initialise the DAG
	 */
	public static Block[] generateGenesisBlocks()
	{
		long genesisHash11 = Block.stringToShortArray("Genesis 1a");
		long genesisHash12 = Block.stringToShortArray("Genesis 1b");
		long genesisHash13 = Block.stringToShortArray("Genesis Hash 1");
		long genesisHash21 = Block.stringToShortArray("Genesis 2a");
		long genesisHash22 = Block.stringToShortArray("Genesis 2b");
		long genesisHash23 = Block.stringToShortArray("Genesis Hash 2");
		
		Block g1 = new Block(0, 0, new int[] {0}, genesisHash11, genesisHash12, genesisHash13, 0);
		Block g2 = new Block(0, 0, new int[] {0}, genesisHash21, genesisHash22, genesisHash23, 0);
		return new Block[] {g1, g2};
	}
	
	
	/**
	 * Motes are unable to send String over the Radio Channel
	 * Hence we need to convert String -> long[]
	 * 
	 * @param s The string to convert
	 * @return an long array representing the input String
	 */
	static long stringToShortArray(String s)
	{
		int i = 0;
		long base = 1;
		long out = 0;
		for(char c : s.toCharArray())
		{
			out += (long) base * Character.getNumericValue(c);
			i++;
			base *= 10;
		}
		return out;
	}


	public int getMote_ID()
	{
		return mote_ID;
	}


	public void setMote_ID(int mote_ID)
	{
		this.mote_ID = mote_ID;
	}


	public long getTimestamp()
	{
		return timestamp;
	}


	public void setTimestamp(long timestamp)
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


	public int getNonce()
	{
		return nonce;
	}


	public void setNonce(int nonce)
	{
		this.nonce = nonce;
	}


	public long getPrevHash1()
	{
		return prevHash1;
	}


	public void setPrevHash1(long prevHash1)
	{
		this.prevHash1 = prevHash1;
	}


	public long getPrevHash2()
	{
		return prevHash2;
	}


	public void setPrevHash2(long prevHash2)
	{
		this.prevHash2 = prevHash2;
	}


	public long getHash()
	{
		return hash;
	}


	public void setHash(long hash)
	{
		this.hash = hash;
	}
	
}
