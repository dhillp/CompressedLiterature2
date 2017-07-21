import java.util.PriorityQueue;

/**
 * Takes a string and generates a map of codes using Huffman's encoding procedure.
 * Then converts string into string of the codes.
 * 
 * @author Pamaldeep Dhillon
 * @version 2.0
 */

public class CodingTree {
	
	public MyHashTable<String, String> codes;
	
	public String bits;
	
	public CodingTree(String theMessage) {
		MyHashTable<String, Integer> strFreq = countCharFreq(theMessage);
		PriorityQueue<HuffmanTree> pQueue = createTrees(strFreq);
		HuffmanTree hTree = createFullTree(pQueue);
		codes = new MyHashTable<String, String>(32768);
		createCodeMap(hTree.root);
		bits = encodeMessage(theMessage);
	}
	
	private MyHashTable<String, Integer> countCharFreq(String theString) {
		MyHashTable<String, Integer> freq = new MyHashTable<String, Integer>(32768);
		int i = 0;
		while (i < theString.length()) {
			StringBuilder sb = new StringBuilder();
			boolean run = false;
			boolean test = ((int) theString.charAt(i) >= 48 && (int) theString.charAt(i) <= 57)
					|| ((int) theString.charAt(i) >= 65 && (int) theString.charAt(i) <= 90)
					|| ((int) theString.charAt(i) >= 97 && (int) theString.charAt(i) <= 122)
					|| ((int) theString.charAt(i) == 39 || (int) theString.charAt(i) == 45);
			if (!test) {
				String temp = Character.toString(theString.charAt(i));
				if (freq.containsKey(temp)) {
					int count = freq.get(temp);
					freq.put(temp, count + 1);
				} else {
					freq.put(temp, 1);
				}
			}
			while (i < theString.length() && test) {
				run = true;
				sb.append(theString.charAt(i));
				i++;
				test = ((int) theString.charAt(i) >= 48 && (int) theString.charAt(i) <= 57)
						|| ((int) theString.charAt(i) >= 65 && (int) theString.charAt(i) <= 90)
						|| ((int) theString.charAt(i) >= 97 && (int) theString.charAt(i) <= 122)
						|| ((int) theString.charAt(i) == 39 || (int) theString.charAt(i) == 45);
			}
			if (run && freq.containsKey(sb.toString())) {
				int count = freq.get(sb.toString());
				freq.put(sb.toString(), count + 1);
				i--;
			} else if (run) {
				freq.put(sb.toString(), 1);
				i--;
			}
			i++;
		}
		return freq;
	}
	
	private PriorityQueue<HuffmanTree> createTrees(MyHashTable<String, Integer> theCharFreq) {
		PriorityQueue<HuffmanTree> tQueue = new PriorityQueue<HuffmanTree>();
		for (String s : theCharFreq.keySet) {
			tQueue.offer(new HuffmanTree(s, theCharFreq.get(s)));
		}
		return tQueue;
	}
	
	private HuffmanTree createFullTree(PriorityQueue<HuffmanTree> theQueue) {
		while (theQueue.size() > 1) {
			HuffmanTree tree1 = theQueue.remove();
		    HuffmanTree tree2 = theQueue.remove();
		    theQueue.add(new HuffmanTree(tree1, tree2));
		}
		return theQueue.remove();
	}
	
	private void createCodeMap(HuffmanTree.Node theRoot) {
		if (theRoot.left != null) {
			theRoot.left.code = theRoot.code + '0';
			createCodeMap(theRoot.left);
			theRoot.right.code = theRoot.code + '1';
			createCodeMap(theRoot.right);
		} else {
			codes.put(theRoot.myString, theRoot.code);
		}
	}
	
	private String encodeMessage(String theMessage) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while (i < theMessage.length()) {
			StringBuilder sb2 = new StringBuilder();
			boolean run = false;
			boolean test = ((int) theMessage.charAt(i) >= 48 && (int) theMessage.charAt(i) <= 57)
					|| ((int) theMessage.charAt(i) >= 65 && (int) theMessage.charAt(i) <= 90)
					|| ((int) theMessage.charAt(i) >= 97 && (int) theMessage.charAt(i) <= 122)
					|| ((int) theMessage.charAt(i) == 39 || (int) theMessage.charAt(i) == 45);
			if (!test) {
				String temp = Character.toString(theMessage.charAt(i));
				if (codes.containsKey(temp)) {
					sb.append(codes.get(temp));
				}
			}
			while (i < theMessage.length() && test) {
				run = true;
				sb2.append(theMessage.charAt(i));
				i++;
				test = ((int) theMessage.charAt(i) >= 48 && (int) theMessage.charAt(i) <= 57)
						|| ((int) theMessage.charAt(i) >= 65 && (int) theMessage.charAt(i) <= 90)
						|| ((int) theMessage.charAt(i) >= 97 && (int) theMessage.charAt(i) <= 122)
						|| ((int) theMessage.charAt(i) == 39 || (int) theMessage.charAt(i) == 45);
			}
			if (run) {
				sb.append(codes.get(sb2.toString()));
				i--;
			}
			i++;
		}
		return sb.toString();
	}
	
	private class HuffmanTree implements Comparable<HuffmanTree> {
		
		private Node root;
		
		public HuffmanTree(String theString, int theWeight) {
			root = new Node(theString, theWeight);
		}
		
		public HuffmanTree(HuffmanTree tree1, HuffmanTree tree2) {
			root = new Node();
			root.left = tree1.root;
			root.right = tree2.root;
			root.weight = tree1.root.weight + tree2.root.weight;
		}
		
		private class Node {
			private String myString;
			private int weight;
			private Node left;
			private Node right;
			private String code = "";
			
			public Node() {
			}
			
			public Node(String theString, int theWeight) {
				myString = theString;
				weight = theWeight;
			}
		}

		@Override
		public int compareTo(HuffmanTree theOther) {
			int result = 0;
			if (root.weight < theOther.root.weight) {
				result = -1;
			} else if (root.weight > theOther.root.weight) {
				result = 1;
			}
			return result;
		}
	}
}
