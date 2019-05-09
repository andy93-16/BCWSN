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
	private HashMap<String, Block> blockChain = new HashMap<String, Block>();
	
	/** 
	 * Support HashMap. 
	 * Enables to keep track of how many new Blocks have verified a specific old Block.
	 * This is helpful when looking for the Least Used Block, get its Hash and give it
	 * to a Mote when creating a new Block.
	 * Higher values for a Hash means more trustworthiness.
	 * In order to create a solid and trustworthy DAG one should aim to verify 
	 * the Least Used Blocks hence retrieving the 2 minimum Blocks' values from this Map.
	 * 
	 * String = The old Block is here represented by its own Hash.
	 * Integer = The amount of new Blocks that have verified the old Block's Hash
	 */
	private HashMap<String, Integer> hashesList = new HashMap<String, Integer>();
	
	
	/**
	 * Represents the pair (MoteID, Difficulty) where:
	 * Difficulty represents the mote's difficulty to meet when creating a Block
	 */
	private HashMap<Integer, Integer> moteDifficulty = new HashMap<Integer, Integer>();
	
	
	/**
	 * General constructor for the DAG
	 * It instantiates all the basic functions needed to start a new DAG
	 */
	public DAG()
	{
		this.generateGenesisBlocks();
		//Other things to initialize...
	}
	
	
	/**
	 * This constructor is meant to open a previously created DAG
	 * and keep updating it with new blocks as they are received
	 * 
	 * @param txtFile Is it a path to the file? Is it the entire txt file? Dunno, help yourself
	 */
	public DAG(Object txtFile)
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
	 * @return Two random hashes from the DAG
	 */
	public String[] getTips()
	{
		Random rng = new Random();
		Object[] hashes = this.hashesList.keySet().toArray();
		String h1 = (String) hashes[rng.nextInt(this.getTotalBlocks())];
		String h2;
		do
		{
			h2 = (String) hashes[rng.nextInt(this.getTotalBlocks())];
		}
		while(h2.equals(h1));
		return new String[] {h1, h2};
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
	
	
//	public String toString()
//	{
//		StringBuffer out = new StringBuffer();
//		Object nodo;
//		ArcoOrdinato a;
//		Iterator arcoI;
//		Iterator nodoI = nodi.keySet().iterator();
//		while (nodoI.hasNext())
//		{
//			arcoI = ((Set) nodi.get(nodo = nodoI.next())).iterator();
//			out.append("Nodo " + nodo.toString() + ": ");
//			while (arcoI.hasNext())
//			{
//				a = (ArcoOrdinato) arcoI.next();
//				// out.append( ((a.x == nodo ) ? a.y.toString() :
//				// a.x.toString()) + "("+a.value.toString()+"), ");
//				out.append(a.toString() + ", ");
//			}
//			out.append("\n");
//		}
//		return out.toString();
//	}
//
//


}

