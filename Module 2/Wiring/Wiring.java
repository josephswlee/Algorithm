import java.util.*;
import java.io.*; 

public class Wiring {
    
    //important for find union
    static class Segment {
        String parent; //storing the name of parent
        int rank; //to track the parent (0 is always the parent)

        //consider creating segments in main
        //constructor 
        public Segment(String parent, int rank) {
            this.parent = parent;
            this.rank = rank;
        }
    }

    //need to implement comparable to use comapreTo
    static class Edge implements Comparable<Edge> {
        String source; //j1 b1 5 then j1 is source
        String destination; //b1 is destination
        int weight; //5 is weight
        boolean valid=false; 

        //constructor 
        public Edge(String source, String destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        //weight - new weight
        public int compareTo(Edge compareEdg) {
            return this.weight-compareEdg.weight;
        }
    }

    static class Graph {
        int vertices;
        int edges;
        List<Edge> edgeList = new ArrayList<>();
        List<String> vertexLabels = new ArrayList<>();

        public Graph(int vertices, int edges) {
            this.vertices = vertices;
            this.edges = edges;
        }
    
        private String find(Map<String, Segment> segments, String label){
            if(!segments.get(label).parent.equals(label)){
                segments.get(label).parent = find(segments,segments.get(label).parent);
            }
            return segments.get(label).parent;
        }
    
        private void union(Map<String, Segment> segments, String segmentLabel, String segmentLabel2){
            String parent1Label = find(segments,segmentLabel);
            String parent2Label = find(segments,segmentLabel2);
    
            if(segments.get(parent1Label).rank < segments.get(parent2Label).rank){
                segments.get(parent1Label).parent = parent2Label;
            }else if(segments.get(parent1Label).rank > segments.get(parent2Label).rank){
                segments.get(parent2Label).parent = parent1Label;
            }else{
                segments.get(parent2Label).parent = parent1Label;
                segments.get(parent1Label).rank+=1;
            }
        }
    
        private Map<String,Segment> makeSet(){
            Map<String,Segment> segmentMap = new HashMap<>();
            for(int i=0;i<vertexLabels.size();i++){
                Segment segment = new Segment(vertexLabels.get(i),0);
                segmentMap.put(vertexLabels.get(i),segment);
            }
    
            return segmentMap;
        }

        public int kruskalMST(List<Edge> newEdges, int newVertices, Map<String,String> labelItemMap){
            //create a list of egdes to form the minimum spanning tree
            int sum = 0;
            List<Edge> qualifiedEdges = new ArrayList<>();
            //sort the edges in ascending order of weight
            //Collections.sort(edgeList);
            Collections.sort(newEdges);
            // create the segment map
            Map<String,Segment> segmentMap = makeSet();
            // i is the edges and j is vertices
            int i=0,j=0;
            //System.out.println("my baby "+newEdges.get(newEdges.size()-1).source+" "+newEdges.get(newEdges.size()-1).destination);
            while (j < newVertices-1 && i < newEdges.size()){
            //while (i < newEdges.size()){
                //pick and ege
                Edge selectedEdge = newEdges.get(i);
                //check if it a valid edge
                if(selectedEdge.valid){
                    // utilize union find to the the parent vertices
                    String parent1Label = find(segmentMap,selectedEdge.source);
                    String parent2Label = find(segmentMap,selectedEdge.destination);
                    if(!parent1Label.equals(parent2Label)){
                        //if the parent are not the same then add the edge to the list of qualified edges

                        
                            qualifiedEdges.add(selectedEdge);
                            //System.out.println("Valid edge: "+selectedEdge.source+"->"+selectedEdge.destination+" "+selectedEdge.weight);
                            //perform a union
                            union(segmentMap,parent1Label,parent2Label);
                            j = j + 1;
                            sum = sum + selectedEdge.weight;
                    }
                }
                i = i + 1;
            }
        
        return sum;
        }
    }

    //not valid cases
   
    private static boolean validateLink(Map<String,String> map,String source,String destination){
        if (!(map.get(source).equals("breaker") && map.get(destination).equals("light")) &&
            !(map.get(source).equals("outlet") && map.get(destination).equals("light")) &&
            !(map.get(source).equals("box") && map.get(destination).equals("light")) &&
            !(map.get(source).equals("light") && map.get(destination).equals("box")) &&
            !(map.get(source).equals("light") && map.get(destination).equals("breaker")) &&
            !(map.get(source).equals("light") && map.get(destination).equals("outlet")) &&
            !(map.get(source).equals("breaker") && map.get(destination).equals("breaker")) &&
            !(map.get(source).equals("switch") && map.get(destination).equals("switch")) 
            ) 
            {return true;}

        else {return false;}

    }


