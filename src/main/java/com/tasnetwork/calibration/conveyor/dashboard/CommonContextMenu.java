package com.tasnetwork.calibration.conveyor.dashboard;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;

public abstract class CommonContextMenu {
    private final ContextMenu contextMenu;
    private final String bayTypeKey;
    
    protected final BayActionHandler actionHandler;

    public CommonContextMenu(String bayTypeKey) {
    	this.actionHandler = new BayActionHandler(bayTypeKey);
        this.bayTypeKey = bayTypeKey;
        this.contextMenu = new ContextMenu();
        initializeMenuItems();
    }

    private void initializeMenuItems() {
            addMenuItem(PalletController.BayActionType.BAY_START);
            addMenuItem(PalletController.BayActionType.BAY_STOP);
            addMenuItem(PalletController.BayActionType.RUN_BAY_ONCE);
            addMenuItem(PalletController.BayActionType.RELEASE_METER_FROM_BAY);
            contextMenu.getItems().add(new javafx.scene.control.SeparatorMenuItem());

        if (bayTypeKey.equals(ConstantConveyor.FT_BAY_KEY)) {										// FUNCTIONAL
            //addMenuItem(PalletController.BayActionType.FINGERTIP_ENGAGE);
            //addMenuItem(PalletController.BayActionType.FINGERTIP_DISENGAGE);

        	addFingertipSubmenu();
            addMenuItem(PalletController.BayActionType.SOURCE_START);
            addMenuItem(PalletController.BayActionType.SOURCE_STOP);           
            addMenuItem(PalletController.BayActionType.DIVERTER_UP);
            addMenuItem(PalletController.BayActionType.DIVERTER_DOWN);
            addMenuItem(PalletController.BayActionType.BLOCK_BAY_ENTRY);
            addMenuItem(PalletController.BayActionType.UNBLOCK_BAY_ENTRY);
            addMenuItem(PalletController.BayActionType.UNBLOCK_BAY_EXIT);
            addMenuItem(PalletController.BayActionType.REFRESH);
            addNoEntrySubmenu();
            addHaltPalletSubmenu();
            addBypassSubmenu();
        } else if (bayTypeKey.equals(ConstantConveyor.HV_BAY_KEY)) {								// HIGH VOLTAGE
            //addMenuItem(PalletController.BayActionType.FINGERTIP_ENGAGE);
           // addMenuItem(PalletController.BayActionType.FINGERTIP_DISENGAGE);
        	addFingertipSubmenu();
            addMenuItem(PalletController.BayActionType.BLOCK_BAY_EXIT);
            addMenuItem(PalletController.BayActionType.REFRESH);
            addNoEntrySubmenu();
            addHaltPalletSubmenu();
            addBypassSubmenu();
        } else if (bayTypeKey.equals(ConstantConveyor.IR_BAY_KEY)) {								// INSULATION RESISTANCE
            //addMenuItem(PalletController.BayActionType.FINGERTIP_ENGAGE);
            //addMenuItem(PalletController.BayActionType.FINGERTIP_DISENGAGE);
        	addFingertipSubmenu();
            addMenuItem(PalletController.BayActionType.BLOCK_BAY_EXIT);
            addMenuItem(PalletController.BayActionType.REFRESH);
            addNoEntrySubmenu();
            addHaltPalletSubmenu();
            addBypassSubmenu();
        } else if (bayTypeKey.equals(ConstantConveyor.CALIBRATION_BAY_KEY)) {						// CALIBRATION
            //addMenuItem(PalletController.BayActionType.FINGERTIP_ENGAGE);
            //addMenuItem(PalletController.BayActionType.FINGERTIP_DISENGAGE);
        	addFingertipSubmenu();
        	//addNoEntrySubmenu();
            addMenuItem(PalletController.BayActionType.SOURCE_START);
            addMenuItem(PalletController.BayActionType.CURRENT_STOP);
            addMenuItem(PalletController.BayActionType.SOURCE_STOP);
            addMenuItem(PalletController.BayActionType.MAIN_CT);
            addMenuItem(PalletController.BayActionType.NEUTRAL_CT);
            addMenuItem(PalletController.BayActionType.BLOCK_BAY_EXIT);
            
            addMenuItem(PalletController.BayActionType.REFRESH);
            addNoEntrySubmenu();
            addHaltPalletSubmenu();
            addBypassSubmenu();
        } else if (bayTypeKey.startsWith(ConstantConveyor.WAITING_BAY_KEY)) {						// WAITING 
        	addMenuItem(PalletController.BayActionType.BLOCK_BAY_EXIT);
            addMenuItem(PalletController.BayActionType.PALLETS_CLEARED);
            //addMenuItem(PalletController.BayActionType.PALLETS_BLOCK);
            addMenuItem(PalletController.BayActionType.REFRESH);
            addNoEntrySubmenu();
            addHaltPalletSubmenu();
            addBypassSubmenu();
		} else if (bayTypeKey.startsWith(ConstantConveyor.VERIFICATION_BAY_KEY)) {					// VERIFICATION
			//addMenuItem(PalletController.BayActionType.FINGERTIP_ENGAGE);
            //addMenuItem(PalletController.BayActionType.FINGERTIP_DISENGAGE);  
			addFingertipSubmenu();
			//addNoEntrySubmenu();
            addMenuItem(PalletController.BayActionType.BLOCK_BAY_EXIT);
            addMenuItem(PalletController.BayActionType.BLOCK_BAY_EXIT2);
            addMenuItem(PalletController.BayActionType.DIVERTER_UP);
            addMenuItem(PalletController.BayActionType.DIVERTER_DOWN);
            addMenuItem(PalletController.BayActionType.PALLETS_CLEARED);
            //addMenuItem(PalletController.BayActionType.PALLETS_BLOCK);
            addMenuItem(PalletController.BayActionType.REFRESH);
            addNoEntrySubmenu();
            addHaltPalletSubmenu();
            addBypassSubmenu();
		} else if (bayTypeKey.startsWith(ConstantConveyor.STA_NLD1_BAY_KEY)) {						// STA NLD BAY 1
			//addMenuItem(PalletController.BayActionType.FINGERTIP_ENGAGE);
            //addMenuItem(PalletController.BayActionType.FINGERTIP_DISENGAGE);    
			addFingertipSubmenu();
			//addNoEntrySubmenu();
            addMenuItem(PalletController.BayActionType.BLOCK_BAY_EXIT);
            addMenuItem(PalletController.BayActionType.BLOCK_BAY_EXIT2);
            addMenuItem(PalletController.BayActionType.DIVERTER_UP);
            addMenuItem(PalletController.BayActionType.DIVERTER_DOWN);
            addMenuItem(PalletController.BayActionType.REFRESH);
            addNoEntrySubmenu();
            addHaltPalletSubmenu();
            addBypassSubmenu();
		} else if (bayTypeKey.startsWith(ConstantConveyor.STA_NLD2_BAY_KEY)) {						// STA NLD BAY 2
			//addMenuItem(PalletController.BayActionType.FINGERTIP_ENGAGE);
            //addMenuItem(PalletController.BayActionType.FINGERTIP_DISENGAGE); 
			addFingertipSubmenu();
			//addNoEntrySubmenu();
            addMenuItem(PalletController.BayActionType.BLOCK_BAY_EXIT);
            addMenuItem(PalletController.BayActionType.DIVERTER_UP);
            addMenuItem(PalletController.BayActionType.DIVERTER_DOWN);
            addMenuItem(PalletController.BayActionType.REFRESH);
            addNoEntrySubmenu();
            addHaltPalletSubmenu();
            addBypassSubmenu();
		}else if (bayTypeKey.startsWith(ConstantConveyor.REJECTION_BAY_KEY)) {						// STA NLD BAY 2

            addMenuItem(PalletController.BayActionType.REFRESH);
            addNoEntrySubmenu();
            addBypassSubmenu();
		}else if (bayTypeKey.startsWith(ConstantConveyor.UNLOADING_BAY_KEY)) {						// STA NLD BAY 2

            addMenuItem(PalletController.BayActionType.REFRESH);
            addNoEntrySubmenu();
            addBypassSubmenu();
		}
        else {
            ApplicationLauncher.logger.warn("CommonContextMenu: Unknown bayType: <" + bayTypeKey + ">");
        }
    }

