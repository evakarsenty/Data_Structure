class QueueList<T> {

    protected int Qsize;
    protected QueueListNode<T> front;
    protected QueueListNode<T> rear;

    protected class QueueListNode<T> {
        T value;
        QueueListNode<T> next;
    }
    public QueueList() {
        this.front = null;
        this.rear = null;
        Qsize = 0;
    }
    public boolean IsEmpty() {
        return (Qsize == 0);
    }
    public int getSize() {
        return Qsize;
    }
    public QueueListNode<T> getFront () throws NullPointerException {
        try {
            return this.front;
        }
        catch (NullPointerException e) { return null;}
    }

    public QueueListNode<T> enqueue(T value) {
        QueueListNode<T> oldRear = rear;
        rear = new QueueListNode();
        rear.value = value;
        rear.next = null;
        if (IsEmpty()) {
            front = rear;
        } else {
            oldRear.next = rear;
        }
        Qsize++;
        return oldRear;
    }

    public T dequeue() {
        try {
            // Store previous front and move front one node ahead
            T value = front.value;
            front = front.next;
            Qsize--;
            // If front becomes NULL, then change rear also as NULL
            if (IsEmpty())
                rear = null;
            return value;
        } catch (NullPointerException E) { return null; }
    }

    public void QueueCleaner() {
        front = null;
        rear = null;
        Qsize = 0;
    }

    public void push(T data){
        QueueList.QueueListNode node = new QueueList.QueueListNode();
        node.value = data;
        node.next = front;
        if(front == null) {
            this.rear = node;
        }
        this.front = node;
        this.Qsize++;
    }

    public T pop(){
        if (front != null){
            QueueListNode current = front;
            this.front = this.front.next;
            this.Qsize --;
            if(this.Qsize == 0)
                this.rear = null;
            return (T)current.value;
        }
        return null;
    }

    public static void QueueReverse (QueueList Q) {
        QueueList q = new QueueList();
        int length = Q.getSize();
        for (int i= 0; i < length; i++){
            q.push(Q.dequeue());
        }
        for (int i= 0; i < length; i++){
            Q.enqueue(q.pop());
        }
    }
}

