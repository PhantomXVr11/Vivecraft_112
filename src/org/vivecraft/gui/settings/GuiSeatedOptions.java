package org.vivecraft.gui.settings;

import org.vivecraft.gui.framework.BaseGuiSettings;
import org.vivecraft.gui.framework.GuiButtonEx;
import org.vivecraft.gui.framework.GuiEventEx;
import org.vivecraft.gui.framework.GuiSliderEx;
import org.vivecraft.gui.framework.GuiSmallButtonEx;
import org.vivecraft.settings.VRSettings;
import org.vivecraft.settings.VRSettings.VrOptions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiSeatedOptions extends BaseGuiSettings implements GuiEventEx
{
	static VRSettings.VrOptions[] seatedOptions = new VRSettings.VrOptions[] {
			VRSettings.VrOptions.X_SENSITIVITY,
			VRSettings.VrOptions.Y_SENSITIVITY,
			VRSettings.VrOptions.KEYHOLE,
            VRSettings.VrOptions.SEATED_HUD_XHAIR,
            VRSettings.VrOptions.WORLD_ROTATION_INCREMENT        
	};
	
	
    static VRSettings.VrOptions[] teleportSettings = new VRSettings.VrOptions[]
    {
            VRSettings.VrOptions.LIMIT_TELEPORT,
            VRSettings.VrOptions.SIMULATE_FALLING,
            VRSettings.VrOptions.TELEPORT_UP_LIMIT,
            VRSettings.VrOptions.TELEPORT_DOWN_LIMIT,
            VRSettings.VrOptions.TELEPORT_HORIZ_LIMIT

    };
    static VRSettings.VrOptions[] freeMoveSettings = new VRSettings.VrOptions[]
    {
        VRSettings.VrOptions.SEATED_HMD,
        VRSettings.VrOptions.FOV_REDUCTION,
    };

	public GuiSeatedOptions(GuiScreen guiScreen, VRSettings guivrSettings) {
		super( guiScreen, guivrSettings );
		screenTitle = "Seated Settings";
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		this.buttonList.clear();
		this.buttonList.add(new GuiButtonEx(ID_GENERIC_DEFAULTS, this.width / 2 - 155 ,  this.height -25 ,150,20, "Reset To Defaults"));
		this.buttonList.add(new GuiButtonEx(ID_GENERIC_DONE, this.width / 2 - 155  + 160, this.height -25,150,20, "Done"));
        mc.vrSettings.vrFreeMove = mc.vrPlayer.getFreeMove();

		GuiSmallButtonEx mode = new GuiSmallButtonEx(VRSettings.VrOptions.MOVE_MODE.returnEnumOrdinal(), this.width / 2 - 68, this.height / 6 + 80,VRSettings.VrOptions.MOVE_MODE, this.guivrSettings.getKeyBinding(VRSettings.VrOptions.MOVE_MODE));
        mode.setEventHandler(this);
        this.buttonList.add(mode);

		VRSettings.VrOptions[] buttons = seatedOptions;

		addButtons(buttons, 0);
		
        if(mc.vrPlayer.getFreeMove())
        	addButtons(freeMoveSettings,104);
        else
        	addButtons(teleportSettings,104); 
        
	}

	private void addButtons(VRSettings.VrOptions[] buttons, int y) {
		for (int var12 = 2; var12 < buttons.length + 2; ++var12)
		{
			VRSettings.VrOptions var8 = buttons[var12 - 2];
			int width = this.width / 2 - 155 + var12 % 2 * 160;
			int height = this.height / 6 + 21 * (var12 / 2) - 10 + y;

			if (var8 == VRSettings.VrOptions.DUMMY)
				continue;
			
			boolean show = true;
			
			if (var8.getEnumFloat())
			{
				float minValue = 0.0f;
				float maxValue = 1.0f;
				float increment = 0.01f;

				if (var8 == VRSettings.VrOptions.X_SENSITIVITY)
				{
					minValue = 0.1f;
					maxValue = 5f;
					increment = 0.01f;
				}
				else if (var8 == VRSettings.VrOptions.Y_SENSITIVITY)
				{
					minValue = 0.1f;
					maxValue = 5f;
					increment = 0.01f;
				}
				else if (var8 == VRSettings.VrOptions.KEYHOLE)
				{
					minValue = 0f;
					maxValue = 40f;
					increment = 5f;
				}
                else if (var8 == VrOptions.WORLD_ROTATION_INCREMENT){
                    minValue = -1f;
                    maxValue = 4f;
                    increment = 1f;
                }
                else if(var8 == VrOptions.TELEPORT_DOWN_LIMIT){
                    minValue = 0f;
                    maxValue = 16f;
                    increment = 1f;
                    show = this.guivrSettings.vrLimitedSurvivalTeleport;
                }
                else if(var8 == VrOptions.TELEPORT_UP_LIMIT){
                    minValue = 0f;
                    maxValue = 4f;
                    increment = 1f;
                    show = this.guivrSettings.vrLimitedSurvivalTeleport;
                }
                else if(var8 == VrOptions.TELEPORT_HORIZ_LIMIT){
                    minValue = 0f;
                    maxValue = 32f;
                    increment = 1f;
                    show = this.guivrSettings.vrLimitedSurvivalTeleport;
                }
				if(show) {
					GuiSliderEx slider = new GuiSliderEx(var8.returnEnumOrdinal(), width, height , var8, this.guivrSettings.getKeyBinding(var8), minValue, maxValue, increment, this.guivrSettings.getOptionFloatValue(var8));
					slider.setEventHandler(this);
					this.buttonList.add(slider);
				}
			}
			else
			{
				this.buttonList.add(new GuiSmallButtonEx(var8.returnEnumOrdinal(), width, height , var8, this.guivrSettings.getKeyBinding(var8)));
			}
		}
	}
	

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.enabled)
		{
			if (par1GuiButton.id == ID_GENERIC_DONE)
			{
				Minecraft.getMinecraft().vrSettings.saveOptions();
				this.mc.displayGuiScreen(this.parentGuiScreen);
			}
			else if (par1GuiButton.id == ID_GENERIC_DEFAULTS)
			{
				VRSettings vrSettings=Minecraft.getMinecraft().vrSettings;
				vrSettings.keyholeX=15;
				vrSettings.xSensitivity=1;
				vrSettings.ySensitivity=1;
				vrSettings.vrFreeMove = true;
				vrSettings.useFOVReduction = false;
				vrSettings.vrFreeMove = true;
				vrSettings.seatedUseHMD = false;
				vrSettings.seatedHudAltMode = false;
				vrSettings.vrTeleportDownLimit = 4;
				vrSettings.vrTeleportUpLimit = 1;
				vrSettings.vrTeleportHorizLimit = 16;
				mc.vrPlayer.setFreeMove(true);
				Minecraft.getMinecraft().vrSettings.saveOptions();
				this.reinit = true;
			}

			else if (par1GuiButton instanceof GuiSmallButtonEx)
			{
				VRSettings.VrOptions num = VRSettings.VrOptions.getEnumOptions(par1GuiButton.id);
				this.guivrSettings.setOptionValue(((GuiSmallButtonEx)par1GuiButton).returnVrEnumOptions(), 1);
				par1GuiButton.displayString = this.guivrSettings.getKeyBinding(VRSettings.VrOptions.getEnumOptions(par1GuiButton.id));
                
				if(num == VRSettings.VrOptions.MOVE_MODE){
                	this.reinit = true;
                }     
				if(num == VRSettings.VrOptions.LIMIT_TELEPORT){
                	this.reinit = true;
                }  
			}
		}
	}

	@Override
	protected String[] getTooltipLines(String displayString, int buttonId)
	{
        VRSettings.VrOptions e = VRSettings.VrOptions.getEnumOptions(buttonId);
        if( e != null )
            switch(e)
            {
            case KEYHOLE:
                return new String[] {
                        "The number of degrees to the left and right of center",
                        "Where the view will begin to rotate."
                };
            case X_SENSITIVITY:
                return new String[] {
                        "Speed the view will rotate when pushed on the edge of the keyhole"
                };
            case Y_SENSITIVITY:
                return new String[] {
                        "Vertical speed of the crosshair related to the mouse"
                };
            case MOVE_MODE:
                return new String[] {
                        "Free Move with WASD or Teleport with W"
                };
            case LIMIT_TELEPORT:
                return new String[] {
                        "If enabled the arc teleporter will be have restrictions",
                        "in survival mode. It will not be able to jump up the side", 
                        "of blocks, it will consume food, and it will have an energy",
                        "bar that refills over time."
                } ;
            case SIMULATE_FALLING:
                return new String[] {
                        "If enabled the player will falls to the ground in TP mode",
                        "when standing above empty space. Also allows jumping"
                } ;
            case SEATED_HMD:
                return new String[] {
                        "The direction the forward (W) key will go. You can ",
                        "HMD view direction or crosshair pointing direction"
                } ;
            case SEATED_HUD_XHAIR:
                return new String[] {
                        "The direction the HUD will be placed.",
                        "HMD view direction or crosshair pointing direction"
                } ;
            case FOV_REDUCTION:
                return new String[] {
                        "Shrinks the field of view while moving. Can help with",
                        "motion sickness."
                } ;
            case WORLD_ROTATION_INCREMENT:
                return new String[] {
                        "How many degrees to rotate when",
                        "rotating the world."
                        
                };
            case TELEPORT_DOWN_LIMIT:
                return new String[] {
                        "Limit the number of blocks you can teleport below you"
                };
            case TELEPORT_UP_LIMIT:
                return new String[] {
                        "Limit the number of blocks you can teleport above you"
                };
            case TELEPORT_HORIZ_LIMIT:
                return new String[] {
                        "Limit the number of blocks you can teleport sideways you"
                };
            default:
                return null;
            }
        else{

        }
		return null;
    }

	@Override
	public boolean event(int id, VrOptions enumm) {
		return true;
	}

	@Override
	public boolean event(int id, String s) {
		return true;
	}
}
