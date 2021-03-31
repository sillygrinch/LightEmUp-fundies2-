import tester.*;

// same structure and testing from previous deque assignment

//Represents a boolean-valued question over values of type T
interface IPred<T> {
  boolean apply(T t);
}

// Checks if A node's data is "Fundies II"
// this is for testing the IPred interface only
class IsFundies2 implements IPred<String> {
  public boolean apply(String t) {
    return t.equals("Fundies II");
  }
}

// Is another proof of concept for IPred
// for testing as well
class IsThe implements IPred<String> {
  public boolean apply(String s) {
    return s.equals("The");
  }
}

// Is a container for a list headed by a sentinel
class Deque<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  Deque(Sentinel<T> s) {
    this.header = s;
  }

  // Computes the number of elements in a deque, excluding the sentinel
  public int size() {
    return this.header.size();
  }

  // Add a node at the head of the list
  public void addAtHead(T data) {
    new Node<T>(data, this.header.next, this.header);
  }

  // Adds a node at the tail of a list
  public void addAtTail(T data) {
    new Node<T>(data, this.header, this.header.prev);
  }

  // Removes the data from the start of the list
  public T removeFromHead() {
    T data = this.header.next.getData();
    this.header.next.remove();
    return data;
  }

  // Removes the data from the end of the list
  public T removeFromTail() {
    T data = this.header.prev.getData();
    this.header.prev.remove();
    return data;
  }

  // Finds the given node
  public ANode<T> find(IPred<T> pred) {
    return this.header.next.find(pred);
  }

  // Removes the given node from the list
  public void removeNode(ANode<T> node) {
    node.remove();
  }
}

// -------------------------------------------------------------

// Holds both a sentinel and a node
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // the size of a sentinel/node
  abstract int sizeHelper();

  // Removes the link of the current node
  public void remove() {
    this.prev.next = this.next;
    this.next.prev = this.prev;
  }

  // Gets the data of the node
  public abstract T getData();

  // Finds the node from the list and if it exists then returns that node
  public abstract ANode<T> find(IPred<T> pred);
}

// -------------------------------------------------------------

// Represents a Sentinel value that marks the beginning and end of the list
class Sentinel<T> extends ANode<T> {

  Sentinel() {
    this.next = this;
    this.prev = this;
  }

  // Calculates the size of a list
  int size() {
    return this.next.sizeHelper();
  }

  // Calculates the size on a sentinel
  int sizeHelper() {
    return 0;
  }

  // If the list is empty (no nodes but just a sentinel), it would return an
  // RuntimeException
  public T getData() {
    throw new RuntimeException("Error 404");
  }

  // Returns a sentinel if it can't find the node
  public ANode<T> find(IPred<T> pred) {
    return this;
  }

}

// -------------------------------------------------------------

// Represents a single node
class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  Node(T data, ANode<T> next, ANode<T> prev) {
    if (next == null || prev == null) {
      throw new IllegalArgumentException("One of the nodes is null");
    }
    else {
      this.data = data;
      this.next = next;
      this.prev = prev;
      this.prev.next = this;
      this.next.prev = this;
    }
  }

  // Calculates the size of this node and every node after that
  public int sizeHelper() {
    return 1 + this.next.sizeHelper();
  }

  // Returns the data of the node
  public T getData() {
    return this.data;
  }

  // Finds the first node which matches the pred
  public ANode<T> find(IPred<T> pred) {
    if (pred.apply(this.data)) {
      return this;
    }

    else {
      return this.next.find(pred);
    }
  }
}

// -------------------------------------------------------------

class ExamplesDeque {
  Deque<String> deque1;
  Deque<String> deque2;
  Deque<String> deque3;
  Deque<String> deque4;
  Deque<String> deque5;
  Deque<String> deque6;
  Sentinel<String> s1;
  Sentinel<String> s2;
  Sentinel<String> s3;
  Sentinel<String> s4;
  Sentinel<String> s5;
  Sentinel<String> s6;
  Node<String> n1;
  Node<String> n2;
  Node<String> n3;
  Node<String> n4;
  Node<String> n5;
  Node<String> n6;
  Node<String> n7;
  Node<String> n8;
  Node<String> n9;
  Node<String> n10;
  Node<String> n11;
  Node<String> n12;
  Node<String> n13;
  Node<String> n14;
  Node<String> n15;
  Node<String> n16;
  Node<String> n17;
  Node<String> n18;
  Node<String> n19;
  Node<String> n20;

