import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class assignment_2 extends PApplet {

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

public void setup()
{
  
  doorCounter = (int)random(180, 480);
}

public void draw()
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
public void keyPressed()
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

//------------------Functions for Scoring--------------------

public void displayScore(int circleSize, int score, int penalty) {
  int numberOfRows = 0;
  int padding = circleSize/2;
  int rowPos;
  int totalScore =  score + penalty;

  while (score > 0 || penalty > 0)
  { 
    numberOfRows += 1;

    if (numberOfRows > 1)
    {
      rowPos =  ((circleSize*numberOfRows)-padding);
    } else
    {
      rowPos = padding;
    }

    int rowSize = circleSize*totalScore;
    if (rowSize > width)
    {
      rowSize = (rowSize -(rowSize-width));
    }
    for (int j = padding; j< rowSize; j+= circleSize)
    {
      if (penalty != 0 ) {
        DrawCircles(j, rowPos, circleSize, 255, 0, 0);
        penalty--;
      } else
      {
        if (score > 0)
        {
          DrawCircles(j, rowPos, circleSize, 0, 0, 0);
          score--;
        }
      }
    }
  }
}

public void DrawCircles(int xPos, int yPos, int size, int red, int blue, int green) {
  for (int k = 0; k < 75; k += 5)
  {       
    noStroke();
    fill(red, blue, green, 50+k);
    ellipse(xPos, yPos, size-k-10, size-k-10);
    stroke(0);
  }
}

public void scoreTracker()
{
  if ( score + penalty >= 24)
  {
    score = 0;
    penalty = 0;
    playerPos = 64;
    hammerPosUpdate = 0;
  }
}

//----------------------Functions for Scoring End---------------------

public void drawCharacter(int playerPos, float headCentreY, float headSize)
{
  //head
  fill(0);
  ellipse(playerPos, headCentreY, headSize, headSize);
  //mouth
  fill(200);
  noStroke();
  ellipse(playerPos+headSize/4, headCentreY+headSize/4, headSize/2, headSize/4);
  //body
  stroke(1);
  fill(0);
  triangle(playerPos-headSize/2, headCentreY+headSize, playerPos, headCentreY, playerPos, headCentreY+headSize*1.2f);
}

public void rightDoorTimer()
{
  doorCounter = doorCounter - 1;    
  if (doorCounter <= 0) 
  {
    isDoorOpen = !isDoorOpen;
    doorCounter = (int)random(180, 480);
  }
}
public void drawRightBuilding(float doorX, float doorY) //draw both the right building and the opening and closing door
{
  //draws the door
  if (isDoorOpen)
  {
    noFill();
    rect(doorX, doorY, 48, 87);
    line(doorX+56, doorY-2, doorX+78, doorY-12);
    line(doorX+56, doorY+92, doorX+78, doorY+103);
    line(doorX+78, doorY-12, doorX+78, doorY+103);
    rectMode(CORNERS);
    fill(0);
    quad(doorX+56, doorY+20, doorX+71, doorY+3, doorX+71, doorY+31, doorX+56, doorY+30);
    ellipse(doorX+71, doorY+42, 5, 7);
    rectMode(CORNER);
    moveConstraint = width-64;
  } else {
    fill(200);
    rect(doorX, doorY, 48, 87);
    fill(0);
    rect(doorX+16, doorY+10, 16, 16);
    ellipse(doorX+6, doorY+42, 5, 7);
    moveConstraint = width-128;
  }
  //draws the background building
  strokeWeight(15);
  line(doorX-18, doorY-4, doorX+71, doorY-38);
  line(doorX+71, doorY-38, doorX+475, doorY+170);
  strokeWeight(1);
  line(doorX-10, doorY, doorX-10, doorY+85);
}




//-----------------------------------Hammer Functions---------------------------

public void hammerCollisionChecker()
{
  int distance = (int)dist(hammerPosX, 23+hammerPosUpdate, playerPos, 264);
  if (distance <= 25)
  {
    playerPos = 64;
    penalty += 6;
  }
}

public void hammerTimer()
{ 
  hammerCounter = hammerCounter - 1;
  if (hammerCounter <= 0) 
  {
    if (hammerPosUpdate <= 311)
    {
      hammerPosUpdate += 65;
    } else {
      hammerPosUpdate = 0;
      score += 1;
    }
    hammerCounter = 60;
  }
}

public void drawHammer()
{
  strokeWeight(10);
  line(hammerPosX-12, 16+hammerPosUpdate, hammerPosX+12, 29+hammerPosUpdate);
  strokeWeight(6);
  line(hammerPosX, 23+hammerPosUpdate, hammerPosX+16, -10+hammerPosUpdate);
  strokeWeight(1);
}
//-------------------------------Hammer Functions End------------------------------------


public void drawTrees(float middlePointX, float middlePointY, float size)

{
  noStroke();
  fill(68, 160, 29);
  triangle(middlePointX-size*1.2f, middlePointY+size*2, middlePointX, middlePointY, middlePointX+size*1.2f, middlePointY+size*2);
  triangle(middlePointX-size/1.5f, middlePointY+size/1.5f, middlePointX, middlePointY-size, middlePointX+size/1.5f, middlePointY+size/1.5f);
  triangle(middlePointX-size/2.5f, middlePointY-size+size/2, middlePointX, middlePointY-size*1.5f, middlePointX+size/2.5f, middlePointY-size+size/2);
  stroke(1);
}

public void drawleftBuilding()
{
  //BALCONY
  noStroke();
  fill(234, 161, 12);
  rect(balconyPosX, balconyPosY, 42, 43);
  rect(balconyPosX, balconyPosY-60, 42, 43);
  rect(balconyPosX, balconyPosY-100, 42, 21);
  stroke(234, 161, 12);
  strokeWeight(5);
  line(0, 68, 0, 200);
  fill(255, 162, 176); 
  noStroke();
  rect(balconyPosX+4, balconyPosY+30, 37, 5);
  rect(balconyPosX+4, balconyPosY-30, 37, 5);
  rect(balconyPosX+4, balconyPosY-86, 37, 5);

  //DOOR
  fill(0);
  rect(balconyPosX+13, balconyPosY+60, 16, 16);
  ellipse(balconyPosX+35, balconyPosY+92, 5, 7);
  noFill();
  stroke(1);
  strokeWeight(1);
  rect(balconyPosX, balconyPosY+44, 44, 86);
}
  public void settings() {  size(512, 348); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "assignment_2" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
