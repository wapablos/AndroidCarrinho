public class CmdMatrix {
    private final char[][] CmdLeave = { { 'f', 'l', 'f', 'l', 'l', ' ', ' ', ' ', ' '}, 
                          { 'f', 'l', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, 
                          { 'f', 'f', 'l', ' ', ' ', ' ', ' ', ' ', ' '}, 
                          { 'f', 'l', 'f', 'l', 'r', ' ', ' ', ' ', ' '}, 
                          { 'f', 'l', 'f', 'f', ' ', ' ', ' ', ' ', ' '}, 
                          { 'f', 'l', 'f', 'r', 'l', ' ', ' ', ' ', ' '} };
    private final char[][] CmdBack  = { { 'f', 'L', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, 
                          { 'f', 'l', 'l', 'f', 'L', ' ', ' ', ' ', ' '}, 
                          { 'f', 'l', 'f', 'l', 'f', 'L', ' ', ' ', ' '}, 
                          { 'f', 'r', 'r', 'f', 'r', 'l', 'f', 'L', ' '}, 
                          { 'f', 'l', 'l', 'f', 'f', 'f', 'L', ' ', ' '}, 
                          { 'f', 'l', 'l', 'f', 'r', 'l', 'f', 'L', ' '} };
    private char[] CmdIda, CmdVolta;

    public void FinalPoint(int fp){
        CmdIda = new char[CmdLeave[0].length];
        CmdVolta = new char[CmdBack[0].length];
        switch(fp){
            case 1:
                for(int i=0;i<CmdLeave[0].length;i++){
                    CmdIda[i] = CmdLeave[0][i];
                    CmdVolta[i] = CmdBack[0][i];
                }
                break;
            case 8:
                for(int i=0;i<CmdLeave[0].length;i++){
                    CmdIda[i] = CmdLeave[1][i];
                    CmdVolta[i] = CmdBack[1][i];
                }
                break;
            case 11:
                for(int i=0;i<CmdLeave[0].length;i++){
                    CmdIda[i] = CmdLeave[2][i];
                    CmdVolta[i] = CmdBack[2][i];
                }
                break;
            case 3:
                for(int i=0;i<CmdLeave[0].length;i++){
                    CmdIda[i] = CmdLeave[3][i];
                    CmdVolta[i] = CmdBack[3][i];
                }
                break;
            case 6:
                for(int i=0;i<CmdLeave[0].length;i++){
                    CmdIda[i] = CmdLeave[4][i];
                    CmdVolta[i] = CmdBack[4][i];
                }
                break;
            case 13:
                for(int i=0;i<CmdLeave[0].length;i++){
                    CmdIda[i] = CmdLeave[5][i];
                    CmdVolta[i] = CmdBack[5][i];
                }
                break;
        }
    }
    
    public char[] getCmdIda() {
        return CmdIda;
    }

    public char[] getCmdVolta() {
        return CmdVolta;
    }      
}
