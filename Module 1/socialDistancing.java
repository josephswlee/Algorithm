import java.util.*;


public class socialDistancing {

	private static List<Integer> positions = new ArrayList<>();
	private static int guest;
	
	private static int getDistance() {
		int lowest = 0;
		
		//highest = last element - first element
		// if we dont have +1 
		// lets say if we have 1 or 2 
		// 1-0is not greater than 0 so we will be stuck in infinite loop
		int highest = positions.get(positions.size()-1)-positions.get(0)+1;
		
		while (highest - lowest > 1) {
			int mid = (highest + lowest) / 2;
			if(validPosition(mid)) {
				lowest = mid;
			}
			else {
				highest = mid;
			}
		}
		return lowest;
	}
	
	private static boolean validPosition(int position) {
		int occupied = 1;
		int last = positions.get(0);
		
		for (int i = 1; i < positions.size(); i++) {
			if(positions.get(i) - last >= position) {
				occupied++;
				last = positions.get(i);
			}
		}
		return occupied >= guest;
	}
	
	public static void main(String[] args) {
		
		Scanner keyboard = new Scanner(System.in);
		int i = 0;
		
		while (keyboard.hasNextLine()) {
			String input = keyboard.nextLine().trim();
			if (input.length() == 0) break;
			if (i == 0) {
				guest = Integer.parseInt(input.split(" ")[1]);
				i += 1;
			}
			else {
				positions.add(Integer.parseInt(input));
			}
		}
		
		int calculatedDistance = getDistance();
		System.out.println(calculatedDistance);
		keyboard.close();
		
	}

}
