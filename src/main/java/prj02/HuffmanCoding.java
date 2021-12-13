package prj02;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import DataStructures.HashTable.*;
import DataStructures.List.*;
import DataStructures.SortedList.*;
import DataStructures.Tree.*;


/**
 * The Huffman Encoding Algorithm
 *
 * This is a data compression algorithm designed by David A. Huffman and published in 1952
 *
 * What it does is it takes a string and by constructing a special binary tree with the frequencies of each character.
 * This tree generates special prefix codes that make the size of each string encoded a lot smaller, thus saving space.
 *
 * @author Fernando J. Bermudez Medina (Template)
 * @author A. ElSaid (Review)
 * @author Emmanuel J. Gonzalez Morales <802-19-6606> (Implementation)
 * @version 2.0
 * @since 10/16/2021
 */
public class HuffmanCoding {

	/**
	 * Main class for HuffmanCoding
	 * @param args - array of String
	 */
	public static void main(String[] args) {
	
		HuffmanEncodedResult();

	}
	
	/** This method just runs all the main methods developed or the algorithm */
	private static void HuffmanEncodedResult() {
		String data = load_data("stringData1.txt"); //You can create other test input files and add them to the inputData Folder

		/*If input string is not empty we can encode the text using our algorithm*/
		if(!data.isEmpty()) {
			Map<String, Integer> fD = compute_fd(data);
			BTNode<Integer,String> huffmanRoot = huffman_tree(fD);
			Map<String,String> encodedHuffman = huffman_code(huffmanRoot);
			String output = encode(encodedHuffman, data);
			process_results(fD, encodedHuffman,data,output);
		} else {
			System.out.println("Input Data Is Empty! Try Again with a File that has data inside!");
		}

	}

