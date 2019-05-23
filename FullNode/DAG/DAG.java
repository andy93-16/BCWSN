package DAG;

import java.util.*;

/**
 * This class implements a DAG - Direct Acyclic Graph
 * @author MARCO
 */
public class DAG
{
	/** 
	 * Core element of the class. 
	 * Keeps track of all Blocks created in a pair <Hash, Block>
	 */
	private HashMap<Long, Block> blockChain = new HashMap<Long, Block>();
	
	/** 
	 * Support HashMap. 
	 * Enables to keep track of how many new Blocks have verified a specific old Block.
	 * This is helpful when looking for the Least Used Block, get its Hash and give it
	 * to a Mote when creating a new Block.
	 * Higher values for a Hash means more trustworthiness.
	 * In order to create a solid and trustworthy DAG one should aim to verify 
	 * the Least Used Blocks hence retrieving the 2 minimum Blocks' values from this Map.
	 * 
	 * short[] = The old Block is here represented by its own Hash.
	 * Integer = The amount of new Blocks that have verified the old Block's Hash
	 */
	private HashMap<Long, Integer> hashesList = new HashMap<Long, Integer>();
	
	
	/**
	 * Represents the pair (MoteID, Difficulty) where:
	 * Difficulty represents the mote's difficulty to meet when creating a Block
	 */
	private HashMap<Integer, Integer> moteDifficulty = new HashMap<Integer, Integer>();
	
	
	/**
	 * Record the 2 previous hashes assigned to a specific mote for its next block
	 */
	public HashMap<Integer, Long[]> mote_prevHashes = new HashMap<Integer, Long[]>();
	
	/** Initial difficulty for each mote */
	private final Integer initDifficulty = 2;
	
	/** Minimum difficulty to meet for each mote */
	private final Integer minDifficulty = 0;
	
