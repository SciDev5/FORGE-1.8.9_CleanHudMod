# FORGE-1.8.9_CleanHudMod

A Minecraft Forge mod for 1.8.9 that allows users to display useful information on their screen.

Warning: This mod takes up a decent amount of screen space, if your screen is cluttered you may have to disable some features.

## How to use:

### Accessing the Config:

To get to the config, click on the mods / mod options button and scroll down until you see 
"SciDev's HUD Mod". Click on it and then press the button labeled config. Once you click
on that you will see the config menu.

### Settings:

__`chromaEnabled`__: True if you want rainbow text.

__`chromaSpeed`__: How fast chroma colors should change when chroma is enabled (higher = faster, it will go to a solid color around 1000)

__`showArmorItem`__: If you want armor information to be displayed on screen.

__`showDefaultItemOverlay`__: True if you want to see the default minecraft durability and stack text.

__`showHandItem`__: If you want information about the item in your hand displayed on screen.

__`showItemEnchants`__: If you want enchantment info displayed underneath items displayed on screen.

__`showItemInfo`__: True if you want to show custom durability and the item's name on screen.

__`showLoreNotEnchants`__: True if you want to replace the place where enchantments are displayed with lore text (not disabled by showItemEnchants).

__`showPotionEffects`__: True if you want to display potion effect info on your screen.

### Text Config:

Setting where the text is a format text follow the java String.format syntax for formatting text.
For example `abc %1$d abc` with argument 1 being `43` will result in the text `abc 43 abc`.
Replace "<decimal places>" with the number of decimal places you want.

__`cardinalEast`__: Text for denoting east. (ex. "E" or "east")

__`cardinalWest`__: Text for denoting west. (ex. "W" or "west")

__`cardinalSouth`__: Text for denoting south. (ex. "S" or "south")

__`cardinalNorth`__: Text for denoting north. (ex. "N" or "north")

__`coordPosX`__: Text for denoting positive X. (ex. "+X")

__`coordNegX`__: Text for denoting negative X. (ex. "-X")

__`coordPosZ`__: Text for denoting positive Z. (ex. "+Z")

__`coordNegZ`__: Text for denoting negative Z. (ex. "-Z")

__`debugFormat`__: Format for debug information, (chunk counter, entity counter and tile entity counter; default `C: %1$d, E: %2$d+%3$d`).

 - `%1$d` -> C counter; 
 
 - `%2$d` -> number of rendered entities (E counter); 

 - `%3$d` -> number of rendered tile entities; 

__`directionFormat`__: Format for direction information. (default `%1$s (%2$s; %3$.2f / %4$.2f)`)

 - `%1$s` -> coordinates direction (eg. "+x", "-z", etc.); 
 
 - `%2$s` -> cardinal direction (eg. "N", "east", etc.);

 - `%3$.<decimal places>f` -> pitch in degrees (vertical rotation);
 
 - `%4$.<decimal places>f` -> yaw in degrees (horizontal rotation);
 
__`framerateFormat`__: Format for the framerate. (defalt `%1$d fps`)

 - `%1$d` -> framerate in frames per second; 
 
__`positionFormat`__: Format for the player position.

 - `%1$.<decimal places>f`, `%2$.<decimal places>f`, and `%3$.<decimal places>f` -> coordinates with decimals; 
 
 - `%4$d`, `%5$d`, and `%6$d` -> whole number position; 
 
### Layout Editor:

To get to the layout editor, go to the config screen and press the `Edit Layout` button. 
If you are ingame, you will see your current equipment, if you are configuring from the main menu,
you will see random items.

In the bottom left corner of the screen you should see a large group of buttons. The top left three buttons
select which group of visual element you are moving. The `+` and `-` buttons allow you to select which element
in the category you are editing. The x and y text are interpreted based on the Postype button. 

The postypes are as follows:

- `TL-(px,px)`: Positioned by top-left corner of screen, x and y are interpreted as pixel values.

- `TL-(px,ln)`: Positioned by top-left corner of screen, x is interpreted as pixels, y as number of lines.

- `TR-(px,px)`: Positioned by top-right corner of screen, x and y are interpreted as pixel values.

- `TR-(px,ln)`: Positioned by top-right corner of screen, x is interpreted as pixels, y as number of lines.

- `BL-(px,px)`: Positioned by bottom-left corner of screen, x and y are interpreted as pixel values.

- `BL-(px,ln)`: Positioned by bottom-left corner of screen, x is interpreted as pixels, y as number of lines.

- `BR-(px,px)`: Positioned by bottom-right corner of screen, x and y are interpreted as pixel values.

- `BR-(px,ln)`: Positioned by bottom-right corner of screen, x is interpreted as pixels, y as number of lines.

- `abs-(f,f)`: X and y go from 0 to 1. ((0,0) -> top-left; (1,1) -> bottom-right)


#### The `Edit Text Pos` Category

In this category, you get access to 3 extra inputs: `add`, `del`, and `text`.

`add` allows you to create another line of text to put on the screen.

`del` allows you to remove the current selected screen.

`text` allows you to set the text displayed on the selected line (format is as follows:
  `%1$s` -> position; `%2$s` -> direction; `%3$s` -> fps; `%4$s` -> debug)

#### The `Edit Item Pos` Category

There are no extra buttons. The 1st element is the hand item, the other elements are 
the armor slots (where 2 is the helmet, 3 is chestplate, etc.).

#### The `Edit Potion FX Pos` Category

Everything is moved at once, change the position around and everything stays together.
