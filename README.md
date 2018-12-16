# m1nesweeper

REST resources:

GET `/board` -> Return all boards ids  
POST `/board` -> Create new board  
GET `/board/:ID` -> Returns board by id  
POST `/board/:ID/click` payload: `{ x: 0, y: 0 }` -> Clicks on positions. Returns the new board.