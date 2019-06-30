#            DSDV algorithm simulating

   get the UDP simulation IDEA from :<https://github.com/DeividasZilinskas/DSDV>

 Create by liudongdong,Major in IOT,JLU.

## DSDV Protocol briefly introduce

- DSDV is Proactive (Table Driven)
- Each node maintains routing information for all known destinations 
- Routing information must be updated periodically 
- Traffic overhead even if there is no change in network topology
- Maintains routes which are never used

#### features

- Keep the simplicity of Distance Vector

- Guarantee Loop Freeness

â€‹          New Table Entry for Destination Sequence Number

- Allow fast reaction to topology changes
    Make immediate route advertisement on significant changes in routing table
    but wait with advertising of unstable routes(damping fluctuations)

#### RouteTableStructure

| **Destination** | **Next** | **Metric** | **Seq. Nr** | **Install Time** | **Stable Data** |
| --------------- | -------- | ---------- | ----------- | ---------------- | --------------- |
| **A**           | **A**    | **0**      | **A-550**   | **001000**       | **Ptr_A**       |
| **B**           | **B**    | **1**      | **B-102**   | **001200**       | **Ptr_B**       |
| **C**           | **B**    | **3**      | **C-588**   | **001200**       | **Ptr_C**       |
| **D**           | **B**    | **4**      | **D-312**   | **001200**       | Ptr_D           |

- Sequence number originated from destination. Ensures loop freeness.
- Install Time when entry was made (used to delete stale entries from table)
- Stable Data Pointer to a table holding information on how stable a route is. Used to damp fluctuations in network.

#### RouteTableItemUpdateRules

- Route Advertisements

  1. Advertise to each neighbor own routing information
         Destination Address
         Metric = Number of Hops to Destination
         Destination Sequence Number
  2. Rules to set sequence number information
         On each advertisement increase own destination sequence number (use only even numbers)
         If a node is no more reachable (timeout) increase sequence number of this node by 1 (odd     sequence number) and set metric =æ— ç©·

- Route Selection

  Update information is compared to own routing table

  1. Select route with higher destination sequence number (This ensure to use always newest information from destination)
  2. 2. Select the route with better metric when sequence numbers are equal.



For more detail:please read dsdv.ppt .

## Simulation Test

 test1:

![1561776012733](D:\IDEA\project\dsdv\assets\1561776012733.png)

topology Structure representing in  json file as follows:

```json
{
  "id":"1001",
  "a":[
    {"1":{"distance":0,"port":3030,"name":"A"}},
    {"2":{"distance":2,"port":3031,"name":"B"}},
    {"3":{"distance":1,"port":3032,"name":"C"}},
    {"4":{"distance":1,"port":3033,"name":"D"}}
  ],
  "b":[
    {
      "1":{"distance":0,"port":3031,"name":"B"}},
    {
      "2":{"distance":2,"port":3030,"name":"A"}},
    {
      "3":{"distance":3,"port":3032,"name":"C"}},
    {
      "4":{"distance":2,"port":3033,"name":"D"}}
  ],
  "c":[
    {
      "1":{"distance":0,"port":3032,"name":"C"}},
    {
      "2":{"distance":1,"port":3030,"name":"A"}},
    {
      "3":{"distance":3,"port":3031,"name":"B"}},
    {
      "4":{"distance":3,"port":3033,"name":"D"}},
    {
      "5":{"distance":4,"port":3034,"name":"E"}},
    {
      "6":{"distance":5,"port":3035,"name":"F"}}
  ],
  "d":[
    {
      "1":{"distance":0,"port":3033,"name":"D"}},
    {
      "2":{"distance":1,"port":3030,"name":"A"}},
    {
      "3":{"distance":2,"port":3031,"name":"B"}},
    {
      "4":{"distance":3,"port":3032,"name":"C"}},
    {
      "5":{"distance":1,"port":3034,"name":"E"}}
  ],
  "e":[
    {
      "1":{"distance":0,"port":3034,"name":"E"}},
    {
      "2":{"distance":4,"port":3032,"name":"C"}},
    {
      "3":{"distance":1,"port":3033,"name":"D"}},
    {
      "4":{"distance":2,"port":3035,"name":"F"}}
  ],
  "f":[
    {"1":{"distance":0,"port":3035,"name":"F"}},
    {"2":{"distance":2,"port":3034,"name":"E"}},
    {"3":{"distance":5,"port":3032,"name":"C"}}
  ]
}
```

![1561776137186](D:\IDEA\project\dsdv\assets\1561776137186.png)

test2:

![1561776195929](D:\IDEA\project\dsdv\assets\1561776195929.png)

in piture above,all the line representing  single direction are double directions,and the topology structure are presenting in the .csv file,which are as follows:

| a.csv     | b.csv     | c.csv     | d.csv     | e.csv     | f.csv     |
| --------- | --------- | --------- | --------- | --------- | --------- |
| A,0,12345 | B,0,12332 | C,0,12245 | D,0,12145 | E,0,11345 | F,0,12415 |
| B,2,12332 | A,2,12345 | D,3,12145 | C,3,12245 | A,1,12345 | A,2,12345 |
| E,1,11345 |           | E,1,11345 | F,1,12415 | C,1,12245 | D,1,12145 |
| F,2,12415 |           |           |           |           |           |

![1561777600918](D:\IDEA\project\dsdv\assets\1561777600918.png)

!1561776708704](D:\IDEA\project\dsdv\assets\1561776708704.png)

![1561776718581](D:\IDEA\project\dsdv\assets\1561776718581.png)



## Problem Remain

Damping Fluctuations

![1561776905301](D:\IDEA\project\dsdv\assets\1561776905301.png)

![1561776922299](D:\IDEA\project\dsdv\assets\1561776922299.png)