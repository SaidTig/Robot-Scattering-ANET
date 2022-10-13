//-------Said TIGUEMOUNINE-----------


import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.core.Node;

import io.jbotsim.core.Point;
import java.util.ArrayList;
import java.util.Random;



public class Robot extends Node{

	ArrayList<Point> locations;
	ArrayList<Point> destinations = new ArrayList<Point>();
	Point target = null;
	int myMultiplicity;

	static int NB = 40; // Number of robots
	// To see a scattering that take the size of the robots into account, set EPS to 10
	static double EPS = 0.000001;

	@Override
	public void onPreClock() {   
		//Modify the onPreClock method to compute and store (in an attribut called myMultiplicity) how many robots share the same location
		locations = new ArrayList<Point>();
		myMultiplicity = 0;
		for (Node node : getTopology().getNodes() )
		{
			if (this.getLocation().equals(node.getLocation())) myMultiplicity++;
		//Remove from the array locations the location of the robots that share my position
			else locations.add(node.getLocation());
		}
	}
	int numOfDest = 7;
	@Override
	public void onClock(){   
	
		/*target = locations.get(0);
		for(Point r : locations)
		{
			if(r.getX() > target.getX() || (r.getX() == target.getX() && r.getY() > target.getY()))
			{
				target = r;
			}
		}*/
		//---------------
		//modify the onClock method to choose a destination only by using the attributes locations and myMultiplicity
		/*int x = new Random().nextInt(801);
		int y = new Random().nextInt(401);
		if (myMultiplicity != 0 && (this.getLocation() == target || target == null)){
			target = new Point(x,y);
			*/
		//------------
		//Modify the onClock method to choose a safe destination
		int k = 0;
		if (myMultiplicity > 1 && (new Random()).nextInt(2) == 1){
			target = generateDestinations(numOfDest).get(k%numOfDest);
			k++;
			}
			
		}	
	

	@Override
	public void onPostClock(){ 
		if(target != null){
			setDirection(target);
			move(Math.min(10, distance(target)));
			if(distance(target) <= 10 && distance(target)> 0) System.out.println("Aggregation Done in "+(this.getTopology().getTime()) +" time unit ");
		}
	}
	
	// method generateDestinations that takes an integer n as unique parameter and returns a list of n safe destinations
	public ArrayList<Point> generateDestinations(int n){
		Point p = new Point();
		for (int i=0; i<n; i++){
			boolean t;
			p.setLocation(new Random().nextInt(801), new Random().nextInt(401));
			for (Point q : locations){
				t = p == q;
				while(t){
					p.setLocation(new Random().nextInt(801), new Random().nextInt(401));
					t = p == q;
					System.out.println(t);
					}
				}
			System.out.println(p);
			destinations.add(new Point(p));
			}
		return destinations;
		}

	// Start the simulation
	public static void main(String[] args){

		// Create the Topology (a plane of size 800x400)
		Topology tp = new Topology(800, 400);
		// Create the simulation window
		new JViewer(tp);

		// set the default node to be our Robot class 
		// (When the user click in the simulation window,
		//  a default node is automatically added to the topology)
		tp.setDefaultNodeModel(Robot.class);

		// Robots cannot communicate
		tp.disableWireless();

		// Here we remove the sensing range since the robots have unlimited visibility
		tp.setSensingRange(0);

		// Add NB Robots to the topology (with random positions)
		for (int i = 0; i < NB; i++){
			int x = new Random().nextInt(801);
			int y = new Random().nextInt(401);
			tp.addNode(x,y);
			if(i < 20) tp.addNode(x,y);	//Pour avoir quelques noeuds qui demarrent dans la meme position
			
			}
			
		//-----Modify the main function so that the robots all start at the same random position.
		/*tp.addNode(-1,-1);
		Point initialPoint = tp.getNodes().get(0).getLocation();
		for (int i = 0; i < NB-1; i++)
			tp.addNode(initialPoint.getX(),initialPoint.getY());*/
		//-----

		//The clock click every 0.5 sec (so that you can see the evolution slowly)
		tp.setTimeUnit(50);

		// We pause the simulation 
		// (to start it, you'll have to right click on the window and resume it)
		tp.pause();
	}
}
