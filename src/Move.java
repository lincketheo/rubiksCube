public class Move{

    //A move can be X+ Y+ Z+ or X- Y- or Z-
    //X rotates a layer (rotateLayer)
    //Y rotates a side (rotateSide)
    //Z rotates a frontFace (rotateFrontFace)
    public String main;
    public int layerNo;
    
    //top layer = 0
    //left side = 0
    //back face = 0
    public Move(String _main, int _layerNo){
        layerNo = _layerNo;
        main = _main;
    }
}