/**
 *
 *
 * @author Ermano Arruda
 */

import java.util.*;
import java.io.*;

@SuppressWarnings("serial")
public class ProxyGameTree implements Serializable {

	private final Map<Integer, ProxyGameTree > children;

	public ProxyGameTree(Map<Integer, ProxyGameTree > children) {

		assert (children != null);
		this.children = children;
	}


	public Map<Integer, ProxyGameTree> children() {
		return children;
	}


	// Number of tree nodes:
	public int size() {
		int size = 1;
		for (Map.Entry<Integer, ProxyGameTree> child : children.entrySet()) {
			size += child.getValue().size();
		}
		return size;
	}

	// We take the height of a leaf to be zero (rather than -1):
	public int height() {
		int height = -1;
		for (Map.Entry<Integer, ProxyGameTree> e : children.entrySet()) {
			height = Math.max(height, e.getValue().height());
		}
		return 1 + height;
	}



}