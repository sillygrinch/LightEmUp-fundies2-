class Queue<T> {
  Deque<T> contents;
  
  Queue() {
    this.contents = new Deque<>();
  }
  
  //is Queue empty
  boolean isEmpty() {
    return this.contents.size() == 0;
  }
  
  //removes an item
  T remove() {
    return this.contents.removeFromHead();
  }
  
  //adds an item
  void add(T item) {
    this.contents.addAtTail(item);
  }
}
