import org.tbot.bot.TBot;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.internal.handlers.RandomHandler;
import org.tbot.methods.Game;
import org.tbot.methods.Players;
import org.tbot.methods.Random;
import org.tbot.util.botcontrol.BotControlConnection;
import org.tbot.util.botcontrol.BotControlMessageEvent;
import org.tbot.util.botcontrol.BotControlMessageListener;
import org.tbot.wrappers.Player;
import java.io.IOException;

@Manifest(name = "RatZ Scouter v1", authors = "SKENGRAT", version = 0.1, description = "a lil rat slave peeps worlds 4 u ;l")
public class RatFinder extends AbstractScript implements BotControlMessageListener {

    String server = "irc.swiftirc.net";
   // String channelpass = "passwordBIH";
    String nickname = "ratslave67";
    String channel = "#ratz";


    Player localclient = null;
    private final BotControlConnection connection;
    private static final int[] W0RLDS = {301,308,316,326,335,382,383,384,393,394};
    private static int index = 0;

    public RatFinder() {
        this.connection = new BotControlConnection(server, nickname);
        this.connection.setChannelName(channel);
      //  this.connection.setChannelPassword(channelpass);
        try {
            this.connection.connect();
            if (!this.connection.isClosed()) {

                this.connection.joinChannel();
                this.connection.addListener(this);
                this.connection.startListening();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onStart() {
        return true;

    //    try {
    //        cursor = ImageIO.read(new URL("https://i.imgur.com/H680Jjy.png"));
    //    }
    //    catch (MalformedURLException e) {
    //        log("Error in retrieving mouse cursor!");
    //    }
    //    catch (IOException e) {
   //        log("Error in retrieving mouse cursor!");
   //     }

    }

    private boolean messageSend = false;
    private boolean hopping;

    @Override
    public int loop() {
        if (hopping) {
            TBot.getBot().getScriptHandler().getRandomHandler().get(RandomHandler.AUTO_LOGIN).enable();
        } else {
            TBot.getBot().getScriptHandler().getRandomHandler().get(RandomHandler.AUTO_LOGIN).disable();
        }
        if (!Game.isLoggedIn()) {
            return 600;
        }

        Player localclient = Players.getLocal();
        if (messageSend == true) {
            Player[] players = Players.getLoaded();

            if (players.length > 1) {
                for (Player p : players) {
                    if (p != null && !localclient.equals(p)) {

                        this.connection.sendMessage("-- " + p.getName() + " [Cmb: " + p.getCombatLevel() + "]" + " [Skulled?: " + p.isSkulled() + "]");
                       // this.connection.sendMessage("[test: " + System.out.println(p.getAppearanceEquipment()); + "]");

                        sleep(500,777);

                    }
                }
            } else {
                int currentWorld = Game.getCurrentWorld();
                this.connection.sendMessage("Dam "+ currentWorld + " is empty.");
            }
            messageSend = false;
        }
        randhop();

        return 600;
    }
    public void randhop() {
        if(hopping) {
            //Game.instaHopRandomF2P();
            sleep(45000,68000);
           // Game.hopNextF2P();
            int worldz = getNextRandomWorld();
            Game.instaHop(worldz);
            int currentWorld = Game.getCurrentWorld();
            this.connection.sendMessage("World " + currentWorld + ".");
            messageSend = true;

        } else {
            Game.logout();
        }

    }
    public static int getNextRandomWorld(int[] array){
        int random = Random.nextInt(array.length);
        return array[random];
    }

    public static int getNextRandomWorld() {
        return getNextRandomWorld(W0RLDS);
    }
    @Override
    public void messageReceived(BotControlMessageEvent bcme) {
        LogHandler.log(bcme.getMessage());
        if (bcme.getMessage().equalsIgnoreCase("!log")) {
            Game.logout();
        }
        if (bcme.getMessage().startsWith("!world ")) {
            int world = Integer.parseInt(bcme.getMessage().toLowerCase()
                    .replace("!world ", ""));
            Game.instaHop(world);
            this.connection.sendMessage(bcme.getSender() + ": New World "
                    + world + ".");
        }
       // if (bcme.getMessage().equalsIgnoreCase("!hop")) {
           // int world = Random.nextInt(302, 386);
           // Game.instaHop(world);
         //   this.connection.sendMessage(bcme.getSender() + ": New World "
         //           + world + ".");
      //  }
        if (bcme.getMessage().equalsIgnoreCase("!scout")) {
            Game.instaHopRandomF2P();
            int currentWorld = Game.getCurrentWorld();
            this.connection.sendMessage(bcme.getSender() + ": New World "
                    + currentWorld + ".");
            messageSend = true;
        }
        if (bcme.getMessage().equalsIgnoreCase("!start")) {
            hopping = true;
            this.connection.sendMessage(bcme.getSender() + " started RatZ Scouter. Please wait.");
        }
        if (bcme.getMessage().equalsIgnoreCase("!stop")) {
            hopping = false;
            this.connection.sendMessage(bcme.getSender() + " stopped RatZ Scouter. Orderin som cheez blocks");
        }
    }

}