    private static int sumSwitch(PriorityQueue<Edge> connections, Map<String,String> labelItemMap) {
        List<String> processed = new ArrayList<>();
        int i = 0;
        int sum = 0;
        int size = connections.size();
        while (i < size) {
            Edge e = connections.remove();
            if (!processed.contains(e.source) && !processed.contains(e.destination)) {
                if (labelItemMap.get(e.source).equals("switch")) { 
                    processed.add(e.source);
                }
                else if (labelItemMap.get(e.destination).equals("switch")) {
                    processed.add(e.destination);
                }
                sum+=e.weight;
            }
            i++;
        }
        return sum;
    }
    public static void main(String[] args) {
        int i=0;
        Scanner scanner = new Scanner(System.in);
      
        Map<String,String> labelItemMap = new HashMap<>(); 
        Map<String,String> lightSwitchMap = new HashMap<>();
        List<Edge> lightSwitchEdges = new ArrayList<>();
        List<Edge> noSwitchEdges = new ArrayList<>();


        PriorityQueue<Edge> switchStuff = new PriorityQueue<>();

        List<String> switches = new ArrayList<>();
        List<String> stuff = new ArrayList<>();

        int verticesNoSwitch = 0;
        int verticesLightSwitch = 0;


        Graph graph = null;
        while (scanner.hasNextLine()){
            if(i==0){
                //read in the vertices and edge limits
                String input = scanner.nextLine().trim();
                String[] split = input.split(" ");
                int vertices = Integer.parseInt(split[0]);
                int edges = Integer.parseInt(split[1]);
                graph = new Graph(vertices,edges);
                i++;
            }else{
                String input="";
                String[] split = null;
                String curSwitch = "";
                String prev_type = "";
                //get each vertex label and name
                for(int j=0;j<graph.vertices;j++){
                    input = scanner.nextLine().trim();
                    split = input.split(" ");
                    graph.vertexLabels.add(split[0]);
                    labelItemMap.put(split[0], split[1]);
                    i++;

                    if (split[1].equals("switch")) {
                        //key == label of light , value == label of parent switch
                        prev_type = "switch";
                        curSwitch = split[0];
                        switches.add(split[0]);
                    }
                    else if (split[1].equals("light") && prev_type.equals("switch")) {
                        lightSwitchMap.put(split[0],curSwitch);
                    }
                    else if (split[1].equals("light") && prev_type.equals("light")) {
                        lightSwitchMap.put(split[0],curSwitch);
                    }

                    if (split[1].equals("switch") || split[1].equals("light")) {
                        verticesLightSwitch++;
                    }


                    if (!(split[1].equals("light"))) {
                        if (!(split[1].equals("switch"))) {
                            verticesNoSwitch++;
                            stuff.add(split[0]);
                        }
                    }

                }
               
                //read in the connections and their related weights as edges
                for(int k=0;k<graph.edges;k++){
                    input = scanner.nextLine().trim();
                    split = input.split(" ");
                    String source = split[0];
                    String destination = split[1];
                    int weight = Integer.parseInt(split[2]);
                    Edge edge = new Edge(source,destination,weight);
                    edge.valid = validateLink(labelItemMap,source,destination);
                    graph.edgeList.add(edge);


                    //check whether light-switch, light-light, switch-light is valid
                    if (labelItemMap.get(source).equals("light") && labelItemMap.get(destination).equals("light")) {
                        if (!lightSwitchMap.get(source).equals(lightSwitchMap.get(destination))) {
                            edge.valid = false;
                        }
                        lightSwitchEdges.add(edge);
                    }
                    else if (labelItemMap.get(source).equals("switch") && labelItemMap.get(destination).equals("light")) {
                        if (!lightSwitchMap.get(destination).equals(source)) {
                            edge.valid = false;

                        }
                        lightSwitchEdges.add(edge);

                    }
                    else if (labelItemMap.get(source).equals("light") && labelItemMap.get(destination).equals("switch")) {
                        if (!lightSwitchMap.get(source).equals(destination)) {
                            edge.valid = false; 
                        } 
                        lightSwitchEdges.add(edge);
                    }

                        
                    if (!(labelItemMap.get(source).equals("light") || labelItemMap.get(destination).equals("light") )){
                        //System.out.println("this edge: "+edge.source+ " "+edge.destination);
                        if (!(labelItemMap.get(source).equals("switch") || labelItemMap.get(destination).equals("switch") )){
                            noSwitchEdges.add(edge);
                        }
                    }


                    if (labelItemMap.get(edge.source).equals("switch") && !labelItemMap.get(edge.destination).equals("light")) {
                        if (edge.valid) {
                            switchStuff.add(edge);
                        }
                    }
                    else if (labelItemMap.get(edge.destination).equals("switch") && !labelItemMap.get(edge.source).equals("light")) {
                        if (edge.valid) {
                            switchStuff.add(edge);
                        }
                    }
                    i++;                     
                }

            }

            if(i >= graph.edges+graph.vertices)break;

        }

        //close the scanner
        scanner.close();
        assert graph != null; 
        //print the wiring weight
        int a = graph.kruskalMST(noSwitchEdges,verticesNoSwitch,labelItemMap);
        //System.out.println("from breaker outlet box switch " +a);
        int b = graph.kruskalMST(lightSwitchEdges,verticesLightSwitch,labelItemMap);
        int c = sumSwitch(switchStuff, labelItemMap);
        System.out.println(a+b+c);
    }
    
}