public class GraphNode {

    public int ID;
    public int Nb_In;
    public int Nb_Out;
    public GraphNode parent;
    public int RetractionTime;
    public int color;
    // 1 == white
    // 2 == gray
    // 3 == black
    protected double distance;
    public boolean IsDeleted;
    public QueueList<GraphEdge> InEdge;
    public QueueList<GraphEdge> OutEdge;
    public boolean isExtremeLeft;

    public GraphNode (int id) {
        this.ID = id;
        this.parent = null;
        this.InEdge = new QueueList<>();
        this.OutEdge = new QueueList<>();

    }
    public void setParent(GraphNode parent){
        this.parent = parent;
    }
    public GraphNode getParent() {
        return this.parent;
    }
    public void setExtreme() {
        this.isExtremeLeft = true;
    }
    public void setDistance(double distance){
        this.distance = distance;
    }
    public double getDistance() {
        return this.distance;
    }
    public void setColor(int color){
        this.color = color;
    }
    public int getColor() {
        return this.color;
    }
    public int getOutDegree(){
        return this.Nb_Out;
    }
    public int getInDegree(){
        return this.Nb_In;
    }
    public int getKey(){
        return this.ID;
    }
    public void increaseInDegree() {this.Nb_In ++; }
    public void increaseOutDegree() {this.Nb_Out ++; }
    public void decreaseInDegree() {this.Nb_In--; }
    public void decreaseOutDegree() {this.Nb_Out--; }
}

