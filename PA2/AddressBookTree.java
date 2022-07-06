/* Nicolas Gonzalez
 * Dr. Andrew Steinberg
 * COP3503 Summer 2022
 * Programming Assignment 2
 */

class Node<T extends Comparable,U extends Comparable> {
    T name;
    U office;

    int color;
    Node parent;
    Node left;
    Node right;
    boolean isNil;

    // standard Nil node //
    // Pre-conditions: None
    // Post-conditions: Creates a Nil node with no connections anywhere
    Node() {
        name = null;
        office = null;

        color = 0;
        parent = null;
        left = null;
        right = null;
        isNil = true;
    }
    // Nil node with parent connection //
    // Pre-conditions: Needs the parent node
    // Post-conditions: Creates a NIL node as a child to connect to the parent
    Node(Node parent) {
        name = null;
        office = null;

        color = 0;
        this.parent = parent;
        left = null;
        right = null;
        isNil = true;
    }
    // Regular node //
    // Pre-conditions: The key (in this case the name) and the extra information (in this case, the office)
    // Post-condition: Creates a NON-NIL node with the provided information
    Node(T name, U office) {
        this.name = name;
        this.office = office;

        color = 1;
        parent = null;
        left = null;
        right = null;
        isNil = false;
    }
}
public class AddressBookTree<T extends Comparable,U extends Comparable> {
    private Node root;

    // Contractor //
    // Pre-conditions: None
    // Post-conditions: Creates an empty NIL node as the root.
    public AddressBookTree(){
        root = new Node();
    }

    // Getter //
    // Pre-conditions: None
    // Post-conditions: Returns root
    public Node getRoot() { return root; };

    ////////////////////////////////////
    // Functions used for insertions //
    //////////////////////////////////

    // Pre-conditions: The key and extra information (Office) that will be inserted to the RB tree
    // Post-conditions: Inserts the node into the RB tree
    public void insert(T name, U office) {
        Node node = new Node(name, office);

        Node y = new Node();
        Node x = this.root;

        while(!x.isNil) {
            y = x;
            if(node.name.toString().compareTo(x.name.toString()) < 0)
                x = x.left;
            else
                x = x.right;
        }
        node.parent = y;

        if(y.isNil)
            root = node;
        else if(node.name.toString().compareTo(y.name.toString()) < 0)
            y.left = node;
        else
            y.right = node;

        node.left = new Node(node);
        node.right = new Node(node);
        node.color = 1;
        node.parent = y;


        InsertFix(node);

    }
    // Pre-conditions: Needs the node inserted into the correct place (even if not balanced) in the RB tree
    // Post-conditions: Fixes the balancing of the tree. Works recursively going up the tree until a black node is hit or the root.
    public void InsertFix(Node node){

        Node parent = node.parent;

        if(parent.isNil) {
            node.color = 0;
            return;
        }
        if(parent.color == 0) {
            return;
        }

        Node grandparent = parent.parent;

        if(grandparent == null || grandparent.isNil) {
            parent.color = 0;
            return;
        }

        Node uncle = getUncle(parent);

        if(!(uncle.isNil) && uncle.color == 1) {

            parent.color = 0;
            grandparent.color = 1;
            uncle.color = 0;

            InsertFix(grandparent);
        }
        else if(parent == grandparent.left) {
            if(node == parent.right) {
                LeftRotate(parent);
                parent = node;
            }
            RightRotate(grandparent);
            parent.color = 0;
            grandparent.color = 1;
        }
        else {
            if(node == parent.left) {
                RightRotate(parent);
                parent = node;
            }
            LeftRotate(grandparent);
            parent.color = 0;
            grandparent.color = 1;
        }
    }
    // Pre-conditions: The brother root of the uncle we are looking for
    // Post-conditions: Returns the uncle root of the node inserting.
    public Node getUncle(Node node) {

        Node grandparent = node.parent;

        if(grandparent.left == node)
            return grandparent.right;
        else if(grandparent.right == node)
            return grandparent.left;
        else
            throw new IllegalStateException("The parent node is not connected to the grandparent. ERROR!!!");
    }

    ///////////////////////////////////
    // Functions used for deletions //
    /////////////////////////////////

