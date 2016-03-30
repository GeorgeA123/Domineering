/**
 *
 *
 * @author Ermano Arruda
 */

import static org.junit.Assert.assertArrayEquals;

import org.junit.AfterClass;

import org.junit.runner.RunWith;

import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;

import org.junit.Test;
import org.junit.Before;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;

import static org.junit.Assert.assertArrayEquals;

import java.util.*;


@RunWith(JUnitQuickcheck.class)
public class TestsDomineeringClean {


	ProxyGameTree solutionGameTree;
	GameTree<DomineeringMove> studentGameTree;
	ArrayList<Integer> solutionVals, studentVals;

	static final String TREE_FILE_PREFIX="test_data/treeDomineering";
	static final String TREE_FILE_SUFIX=".pgt";

	public void init(Integer m, Integer n){

		solutionVals = new ArrayList<Integer>();
		studentVals = new ArrayList<Integer>();

		DomineeringBoard studentBoard = new DomineeringBoard(m,n);
		
		String treeFile = TREE_FILE_PREFIX + m + "x" + n + TREE_FILE_SUFIX;
		solutionGameTree = Utils.load(treeFile);
		studentGameTree = studentBoard.tree();

	}

	public void assertion(){

		Collections.sort(solutionVals);
		Collections.sort(studentVals);

		assertArrayEquals(solutionVals.toArray(),studentVals.toArray());


	}

	/**
	 * Every subtree of the student's game tree should have the same height 
	 * as the pre-computed game tree solution
	 *
	 */
	@Property
	public void heightShouldMatchSolution(@InRange(min = "0", max = "4") Integer m, @InRange(min = "0", max = "4") Integer n){

		init(m,n);

		heightTraversal(solutionGameTree,solutionVals);
		heightTraversal(studentGameTree,studentVals);


		assertion();

		//System.out.println("TestsDomineering: Passed HEIGHT test with board dimensions " + m +"x" + n + "!");
	}

	/**
	 * Every subtree of the student's game tree should have the same number of nodes (size) 
	 * as the pre-computed game tree solution
	 *
	 */
	@Property
	public void sizeShouldMatchSolution(@InRange(min = "0", max = "4") Integer m, @InRange(min = "0", max = "4") Integer n){

		init(m,n);

		sizeTraversal(solutionGameTree,solutionVals);
		sizeTraversal(studentGameTree,studentVals);

		assertion();
		//System.out.println("TestsDomineering: Passed SIZE test with board dimensions " + m +"x" + n + "!");
	
	}


	/**
	 * Traverses tree in pre-order adding the height of each corresponding subtree in a list
	 *
	 * @param solutionGameTree
	 *            the solution game tree
	 * @param vals
	 *            height values for all subtrees of solutionGameTree
	 */
	public void heightTraversal(ProxyGameTree solutionGameTree, 
								ArrayList<Integer> vals) {

		vals.add(solutionGameTree.height());

		Set< Map.Entry<Integer,ProxyGameTree> > entrySet = solutionGameTree.children().entrySet();
		Iterator< Map.Entry<Integer,ProxyGameTree> > it = entrySet.iterator();

		while( it.hasNext() ){

			Map.Entry<Integer,ProxyGameTree> entry = it.next();


			heightTraversal(entry.getValue(),vals);
		}

	}

	/**
	 * Traverses tree in pre-order adding the height of each corresponding subtree in a list
	 *
	 * @param studentGameTree
	 *            student's game tree
	 * @param vals
	 *            height values for all subtrees of studentGameTree
	 */

	public void heightTraversal(GameTree<DomineeringMove> studentGameTree, 
							    ArrayList<Integer> vals) {

		vals.add(studentGameTree.height());

		Set< Map.Entry<DomineeringMove,GameTree<DomineeringMove> > > entrySet = studentGameTree.children().entrySet();
		Iterator< Map.Entry<DomineeringMove,GameTree<DomineeringMove>> > it = entrySet.iterator();

		while( it.hasNext() ){

			Map.Entry<DomineeringMove,GameTree<DomineeringMove> > entry = it.next();


			heightTraversal(entry.getValue(),vals);
		}

	}

	/**
	 * Traverses tree in pre-order adding the size of each corresponding subtree in a list
	 *
	 * @param solutionGameTree
	 *            the solution game tree
	 * @param vals
	 *            size values for all subtrees of solutionGameTree
	 */

	public void sizeTraversal(ProxyGameTree solutionGameTree, 
								ArrayList<Integer> vals) {

		vals.add(solutionGameTree.size());

		Set< Map.Entry<Integer,ProxyGameTree> > entrySet = solutionGameTree.children().entrySet();
		Iterator< Map.Entry<Integer,ProxyGameTree> > it = entrySet.iterator();

		while( it.hasNext() ){

			Map.Entry<Integer,ProxyGameTree> entry = it.next();


			sizeTraversal(entry.getValue(),vals);
		}

	}
	/**
	 * Traverses tree in pre-order adding the size of each corresponding subtree in a list
	 *
	 * @param studentGameTree
	 *            student's game tree
	 * @param vals
	 *            size values for all subtrees of studentGameTree
	 */
	public void sizeTraversal(GameTree<DomineeringMove> studentGameTree, 
								ArrayList<Integer> vals) {

		vals.add(studentGameTree.size());

		Set< Map.Entry<DomineeringMove,GameTree<DomineeringMove> > > entrySet = studentGameTree.children().entrySet();
		Iterator< Map.Entry<DomineeringMove,GameTree<DomineeringMove>> > it = entrySet.iterator();

		while( it.hasNext() ){

			Map.Entry<DomineeringMove,GameTree<DomineeringMove> > entry = it.next();


			sizeTraversal(entry.getValue(),vals);
		}

	}





}
