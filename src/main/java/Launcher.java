public class Launcher {

    public static void main(String[] args) {
        if(args.length > 0 && args[0].equals("client")) {
            System.out.println("Running client");
            ClientApplication.main(args);
        } else {
            System.out.println("Running server");
            ServerApplication.main(args);
        }
    }
}