# m1nesweeper

## REST resources

| METHOD        | ENDPOINT                                    | Description                                                             | Returns
| ------------- |---------------------------------------------| ------------------------------------------------------------------------|---------
| GET           | /game                                       | Return all boards ids                                                   | List[String]
| POST          | /game                                       | Create new board                                                        | BOARD_INFO
| GET           | /game/:ID?[pretty:bool]                     | Returns game by id                                                      | GAME
| GET           | /game/:ID/board?[pretty:bool]               | Returns board by id                                                     | BOARD_INFO
| POST          | /game/:ID/:x/:y/click?[pretty:bool]         | Clicks on coordinate [x, y]. Returns the new board.                     | BOARD_INFO
| POST          | /game/:ID/:x/:y/flag?[pretty:bool]          | Puts/Removes a flag on coordinate [x, y]. Returns the new board.        | BOARD_INFO
| POST          | /game/:ID/:x/:y/question-mark?[pretty:bool] | Puts/Removes question mark on coordinate [x, y]. Returns the new board. | BOARD_INFO

Use query param `[pretty:bool]` to print a visually usefull representation fo the board.

#### Pretty print meaning

* `Empty space` Unknown   
* `*` Bomb  
* `F` Flag  
* `#` Explosion  
* `?` Question mark  
* `Number` Amount of nearby bombs  

#### GAME

```
{
  "timestamp": 1545052235575,
  "state": "Playing",
  "solution": [
    [
      0,
      0,
      0,
      0,
      0
    ],
    [
      0,
      0,
      0,
      0,
      0
    ],
    [
      0,
      0,
      0,
      1,
      1
    ],
    [
      0,
      0,
      0,
      1,
      "Bomb"
    ],
    [
      0,
      0,
      0,
      1,
      1
    ]
  ],
  "id": "507f191e810c19729de860ea",
  "board": [
    [
      0,
      0,
      0,
      0,
      0
    ],
    [
      0,
      0,
      0,
      0,
      0
    ],
    [
      0,
      0,
      0,
      1,
      1
    ],
    [
      0,
      0,
      0,
      1,
      "Unknown"
    ],
    [
      0,
      0,
      0,
      1,
      "Unknown"
    ]
  ]
}
```

#### GAME (With Pretty print)

```
{
  "timestamp": 1545052235575,
  "state": "Playing",
  "solution": [
    "00000",
    "00000",
    "00011",
    "0001*",
    "00011"
  ],
  "id": "507f191e810c19729de860ea",
  "board": [
    "00000",
    "00000",
    "00011",
    "0001 ",
    "0001 "
  ]
}
```

#### BOARD_INFO

```
{
  "id": "507f191e810c19729de860ea",
  "board": [
    [
      "Unknown",
      "Unknown",
      "Unknown",
      "Unknown",
      "Unknown"
    ],
    [
      "Unknown",
      "Unknown",
      "Unknown",
      "Unknown",
      "Unknown"
    ],
    [
      "Unknown",
      "Unknown",
      "Unknown",
      "Unknown",
      "Unknown"
    ],
    [
      "Unknown",
      "Unknown",
      "Unknown",
      "Unknown",
      "Unknown"
    ],
    [
      "Unknown",
      "Unknown",
      "Unknown",
      "Unknown",
      "Unknown"
    ]
  ],
  "state": "Playing",
  "timestamp": 1545052080879
}
``` 

#### BOARD_INFO (with pretty print)

```
{
  "id": "507f191e810c19729de860ea",
  "board": [
    "00000",
    "00000",
    "00F11",
    "0001#",
    "0001 "
  ],
  "state": "Playing",
  "timestamp": 1545052235575
}
```    
  
  
  