	/** Maximum difficulty to meet for each mote */
	private final Integer maxDifficulty = 7;
	
	
	/**
	 * General constructor for the DAG
	 * It instantiates all the basic functions needed to start a new DAG
	 */
	public DAG()
	{
		this.generateGenesisBlocks();
		//Other things to initialise...
	}
	
	
	/**
	 * This constructor is meant to open a previously created DAG
	 * and keep updating it with new blocks as they are received
	 * 
	 * @param txtFile Is it a path to the file? Is it the entire txt file? Dunno, help yourself
	 */
	private DAG(Object txtFile)
	{
		//ToDo;
		//this.blockChain = txtFile.getBlockChain();
		//this.hashesList = txtFile.getHashesList();
		//and so on...
	}
	
	
	/**
	 * Generate the 2 Genesis Blocks needed for the DAG initialisation
	 */
	private void generateGenesisBlocks()
	{
		Block[] genesis = Block.generateGenesisBlocks();
		
		this.blockChain.put(genesis[0].getHash(), genesis[0]);
		this.blockChain.put(genesis[1].getHash(), genesis[1]);
		
		this.hashesList.put(genesis[0].getHash(), 0);
		this.hashesList.put(genesis[1].getHash(), 0);
	}
	
	
	/**
	 * Add a Block to the DAG and
	 * Update the list of validations
	 * 
	 * @param b The new Block to add
	 */
	public void addToDAG(Block b)
	{
		this.blockChain.put(b.getHash(), b);
		this.updateHashesList(b);
	}
	
	
	/**
	 * Add a new Block's hash to the list and 
	 * Update the 2 verified Blocks increasing their number of validations
	 *
	 * @param b The new Block to add
	 */
	private void updateHashesList(Block b)
	{
		this.hashesList.put(b.getHash(), 0);
		
		int validations = this.hashesList.get(b.getPrevHash1()) + 1;
		this.hashesList.put(b.getPrevHash1(), validations);

		validations = this.hashesList.get(b.getPrevHash2()) + 1;
		this.hashesList.put(b.getPrevHash2(), validations);
	}
	
	
	/**
	 * Search in the this.hashesList for all the hashes and return 2 random ones
	 * 
	 * @param moteID represents the requesting mote ID. 
	 * Why it is necessary: it lessens the memory usage of Blocks sent by the Mote via radio
	 * Recording the moteID and its designated tips leaves less work for the mote. 
	 * @return Two random hashes from the DAG
	 */
	public Long[] getTips(int moteID)
	{
		Random rng = new Random();
		Object[] hashes = this.hashesList.keySet().toArray();
		Long h1 = (Long) hashes[rng.nextInt(this.getTotalBlocks())];
		Long h2;
		do
		{
			h2 = (Long) hashes[rng.nextInt(this.getTotalBlocks())];
		}
		while(h2.equals(h1));
		Long[] tips = new Long[] {h1, h2};
		this.mote_prevHashes.put(moteID, tips);
		return tips;
	}
	
	
	/**
	 * A correct DAG should provide the Least Used Blocks in order to 
	 * increase the number of their validations and create a more robust structure.
	 * 
	 * You should implement this method to get the 2 minimum values stored 
	 * into this.hashesList and return their associated Block Hashes
	 * @return
	 */
	private String[] getMinimumValidatedBlocks()
	{
		//ToDo;
		String[] toBeImplemented = {"To", "Do"};
		return toBeImplemented;
	}
	
	
	/**
	 * Retrieves the amount of blocks stored in the DAG
	 * 
	 * @return the amount of blocks stored in the DAG
	 */
	public int getTotalBlocks()
	{
		return this.hashesList.size();
	}	
	
	
	/**
	 * ToDo:
	 * Checks whether the received Block is valid
	 * 
	 * @param b The block to check
	 * @return Whether the block is valid or not
	 */
	private boolean isBlockValid(Block b)
	{
		//ToDo:
		//1) Prendi il moteID con b.getMote_ID()
		//2) Trova la chiave pubblica del moteID
		//3) Decifra il blocco 
		//3b)Se non decifrabile con la chiave pubblica -> this.updateMoteDifficulty(moteID, 2)
		//4) Prendi i due Tips relativi al mote da this.mote_prevHashes.get(moteID)
		//5) Calcola l'hash (hashCalcolato) dei dati inseriti nel blocco insieme ai due Tips trovati al punto precedente
		//6) Controlla che l'hash appena calcolato sia lo stesso di quello inviato nel Blocco b (ovvero b.hash)
		//6b)Se gli hash sono diversi -> this.updateMoteDifficulty(moteID, 1)
		//6c)Altrimenti -> this.updateMoteDifficulty(moteID, -1)
		//7) return hashCalcolato.equals(b.hash);
		return false;
	}
	
	
	/**
	 * Updates the HashMap for motes difficulties
	 * 
	 * @param moteID The mote to judge
	 * @param d The added difficulty to give. Can be positive (to punish) or negative (to reward).
	 */
	private void updateMoteDifficulty(int moteID, int d)
	{
		int previousDifficulty = this.getMoteDifficulty(moteID);
		if(d > 0)
		{
			if(previousDifficulty + d >= this.maxDifficulty)
			{
				this.moteDifficulty.put(moteID, this.maxDifficulty);
			}
			else
			{
				this.moteDifficulty.put(moteID, previousDifficulty + d);
			}
		}
		else if(d < 0)
		{
			if(previousDifficulty + d <= this.minDifficulty)
			{
				this.moteDifficulty.put(moteID, this.minDifficulty);
			}
			else
			{
				this.moteDifficulty.put(moteID, previousDifficulty + d);
			}
		}
	}
	
	/**
	 * Returns the difficulty to meet for the given mote
	 * 
	 * @param moteID The mote
	 * @return The difficulty linked to the given mote
	 */
	public int getMoteDifficulty(int moteID)
	{
		if(!this.moteDifficulty.containsKey(moteID))
		{
			this.moteDifficulty.put(moteID, this.initDifficulty);
			return this.initDifficulty;
		}
		return this.moteDifficulty.get(moteID);
	}
}
