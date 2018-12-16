# m1nesweeper

REST resources:

GET `/game` -> Return all boards ids  
POST `/game` -> Create new board  
GET `/game/:ID` -> Returns board by id  
POST `/game/:ID/click` payload: `{ x: 0, y: 0 }` -> Clicks on positions. Returns the new board.