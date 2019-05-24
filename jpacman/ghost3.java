package nl.tudelft.jpacman;
import  java.util.*;

import com.google.common.collect.Lists;
import nl.tudelft.jpacman.Launcher;
import nl.tudelft.jpacman.level.Level;
public class ghost3 extends Launcher {
    @Override
    public Level makeLevel(){
        return getMapParser().parseMap(Lists.newArrayList("GP.##","#####"));
    }
}