    // Pre-conditions: Key to node needed to delete
    // Post-conditions: Node is deleted from the RB tree
    public void deleteNode(T key){

        Node node = search(key);
        Node y,x;

        if(node == null) {
            System.out.println("Node not found....");
            return;
        }

        y = node;
        int yOriginalColor = y.color;

        if(node.left.isNil) {
            x = node.right;
            RBTransplant(node, node.right);
        }
        else if(node.right.isNil) {
            x = node.left;
            RBTransplant(node,node.left);
        }
        else {
            y = successor(node.right);
            yOriginalColor = y.color;
            x = y.right;

            if(y.parent == node)
                x.parent = y;
            else {
                RBTransplant(y,y.right);
                y.right = node.right;
                y.right.parent = y;
            }
            RBTransplant(node, y);

            y.left = node.left;
            y.left.parent = y;
            y.color = node.color;
        }

        if(yOriginalColor == 0)
            DeleteFix(x);
    }
    // Pre-conditions: The node that is transplanted in for the deleted note
    // Post-conditions: Fixes the tree and maintains the balance of the RB tree
    public void DeleteFix(Node node){

        Node w;

        while(node != root && node.color == 0) {
            if(node == node.parent.left) {
                w = node.parent.right;
                if(w.color == 1) {
                    w.color = 0;
                    node.parent.color = 1;
                    LeftRotate(node.parent);
                    w = node.parent.right;
                }
                if(w.left.color == 0 && w.right.color == 0) {
                    w.color = 1;
                    node = node.parent;
                }
                else {
                    if(w.right.color == 0) {
                        w.left.color = 0;
                        w.color = 1;
                        RightRotate(w);
                        w = node.parent.right;
                    }
                    w.color = node.parent.color;
                    node.parent.color = 0;
                    w.right.color = 0;
                    LeftRotate(node.parent);
                    node = root;
                }
            }
            else {
                w = node.parent.left;
                if(w.color == 1) {
                    w.color = 0;
                    node.parent.color = 1;
                    LeftRotate(node.parent);
                    w = node.parent.left;
                }
                if(w.left.color == 0 && w.right.color == 0) {
                    w.color = 1;
                    node = node.parent;
                }
                else {
                    if(w.left.color == 0) {
                        w.right.color = 0;
                        w.color = 1;
                        LeftRotate(w);
                        w = node.parent.left;
                    }
                    w.color = node.parent.color;
                    node.parent.color = 0;
                    w.left.color = 0;
                    RightRotate(node.parent);
                    node = root;
                }
            }
        }
        node.color = 0;
    }
    // Pre-conditions: The node to the right of the node we want to delete
    // Post-conditions: Finds the successor of the node we want to delete
    public Node successor(Node node) {

        Node min = node;

        while(!node.isNil) {
            min = node;
            node = node.left;
        }
        return min;
    }
    // Pre-conditions: Two nodes that are going to be transplanted
    // Post-conditions: Transplants the nodes
    public void RBTransplant(Node u, Node v) {

        if(u.parent.isNil)
            this.root = v;
        else if(u == u.parent.left)
            u.parent.left = v;
        else
            u.parent.right = v;

        v.parent = u.parent;
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // General functions that can be used for both deletion, insertion, and other..... //
    ////////////////////////////////////////////////////////////////////////////////////

    // Pre-condition: Key to search
    // Post-condition: Returns the node that contains the key, if not found, returns null
    public Node search(T key) {

        Node temp = this.root;
        Node found = null;

        while(!temp.isNil) {
            if (key.toString().compareTo(temp.name.toString()) == 0) {
                found = temp;
                break;
            }
            else if(key.toString().compareTo(temp.name.toString()) < 0)
                temp = temp.left;
            else
                temp = temp.right;
        }
        return found;
    }
    // Pre-conditions: Node at which the rotation pivot will occur
    // Post-condition: The node will rotate left at the pivot node location
    public void LeftRotate(Node node){

        Node rightChild = node.right;
        node.right = rightChild.left;

        if(rightChild.left != null)
            rightChild.left.parent = node;

        rightChild.parent = node.parent;

        if(node.parent == null || node.parent.isNil)
            this.root = rightChild;
        else if(node == node.parent.left)
            node.parent.left = rightChild;
        else
            node.parent.right = rightChild;

        rightChild.left = node;
        node.parent = rightChild;
    }
    // Pre-conditions: Node at which the rotation pivot will occur
    // Post-condition: The node will rotate right at the pivot node location
    public void RightRotate(Node node){

        Node leftChild = node.left;
        node.left = leftChild.right;

        if(leftChild.right != null)
            leftChild.right.parent = node;

        leftChild.parent = node.parent;

        if(node.parent == null || node.parent.isNil)
            this.root = leftChild;
        else if(node == node.parent.right)
            node.parent.right = leftChild;
        else
            node.parent.left = leftChild;

        leftChild.right = node;
        node.parent = leftChild;
    }

    //////////////////////////////////////////////////////////////
    // General functions for displaying and counting red/black //
    ////////////////////////////////////////////////////////////

    // Pre-condition: None
    // Post-condition: Displays the RB tree inorder
    public void display() { DisplayRecursively(root); }
    // Pre-condition: Root node
    // Post-condition: Recursively prints the RB tree inorder
    public void DisplayRecursively(Node temp) {

        if(!temp.isNil) {
            DisplayRecursively(temp.left);
            System.out.println(temp.name + " " + temp.office);
            DisplayRecursively(temp.right);
        }
    }
    // Pre-conditions: Node to begin count from (Usually the root node)
    // Post-condition: Recursively counts the black nodes from the start position and down.
    public int countBlack(Node temp) {

        int count = 0;
        if(temp.isNil)
            return 0;

        count += countBlack(temp.left) + countBlack(temp.right);

        if(temp.color == 0)
            count++;

        return count;
    }
    // Pre-conditions: Node to begin count from (Usually the root node)
    // Post-condition: Recursively counts the red nodes from the start position and down.
    public int countRed(Node temp) {

        int count = 0;
        if(temp.isNil)
            return 0;

        count += countRed(temp.left) + countRed(temp.right);

        if(temp.color == 1)
            count++;

        return count;
    }
}
