import java.util.*;

public class drainage {
    
    
    public static int longestPath(int[][] dp, int x, int y, Map<String, Integer> elevation) {
        

        String key = x + String.valueOf(y);
       

        int[] pathLength = new int[]{1, 1, 1, 1};
        //go up
        if (x-1 > 0 && dp[x - 1][y] < dp[x][y]) {
            pathLength[0] += longestPath(dp, x - 1, y, elevation);
        }
        //go down
        if (x+1 < dp.length - 1 && dp[x + 1][y] < dp[x][y]) {
            pathLength[1] += longestPath(dp, x + 1, y, elevation);
        }
        //go right
        if (y+1 < dp[0].length - 1 && dp[x][y + 1] < dp[x][y]) {
            pathLength[2] += longestPath(dp, x, y + 1, elevation);
        }
        //go left

        if (y-1 > 0 && dp[x][y - 1] < dp[x][y]) {
            pathLength[3] += longestPath(dp, x, y - 1, elevation);
        }

        Arrays.sort(pathLength);

        elevation.put(key, pathLength[3]);

        return pathLength[3];

    }

   
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int testCases = 0;

        List<String> results = new ArrayList<>();

        while (scanner.hasNextLine()){

            if(testCases==0){

                testCases = Integer.parseInt(scanner.nextLine());

            }

            else{

                String[] sections = scanner.nextLine().trim().split(" ");

                String name =sections[0];

                int rows = Integer.parseInt(sections[1]);

                int columns = Integer.parseInt(sections[2]);

                int dp[][] = new int[rows][columns];


                for(int i=0;i<rows;i++){
                    String[] rowValues = scanner.nextLine().trim().split(" ");
                    for(int j=0;j<columns;j++){
                        dp[i][j] = Integer.parseInt(rowValues[j]);
                    }
                }

                Map<String, Integer> myMap = new HashMap<>();

                for (int i = 0; i < dp.length; i++) {
                    for (int j = 0; j < dp[0].length; j++) {

                        longestPath(dp, i, j, myMap);
                    }
                }
                List<Integer> sortable = new ArrayList<>(myMap.values());

                Collections.sort(sortable);

                results.add(String.format("%s: %d",name,sortable.get(sortable.size() - 1)));

                testCases--;

                if(testCases==0){
                    break;
                }
            }
        }

        for(String result:results){
            System.out.println(result);
        }

    }
}
