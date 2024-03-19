public class DynamicGraph {

    private int time;
    public GraphNode Root;
    private QueueList<GraphNode> QNodes;
    private QueueList<GraphEdge> QEdges;

    public DynamicGraph() {
        QNodes = null;
        QEdges = null;
        this.Root = null;
        this.time = 0;
    }

    public GraphNode insertNode(int nodeKey) {
        GraphNode New_node = new GraphNode(nodeKey);
        if (Root == null) {
            Root = New_node;
            QNodes = new QueueList<GraphNode>();
            QEdges = new QueueList<GraphEdge>();
        }
        New_node.IsDeleted = false;
        QNodes.enqueue(New_node);
        return New_node;
    }

    public void deleteNode(GraphNode node) {
        if (node.getOutDegree() == 0 && node.getInDegree() == 0) {
            node.IsDeleted = true;
        }
    }

    public GraphEdge insertEdge(GraphNode from, GraphNode to) {
        GraphEdge New_Edge = new GraphEdge(from, to);
        QEdges.enqueue(New_Edge);
        New_Edge.Isdeleted = false;
        from.increaseOutDegree();
        to.increaseInDegree();
        return New_Edge;
    }

    public void deleteEdge(GraphEdge edge) {
        edge.Isdeleted = true;
        edge.Node_From.decreaseOutDegree();
        edge.Node_To.decreaseInDegree();
    }

    public RootedTree scc() {
        QueueList<GraphNode> S = new QueueList<>();
        QueueList<GraphNode> NCopy = new QueueList<>();
        QueueList<GraphNode> NodesCopy = new QueueList<>();
        int len = QNodes.Qsize;
        for (int i = 0; i < len; i++) {
            GraphNode t = QNodes.dequeue();
            NCopy.enqueue(t);
            QNodes.enqueue(t);
        }
        DFS(S, NCopy, true);
        int SLength = S.getSize();
        for (int i = 0; i < SLength; i++) {
            GraphNode temp = S.pop();
            if (temp.IsDeleted)
                continue;
            NCopy.enqueue(temp);
            NodesCopy.enqueue(temp);
        }
        QueueList.QueueReverse(NCopy);
        DFS(S, NCopy, false);
        RootedTree RootTree = new RootedTree();
        GraphNode node = new GraphNode(0);
        RootTree.setRoot(node);
        for (int i = 0; i < SLength; i++) {
            GraphNode n = NodesCopy.dequeue();
            if (n.getParent() == null) {
                n.setParent(node);
                new GraphEdge(n, node);
            }
        }
        return RootTree;
    }

    // dfs algorithm like we saw in Lecture 4
    private void DFS (QueueList<GraphNode> Qn, QueueList<GraphNode> QVertices, boolean First) {
        int Vlength = QVertices.getSize();
        QueueList.QueueReverse(QVertices);
        for(int i =0; i< Vlength; i++){
            GraphNode head = QVertices.dequeue();
            if (!head.IsDeleted) {
                head.setColor(1);
                head.setDistance(1);
                head.setParent(null);
                QVertices.enqueue(head);
            }
        }
        Vlength = QVertices.getSize();
        this.time = 0;
        for(int i =0; i< Vlength; i++) {
            GraphNode head = QVertices.dequeue();
            if (head.getColor() == 1) {
                DFS_Visit(head, Qn, First);
            }
        }
    }

    private void DFS_Visit(GraphNode u, QueueList<GraphNode> Qn, boolean First) {
        int length;
        this.time ++;
        u.distance = time;
        u.color = 2;
        QueueList.QueueListNode node;
        if (First) {
            length = u.OutEdge.getSize();
            QueueList.QueueReverse(u.OutEdge);
            node = u.OutEdge.getFront();
        } else {
            length = u.InEdge.getSize();
            QueueList.QueueReverse(u.InEdge);
            node = u.InEdge.getFront();
        }
        for(int i = 0; i < length; i++) {
            GraphEdge child = (GraphEdge) node.value;
            if (!child.Isdeleted) {
                if (First) {
                    if (child.Node_To.getColor() < 2) {
                        child.Node_To.setParent(u);
                        DFS_Visit(child.Node_To, Qn, true);
                    }
                } else {
                    if (child.Node_From.getColor() < 2) {
                        child.Node_From.setParent(u);
                        DFS_Visit(child.Node_From, Qn, First);
                    }
                }
            }
            node = node.next;
        }
        u.setColor(3);
        if (First){
            QueueList.QueueReverse(u.OutEdge);
        }
        else{
            QueueList.QueueReverse(u.InEdge);
        }
        this.time ++ ;
        u.RetractionTime = time;
        Qn.push(u);
    }

    public RootedTree bfs(GraphNode source) {
        // bfs algorithm like we saw in Lecture 4
        QueueList<GraphNode> Q = new QueueList();
        BFS_Initialization(source, Q);
        while (!Q.IsEmpty()) {
            GraphNode u = Q.dequeue();
            if (!u.IsDeleted) {
                QueueList<GraphEdge> children = u.OutEdge;
                QueueList.QueueReverse(children);
                int length = children.Qsize;
                for (int i = 0; i < length; i++) {
                    GraphEdge child = children.dequeue();
                    children.enqueue(child);
                    if ((child.Node_To.getColor() < 2) && (!child.Isdeleted)) {
                        child.Node_To.color = 2;
                        child.Node_To.distance = u.getDistance() + 1;
                        child.Node_To.parent = u;
                        Q.enqueue(child.Node_To);
                    }
                }
                u.color = 3;
                QueueList.QueueReverse(children);
            }
        }
        RootedTree RootTree = new RootedTree();
        RootTree.setRoot(source);
        RootTree.ChangeColor(source);
        return RootTree;
    }

    private void BFS_Initialization(GraphNode source, QueueList<GraphNode> Q) {
        // Initialize bfs algorithm like we saw in Lecture 4
        int Nlenght = QNodes.getSize();
        for (int i = 0; i < Nlenght; i++) {
            GraphNode v = QNodes.dequeue();
            QNodes.enqueue(v);
            if (!v.IsDeleted) {
                if (v != source) {
                    v.color = 1;
                    v.distance = Double.POSITIVE_INFINITY;
                    v.setParent(null);
                }
            }
        }
        source.color = 2;
        source.distance = 0;
        source.setParent(null);
        Q.QueueCleaner();
        Q.enqueue(source);
    }
}



