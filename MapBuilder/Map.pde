class Map{
  private Tile[][] tiles;
  int w, h;
  
  public Map(int grid_width, int grid_height){
    tiles = createTiles(grid_width, grid_height);
    w = grid_width;
    h = grid_height;
  }
  public void draw(){
    float i=0;
    for (Tile[] row : tiles){
      float j =0;
      for(Tile tile : row){  
        tile.draw();
        j++;
      }
    }
    i++;
  }

  public String toJson(){
    String tiles_json = "";
    for(Tile[] t_list: tiles){
      for(Tile tile: t_list){
        tiles_json += tile.toJson() + ",\n";
      }  
    }
    tiles_json = tiles_json.substring(0, tiles_json.length() - 2) + '\n';
    return String.format("{ \"tiles\":[%s], \"gridSize\": {\"x\": %d,\"y\": %d,\"hash\": 0 }}", tiles_json, w, h);
  }
     
  public void updateTile(float x, float y, color newColor){
    int xPos = (int)x / w;
    int yPos = (int)y / h;
    for(Tile[] t_list : tiles){
      for(Tile tile : t_list){
        if(tile.mouseOver()) tile.setColor(newColor);
      }
    }
    print("size: " + tiles.length + " ");
    println(xPos +", "+ yPos);
  }
  
}
