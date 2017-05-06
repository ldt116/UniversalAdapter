package me.timos.thuanle.example;

/**
 * Created by thuanle on 5/6/17.
 */

public class HeroService {
    public static Hero[] getHeroes() {
        return new Hero[]{
                new Hero("Nature's Prophet", "http://cdn.dota2.com/apps/dota2/images/heroes/furion_vert.jpg", "intelligence"),
                new Hero("Anti-Mage", "http://cdn.dota2.com/apps/dota2/images/heroes/antimage_vert.jpg", "agility"),
                new Hero("Monkey King", "http://cdn.dota2.com/apps/dota2/images/heroes/monkey_king_vert.jpg", "agility"),
                new Hero("Sven", "http://cdn.dota2.com/apps/dota2/images/heroes/sven_vert.jpg", "strength"),
                new Hero("Troll Warlord", "http://cdn.dota2.com/apps/dota2/images/heroes/troll_warlord_vert.jpg", "agility")};
    }
}
