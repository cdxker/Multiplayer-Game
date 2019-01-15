public class ColorButton{
  // simple button that chagnes the global color to set things to 
  
  color c;
  float y, h;
  final static float x = 0;
  final static float w = 50;
  String text;
  
  
  ColorButton(float y, float h, color c, String text){
    this.y = y;
    this.h = h;
    this.c = c;
    this.text = text;
  }
  
  void draw(){
    fill(this.c);
    rect(x, y, w, h);
    fill(0);
    text(this.text, x, y);
  }
  
  boolean mouseOn(){
    return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
  }
  
}
