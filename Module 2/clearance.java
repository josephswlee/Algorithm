import java.util.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class clearance {
	
	static class Edge implements Comparable<Edge> {
		int vertice;
		int first;
		int last;
		
		public Edge (int vertice, int first, int last) {
			this.vertice = vertice;
			this.first = first;
			this.last = last;
		}
		
		public int compareTo(Edge compareEdge) {
			if (this.vertice > compareEdge.vertice) {
				return 1;
			}
			
			else if (this.vertice < compareEdge.vertice) {
				return -1;
			}
			
			else {
				return 0;
			}
		}
	}

	static class Graph {
		int index;
		int verticeCount;
		ArrayList<ArrayList<Edge> > adjacencyList;

		public Graph(int vert) {
			index = 0;
			int next = vert + 1;
			adjacencyList = new ArrayList<ArrayList<Edge> >(next);
			this.verticeCount = vert;
			for (index = 0; index < vert+1; index++) {
				adjacencyList.add(new ArrayList<Edge>());
			}
		}

		public void addEdge (int source, int dest, int begin, int end) {
			ArrayList<Edge> lists = adjacencyList.get(source);
			Edge e = new Edge (dest, begin, end);
			lists.add(e);
		}
		public Boolean BFS (int source, int dest, int badges) {
			PriorityQueue<Integer> q = new PriorityQueue<Integer>();
			boolean isVisited[] = new boolean[verticeCount+1];
			isVisited[source] = true;
			Iterator<Edge> itr;
			q.add(source);
		
			while (q.size() > 0) {
				Edge e;
				source = q.poll();
				
				itr = adjacencyList.get(source).iterator();
			
				while (itr.hasNext()) {
					e = itr.next();
				
					if (badges <= e.last && badges >= e.first) {
						if (dest == e.vertice) {
							return true;
						}
						else if (isVisited[e.vertice] != true) {
							isVisited[e.vertice] = true;
							q.add(e.vertice);
						}
					}
				
				}
			
			}
			return false;
		}

		public int countBadges (int startRoom, int endRoom, Set <Integer> range) {
			List <Integer> trackNum = new ArrayList <Integer>();
			Iterator <Integer> itr = range.iterator();

			int result = 0;
			while (itr.hasNext()) {
				int cur = itr.next();
				if (BFS(startRoom, endRoom, cur) == true) {
					result += 1;
					trackNum.add(cur);
				}
			
				if (trackNum.size() == 2) {
					if(trackNum.get(1) - trackNum.get(0) != 1) {
						int badge1 = trackNum.get(0);
						int badge2 = trackNum.get(1);
						int next = badge1 + 1;
					
						if (BFS(startRoom, endRoom, next)) {
							result += ((badge2 - badge1) - 1);
						}
					}
					trackNum.remove(0);
				}
			}

			return result;
		}

	}
	
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner (System.in);
		int index = 0;
		int startRoom = 0;
		int endRoom = 0;
		int result = 0;
		Set <Integer> range = new TreeSet <Integer>();
		Graph graph = null;
		
		int vertices = keyboard.nextInt();
		int edges = keyboard.nextInt();
		int badges = keyboard.nextInt();
		startRoom = keyboard.nextInt();
		endRoom = keyboard.nextInt();
		
		graph = new Graph(vertices);
		
		while (index < edges) {
			int begin = keyboard.nextInt();
			int end = keyboard.nextInt();
			int roomA = keyboard.nextInt();
			int roomB = keyboard.nextInt();
			
			graph.addEdge(begin, end, roomA, roomB);
			range.add(roomA);
			range.add(roomB);
			
			index += 1;
		}

		keyboard.close();
		assert graph != null;
		
		System.out.println(graph.countBadges (startRoom, endRoom, range));
		
	}

}