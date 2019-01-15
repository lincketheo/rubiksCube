
public class Move{

    //A move can be X+ Y+ Z+ or X- Y- or Z-
    //X rotates a layer (rotateLayer)
    //Y rotates a side (rotateSide)
    //Z rotates a frontFace (rotateFrontFace)
    public String main;
    public int layerNo;
    boolean translation;
    
    //top layer = 0
    //left side = 0
    //back face = 0
    public Move(String _main, int _layerNo){
        layerNo = _layerNo;
        main = _main;
        translation = false;
    }

    public Move(String _main, int _layerNo, boolean _translation){
        layerNo = _layerNo;
        main = _main;
        translation = _translation;
    }

    public boolean equals(Move move){
        return main.equals(move.main) && layerNo == move.layerNo;
    }

    public static void main (String [] args){
        Move move1 = new Move("XX", 1);
        Move move2 = new Move("YY", 1);
        System.out.println(move1.equals(move2));
    }
}