# m1nesweeper

REST resources:

| METHOD        | ENDPOINT                      | Description                                                             |
| ------------- |-------------------------------| ------------------------------------------------------------------------|
| GET           | /game                         | Return all boards ids                                                   |
| POST          | /game                         | Create new board                                                        |
| GET           | /game/:ID                     | Returns board by id                                                     |
| POST          | /game/:ID/:x/:y/click         | Clicks on coordinate [x, y]. Returns the new board.                     |
| POST          | /game/:ID/:x/:y/flag          | Puts/Removes a flag on coordinate [x, y]. Returns the new board.        |
| POST          | /game/:ID/:x/:y/question-mark | Puts/Removes question mark on coordinate [x, y]. Returns the new board. |

  
  
    
  
  
  