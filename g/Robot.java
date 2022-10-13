// Q4 and Q5 : We assume that the robots may not all share the same location
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.core.Node;

import io.jbotsim.core.Point;
import java.util.ArrayList;
import java.util.Random;



public class Robot extends Node{

	ArrayList<Point> locations;
	Point target = null;
	int myMultiplicity = 0;
	ArrayList<Point> destinations = null;

	static int NB = 40; // Number of robots
	// To see a scattering that take the size of the robots into account, set EPS to 10
	static double EPS = 0.000001;

	@Override
	public void onPreClock() {   
		locations = new ArrayList<Point>();
		for (Node node : getTopology().getNodes()) {
			if (this.getLocation().equals(node.getLocation())) myMultiplicity++;
			else locations.add(node.getLocation());
		}
		myMultiplicity = myMultiplicity-1;
	}

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
		int x = new Random().nextInt(801);
		int y = new Random().nextInt(401);
		if (myMultiplicity != 0 && (this.getLocation() == target || target == null)){
			target = new Point(x,y);
		}
	}

	@Override
	public void onPostClock(){ 
		setDirection(target);
		move(Math.min(10, distance(target)));
	}
	
	// method generateDestinations that takes an integer n as unique parameter and returns a list of n safe destinations
	public ArrayList<Point> generateDestinations(int n){
		Point p = new Point();
		for (int i=0; i<n; i++){
			boolean t;
			p.setLocation(new Random().nextInt(801), new Random().nextInt(401));
			for (Node node : getTopology().getNodes()){
				for(Point u : node.getDest()){
					t = p == u;
					while(t){
						p.setLocation(new Random().nextInt(801), new Random().nextInt(401));
						t = p == u;
						}
					}
				}
			destinations.add(p);
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
		/*for (int i = 0; i < NB; i++)
			tp.addNode(-1,-1);*/
			
		//-----Modify the main function so that the robots all start at the same random position.
		tp.addNode(-1,-1);
		Point initialPoint = tp.getNodes().get(0).getLocation();
		for (int i = 0; i < NB-1; i++)
			tp.addNode(initialPoint.getX(),initialPoint.getY());
		//-----

		//The clock click every 0.5 sec (so that you can see the evolution slowly)
		tp.setTimeUnit(500);

		// We pause the simulation 
		// (to start it, you'll have to right click on the window and resume it)
		tp.pause();
	}
	}
