#Design Goals

The goal of this program is to run Cellular Automaton simulations based on user-
given rules and display the simulations visually as an animated grid.

#Adding New Features

To add a new simulation to this program, a new Society subclass, Cell subclass, and
State class must be created for the new simulation. The Society subclass must contain
the rules for updating the Cell states in the simulation, the Cell subclass must contain
the fields for the different Cell types in the simulation (such as hitpoints, energy,
hunger, etc.), and the State subclass must contain enums of all possible states
a Cell can be in in the given simulation. After creating these classes, simply add
to the createSociety method in the UI class a constructor for the new type of simulation, 
add the newly constructed simulation as an option to the comboBox for selecting
simulations, and run. 

#Major Design Choices

The choice was made to have a hierarchy with UI and XML parser at the top, calling
Society constructors, which call Cell constructors, which fill themselves with States
generated from the XML data. This is a straightforward way to update the cell states
each cycle, although at times the burden of updating is shared with the State class
methods, especially in the simpler simulations such as Game of Life. 
Also, we have not accounted for more complex types of CA simulations which require
objects such as "patches of ground" to be separate from the Cells that occupy them,
so implementing this type of simulation will require the patch structure to be initialized
in the new Society subclasses. 