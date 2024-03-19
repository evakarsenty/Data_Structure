import java.io.DataOutputStream;
import java.io.IOException;

class RootedTree {

    private boolean IsLast;
    public GraphNode Root ;

    public RootedTree(){
        this.IsLast = true;
    }
    public void setRoot(GraphNode root){
        this.Root = root;
        root.setExtreme();
    }

    public void printByLayer (DataOutputStream out) throws IOException{
        try {
            int max = 0;
            boolean bool = true;
            if(Root.getKey() != 0){
                ChangeColor(Root);
                if(Root.OutEdge.front != null){
                    max = maxDepth(Root)+1;
                    bool = false;
                }
                else max = FindLevel(Root)+1;
                ChangeColor(Root);
            }
            else{
                Scc_ChangeColor(Root);
                max = maxDepthScc(Root);
                Scc_ChangeColor(Root);
            }
            for (int i = 1; i <= max; i++) {
                if(Root.getKey() == 0)
                    printLevelScc(out, Root, i);
                else
                    printLevel(out, Root, i, bool);
                if (i != max) {
                    IsLast = true;
                    out.writeBytes("\n");
                }
            }
        } catch (NullPointerException e) {return;}
    }

    public void preorderPrint(DataOutputStream out) throws IOException {
        if (Root.getKey() != 0){
            ChangeColor(Root);
            boolean bool = true;
            if(!Root.OutEdge.IsEmpty()){
                if(Root.OutEdge.front.value.Node_To.getDistance() > 0){
                    bool = false;
                }
            }
            RecursionPreorder(out, Root, bool);
        }
        else {
            Scc_ChangeColor(Root);
            RecursionPreorderScc(out, Root);
            GraphEdge child = Root.InEdge.dequeue();
            while(child != null){
                child.Isdeleted = true;
                child = Root.InEdge.dequeue();
            }
            Root.IsDeleted = true;
        }
    }

    public void RecursionPreorder (DataOutputStream out, GraphNode n, boolean bool) throws IOException {
        if ( n == null || n.getColor() > 2)
            return;
        else {
            if ((n.getInDegree() == 0 && n.isExtremeLeft) || (n.getParent() == null && n.isExtremeLeft)) {
                out.writeBytes(String.valueOf(n.getKey()));
            }
            else {
                out.writeBytes(",");
                out.writeBytes(String.valueOf(n.getKey()));
                n.setColor(3);
            }
            try{
                int length = n.OutEdge.getSize();
                if(!bool)
                    QueueList.QueueReverse(n.OutEdge);
                for(int i = 0; i< length; i++){
                    GraphEdge edge = n.OutEdge.dequeue();
                    n.OutEdge.enqueue(edge);
                    if(n == edge.Node_To.getParent()) {
                        RecursionPreorder(out, edge.Node_To, bool);
                    }
                }
                if(!bool)
                    QueueList.QueueReverse(n.OutEdge);
            }
            catch (NullPointerException e) {return;}
        }
    }

    public void RecursionPreorderScc (DataOutputStream out, GraphNode n) throws IOException {
        if ( n == null || n.getColor() > 1)
            return;
        else {
            if(n.getKey() == 0)
                out.writeBytes(String.valueOf(n.getKey()));
            else {
                out.writeBytes(",");
                out.writeBytes(String.valueOf(n.getKey()));
                n.setColor(3);
            }
            try{
                if(n.getKey() != 0)
                    QueueList.QueueReverse(n.InEdge);
                int len = n.InEdge.getSize();
                int i = 0;
                while (i< len){
                    GraphEdge edge = n.InEdge.dequeue();
                    n.InEdge.enqueue(edge);
                    if(n == edge.Node_From.getParent()) {
                        RecursionPreorderScc(out, edge.Node_From);
                    }
                    i++;
                }
                if(n.getKey() != 0)
                    QueueList.QueueReverse(n.InEdge);
            }
            catch (NullPointerException e) {return;}
        }
    }

    protected void ChangeColor(GraphNode n){
        if (n.getColor() == 1 && n.getKey() != 0)
            return;
        try{
            n.setColor(1);
            int len = n.OutEdge.getSize();
            int i = 0;
            while (i< len){
                GraphEdge edge = n.OutEdge.dequeue();
                if(!edge.Isdeleted) {
                    n.OutEdge.enqueue(edge);
                    ChangeColor(edge.Node_To);
                }
                i++;
            }
        }
        catch (NullPointerException e) {return;}
    }

