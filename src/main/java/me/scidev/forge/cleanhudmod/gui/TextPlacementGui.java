package me.scidev.forge.cleanhudmod.gui;

import java.io.IOException;
import java.util.Arrays;

import me.scidev.forge.cleanhudmod.HudMod;
import me.scidev.forge.cleanhudmod.util.ConfigUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class TextPlacementGui extends GuiScreen {

	private GuiButton closeButton;
	
	private GuiButton selectedIndexIncButton;
	private GuiButton selectedIndexDecButton;
	private GuiLabel selectedIndexLabel;
	private GuiButton selectedIndexAddButton;
	private GuiButton selectedIndexDelButton;

	private GuiButton selectLinesButton;
	private GuiButton selectSlotsButton;
	private GuiButton selectPotionEffectsButton;

	private GuiButton positionTypeSelect;

	private GuiLabel titleLabel;
	private GuiLabel xLabel;
	private GuiLabel yLabel;
	private GuiLabel contentLabel;

	private GuiTextField lineTextField;
	
	private GuiTextField positionXField;
	private GuiTextField positionYField;

	private int selectedIndex = 0;
	private int positionType = 0;
	private static final String[] positionTypeNames = new String[] {"TL-(px,px)","TL-(px,ln)","TR-(px,px)","TR-(px,ln)","BL-(px,px)","BL-(px,ln)","BR-(px,px)","BR-(px,ln)","abs-(f,f)"};
	private EditTarget currentEditTarget = EditTarget.LINES;
	private enum EditTarget {LINES,SLOTS,POTIONEFFECTS};
	
	private ScreenOverlayGui overlayGUI;
	
	public final ModConfigGui parent;
	
	public TextPlacementGui(ModConfigGui parent) {
		this.parent = parent;
	}
	
	private int posX;
	private int posY;
	private int margin = 10;
	
	@Override
	public void initGui() {
		super.initGui();
		
		posX = 50;
		posY = height-100;

		closeButton = new GuiButton(0, posX+490, posY-15, 40, 20, "Done"); buttonList.add(closeButton);
		
		selectedIndexIncButton = new GuiButton(0, posX, posY+20, 20, 20, "+"); buttonList.add(selectedIndexIncButton);
		selectedIndexDecButton = new GuiButton(0, posX+20, posY+20, 20, 20, "-"); buttonList.add(selectedIndexDecButton);
		selectedIndexAddButton = new GuiButton(0, posX+60, posY+20, 40, 20, "new"); buttonList.add(selectedIndexAddButton);
		selectedIndexDelButton = new GuiButton(0, posX+100, posY+20, 40, 20, "del"); buttonList.add(selectedIndexDelButton);

		positionTypeSelect = new GuiButton(0, posX+270, posY+20, 120, 20, "[postype]"); buttonList.add(positionTypeSelect);

		selectLinesButton = new GuiButton(0, posX, posY, 120, 20, "Edit Text Pos"); buttonList.add(selectLinesButton);
		selectSlotsButton = new GuiButton(0, posX+120, posY, 120, 20, "Edit Items Pos"); buttonList.add(selectSlotsButton);
		selectPotionEffectsButton = new GuiButton(0, posX+240, posY, 120, 20, "Edit Potion FX Pos"); buttonList.add(selectPotionEffectsButton);

		selectedIndexLabel = new GuiLabel(fontRendererObj,0,posX+40,posY+20,20,20,0xFFFFFFFF); labelList.add(selectedIndexLabel);
		titleLabel = new GuiLabel(fontRendererObj,0,posX,posY-20,100,20,0xFFFFFFFF); labelList.add(titleLabel);

		xLabel = new GuiLabel(fontRendererObj,0,posX+140,posY+20,20,20,0xFFFFFFFF); labelList.add(xLabel);
		yLabel = new GuiLabel(fontRendererObj,0,posX+200,posY+20,20,20,0xFFFFFFFF); labelList.add(yLabel);
		contentLabel = new GuiLabel(fontRendererObj,0,posX+390,posY+20,40,20,0xFFFFFFFF); labelList.add(contentLabel);
		positionXField = new GuiTextField(0, fontRendererObj, posX+160, posY+21, 38, 18);
		positionYField = new GuiTextField(0, fontRendererObj, posX+220, posY+21, 38, 18);
		lineTextField = new GuiTextField(0, fontRendererObj, posX+430, posY+21, 98, 18);
		

		selectedIndexLabel.func_175202_a(Integer.toString(selectedIndex+1)); selectedIndexLabel.setCentered();
		titleLabel.func_175202_a("Position Text:");
		
		xLabel.func_175202_a("X:"); xLabel.setCentered();
		yLabel.func_175202_a("Y:"); yLabel.setCentered();
		contentLabel.func_175202_a("text:"); contentLabel.setCentered();
		
		overlayGUI = new ScreenOverlayGui();
		
		loadConfig();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button == selectedIndexAddButton) {
			if (currentEditTarget != EditTarget.LINES) return;
			double[] xs = ConfigUtil.linesX.getDoubleList();
			double[] ys = ConfigUtil.linesY.getDoubleList();
			int[] positionTypes = ConfigUtil.linePosTypes.getIntList();
			String[] contents = ConfigUtil.lineContent.getStringList();

			int len = positionTypes.length;
			double[] newXs = Arrays.copyOf(xs,len+1); newXs[len] = 0;
			double[] newYs = Arrays.copyOf(ys,len+1); newYs[len] = 0;
			int[] newPositionTypes = Arrays.copyOf(positionTypes,len+1); newPositionTypes[len] = 0;
			String[] newContents = Arrays.copyOf(contents,len+1); newContents[len] = "TEXT";
			
			ConfigUtil.linesX.set(newXs);
			ConfigUtil.linesY.set(newYs);
			ConfigUtil.linePosTypes.set(newPositionTypes);
			ConfigUtil.lineContent.set(newContents);
			
			selectedIndex = len;
			loadConfig();
		} else if (button == selectedIndexDelButton) {
			if (currentEditTarget != EditTarget.LINES) return;
			
			double[] xs = ConfigUtil.linesX.getDoubleList();
			double[] ys = ConfigUtil.linesY.getDoubleList();
			int[] positionTypes = ConfigUtil.linePosTypes.getIntList();
			String[] contents = ConfigUtil.lineContent.getStringList();
			
			int len = positionTypes.length;
			if (len == 1) return;
			double[] newXs = new double[len-1];
			double[] newYs = new double[len-1];
			int[] newPositionTypes = new int[len-1];
			String[] newContents = new String[len-1];
			int j = 0;
			for (int i = 0; i < len; i ++) {
				if (i == selectedIndex) continue;
				newXs[j] = xs[i];
				newYs[j] = ys[i];
				newContents[j] = contents[i];
				newPositionTypes[j] = positionTypes[i];
				j++;
			}
			
			ConfigUtil.linesX.set(newXs);
			ConfigUtil.linesY.set(newYs);
			ConfigUtil.linePosTypes.set(newPositionTypes);
			ConfigUtil.lineContent.set(newContents);
			
			if (selectedIndex > 0) selectedIndex --;
			loadConfig();
		} else if (button == selectedIndexIncButton) {
			int len = -1;
			switch (currentEditTarget) {
			case LINES: len = ConfigUtil.linePosTypes.getIntList().length; break;
			case SLOTS: len = 5; break;
			case POTIONEFFECTS: len = 1; break;
			}
			if (selectedIndex + 1 < len) {
				updateConfig();
				selectedIndex++;
				loadConfig();
				
			}
		} else if (button == selectedIndexDecButton) {
			if (selectedIndex > 0) {
				updateConfig();
				selectedIndex--;
				loadConfig();
			}
		} else if (button == selectLinesButton) {
			updateConfig();
			currentEditTarget = EditTarget.LINES;
			selectedIndex = 0;
			loadConfig();
		} else if (button == selectSlotsButton) {
			updateConfig();
			currentEditTarget = EditTarget.SLOTS;
			selectedIndex = 0;
			loadConfig();
		} else if (button == selectPotionEffectsButton) {
			updateConfig();
			currentEditTarget = EditTarget.POTIONEFFECTS;
			selectedIndex = 0;
			loadConfig();
		} else if (button == positionTypeSelect) {
			positionType++;
			positionType %= 9;
			updateConfig();
		} else if (button == closeButton) {
			close();
		} else 
			super.actionPerformed(button);
	}
	
	private void loadConfig() {
		double x = -1;
		double y = -1;
		
		switch(currentEditTarget) {
		case LINES:
			x = ConfigUtil.linesX.getDoubleList()[selectedIndex];
			y = ConfigUtil.linesY.getDoubleList()[selectedIndex];
			positionType = ConfigUtil.linePosTypes.getIntList()[selectedIndex];
			lineTextField.setText(ConfigUtil.lineContent.getStringList()[selectedIndex]);
			break;
		case SLOTS:
			x = ConfigUtil.slotsX.getDoubleList()[selectedIndex];
			y = ConfigUtil.slotsY.getDoubleList()[selectedIndex];
			positionType = ConfigUtil.slotPosTypes.getIntList()[selectedIndex];
			break;
		case POTIONEFFECTS:
			x = ConfigUtil.potionEffectsX.getDouble();
			y = ConfigUtil.potionEffectsY.getDouble();
			positionType = ConfigUtil.potionEffectsPosType.getInt();
			break;
		}

		if (positionType == 8) {
			positionXField.setText(Double.toString(x));
			positionYField.setText(Double.toString(y));
		} else {
			positionXField.setText(Integer.toString((int)x));
			positionYField.setText(Integer.toString((int)y));
		}
	}

	private void updateConfig() {
		double x;
		double y;
		
		boolean positionUpdate = true;
		
		try {
			x = Double.parseDouble(positionXField.getText());
			y = Double.parseDouble(positionYField.getText());
		} catch (NumberFormatException e) {
			x = -1; y = -1;
		}
		
		if (x < 0 || y < 0) {
			positionUpdate = false;
			HudMod.LOGGER.warn("(at TextPlacementGui.updateConfig) Position text for element was invalid, leaving unchanged.");
		}
		
		double[] xs; double[] ys; int[] positionTypes;
		
		switch(currentEditTarget) {
		case LINES:
			xs = ConfigUtil.linesX.getDoubleList();
			ys = ConfigUtil.linesY.getDoubleList();
			positionTypes = ConfigUtil.linePosTypes.getIntList();
			String[] contents = ConfigUtil.lineContent.getStringList();
			if (!positionUpdate) {
				if (positionType == 8) {
					positionXField.setText(Double.toString(xs[selectedIndex]));
					positionYField.setText(Double.toString(ys[selectedIndex]));
				} else {
					positionXField.setText(Integer.toString((int)xs[selectedIndex]));
					positionYField.setText(Integer.toString((int)ys[selectedIndex]));
				}
			}
			xs[selectedIndex] = x;
			ys[selectedIndex] = y;
			positionTypes[selectedIndex] = positionType;
			contents[selectedIndex] = lineTextField.getText();
			if (positionUpdate) {
				ConfigUtil.linesX.set(xs);
				ConfigUtil.linesY.set(ys);
			}
			ConfigUtil.linePosTypes.set(positionTypes);
			ConfigUtil.lineContent.set(contents);
			break;
		case SLOTS:
			xs = ConfigUtil.slotsX.getDoubleList();
			ys = ConfigUtil.slotsY.getDoubleList();
			positionTypes = ConfigUtil.slotPosTypes.getIntList();
			if (!positionUpdate) {
				if (positionType == 8) {
					positionXField.setText(Double.toString(xs[selectedIndex]));
					positionYField.setText(Double.toString(ys[selectedIndex]));
				} else {
					positionXField.setText(Integer.toString((int)xs[selectedIndex]));
					positionYField.setText(Integer.toString((int)ys[selectedIndex]));
				}
			}
			xs[selectedIndex] = x;
			ys[selectedIndex] = y;
			positionTypes[selectedIndex] = positionType;
			if (positionUpdate) {
				ConfigUtil.slotsX.set(xs);
				ConfigUtil.slotsY.set(ys);
			}
			ConfigUtil.slotPosTypes.set(positionTypes);
			break;
		case POTIONEFFECTS:
			if (positionUpdate) {
				ConfigUtil.potionEffectsX.set(x);
				ConfigUtil.potionEffectsY.set(y);
			} else {
				if (positionType == 8) {
					positionXField.setText(Double.toString(ConfigUtil.potionEffectsX.getDouble()));
					positionYField.setText(Double.toString(ConfigUtil.potionEffectsY.getDouble()));
				} else {
					positionXField.setText(Integer.toString((int)ConfigUtil.potionEffectsX.getDouble()));
					positionYField.setText(Integer.toString((int)ConfigUtil.potionEffectsY.getDouble()));
				}
			}
			ConfigUtil.potionEffectsPosType.set(positionType);
			break;
		}
	}

	@Override
	public void updateScreen() {
		selectLinesButton.enabled = currentEditTarget != EditTarget.LINES;
		selectSlotsButton.enabled = currentEditTarget != EditTarget.SLOTS;
		selectPotionEffectsButton.enabled = currentEditTarget != EditTarget.POTIONEFFECTS;

		labelList.remove(selectedIndexLabel);
		selectedIndexLabel = new GuiLabel(fontRendererObj,0,selectedIndexLabel.field_146162_g,selectedIndexLabel.field_146174_h,20,20,0xFFFFFFFF); labelList.add(selectedIndexLabel);
		selectedIndexLabel.func_175202_a(currentEditTarget == EditTarget.POTIONEFFECTS?"all":Integer.toString(selectedIndex+1)); selectedIndexLabel.setCentered();

		selectedIndexIncButton.enabled = currentEditTarget != EditTarget.POTIONEFFECTS;
		selectedIndexDecButton.enabled = currentEditTarget != EditTarget.POTIONEFFECTS;
		selectedIndexAddButton.enabled = currentEditTarget == EditTarget.LINES;
		selectedIndexDelButton.enabled = currentEditTarget == EditTarget.LINES;

		lineTextField.setVisible(currentEditTarget == EditTarget.LINES);
		contentLabel.visible = currentEditTarget == EditTarget.LINES;
		
		switch (currentEditTarget) {
		case LINES: 
			if (selectedIndex >= ConfigUtil.linePosTypes.getIntList().length-1)
				selectedIndexIncButton.enabled = false;
			break;
		case SLOTS: 
			if (selectedIndex >= 4)
				selectedIndexIncButton.enabled = false;
			break;
		default: break;
		}
		if (selectedIndex <= 0) 
			selectedIndexDecButton.enabled = false;
		
		positionTypeSelect.displayString = "PosType: "+positionTypeNames[positionType];
		
		super.updateScreen();
	}
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawBackground(0);
		overlayGUI.draw();
		drawRect(posX-margin, posY-15-margin, posX+530+margin, posY+40+margin, 0x88000000);
		lineTextField.drawTextBox();
		positionXField.drawTextBox();
		positionYField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void onResize(Minecraft mcIn, int w, int h) {
		labelList.clear();
		super.onResize(mcIn, w, h);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		updateWasFocused();
		lineTextField.mouseClicked(mouseX, mouseY, mouseButton);
		positionXField.mouseClicked(mouseX, mouseY, mouseButton);
		positionYField.mouseClicked(mouseX, mouseY, mouseButton);
		updateNowFocused();
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		updateWasFocused();
		if (keyCode == 28) {
			lineTextField.setFocused(false);
			positionXField.setFocused(false);
			positionYField.setFocused(false);
		}
		updateNowFocused();
		lineTextField.textboxKeyTyped(typedChar, keyCode);
		positionXField.textboxKeyTyped(typedChar, keyCode);
		positionYField.textboxKeyTyped(typedChar, keyCode);
		
		// Close to parent on press escape
		if (keyCode == 1) 
			close();
	}

	private boolean lineTextFieldWasFocused = false;
	private boolean positionXFieldWasFocused = false;
	private boolean positionYFieldWasFocused = false;
	private String fieldLastValue = null;
	private void updateWasFocused() {
		lineTextFieldWasFocused = lineTextField.isFocused();
		positionXFieldWasFocused = positionXField.isFocused();
		positionYFieldWasFocused = positionYField.isFocused();
	}
	private void updateNowFocused() {
		boolean lineTextFieldChanged = lineTextFieldWasFocused != lineTextField.isFocused();
		boolean positionXFieldChanged = positionXFieldWasFocused != positionXField.isFocused();
		boolean positionYFieldChanged = positionYFieldWasFocused != positionYField.isFocused();
		if (lineTextFieldChanged || positionXFieldChanged || positionYFieldChanged) {
			if (lineTextFieldChanged && lineTextFieldWasFocused) {
				if (lineTextField.getText().trim().isEmpty())
					lineTextField.setText(fieldLastValue);
			} else if (positionXFieldChanged && positionXFieldWasFocused) {
				try {
					if (positionType == 8) Double.parseDouble(positionXField.getText());
					else Integer.parseInt(positionXField.getText());
				} catch (NumberFormatException e) {
					positionXField.setText(fieldLastValue);
				}
			} else if (positionYFieldChanged && positionYFieldWasFocused) {
				try {
					if (positionType == 8) Double.parseDouble(positionYField.getText());
					else Integer.parseInt(positionYField.getText());
				} catch (NumberFormatException e) {
					positionYField.setText(fieldLastValue);
				}
			} else if (lineTextFieldChanged && !lineTextFieldWasFocused) {
				fieldLastValue = lineTextField.getText();
			} else if (positionXFieldChanged && !positionXFieldWasFocused) {
				fieldLastValue = positionXField.getText();
			} else if (positionYFieldChanged && !positionYFieldWasFocused) {
				fieldLastValue = positionYField.getText();
			}
			updateConfig();
		}
	}
	
	@Override
	public void onGuiClosed() {
		updateConfig();
		ConfigUtil.config.save();
	}
	
	private void close() {
		mc.displayGuiScreen(new ModConfigGui(parent.parentScreen));
		if (mc.currentScreen == null)
			mc.setIngameFocus();
	}
}
