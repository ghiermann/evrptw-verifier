# E-VRPTW Solution Verifier (Java)
Solution Verifier for the Electric Vehicle Routing Problem with Time Windows and recharging stations (E-VRPTW)
as presented in

`M. Schneider, A. Stenger, D. Goeke, The electric vehicle routing problem with time windows and recharging stations, Transportation Science 48 (4)(2014) 500-520`


Usage: `java -jar evrptw-verifier-<version> (-d) instancePath solutionPath(s)`
* `-d`              : detailed mode (show additional information) (optional)
* `instancePath`    : path to the instance file
* `solutionPath(s)` : one (or several, seperated by a whitespace) solution files to be verified

## Solution file
* lines at the beginning of the file starting with an '#' are ignored (can be used for comments)
* first line consists of the cost value (distance) - rounded to the third digit after the punctation (only the sum is rounded, not each route)
* all following, non-emtpy lines, are treated as routes consisting of the node names according to the instance file (e.g., D0, C21, S7, D0). Each visit of the route is seperated by a whitespace or a comma.

### Example solution
```
# solution for r205_21
974.832
D0, C52, C7, C8, C46, C48, C19, C88, S5, C10, C90, C32, C30, C20, C66, C71, C35, C9, C51, C3, C29, C24, C12, D0
D0, C13, C95, C92, C59, C99, C5, C84, C83, C60, C18, C89, D0
D0, C2, C42, C100, C14, C43, C38, C44, C91, C93, C94, C96, C6, D0
D0, C27, C28, C53, C58, C87, C97, C37, C98, C85, C61, C16, C86, C17, C45, C82, C47, C36, C49, C64, C11, C63, C62, D0
D0, C26, C21, C72, C23, C67, C25, C54, C80, C68, C55, S18, C39, C75, C74, C57, C15, C41, C22, C56, C4, C73, C40, D0
D0, C1, C70, C65, C34, C78, C81, C33, C79, C77, C76, C50, C69, C31, D0
```

## Misc
The verifier was developed as part of the Lecture "Optimization in Transportation and Logistics" (OTL) at the TU Wien, Austria

### Licence
Apache Licence 2.0