    protected void Scc_ChangeColor(GraphNode n){
        if (n.getColor() == 1 && n.getKey() != 0)
            return;
        try{
            n.setColor(1);
            int length = n.InEdge.getSize();

            for (int i = 0; i< length; i++){
                GraphEdge edge = n.InEdge.dequeue();
                if(!edge.Isdeleted) {
                    n.InEdge.enqueue(edge);
                    Scc_ChangeColor(edge.Node_From);
                }
            }
        }
        catch (NullPointerException e) {return;}
    }

    private void printLevel(DataOutputStream out, GraphNode root, int level, boolean bool)throws IOException{
        if (root == null)
            return;
        if (level == 1) {
            if(root.getColor() != 2){
                if(!IsLast) {
                    out.writeBytes(",");
                }
                else IsLast = false;
                out.writeBytes(String.valueOf(root.getKey()));
            }
            root.setColor(2);
        }
        else if (level > 1)
        {
            try {
                if (!root.OutEdge.IsEmpty()) {
                    int len = root.OutEdge.getSize();
                    if(!bool){
                        QueueList.QueueReverse(root.OutEdge);
                    }
                    int i = 0;
                    while (i < len){
                        GraphEdge edge = root.OutEdge.dequeue();
                        root.OutEdge.enqueue(edge);
                        if(edge.Node_To.getColor() < 2 && level == 2){
                            printLevel(out, edge.Node_To, level - 1,bool);
                        }
                        if(level > 2){
                            printLevel(out, edge.Node_To, level - 1, bool);
                        }
                        i++;
                    }
                    if(!bool){
                        QueueList.QueueReverse(root.OutEdge);
                    }
                }
            }
            catch (NullPointerException e) {return;}
        }
    }

    private void printLevelScc(DataOutputStream out, GraphNode root, int level) throws IOException{
        if (root == null)
            return;
        if (level == 1) {
            if(root.getColor() != 2){
                if(!IsLast) {
                    out.writeBytes(",");
                }
                else IsLast = false;
                out.writeBytes(String.valueOf(root.getKey()));
                root.setColor(2);
            }
        }
        else if (level > 1)
        {
            try {
                if (!root.InEdge.IsEmpty()) {
                    if(root.getKey() != 0){
                        QueueList.QueueReverse(root.InEdge);
                    }
                    int len = root.InEdge.getSize();
                    QueueList.QueueListNode node = root.InEdge.getFront();
                    int i = 0;
                    while (i < len){
                        GraphEdge edge = (GraphEdge) node.value;
                        if(edge.Node_From.getParent() == root && !edge.Isdeleted){
                            printLevelScc(out, edge.Node_From, level - 1);
                        }
                        node = node.next;
                        i++;
                    }
                    if(root.getKey() != 0)
                        QueueList.QueueReverse(root.InEdge);
                }
            }
            catch (NullPointerException e) {return;}
        }
    }

    private int maxDepth(GraphNode n){
        int depth = (int) n.getDistance();
        int length = n.OutEdge.getSize();
        QueueList.QueueListNode node = n.OutEdge.getFront();
        int i = 0;
        while (i< length){
            GraphEdge edge = (GraphEdge) node.value;
            if(!edge.Isdeleted && !edge.Node_To.IsDeleted){
                int temp = 0;
                if(n.getDistance() < edge.Node_To.getDistance()) {
                    temp = maxDepth(edge.Node_To);
                }
                depth = Math.max(depth, temp);
            }
            node = node.next;
            i++;
        }
        return depth;
    }

    private int maxDepthScc(GraphNode n) {
        int depth = 0;
        if (n.InEdge.IsEmpty() || n.getColor()==2 || n.getColor()==3)
            return depth;
        else {
            int len = n.InEdge.getSize();
            n.setColor(2);
            for (int i = 0; i< len; i++){
                GraphEdge edge = n.InEdge.dequeue();
                n.InEdge.enqueue(edge);
                if(!edge.Isdeleted) {
                    if(edge.Node_From.getParent() == n){
                        depth = Math.max(depth, this.maxDepthScc(edge.Node_From));
                    }
                }
            }
            return depth+1;
        }
    }

    private int FindLevel (GraphNode n){
        int len = n.OutEdge.getSize();
        QueueList.QueueListNode node = n.OutEdge.getFront();
        int temp = 0;
        for (int i = 0; i< len; i++){
            GraphEdge edge = (GraphEdge) node.value;
            if(!edge.Isdeleted && !edge.Node_To.IsDeleted){
                temp = Math.max(temp, FindLevel(edge.Node_To)+1);
            }
            node = node.next;
        }
        return temp;
    }
}