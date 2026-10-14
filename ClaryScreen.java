package com.clary.visuals;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ClaryScreen extends Screen {
    private static final int PURPLE = 0xFFB65CFF;
    private static final Map<String, Boolean> OPTIONS = new LinkedHashMap<>();
    private static String selectedCategory = "Crystal";
    private static String selectedStyle = "Purple";
    private int panelX, panelY, panelW, panelH;

    static {
        OPTIONS.put("Hide crystal particles", false);
        OPTIONS.put("Reduce explosion flash", true);
        OPTIONS.put("Hide enchantment glint", false);
        OPTIONS.put("Short fire (visual)", true);
        OPTIONS.put("Hide totem overlay", false);
        OPTIONS.put("Custom sky", false);
    }

    public ClaryScreen() { super(Text.literal("Clary Visuals")); }

    @Override
    protected void init() {
        clearChildren();
        panelW = Math.min(570, width - 24);
        panelH = Math.min(370, height - 24);
        panelX = (width - panelW) / 2;
        panelY = (height - panelH) / 2;

        addDrawableChild(ButtonWidget.builder(Text.literal("✕"), b -> close())
                .dimensions(panelX + panelW - 34, panelY + 8, 26, 22).build());

        String[] categories = {"Crystal", "Obsidian", "Fire", "Effects", "Totem", "Anchor", "Launcher", "Sky", "Skins"};
        for (int i = 0; i < categories.length; i++) {
            final String category = categories[i];
            int col = i % 3, row = i / 3;
            addDrawableChild(ButtonWidget.builder(Text.literal(category), b -> {
                selectedCategory = category;
                clearAndInit();
            }).dimensions(panelX + 14 + col * 112, panelY + 52 + row * 31, 106, 25).build());
        }

        String[] styles = stylesFor(selectedCategory);
        int startX = panelX + 16;
        int startY = panelY + 166;
        for (int i = 0; i < styles.length; i++) {
            final String style = styles[i];
            int col = i % 4, row = i / 4;
            addDrawableChild(ButtonWidget.builder(Text.literal((style.equals(selectedStyle) ? "✓ " : "") + style), b -> {
                selectedStyle = style;
                clearAndInit();
            }).dimensions(startX + col * 132, startY + row * 34, 124, 28).build());
        }

        int optionY = panelY + panelH - 116;
        int idx = 0;
        for (String option : OPTIONS.keySet()) {
            if (idx >= 3) break;
            final String key = option;
            addDrawableChild(ButtonWidget.builder(Text.literal((OPTIONS.get(key) ? "[ON]  " : "[OFF] ") + key), b -> {
                OPTIONS.put(key, !OPTIONS.get(key));
                clearAndInit();
            }).dimensions(panelX + 16, optionY + idx * 30, panelW - 32, 25).build());
            idx++;
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), b -> close())
                .dimensions(panelX + panelW - 100, panelY + panelH - 32, 84, 22).build());
    }

    private static String[] stylesFor(String category) {
        return switch (category) {
            case "Crystal" -> new String[]{"Default", "Small", "Purple", "Transparent", "Clean", "Animated", "Minimal", "No Particles"};
            case "Obsidian" -> new String[]{"Default", "Purple", "Dark", "Cracked"};
            case "Fire" -> new String[]{"Default", "Short", "Purple", "Blue", "Green", "Transparent"};
            case "Effects" -> new String[]{"Default", "Reduced", "Minimal", "Off"};
            case "Totem" -> new String[]{"Default", "Purple", "Gold", "Minimal"};
            case "Anchor" -> new String[]{"Default", "Dark", "Purple", "Clean"};
            case "Launcher" -> new String[]{"Default", "Minimal", "Hidden"};
            case "Sky" -> new String[]{"Default", "Night", "Purple", "Galaxy"};
            default -> new String[]{"Clary", "Black", "Purple", "White"};
        };
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.fill(0, 0, width, height, 0x99060012);
        context.fill(panelX, panelY, panelX + panelW, panelY + panelH, 0xF20B0716);
        context.fill(panelX, panelY, panelX + panelW, panelY + 3, PURPLE);
        context.drawBorder(panelX, panelY, panelW, panelH, 0xFF7130B5);
        context.drawText(textRenderer, "CLARY VISUALS", panelX + 16, panelY + 15, 0xFFE5C5FF, true);
        context.drawText(textRenderer, "Minecraft 1.21.5  |  Fabric  |  PojavLauncher", panelX + 16, panelY + 34, 0xFFB7A1C9, false);
        context.drawText(textRenderer, "Category: " + selectedCategory, panelX + 16, panelY + 148, 0xFFD8B0FF, true);
        context.drawText(textRenderer, "Style: " + selectedStyle, panelX + 16, panelY + panelH - 130, 0xFFE5C5FF, true);
        context.drawText(textRenderer, "UI prototype: visual selections are placeholders until textures/render hooks are added.", panelX + 16, panelY + panelH - 12, 0xFFB7A1C9, false);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override public boolean shouldPause() { return false; }
}
