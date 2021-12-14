# Parallel and distributed system using Java MPI

Distributed systems are groups of networked computers which share a common goal for their work. 
The terms "concurrent computing", "parallel computing", and "distributed computing" have much overlap, and no clear distinction exists between them. 
The same system may be characterized both as "parallel" and "distributed"; the processors in a typical distributed system run concurrently in parallel.
Parallel computing may be seen as a particular tightly coupled form of distributed computing, and distributed computing may be seen as a loosely coupled form of parallel computing.
Nevertheless, it is possible to roughly classify concurrent systems as
"parallel" or "distributed" using the following criteria:

- In parallel computing, all processors may have access to a shared memory to exchange information between processors.
- In distributed computing, each processor has its own private memory (distributed memory). Information is exchanged by passing messages between the processors.

## MPJ express
MPJ Express is an open source Java message passing library that allows application developers to write and execute parallel applications for multicore processors and compute clusters/clouds. 
The software is distributed under the MIT (a variant of the LGPL) license.

For additional information refer to this [link](http://mpjexpress.org/)