  void init() {
    this.deque1 = new Deque<String>();
    this.deque2 = new Deque<String>();
    this.deque3 = new Deque<String>();
    this.deque4 = new Deque<String>();
    this.deque5 = new Deque<String>();
    this.deque6 = new Deque<String>();
    this.s1 = new Sentinel<String>();
    this.s2 = new Sentinel<String>();
    this.s3 = new Sentinel<String>();
    this.s4 = new Sentinel<String>();
    this.s5 = new Sentinel<String>();
    this.s6 = new Sentinel<String>();
    this.n1 = new Node<String>("abc", s1, s1);
    this.n2 = new Node<String>("bcd", s1, n1);
    this.n3 = new Node<String>("cde", s1, n2);
    this.n4 = new Node<String>("def", s1, n3);
    // Given deque on assignment page
    this.deque1.header = s1; 
    // Empty deque
    this.deque3.header = s3;

    this.n5 = new Node<String>("Fundies II", s2, s2);
    this.n6 = new Node<String>("should", s2, n5);
    this.n7 = new Node<String>("be", s2, n6);
    this.n8 = new Node<String>("awesome!", s2, n7);
    // Our own deque example
    this.deque2.header = s2;

    this.n9 = new Node<String>("The", s4, s4);
    this.n10 = new Node<String>("Fundies II", s4, n9);
    this.n11 = new Node<String>("should", s4, n10);
    this.n12 = new Node<String>("be", s4, n11);
    this.n13 = new Node<String>("awesome!", s4, n12);
    // Own deque example, with a node added at head
    this.deque4.header = s4;

    this.n14 = new Node<String>("The", s5, s5);
    this.n15 = new Node<String>("Fundies II", s5, n14);
    this.n16 = new Node<String>("should", s5, n15);
    this.n17 = new Node<String>("be", s5, n16);
    this.n18 = new Node<String>("awesome!", s5, n17);
    this.n19 = new Node<String>("Seriously", s5, n18);
    // Previous example with a node added at the tail
    this.deque5.header = s5;

    this.n20 = new Node<String>("be", s6, s6);
    this.deque6.header = s6;

  }

  void testDeques(Tester t) {
    this.init();
    t.checkExpect(s1.next, n1);
    t.checkExpect(s1.prev, n4);

    t.checkExpect(deque3.header, s3);

    t.checkExpect(s2.next, this.n5);
    t.checkExpect(s2.prev, this.n8);
  }

  void testSize(Tester t) {
    this.init();
    t.checkExpect(deque1.size(), 4);
    t.checkExpect(deque3.size(), 0);
    t.checkExpect(deque2.size(), 4);
  }

  void testSizeHelper(Tester t) {
    this.init();
    t.checkExpect(this.s1.sizeHelper(), 0);
    t.checkExpect(this.n19.sizeHelper(), 1);
    t.checkExpect(this.n14.sizeHelper(), 6);
  }

  void testAdd(Tester t) {
    this.init();
    this.deque1.addAtHead("Well");
    this.deque1.addAtTail("no?");
    this.deque3.addAtHead("yeah, right!");
    this.deque2.addAtHead("The");

    t.checkExpect(this.deque1.size(), 6);
    t.checkExpect(this.deque3.size(), 1);
    t.checkExpect(this.deque2, this.deque4);

    this.deque2.addAtTail("Seriously");

    t.checkExpect(this.deque2, this.deque5);
  }

  void testRemoveHeadAndTail(Tester t) {
    this.init();
    this.deque5.removeFromTail();
    t.checkExpect(this.deque5, this.deque4);
    // Checks if remove the tail worked
    this.deque5.removeFromHead();
    t.checkExpect(this.deque5, this.deque2);
    // Checks if remove the head worked
    this.deque5.removeFromHead();
    this.deque5.removeFromHead();
    this.deque5.removeFromTail();
    this.deque5.removeFromTail();
    t.checkExpect(this.deque5, this.deque3);
    // Checks if the deque is now empty.
    t.checkException(new RuntimeException("Error 404"), this.deque5, "removeFromTail");

  }

  void testFind(Tester t) {
    this.init();
    t.checkExpect(this.deque5.find(new IsFundies2()), this.n15);
    t.checkExpect(this.deque1.find(new IsFundies2()), this.s1);
    t.checkExpect(this.deque5.find(new IsThe()), this.n14);
    t.checkExpect(this.deque1.find(new IsThe()), this.s1);

  }

  void testRemoveNode(Tester t) {
    this.init();
    this.deque5.removeNode(n19);
    t.checkExpect(this.deque5, this.deque4);
    this.deque5.removeNode(n14);
    t.checkExpect(this.deque5, this.deque2);
    this.deque5.removeNode(n15);
    this.deque5.removeNode(n16);
    this.deque5.removeNode(n18);
    t.checkExpect(this.deque5, this.deque6);
    this.deque5.removeNode(n17);
    t.checkExpect(this.deque5, this.deque3);
  }
  
  void testRemove(Tester t) {
    this.init();
    this.n19.remove();
    t.checkExpect(this.deque5, this.deque4);
    this.n14.remove();
    t.checkExpect(this.deque5, this.deque2);
    
  }
  
  void testGetData(Tester t) {
    this.init();
    t.checkExpect(this.n19.getData(), "Seriously");
    t.checkException(new RuntimeException("Error 404"), this.s5, "getData");
  }
}