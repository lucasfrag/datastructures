/*
 * Data Structures and Algorithms.
 * Copyright (C) 2016 Rafael Guterres Jeffman
 *
 * See the LICENSE file accompanying this source code, for
 * licensing restrictions that might apply.
 *
 */

package datastructures;

class RedBlackNode<T extends Comparable<T>> {

	/* private removidos para tornar os atributos visiveis para os metodos de remocao */
	T value;
	RedBlackNode<T> left;
	RedBlackNode<T> right;
	RedBlackNode<T> parent;
	boolean red;
	
	public RedBlackNode(T value) {
		this.value = value;
		this.red = true;
	}

	public RedBlackNode<T> insert(T value) throws DuplicateKeyException {
		int cmp = value.compareTo(this.value);
		if (cmp < 0)
			return insertLeft(value);
		else if (cmp > 0)
			return insertRight(value);
		else
			throw new DuplicateKeyException("Already inserted: "+value);
	}
	
	
	private RedBlackNode<T> insertLeft(T value) throws DuplicateKeyException {
		if (left == null) {
			left = new RedBlackNode<>(value);
			left.parent = this;
			return left;
		} else
			return left.insert(value);
	}

	private RedBlackNode<T> insertRight(T value) throws DuplicateKeyException {
		if (right == null) {
			right = new RedBlackNode<>(value);
			right.parent = this;
			return right;
		} else
			return right.insert(value);
	}
	
	public RedBlackNode<T> getParent() {
		return parent;
	}
	
	// Metodo para achar irmao de node
	public RedBlackNode<T> getSibling(RedBlackNode<T> node) {
		RedBlackNode<T> sibling = null;
		
		if (node.getParent().left == node) {
			sibling = node.getParent().right;
		} else if (node.getParent().right == node) {
			sibling = node.getParent().left;
		}
		return sibling;
	}

	
	public void setBlack() {
		this.red = false;
	}

	public void setRed() {
		this.red = true;
	}

	public boolean isRed() {
		return red;
	}
	
	public void print() {
		String r = red ? "*" : ""; 
		System.out.print("(" + value + r +" ");
		if (left != null)
			left.print();
		else
			System.out.print("_");
		if (right != null)
			right.print();
		else
			System.out.print(" _");
		System.out.print(")");
	}

	public RedBlackNode<T> getUncle() {
		if (parent == null)
			return null;
		RedBlackNode<T> G = parent.parent;
		if (G == null)
			return null;
		if (G.left == parent)
			return G.right;
		return G.left;
	}

	public boolean isRightSon() {
		if (parent == null)
			return false;
		return parent.right == this;
	}

	public boolean isLeftSon() {
		if (parent == null)
			return false;
		return parent.left == this;
	}

	public void rotateLeft() {
		if (right == null) return;
		RedBlackNode<T> N = this;
		RedBlackNode<T> P = this.parent;
		RedBlackNode<T> R = this.right;
		RedBlackNode<T> S = R.left;
		//
		RedBlackNode<T> B = S;
		R.left = N;
		N.right = B;
		// parents
		N.parent = R;
		R.parent = P;
		if (B != null)
			B.parent = N;
	}

	public void rotateRight() {
		if (left == null) return;
		RedBlackNode<T> N = this;
		RedBlackNode<T> P = this.parent;
		RedBlackNode<T> L = this.left;
		RedBlackNode<T> S = L.right;
		//
		RedBlackNode<T> B = S;
		L.right = N;
		N.left = B;
		// parents
		N.parent = L;
		L.parent = P;
		if (B != null)
			B.parent = N;
	}

	public void setRight(RedBlackNode<T> node) {
		right = node;
	}

	public void setLeft(RedBlackNode<T> node) {
		left = node;
	}
	
}

public class RedBlackTree<T extends Comparable<T>>
{
	private RedBlackNode<T> root;
	
	public void insert(T data) throws DuplicateKeyException
	{
		RedBlackNode<T> node;
		if (root == null)
			node = root = new RedBlackNode<>(data);
		else
			node = root.insert(data);
		insert_case1(node);
	}
	
	private void insert_case1(RedBlackNode<T> node) {
		if (node.getParent() == null) {
			node.setBlack();
			return;
		}
		insert_case2(node);
	}
	
	private void insert_case2(RedBlackNode<T> node) {
		RedBlackNode<T> P = node.getParent();
		if (!P.isRed()) return;
		insert_case3(node);
	}
	
	private void insert_case3(RedBlackNode<T> node) {
		RedBlackNode<T> P = node.getParent();
		RedBlackNode<T> U = node.getUncle();
		RedBlackNode<T> G = P.getParent();
		if (P.isRed() && (U != null && U.isRed())) {
			P.setBlack();
			U.setBlack();
			G.setRed();
			insert_case1(G);
		} else
			insert_case4(node);
	}
	