	/**
	 * Receives a file named in parameter inputFile (including its path),
	 * and returns a single string with the contents.
	 *
	 * @param inputFile name of the file to be processed in the path inputData/
	 * @return String with the information to be processed
	 */
	public static String load_data(String inputFile) {
		BufferedReader in = null;
		String line = "";

		try {
			/*We create a new reader that accepts UTF-8 encoding and extract the input string from the file, and we return it*/
			in = new BufferedReader(new InputStreamReader(new FileInputStream("inputData/" + inputFile), "UTF-8"));

			/*If input file is empty just return an empty string, if not just extract the data*/
			String extracted = in.readLine();
			if(extracted != null)
				line = extracted;

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
		return line;
	}

	/**
	 * Receives a String parameter, and return the frequency distribution of each character 
	 * @param inputString String that contains the character of which we obtain their frequency
	 * @return Map<String, Integer> with the symbol frequency distribution
	 */
	public static Map<String, Integer> compute_fd(String inputString) {
		/* TODO Compute Symbol Frequency Distribution of each character inside input string */
		// Receives a string and returns a Map with the symbol frequency distribution
		char charAtPosition;
		SimpleHashFunction<String> hashF = new SimpleHashFunction<>();
		Map<String, Integer> cpm = new HashTableSC<String,Integer>(hashF);
		for(int i = 0; i < inputString.length(); i++) {
			charAtPosition = inputString.charAt(i);
			if(cpm.containsKey(String.valueOf(charAtPosition))) {
				cpm.put(String.valueOf(charAtPosition), cpm.get(String.valueOf(charAtPosition))+1);
			}else
			{
				cpm.put(String.valueOf(charAtPosition), 1);
			}
		}
		
		return cpm; 
	}

	

	/**
	 * Receive a Map with String as key and Integer as value,
	 * and returns the root node of the corresponding Huffman tree.
	 *
	 * @param fD map of which root node is going to be searched
	 * @return BTNode<Integer,String> with the root node of the corresponding Huffman tree
	 */
	public static BTNode<Integer, String> huffman_tree(Map<String, Integer> fD) {
		
		List<String> fDList = fD.getKeys();
		SortedLinkedList<BTNode<Integer, String>> SL = new SortedLinkedList<BTNode<Integer, String>>();
		BTNode<Integer, String> newNode;
		BTNode<Integer,String> rootNode = new BTNode<Integer, String>(null, null);
	
		
		for(String s : fDList) {
			
			newNode = new BTNode<Integer, String>(fD.get(s),s);
			SL.add(newNode);
			
		}
	
		while(SL.size() != 1) {
			
			newNode = new BTNode<Integer, String>(); // create Node
			newNode.setLeftChild(SL.removeIndex(0)); //Set node with  < value in SL
			newNode.setRightChild(SL.removeIndex(0)); // Set node with < value but bigger than left
			newNode.setKey(newNode.getLeftChild().getKey()+newNode.getRightChild().getKey()); // set the funsion of the key of right and left  to parent
			newNode.setValue(newNode.getLeftChild().getValue()+newNode.getRightChild().getValue()); //set the funsion of the value of right and left  to parent
			SL.add(newNode); //Add node again in Sl and repeat process
	
		}
		
		rootNode = SL.get(0);
		return rootNode;
	}
	
	/**
	 * Receive the huffmanRoot of which it will return a Map with the code of each
	 * Node value. 
	 *
	 * @param huffmanRoot root of the huffman tree to be mapped
	 * @return Map<String,String> with the mapping if every symbol to its corresponding Huffman code
	 */
	public static Map<String, String> huffman_code(BTNode<Integer,String> huffmanRoot) {
		
		SimpleHashFunction<String> hashF = new SimpleHashFunction<>();
		Map<String, String> codeMap = new HashTableSC<String,String>(hashF);
		
		huffman_code_aux(huffmanRoot, codeMap, ""); //call helping method 
		
		return codeMap; 
	}  

	/**
	 * Receive Map of which contain the huffmancode, the input string.
	 *
	 * @param encodingMap Map with the code for the inputString
	 * @param inputString string to be encoded 
	 * @return String with the encoding of the inputString
	 */
	public static String encode(Map<String, String> encodingMap, String inputString) {
		String encodedString = "";
	
		for(int i = 0; i < inputString.length(); i++) {
			encodedString += encodingMap.get(Character.toString(inputString.charAt(i)));
		}
		
		return encodedString; 
	}

	/**
	 * Receives the frequency distribution map, the Huffman Prefix Code HashTable, the input string,
	 * and the output string, and prints the results to the screen (per specifications).
	 *
	 * Output Includes: symbol, frequency and code.
	 * Also includes how many bits has the original and encoded string, plus how much space was saved using this encoding algorithm
	 *
	 * @param fD Frequency Distribution of all the characters in input string
	 * @param encodedHuffman Prefix Code Map
	 * @param inputData text string from the input file
	 * @param output processed encoded string
	 */
	public static void process_results(Map<String, Integer> fD, Map<String, String> encodedHuffman, String inputData, String output) {
		/*To get the bytes of the input string, we just get the bytes of the original string with string.getBytes().length*/
		int inputBytes = inputData.getBytes().length;

		/**
		 * For the bytes of the encoded one, it's not so easy.
		 *
		 * Here we have to get the bytes the same way we got the bytes for the original one but we divide it by 8,
		 * because 1 byte = 8 bits and our huffman code is in bits (0,1), not bytes.
		 *
		 * This is because we want to calculate how many bytes we saved by counting how many bits we generated with the encoding
		 */
		DecimalFormat d = new DecimalFormat("##.##");
		double outputBytes = Math.ceil((float) output.getBytes().length / 8);

		/**
		 * to calculate how much space we saved we just take the percentage.
		 * the number of encoded bytes divided by the number of original bytes will give us how much space we "chopped off"
		 *
		 * So we have to subtract that "chopped off" percentage to the total (which is 100%)
		 * and that's the difference in space required
		 */
		String savings =  d.format(100 - (( (float) (outputBytes / (float)inputBytes) ) * 100));


		/**
		 * Finally we just output our results to the console
		 * with a more visual pleasing version of both our Hash Tables in decreasing order by frequency.
		 *
		 * Notice that when the output is shown, the characters with the highest frequency have the lowest amount of bits.
		 *
		 * This means the encoding worked and we saved space!
		 */
		System.out.println("Symbol\t" + "Frequency   " + "Code");
		System.out.println("------\t" + "---------   " + "----");

		SortedList<BTNode<Integer,String>> sortedList = new SortedLinkedList<BTNode<Integer,String>>();

		/* To print the table in decreasing order by frequency, we do the same thing we did when we built the tree
		 * We add each key with it's frequency in a node into a SortedList, this way we get the frequencies in ascending order*/
		for (String key : fD.getKeys()) {
			BTNode<Integer,String> node = new BTNode<Integer,String>(fD.get(key),key);
			sortedList.add(node);
		}

		/**
		 * Since we have the frequencies in ascending order,
		 * we just traverse the list backwards and start printing the nodes key (character) and value (frequency)
		 * and find the same key in our prefix code "Lookup Table" we made earlier on in huffman_code().
		 *
		 * That way we get the table in decreasing order by frequency
		 * */
		for (int i = sortedList.size() - 1; i >= 0; i--) {
			BTNode<Integer,String> node = sortedList.get(i);
			System.out.println(node.getValue() + "\t" + node.getKey() + "\t    " + encodedHuffman.get(node.getValue()));
		}

		System.out.println("\nOriginal String: \n" + inputData);
		System.out.println("Encoded String: \n" + output);
		System.out.println("Decoded String: \n" + decodeHuff(output, encodedHuffman) + "\n");
		System.out.println("The original string requires " + inputBytes + " bytes.");
		System.out.println("The encoded string requires " + (int) outputBytes + " bytes.");
		System.out.println("Difference in space requiered is " + savings + "%.");
	}


	/*************************************************************************************
	 ** ADD ANY AUXILIARY METHOD YOU WISH TO IMPLEMENT TO FACILITATE YOUR SOLUTION HERE **
	 *************************************************************************************/
	/**
	 * 	Huffmancode auxiliary method, it receive the huffmanRoot, the map, and the String value of which 
	 *  contain the code of each node
	 *  
	 * @param huffmanRoot - huffman tree which is going to be encoded
	 * @param map - contains the huffmanRoot's value with the corresponding code value
	 * @param value string value of each node
	 */
	public static void huffman_code_aux( BTNode<Integer,String> huffmanRoot, Map<String, String> map, String value) {
		if(huffmanRoot == null) return;
		
	
		 huffman_code_aux(huffmanRoot.getLeftChild(), map, value +"0");
	
		 if(huffmanRoot.getRightChild() == null && huffmanRoot.getRightChild() == null) {
			 map.put(huffmanRoot.getValue(), value); 
		 }
		 
		 huffman_code_aux(huffmanRoot.getRightChild(), map, value + "1");
		 if(huffmanRoot.getRightChild() == null && huffmanRoot.getRightChild() == null) {
			 map.put(huffmanRoot.getValue(), value);
		 }
		 
	} 
	
	/**
	 * Auxiliary Method that decodes the generated string by the Huffman Coding Algorithm
	 *
	 * Used for output Purposes
	 *
	 * @param output - Encoded String
	 * @param lookupTable - Map with code values
	 * @return The decoded String, this should be the original input string parsed from the input file
	 */
	public static String decodeHuff(String output, Map<String, String> lookupTable) {
		String result = "";
		int start = 0;
		List<String>  prefixCodes = lookupTable.getValues();
		List<String> symbols = lookupTable.getKeys();

		/*looping through output until a prefix code is found on map and
		 * adding the symbol that the code that represents it to result */
		for(int i = 0; i <= output.length();i++){

			String searched = output.substring(start, i);

			int index = prefixCodes.firstIndex(searched);

			if(index >= 0) { //Found it
				result= result + symbols.get(index);
				start = i;
			}
		}
		return result;
	}


}
