package pl.dominik.hinc.bounceback.tools;

import com.badlogic.gdx.Preferences;

import pl.dominik.hinc.bounceback.BounceBack;

public class ScoreBoardManager {

    private BounceBack context;
    private Preferences preferences;

    public long jumps,points,deaths,powerUps,shields,plusFive,randomTeleport;


    public ScoreBoardManager(BounceBack context){
        this.context = context;
        this.preferences = context.getPreferences();
        jumps = preferences.getLong(BounceBack.TOTALJUMPSPREF);
        points = preferences.getLong(BounceBack.TOTALPOINTSPREF);
        deaths = preferences.getLong(BounceBack.TOTALDEATHSPREF);
        powerUps = preferences.getLong(BounceBack.TOTALPOWERUPSPREF);
        shields = preferences.getLong(BounceBack.TOTALSHIELDPREF);
        plusFive = preferences.getLong(BounceBack.TOTALPLUSFIVEPREF);
        randomTeleport = preferences.getLong(BounceBack.TOTALRANDOMTELEPORTPREF);
    }
    public void flushPrefs(){
        preferences.putLong(BounceBack.TOTALJUMPSPREF,jumps);
        preferences.putLong(BounceBack.TOTALPOINTSPREF,points);
        preferences.putLong(BounceBack.TOTALDEATHSPREF,deaths);
        preferences.putLong(BounceBack.TOTALPOWERUPSPREF,powerUps);
        preferences.putLong(BounceBack.TOTALSHIELDPREF,shields);
        preferences.putLong(BounceBack.TOTALPLUSFIVEPREF,plusFive);
        preferences.putLong(BounceBack.TOTALRANDOMTELEPORTPREF,randomTeleport);
        preferences.flush();
    }

    public void addJumps(){
        jumps++;
    }
    public void addPoints(){
        points++;
    }
    public void addDeaths(){
        deaths++;
    }
    public void addPowerUps(){
        powerUps++;
    }
    public void addShields(){
        shields++;
    }
    public void addPlusFive(){
        plusFive++;
    }
    public void addRandomTeleport(){
        randomTeleport++;
    }

}
