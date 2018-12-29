import static javax.swing.JOptionPane.*;

PrintWriter output;
String saveFilePath;
int tileSize = 100;
int gridWidth, gridHeight;

final color dirtColor = color(182,159,102);
final color borderColor = color(255, 0 , 0);
final color roadColor = #D2D1CD;
final color blankColor = color(100, 100, 100); 
final color[] Types = {blankColor, dirtColor, roadColor, borderColor};
int index = 0;

HashMap<Integer, String> typeMap = new HashMap<Integer, String>();{
  typeMap.put(dirtColor, "SlowPowerUp");
  typeMap.put(borderColor, "HealthPowerUp");
  typeMap.put(roadColor, "SpeedPowerUp");
  typeMap.put(blankColor, "blank");
}

color PushPower = Types[index];

Tile[][] createTiles(int w, int h){
  Tile[][] tiles = new Tile[h][w];
  for(int col = 0; col< h; col++){
    for(int row= 0; row< w; row++){
      tiles[col][row] = new Tile(row * tileSize, col * tileSize, blankColor);
    }
  }
  return tiles;
}
Map map;

void drawUi(){
   
}

void setup(){
  size(600, 400);
  drawUi();
  gridWidth = width / tileSize;
  gridHeight = height / tileSize;
  map = new Map(gridWidth, gridHeight);    

}

void draw(){
  map.draw();
  drawCircle();
}

void drawCircle(){
  ellipseMode(RADIUS);
  fill(PushPower);
  stroke(0);
  ellipse(mouseX, mouseY, 10, 10);
}

void mousePressed(){
  if (mouseX >= 0 && mouseY >= 0){
    map.updateTile(mouseX, mouseY, PushPower);
  }
}

void mouseDragged(){
  mousePressed();
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
