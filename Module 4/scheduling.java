import java.util.*;

public class scheduling {

    private static int numVertices = 0;

  	//BFS return true when there is a path from source to sink
    private static boolean BFS(int[][] matrix, int source, int sink, int[] previous){
        boolean passed[] = new boolean[numVertices];
        Arrays.fill(passed,false);
        PriorityQueue<Integer> sq = new PriorityQueue<>(); //source queue
        sq.add(source);
        passed[source] = true;

        previous[source] =- 1;

        while (sq.size() != 0){
            int uVertex = sq.poll();
            for(int vertex = 0; vertex < numVertices; vertex++){
                if(!passed[vertex] && matrix[uVertex][vertex] > 0){
                    if(vertex == sink){
                        previous[vertex] = uVertex;
                        return true;
                    }
                    sq.add(vertex);
                    previous[vertex] = uVertex;
                    passed[vertex] = true;
                }
            }
        }
        return false;
    }

    //Ford Fulkerson to calculate maximum flow
    private static int callFordF(int[][] adjacencyMatrix, int source, int sink){
        int row,column;
        int[][] matrix = adjacencyMatrix.clone();
        int[] previous = new int[numVertices];
        int maximumFlow = 0;
        while (BFS(matrix,source,sink,previous)){

            int flow = Integer.MAX_VALUE;

            for(column = sink;column != source; column = previous[column]){
                row = previous[column];
                flow = Math.min(flow,matrix[row][column]);
            }

            for(column = sink;column != source; column = previous[column]){
                row = previous[column];
                matrix[row][column] -= 1;
                matrix[column][row] += 1;
            }

            maximumFlow += flow;
        }
        return maximumFlow;
    }

    //create student row in adjacent matrix
    private static int[] createStudentRow(int minCourses,String student,Map<String,List<String>> studentMap, List<String> courses){
        int[] studentRow = new int[numVertices];
        studentRow[0] = minCourses;
        studentRow[numVertices-1] = 0;
        for(int i = 1; i <= studentMap.size(); i++){
            studentRow[i] = 0;
        }
        int k = 0;
        for(int j = studentMap.size()+1; j < numVertices-1; j++){

            if (studentMap.get(student).contains(courses.get(k++))) {
                studentRow[j] = 1;
            }
            else {
                studentRow[j] = 0;
            }

        }
        return studentRow;
    }

    private static int[] createCourseRow(int[][] matrix,int currentRowNum,int studentNum,String course,Map<String,Integer> courseMap){
        int[] courseRow = new int[numVertices];
        courseRow[0] = 0;
        courseRow[numVertices-1]=courseMap.get(course);
        for(int i = 1; i<=studentNum; i++){
            courseRow[i] = matrix[i][currentRowNum]; //flipping matrix to obtain the values in constant time
        }
        for(int j = studentNum+1; j < numVertices-1; j++){
            courseRow[j] = 0;
        }
        return courseRow;
    }


    private static int[][] createAdjacencyMatrix(int minCourses,List<String> students,List<String> courses,Map<String,List<String>> studentMap,Map<String,Integer> courseMap){
        int[][] adjacencyMatrix = new int[numVertices][numVertices];

        //create source row in adjacent matrix
        int[] sourceRow = new int[numVertices];
        sourceRow[0] = 0;

        for (int i = 1; i<= studentMap.size(); i++) {
            sourceRow[i] = minCourses;
        }

        for (int i = studentMap.size()+1; i < numVertices; i++) {
            sourceRow[i] = 0;
        }

        adjacencyMatrix[0] = sourceRow;

        //create sink row in adjacent matrix
        int [] sinkRow = new int[numVertices];

        sinkRow[0] = 0;

        for (int i = 1; i <= studentMap.size(); i++) {
            sinkRow[i] = 0;
        }

        int j = 0;

        for (int i = studentMap.size()+1; i < numVertices-1; i++) {
            sinkRow[i] = courseMap.get(courses.get(j));
            j++;
        }

        sinkRow[numVertices-1] = 0;

        adjacencyMatrix[numVertices-1] = sinkRow;
        

        for(int i = 1; i <= students.size(); i++){
            adjacencyMatrix[i] = createStudentRow(minCourses,students.get(i-1),studentMap,courses);
        }

        int k = 0;

        for(int a = students.size()+1; a < numVertices-1; a++){
            adjacencyMatrix[a] = createCourseRow(adjacencyMatrix,a,students.size(),courses.get(k++),courseMap);
        }
        return adjacencyMatrix;
    }
    
    public static void main(String[] args) {
        int registrationRequest = 0;
        int numCourses = 0;
        int minStudentCourses = 0;
        List<String> courses = new ArrayList<>();
        List<String> students = new ArrayList<>();
        Map<String,Integer> courseMap = new HashMap<>();
        Map<String,List<String>> studentMap = new HashMap<>();
        List<String> solutions = new ArrayList<>();
        Scanner keyboard = new Scanner(System.in);

        int i = 0;
        
        while(true){
            
            if(i == 0){
                
                String input = keyboard.nextLine().trim();
                
                if (input.equals("")){
                    continue;
                }

                else if(input.equals("0 0 0")){
                    break;
                }
                int[] values = Arrays.stream(input.trim().split(" ")).mapToInt(Integer::parseInt).toArray();
                registrationRequest = values[0];
                numCourses = values[1];
                minStudentCourses = values[2];
                i++;
                continue;
            }

            for (int a = 0; a < registrationRequest; a++){
                String[] studentDetails = keyboard.nextLine().trim().split(" ");
                if(studentMap.containsKey(studentDetails[0])){
                    studentMap.get(studentDetails[0]).add(studentDetails[1]);
                }

                else{
                    List<String> studentCourse = new ArrayList<>();
                    studentCourse.add(studentDetails[1]);
                    studentMap.put(studentDetails[0],studentCourse);
                    students.add(studentDetails[0]);
                }
            }

            for (int b = 0; b < numCourses; b++){
                String[] courseDetails = keyboard.nextLine().trim().split(" ");
                courses.add(courseDetails[0]);
                courseMap.put(courseDetails[0],Integer.parseInt(courseDetails[1]));
            }

            i = 0;

            numVertices = studentMap.size()+courses.size()+2;
            int[][] adjacencyMatrix = createAdjacencyMatrix(minStudentCourses, students, courses, studentMap, courseMap);
            int maxFlow = callFordF(adjacencyMatrix,0, adjacencyMatrix.length-1);

            String answer = "";
            if (maxFlow == studentMap.size()*minStudentCourses) {
                answer = "Yes";
            }
            else {
                answer = "No";
            }

            solutions.add(answer);
            studentMap.clear();
            students.clear();
            courses.clear();
            courseMap.clear();
        }

        keyboard.close();
        for(String solution:solutions){
            System.out.println(solution);
        }
    }
}