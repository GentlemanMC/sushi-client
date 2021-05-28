package net.toshimichi.sushi.gui.theme.simple.config;

import net.toshimichi.sushi.config.Configuration;
import net.toshimichi.sushi.config.FakeConfiguration;
import net.toshimichi.sushi.config.data.EspColor;
import net.toshimichi.sushi.config.data.IntRange;
import net.toshimichi.sushi.gui.Component;
import net.toshimichi.sushi.gui.ConfigComponent;
import net.toshimichi.sushi.gui.Insets;
import net.toshimichi.sushi.gui.base.BasePanelComponent;
import net.toshimichi.sushi.gui.layout.FlowDirection;
import net.toshimichi.sushi.gui.layout.FlowLayout;
import net.toshimichi.sushi.gui.theme.ThemeConstants;
import net.toshimichi.sushi.utils.GuiUtils;

import java.awt.Color;

public class SimpleEspComponent extends BasePanelComponent<Component> implements ConfigComponent<EspColor> {

    private final ThemeConstants constants;
    private final Configuration<EspColor> configuration;
    private final Configuration<Color> color;
    private final Configuration<Boolean> rainbow;
    private final Configuration<Boolean> alphaEnabled;
    private final Configuration<IntRange> alpha;
    private final SimpleColorComponent colorComponent;
    private final SimpleBooleanComponent rainbowComponent;
    private final SimpleBooleanComponent alphaEnabledComponent;
    private final SimpleIntRangeComponent alphaComponent;

    public SimpleEspComponent(ThemeConstants constants, Configuration<EspColor> c) {
        this.constants = constants;
        this.configuration = c;
        EspColor espColor = c.getValue();
        this.color = new FakeConfiguration<>("color", c.getName() + " Color", null, Color.class, espColor.getColor());
        this.rainbow = new FakeConfiguration<>("rainbow", "Rainbow", null, Boolean.class, espColor.isRainbow());
        this.alphaEnabled = new FakeConfiguration<>("alpha_enabled", "Alpha Enabled", null, Boolean.class, espColor.isAlphaEnabled());
        this.alpha = new FakeConfiguration<>("alpha", "Alpha", null, IntRange.class, new IntRange(espColor.getColor().getAlpha(), 255, 0, 1));

        this.colorComponent = new SimpleColorComponent(constants, color);
        this.rainbowComponent = new SimpleBooleanComponent(constants, rainbow);
        this.alphaEnabledComponent = new SimpleBooleanComponent(constants, alphaEnabled);
        this.alphaComponent = new SimpleIntRangeComponent(constants, alpha);

        c.addHandler(esp -> {
            if (esp.getColor().getRGB() != color.getValue().getRGB()) color.setValue(esp.getColor());
            if (esp.isRainbow() != rainbow.getValue()) rainbow.setValue(esp.isRainbow());
            if (esp.isAlphaEnabled() != alphaEnabled.getValue()) alphaEnabled.setValue(esp.isAlphaEnabled());
            if (esp.getColor().getAlpha() != color.getValue().getAlpha())
                alpha.setValue(alpha.getValue().setCurrent(esp.getColor().getAlpha()));
        });
        color.addHandler(it -> c.setValue(c.getValue().setColor(it)));
        rainbow.addHandler(it -> c.setValue(c.getValue().setRainbow(it)));
        alphaEnabled.addHandler(it -> c.setValue(c.getValue().setAlphaEnabled(it)));
        alpha.addHandler(it -> c.setValue(c.getValue().setAlpha(it.getCurrent())));

        setLayout(new FlowLayout(this, FlowDirection.DOWN));

        double marginLeft = colorComponent.getMarginLeft();
        double marginRight = colorComponent.getMarginRight();
        colorComponent.setMargin(new Insets(0, 0, 0, 0));
        rainbowComponent.setMargin(new Insets(0, marginLeft, 0, marginRight));
        alphaEnabledComponent.setMargin(new Insets(0, marginLeft, 0, marginRight));
        alphaComponent.setMargin(new Insets(0, marginLeft, 2, marginRight));
        add(colorComponent);
        add(rainbowComponent);
        add(alphaEnabledComponent);
        add(alphaComponent);
    }

    @Override
    public void onRender() {
        GuiUtils.drawRect(getWindowX(), getWindowY(), getWidth(), getHeight(), constants.outlineColor.getValue());
        super.onRender();
    }

    @Override
    public void onRelocate() {
        if (!alphaEnabled.getValue()) remove(alphaComponent);
        else if (alphaEnabled.getValue() && !contains(alphaComponent)) add(alphaComponent);
        super.onRelocate();
    }

    @Override
    public Configuration<EspColor> getValue() {
        return configuration;
    }
}