    private void addMenuItem(PalletController.BayActionType actionType) {
        MenuItem menuItem = new MenuItem(actionType.toString());
        menuItem.setOnAction(e -> handleAction(actionType));
        contextMenu.getItems().add(menuItem);
    }
    
    private void addFingertipSubmenu() {
        // Create the main Fingertip menu
        Menu fingertipMenu = new Menu("Fingertip");
        
        // Create the submenu items
        MenuItem engageItem = new MenuItem(PalletController.BayActionType.FINGERTIP_ENGAGE.toString());
        engageItem.setOnAction(e -> handleAction(PalletController.BayActionType.FINGERTIP_ENGAGE));
        
        MenuItem disengageItem = new MenuItem(PalletController.BayActionType.FINGERTIP_DISENGAGE.toString());
        disengageItem.setOnAction(e -> handleAction(PalletController.BayActionType.FINGERTIP_DISENGAGE));
        
        // Add the submenu items to the main menu
        fingertipMenu.getItems().addAll(engageItem, disengageItem);
        
        // Add the main menu to the context menu
        contextMenu.getItems().add(fingertipMenu);
    }
    
    private void addNoEntrySubmenu() {
        // Create the main Fingertip menu
        Menu noEntryMenu = new Menu("NoEntry");
        
        // Create the submenu items
        MenuItem noEntryActiveItem = new MenuItem(PalletController.BayActionType.NO_ENTRY_ACTIVE.toString());
        noEntryActiveItem.setOnAction(e -> handleAction(PalletController.BayActionType.NO_ENTRY_ACTIVE));
        
        MenuItem noEntryInactiveItem = new MenuItem(PalletController.BayActionType.NO_ENTRY_INACTIVE.toString());
        noEntryInactiveItem.setOnAction(e -> handleAction(PalletController.BayActionType.NO_ENTRY_INACTIVE));
        
        // Add the submenu items to the main menu
        noEntryMenu.getItems().addAll(noEntryActiveItem, noEntryInactiveItem);
        
        // Add the main menu to the context menu
        contextMenu.getItems().add(noEntryMenu);
    }
    
