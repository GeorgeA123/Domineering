/**
 *
 *
 * @author Ermano Arruda
 */


import java.util.*;
import java.io.*;

public abstract class Utils {


	public static void save(ProxyGameTree gameTree, String filename){
		try{
	         FileOutputStream fout = new FileOutputStream(filename);
	         ObjectOutputStream out = new ObjectOutputStream(fout);
	         out.writeObject(gameTree);
	         out.close();
	         fout.close();
	      }
	      catch(IOException e) {
	          e.printStackTrace();
	      }
	}

	public static <Move> ProxyGameTree load(String filename) {


		ProxyGameTree gameTree = null;
	    try{
	    	FileInputStream fin = new FileInputStream(filename);
	    	ObjectInputStream in = new ObjectInputStream(fin);
	    	gameTree = (ProxyGameTree) in.readObject();
	    	in.close();
	    	fin.close();
	      }
	      catch(IOException e)
	      {
	         e.printStackTrace();
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("ProxyGameTree class not found");
	         c.printStackTrace();
	      }

	     
	    return gameTree;

	}


	public static <Move> ProxyGameTree toProxyGameTree(GameTree<Move> gameTree) {


		Set< Map.Entry<Move,GameTree<Move>> > entrySet = gameTree.children().entrySet();
		Iterator< Map.Entry<Move,GameTree<Move>> > it = entrySet.iterator();

		Map<Integer, ProxyGameTree> children = new LinkedHashMap<Integer, ProxyGameTree>();

		while( it.hasNext() ){

			Map.Entry<Move,GameTree<Move>> entry = it.next();
			ProxyGameTree subtree = toProxyGameTree(entry.getValue());
			

			children.put(entry.getKey().hashCode(), subtree);

		}


		return new ProxyGameTree(children);

	}
}