package maprouting;

public class MapRouting {
    
    public static void main(String[] args) {
       CmdMatrix cmd = new CmdMatrix();
       cmd.FinalPoint(13);
       char[] Ida = cmd.getCmdIda(), Volta = cmd.getCmdVolta();
        System.out.print("Ida: ");
        for(int i=0;i<cmd.getCmdIda().length;i++){
            if(Ida[i] != ' ')
                System.out.print(Ida[i]);
            else{
                System.out.println();
                break;
            }
        }
        System.out.print("Volta: ");
        for(int i=0;i<cmd.getCmdVolta().length;i++){
            if(Volta[i] != ' ')
                System.out.print(Volta[i]);
            else{
                System.out.println();
                break;
            }
        }
    }
    
}
