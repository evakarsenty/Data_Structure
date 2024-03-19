public class GraphEdge {

    public GraphNode Node_From;
    public GraphNode Node_To;
    public boolean Isdeleted;
    public boolean hasAParent;

    public GraphEdge(GraphNode origin, GraphNode destination){
        this.Node_From = origin;
        this.Node_To = destination;
        QueueUpdate(origin, destination);
        if(origin.isExtremeLeft && hasAParent) {
            destination.setExtreme();
        }
    }

    private void QueueUpdate(GraphNode origin, GraphNode destination)throws NullPointerException{
        origin.OutEdge.enqueue(this);
        destination.InEdge.enqueue(this);
    }
}

