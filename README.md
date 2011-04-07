# CS3114 Project 3: DNA Tree Database
### Virginia Tech, Spring 2011

## Project Members:
- Logan Linn <llinn@vt.edu>
- Matthew Ibarra <mibarra@vt.edu>

## [Project Specification](http://github.com/loganlinn/DNATrees/raw/master/P3_spec.pdf)

### Overview
This project stores DNA sequences using the alphabet, {A, C, G, T}, in a custom structure based on the structure of loganlinn/DNATreesBase.

As an extension to Project 2, we store our sequences in an encoded binary file rather than in the tree. Since there are 4 characters in the alphabet, each character is encoded using 2 bits (ie 4 characters to a byte). DNA sequences are stored using a first-fit free list. 

Tree nodes are implemented using a composition design pattern, and tree operations are recursive. 
Empty leaf nodes on the tree use a flyweight design. There exists only 1 empty leaf node object for the project, rather than a new instance of for each empty leaf node. Internal nodes with empty children reference the singleton empty leaf.

### Command File
The valid command structure is as follows:

* `insert <sequenceIdentifier> <sequenceLength>`

  `<sequence>`

* `remove <sequenceID>`

* `print`

* `search <sequenceID>`

* `search <sequenceID>`

### Running
`java P3 <commandFile>`

