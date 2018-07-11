int playerPos = 64; 
float headCentreY = 248;
float headSize = 30;
int moveConstraint;  //stores the max distance the player can travel to the right 

int hammerCounter = 60; 
int hammerPosUpdate = 0;
int hammerPosX = 197;

int doorCounter;
boolean isDoorOpen = true; 

int score = 0;
int penalty = 0;

float rightDoorPosX = 419;
float rightDoorPosY = 210;

float groundPosX = 53;
float groundPosY = 295;

float balconyPosX = 0;
float balconyPosY = 160;

void setup()
{
  size(512, 348);
  doorCounter = (int)random(180, 480);
}

void draw()
{
  background(200);
  
  //DRAWING THE GROUND
  strokeWeight(1);
  line(groundPosX, groundPosY, 398, 295);

  //DRAWING THE BALCONY AND LEFT DOOR
  drawleftBuilding();

  //TREES
  drawTrees(435, 130, 35);
  drawTrees(500, 138, 27);

  //TOOL FUCTIONS
  hammerTimer();
  drawHammer();
  hammerCollisionChecker();

  //DRAWING THE CHARACTER
  drawCharacter(playerPos, headCentreY, headSize);

  //DRAWING THE BUILDING AND DOOR ON THE RIGHT
  rightDoorTimer();
  drawRightBuilding(rightDoorPosX, rightDoorPosY);

  //DISPLAYING THE SCORE
  displayScore(85, score, penalty);
  scoreTracker();  //checks total score and resets game if screen is filled
}

//PLAYER MOVEMENT 
void keyPressed()
{
  if (keyCode == RIGHT && playerPos < moveConstraint)
  {
    playerPos += 64;
    if (playerPos == (width-64))
    {
      playerPos = 64;
      score += 3;
    }
  }
  if ((keyCode ==LEFT  && playerPos > 64) )
  {  
    if (playerPos == (moveConstraint + 64) && isDoorOpen == false)
    {
      playerPos = moveConstraint + 64;
    } else
    {
      playerPos -= 64;
    }
  }
}