    private void addHaltPalletSubmenu() {
        // Create the main Fingertip menu
        Menu haltPalletMenu = new Menu("Halt Pallet");
        
        // Create the submenu items
        MenuItem haltPalletActiveItem = new MenuItem(PalletController.BayActionType.HALT_PALLET_ACTIVE.toString());
        haltPalletActiveItem.setOnAction(e -> handleAction(PalletController.BayActionType.HALT_PALLET_ACTIVE));
        
        MenuItem haltPalletInactiveItem = new MenuItem(PalletController.BayActionType.HALT_PALLET_INACTIVE.toString());
        haltPalletInactiveItem.setOnAction(e -> handleAction(PalletController.BayActionType.HALT_PALLET_INACTIVE));
        
        // Add the submenu items to the main menu
        haltPalletMenu.getItems().addAll(haltPalletActiveItem, haltPalletInactiveItem);
        
        // Add the main menu to the context menu
        contextMenu.getItems().add(haltPalletMenu);
    }

    private void addBypassSubmenu() {
        Menu bypassMenu = new Menu("Bypass Mode");
        
        MenuItem bypassActiveItem = new MenuItem(PalletController.BayActionType.BYPASS_MODE_ACTIVE.toString());
        bypassActiveItem.setOnAction(e -> handleAction(PalletController.BayActionType.BYPASS_MODE_ACTIVE));
        
        MenuItem bypassInactiveItem = new MenuItem(PalletController.BayActionType.BYPASS_MODE_INACTIVE.toString());
        bypassInactiveItem.setOnAction(e -> handleAction(PalletController.BayActionType.BYPASS_MODE_INACTIVE));
        
        bypassMenu.getItems().addAll(bypassActiveItem, bypassInactiveItem);
        
        contextMenu.getItems().add(bypassMenu);
    }

    // Abstract method to be implemented by concrete classes
    protected abstract void handleAction(PalletController.BayActionType actionType);

    
    protected boolean shouldShowMenu() {
        return true; // Default implementation always shows menu
    }
    public void attachToNode(Node node) {
    	node.setOnContextMenuRequested(event -> {
            if (contextMenu != null && shouldShowMenu()) {
                contextMenu.show(node, event.getScreenX(), event.getScreenY());
            }
        });

        // Hide menu on left click
        node.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                contextMenu.hide();
            }
        });
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }
}