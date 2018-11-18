# Multiagent Systems Coursework 2018

The task for this assignment is to implement a variant specification of Steel’s Mars [1990] autonomous
explorer vehicle, using a subsumption architecture.

The objective is to explore a distant planet, more concretely, to collect samples of a particular
type of precious rock. The location of the rock samples is not known in advance, but they
are typically clustered in certain spots. A number of autonomous vehicles are available that
can drive around the planet collecting samples and later reenter a mother ship spacecraft to go
back to Earth. There is no detailed map of the planet available, although it is known that the
terrain is full of obstacles (hills, valleys, etc.) which prevent the vehicles from exchanging any
communication.” [from “An Introduction to MultiAgent Systems” by M. Wooldridge, John
Wiley & Sons, 2009.]

## Implementation

The implementation of the agent can be found in the folder with path:

  ```MASCW/0/src/main/java/Mars/```
 
The files that contain edited code are:
 * ```Simulator.java```
 * ```Vehicle.java```
