package de.chris.generator;

import de.chris.generator.generator.FloatGenerator;
import de.chris.generator.generator.MaxChangeRate;
import de.chris.generator.generator.MaxDecreasingRate;
import de.chris.generator.generator.MaxIncreasingRate;

/**
 * Prototype for measure generator
 */
public class GeneratorPrototype {
    public static void main(String[] args) {
        FloatGenerator fg = new FloatGenerator(1500.0f, 4f, new MaxDecreasingRate(0.09f));
        //FloatGenerator fg = new FloatGenerator(600f, 136, 1500f, 4f, new MaxChangeRate(0.25f));

        int maxLoops = 10000;
        int loops = 0;

        while(fg.hasNext() && loops < maxLoops) {
            System.out.println(fg.next());
            loops++;
        }

        System.out.println("Loops: " + loops);
    }
}
