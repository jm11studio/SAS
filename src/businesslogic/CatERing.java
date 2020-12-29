package businesslogic;

import businesslogic.event.EventManager;
import businesslogic.kitchen.Kitchen;
import businesslogic.kitchen.KitchenManager;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuManager;
import businesslogic.recipe.RecipeManager;
import businesslogic.shift.ShiftManager;
import businesslogic.user.UserManager;
import persistence.MenuPersistence;
import persistence.PersistenceManager;

public class CatERing {
    public static CatERing singleInstance;

    public static CatERing getInstance() {
        if (singleInstance == null) { singleInstance = new CatERing(); }
        return singleInstance;
    }

    private MenuManager menuMgr;
    private RecipeManager recipeMgr;
    private UserManager userMgr;
    private EventManager eventMgr;
    private KitchenManager kitchenManager;
    private ShiftManager shiftManager;

    public MenuPersistence menuPersistence;

    public CatERing() {
        menuMgr = new MenuManager();
        recipeMgr = new RecipeManager();
        userMgr = new UserManager();
        eventMgr = new EventManager();
        menuPersistence = new MenuPersistence();
        menuMgr.addEventReceiver(menuPersistence);
    }


    public MenuManager getMenuManager() {
        return menuMgr;
    }

    public RecipeManager getRecipeManager() {
        return recipeMgr;
    }

    public UserManager getUserManager() {
        return userMgr;
    }

    public EventManager getEventManager() { return eventMgr; }

    public KitchenManager getKitchenManager() { return kitchenManager; }

    public ShiftManager getShiftManager() { return shiftManager; }
}
