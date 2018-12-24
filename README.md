# rddl_example_domain
rddl code implementing a simple ambulance domain

Contents 
1) Domain file : Jihwan/rddl/ambulance.rddl
2) Instance files : Jihwan/rddl/ambulance_inst_x.rddl (x \in {1, 12, 2, 22_larger, 32, 6x4_21})
3) Visualization : AmbulanceDisplay.java
4) Random policy : RandomBoolPolicy.java (used the given random boolean policy)

Place the domain description file and an instance file in the same directory and run the code, e.g., 
  run rddl.sim.Simulator files/Jihwan/rddl rddl.policy.RandomBoolPolicy ambulance_inst_4x4_22 rddl.viz.AmbulanceDisplay

Note that the visualization is based on the example provided on the RDDL tutorial website. At each step, currently active (non answered emergency call) location, current positions of ambulances (one or more depending on instantiation), dispatch (if any) are printed out. This might slow execution of the simulation. 
