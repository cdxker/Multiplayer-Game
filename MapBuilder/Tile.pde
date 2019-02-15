class Tile{
  public final float x;
  public final float y;
  public String type;
  public String id;
  color c;
  
  Tile(){
    this(0, 0, color(0,0,0));
  }
  
  Tile(float x, float y, color c){
      this.x = x;
      this.y = y;
      this.c = c;
      this.id = Integer.toString(hashCode());
      println(x);
  }
  
  public String toJson(){

    return String.format("{\n\"uniqueId\": \"%s\",\n\"type\": \"%s\",\n\"pos\": {\n  \"x\": %f,\n  \"y\": %f,\n  \"hash\": 0\n}}", id, typeMap.get(c), (x-50)/tileWidth, y/tileHeight); // Divide by tilesize to get index
  }
  
  private void setColor(color c){
    this.c = c;
  }
  
  public boolean mouseOver(){
    return mouseX >= x && mouseX <= x + tileWidth && mouseY >= y && mouseY <= y + tileHeight;
  }
  
  public void draw(){
    fill(c);
    rect(x, y, tileWidth, tileHeight);
  }
  
  public String toString(){
    return "x: " +x+",y:" +"color: "+c;
  }
  
}