	private void insert_case4(RedBlackNode<T> node) { // P is R, U is B 
		RedBlackNode<T> P = node.getParent();
		RedBlackNode<T> G = P.getParent();
		RedBlackNode<T> N = node;

		if (P.isRightSon() && !node.isRightSon()) {
			P.rotateRight();
			G.setRight(node);
			N = P;
		}
		else if (P.isLeftSon() && !node.isLeftSon()) {
			P.rotateLeft();
			G.setLeft(node);
			N = P;
		}
			
		insert_case5(N);
	}
	
	private void insert_case5(RedBlackNode<T> node) { // P is R, U is B 
		RedBlackNode<T> P = (RedBlackNode<T>)(node.getParent());
		RedBlackNode<T> G = (RedBlackNode<T>)P.getParent();
		RedBlackNode<T> GG = null;
		boolean gl = G.isLeftSon(); 
		if (G.getParent() != null)
			GG = (RedBlackNode<T>)(G.getParent());
		
		P.setBlack();
		G.setRed();
		if (P.isRightSon()) {
			G.rotateLeft();
		} else {
			G.rotateRight();
		}
		if (GG != null)
			if (gl) GG.setLeft(P);
			else GG.setRight(P);
		else
			root = P;
	}
	
	public void print() {
		if (root == null)
			System.out.println("Empty tree.");
		else
			root.print();
	}

	
	/* Remover da arvore */
	
	public void remove(RedBlackNode<T> node) {
		
		
	}
	
	public void removeOneChild(RedBlackNode<T> node) {
		/* Node precisa ter pelo menos um filho que nao seja folha*/
		RedBlackNode<T> child = node.right.red ? node.left : node.right;
	    
		/* Incompleto: */
        replace_node();
        
        if (node.red == false) {
            if(child.red == true) {
                child.red = false;
            } else {
                remove_case1(child);
            }
            free(node);
        }
	}
	
	public void remove_case1(RedBlackNode<T> node) {
		if (node.getParent() != null ) {
			remove_case2(node);
		}
	}

	public void remove_case2(RedBlackNode<T> node) {
		
		RedBlackNode<T> parent = node.getParent();
		RedBlackNode<T> sibling = node.getSibling(node);
		
		if (sibling.red == true) {
			parent.red = true;
			sibling.red = false;
			
			if (node == parent.left) {
				parent.rotateLeft();
			} else {
				parent.rotateRight();
			}
			remove_case3(node);
		}	
	}

	public void remove_case3(RedBlackNode<T> node) {
		RedBlackNode<T> parent = node.getParent();
		RedBlackNode<T> sibling = node.getSibling(node);
		
		if ((parent.red == false) && (sibling.red == false) && (sibling.left.red == false) && (sibling.right.red == false)) {
			sibling.red = true;
			remove_case1(parent);
		} else {
			remove_case4(node);
		}
		
	}

	public void remove_case4(RedBlackNode<T> node) {
		RedBlackNode<T> parent = node.getParent();
		RedBlackNode<T> sibling = node.getSibling(node);
		
		if ((parent.red == true) && (sibling.red == false) && (sibling.left.red == false) && (sibling.right.red == false)) {
			sibling.red = true;
			parent.red = false;
		} else {
			remove_case5(node);
		}
		
	}

	public void remove_case5(RedBlackNode<T> node) {
		RedBlackNode<T> parent = node.getParent();
		RedBlackNode<T> sibling = node.getSibling(node);
		
		if (sibling.red == false) {
			if ((node == parent.left) && (sibling.right.red == false) && (sibling.left.red == true)) {
				sibling.red = true;
				sibling.left.red = false;
				sibling.rotateRight();
			} else if ((node == parent.right) && (sibling.left.red == false) && (sibling.right.red == true)) {
				sibling.red = true;
				sibling.right.red = false;
				sibling.rotateLeft();
			}
		}
		remove_case6(node);
	}
	
	public void remove_case6(RedBlackNode<T> node) {
		RedBlackNode<T> parent = node.getParent();
		RedBlackNode<T> sibling = node.getSibling(node);
		
		sibling.red = parent.red;
		parent.red = false;
		
		if (node == parent.left) {
			sibling.right.red = false;
			parent.rotateLeft();
		} else {
			sibling.left.red = false;
			parent.rotateLeft();
		}
	}
	

	public void free(RedBlackNode<T> node) {
		node.left = null;
		node.right = null;
		node.parent = null;
		node.value = null;
	}
	
	public void replace_node() {
		// TODO metodo para node e child trocarem de posicao
	}

}
