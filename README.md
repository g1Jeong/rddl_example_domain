# rddl_example_domain
rddl code implementing a simple ambulance domain

Contents 
1) Domain file : Jihwan/rddl/ambulance.rddl
2) Instance files : Jihwan/rddl/ambulance_inst_x.rddl (x \in {1, 12, 2, 22_larger, 32, 4x6_21})
3) Visualization : AmbulanceDisplay.java
4) Random policy : RandomBoolPolicy.java (used the given random boolean policy)

Place the domain description file and an instance file in the same directory and run the code, e.g., 
  run rddl.sim.Simulator files/Jihwan/rddl rddl.policy.RandomBoolPolicy ambulance_inst_12 rddl.viz.AmbulanceDisplay

Note that the visualization is based on the example provided on the RDDL tutorial website. At each step, currently active (non answered emergency call) location, current positions of ambulances (one or more depending on instantiation), dispatch (if any) are printed out. This might slow execution of the simulation. 

Due to the complex relations between variables and constraints, it seems like deciding a valid action takes the most of execution time. 

+) Emergency calls arrive according to a Poisson process with the arrival rate specified in the instance file. Each of the location has equal probability of arrival in a single time step, i.e., P(emergency call occurs at a location) = P(emergency calls occur in the system)/(# of locations with no current emergency calls). Here, P(emergency calls in system) = 1 - P(no emergency calls) = 1 - exp(-ARRIVAL_RATE). 

+) There is a single action "dispatch" which takes (ambulance, location, location) as parameters. It assigns an ambulance to an active emergency call and a hospital.
