import static javax.swing.JOptionPane.*;

PrintWriter output;
String saveFilePath;
int tileWidth, tileHeight;
int gridWidth = 50;
int gridHeight = 50;

final color dirtColor = color(182,159,102);
final color borderColor = color(255, 0 , 0);
final color roadColor = #D2D1CD;
final color blankColor = color(100, 100, 100);
final color HealthPower = color(0, 255, 0);
final color SlowPower = color(255, 0, 0);
final color SpeedPower = color(0, 100, 0);
final color[] Types = {blankColor, dirtColor, roadColor, borderColor};
int index = 0;

ArrayList<ColorButton> uiElements;

HashMap<Integer, String> typeMap = new HashMap<Integer, String>();{
  typeMap.put(dirtColor, "Dirt");
  typeMap.put(borderColor, "Wall");
  typeMap.put(roadColor, "Road");
  typeMap.put(blankColor, "Blank");
  typeMap.put(HealthPower, "HealthPowerUp");
  typeMap.put(SpeedPower, "SpeedPowerUp");
  typeMap.put(SlowPower, "SlowPowerUp");
}

color PushPower = Types[index];

Tile[][] createTiles(int w, int h){
  Tile[][] tiles = new Tile[h][w];
  for(int col = 0; col< h; col++){
    for(int row= 0; row< w; row++){
      tiles[col][row] = new Tile((row * tileWidth) + 50 , col * tileHeight, blankColor);
    }
  }
  return tiles;
}
Map map;

ArrayList<ColorButton> createUi(){
  int i = 0;
  int elementCount = typeMap.size();
  float h = height / elementCount;
  ArrayList<ColorButton> colorButtons = new ArrayList();
  for(Integer c: typeMap.keySet()){
    ColorButton b = new ColorButton(i * h ,h, c, typeMap.get(c)); 
    colorButtons.add(b);
    i ++;
  }
  return colorButtons;
  
}

void setup(){
  size(600, 400);
  uiElements = createUi();
  tileWidth = (int)(width - ColorButton.w) / gridWidth;
  tileHeight = height  / gridHeight;
  map = new Map(gridWidth, gridHeight);
  drawUi();
}

void draw(){
  background(0);
  map.draw();
  drawUi();
  drawCircle();
}

void drawUi(){
   for(ColorButton b : uiElements){
    b.draw();
  } 
}

void drawCircle(){
  ellipseMode(RADIUS);
  fill(PushPower);
  stroke(0);
  ellipse(mouseX, mouseY, 10, 10);
}

void mousePressed(){
  if (isOnMap()){
    map.updateTile(mouseX, mouseY, PushPower);
  }else{
    for(ColorButton b : uiElements){
      if (b.mouseOn()){
        PushPower = b.c;
      }
    }
  }
}

void mouseDragged(){
  mousePressed();
}

boolean isOnMap(){
  return mouseX > ColorButton.w;
}

void mouseWheel(MouseEvent event) {
    float e = event.getCount();
    index += (int)abs(e);
    index %= Types.length;
    PushPower = Types[index];
}

void keyPressed(){
  if(key == 'p'){    
     selectOutput("Save image as:", "outfileSelected");
     }
}

void outfileSelected(File selection){
  if (selection == null) {
    println("IMAGE NOT SAVED: Window was closed or the user hit cancel.");
    saveFilePath = "output.json";
    } else {
    println("IMAGE SAVED: User selected " + selection.getAbsolutePath());
    saveFilePath = selection.getAbsolutePath();
  }
  println("what?");
    output = createWriter(saveFilePath);
    output.println(map.toJson());
    output.flush();
    output.close();
    println(map.toJson());
}
