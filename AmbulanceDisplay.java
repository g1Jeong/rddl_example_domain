package rddl.viz;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Map;

import rddl.EvalException;
import rddl.State;
import rddl.RDDL.LCONST;
import rddl.RDDL.PVARIABLE_DEF;
import rddl.RDDL.PVAR_NAME;
import rddl.RDDL.TYPE_NAME;

public class AmbulanceDisplay extends StateViz {
	
	public AmbulanceDisplay() {
		_nTimeDelay = 200;
	}
	
	public AmbulanceDisplay(int time_delay_per_frame) {
		_nTimeDelay = time_delay_per_frame;
	}
	
	public boolean _bSuppressNonFluents = false;
	public BlockDisplay _bd = null;
	public int _nTimeDelay = 0;
	
	public void display(State s, int time) {
		try {
			System.out.println("TIME = " + time + ": " + getStateDescription(s));
		} catch (EvalException e) {
			System.out.println("\n\nError during visualization:\n"+e);
			e.printStackTrace();
			System.exit(1);
		}
	}
	/////////////////////////////////////////////////////
	
	public String getStateDescription(State s) throws EvalException {
		StringBuilder sb = new StringBuilder();
		
		TYPE_NAME ambulance_type = new TYPE_NAME("ambulance");
		ArrayList<LCONST> ambulances = s._hmObject2Consts.get(ambulance_type);
		
		TYPE_NAME location_type = new TYPE_NAME("location");
		ArrayList<LCONST> locations = s._hmObject2Consts.get(location_type);
		
		PVAR_NAME num_row = new PVAR_NAME("NUM_ROW");
		PVAR_NAME num_col = new PVAR_NAME("NUM_COL");
		PVAR_NAME amb_xpos = new PVAR_NAME("xPos");
		PVAR_NAME amb_ypos = new PVAR_NAME("yPos");
		PVAR_NAME xpos = new PVAR_NAME("x_Pos");
		PVAR_NAME ypos = new PVAR_NAME("y_Pos");
		PVAR_NAME hospital = new PVAR_NAME("HOSPITAL");
		PVAR_NAME passenger_on = new PVAR_NAME("PassengerOn");
		PVAR_NAME active = new PVAR_NAME("active");
		PVAR_NAME dispatch_pvar = new PVAR_NAME("dispatch");
		
		if (_bd == null) {
			int max_row = 2 * 4;
			int max_col = 2 * 6;
			_bd = new BlockDisplay("RDDL Ambulance Simulation", "Simulation", max_row + 1, max_col +1);
		}
		
		// Set up two arity-1 parameter lists
		ArrayList<LCONST> params1 = new ArrayList<LCONST>(1);
		params1.add(null);
		ArrayList<LCONST> params2 = new ArrayList<LCONST>(1);
		params2.add(null);
		
		_bd.clearAllText();
		// Plot all grid points with corresponding states
		for (int i = 0; i < locations.size(); i++) {
			
			LCONST location = locations.get(i);
			params1.set(0, location);
			
			// Check if hospital or not, and get state values
			boolean location_is_hospital	= (Boolean)s.getPVariableAssign(hospital, params1);
			boolean is_active 				= (Boolean)s.getPVariableAssign(active, params1);
			int x_location 					= (int)s.getPVariableAssign(xpos, params1);
			int y_location					= (int)s.getPVariableAssign(ypos, params1);
			
			String letter = null;
			Color color = _bd._colors[9];		// grid points have dark-gray color
			if (location_is_hospital) {			// Hospitals have green color with "+" sign
				letter = "+";
				color = Color.green;
			} else if (is_active) {
				letter = "!";					// when there is a pending emergency call
				color = Color.magenta;			// the sign is '!' with magenta color.
			} else {
				letter = "o";
			}
			
			// Display on screen
			_bd.addText(color, 2*x_location+1, 2*y_location+1, letter);
		}
		
		for (int i = 0; i < ambulances.size(); i++) {
			LCONST ambulance = ambulances.get(i);
			params1.set(0, ambulance);
			
			// Get state values
			boolean check_passenger 	= (Boolean)s.getPVariableAssign(passenger_on, params1);
			int x_ambulance				= (int)s.getPVariableAssign(amb_xpos, params1);
			int y_ambulance				= (int)s.getPVariableAssign(amb_ypos, params1);
			
			//System.out.println("PRINT AMBULANCE LOCATIONS");
			
			String amb_letter = null;
			Color amb_color = _bd._colors[11];			// red color
			if (check_passenger) {
				amb_letter = "F";
			} else {
				amb_letter = "E";
			}
			
			// Display on screen
			_bd.addText(amb_color, 2*x_ambulance, 2*y_ambulance+1, amb_letter);
		}
		
		_bd.repaint();
		sb.append("\n");
		// Skim through all (ambulance, location, location) tuples to check which ambulance is dispatched to
		// which emergency call. 
		for (Map.Entry<String, ArrayList<PVAR_NAME>> entry : s._hmTypeMap.entrySet()) {
			
			if (entry.getKey().equals("nonfluent") | entry.getKey().equals("interm"))
				continue;
			
			for (PVAR_NAME p : entry.getValue()) {
				if ( !(p.toString().equals("xPos") | p.toString().equals("yPos") | p.toString().equals("dispatch") ))
					continue;
			
				try {
					ArrayList<ArrayList<LCONST>> gfluents = s.generateAtoms(p);
					for (ArrayList<LCONST> gfluent : gfluents) {
						if ( p.toString().equals("dispatch") && (!(Boolean)s.getPVariableAssign(p, gfluent)) ) 
								continue;
						
						sb.append(entry.getKey() + ": " + p + 
								(gfluent.size() > 0 ? gfluent : "") + " := " +
								s.getPVariableAssign(p, gfluent) + "\n");
					}
				} catch (EvalException ex) {
					sb.append("- could not retrieve assignment" + s + " for " + p + "\n");
				}
		
			}
		}
		// Sleep so the animation can be viewed at a frame rate of 1000/_nTimeDelay per second
	    try {
			Thread.currentThread().sleep(_nTimeDelay);
		} catch (InterruptedException e) {
			System.err.println(e);
			e.printStackTrace(System.err);
		}
				
		return sb.toString();
	}
	
	public void close() {
		_bd.close();
	}
}

