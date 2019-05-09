package DAG;

public class Block
{
	private int mote_ID;
	private int timestamp;
	private int[] measures;
	private String prevHash1; 
	private String prevHash2;
	private String hash;
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
	public Block(int m, int t, int[] measures, String h1, String h2, String hash, int n)
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
	 * Generates the 2 Genesis Blocks to initialise the DAG
	 */
	public static Block[] generateGenesisBlocks()
	{
		Block g1 = new Block(0, 0, new int[] {0}, "Genesis 1a", "Genesis 1b", "Genesis Hash 1", 0);
		Block g2 = new Block(0, 0, new int[] {0}, "Genesis 2a", "Genesis 2b", "Genesis Hash 2", 0);
		return new Block[] {g1, g2};
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
//	/**
//	 * Computes the Block's hash and compares it to the given hash
//	 * @return whether the given hash matches the Block's hash
//	 */
//	public boolean verifyBlock()
//	{
//		String baseHash = 
//				this.mote_ID 
//				+ this.measuresTimestamps.toString()
//				+ this.prevHash1
//				+ this.prevHash2
//				+ this.nonce;
//		return this.hash.equals(this.hashIt(baseHash));
//	} 


	public int getMote_ID()
	{
		return mote_ID;
	}


	public void setMote_ID(int mote_ID)
	{
		this.mote_ID = mote_ID;
	}
	

	public String getPrevHash1()
	{
		return prevHash1;
	}


	public void setPrevHash1(String prevHash1)
	{
		this.prevHash1 = prevHash1;
	}


	public String getPrevHash2()
	{
		return prevHash2;
	}


	public void setPrevHash2(String prevHash2)
	{
		this.prevHash2 = prevHash2;
	}


	public String getHash()
	{
		return hash;
	}


	public void setHash(String hash)
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
