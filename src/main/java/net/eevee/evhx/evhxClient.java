package net.eevee.evhx;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.glfw.GLFW;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

@Environment(EnvType.CLIENT)
public class evhxClient implements ClientModInitializer {
    private boolean flyhackEnabled = false;
    private int bypasscounter = 0;
    private static final KeyBinding FLY_KEY = new KeyBinding("Enable Fly",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_BRACKET, "Eevee HX");
    private static final KeyBinding FLY_KEY_D = new KeyBinding("Disable Fly",
            InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_BRACKET, "Eevee HX");

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(FLY_KEY);
        KeyBindingHelper.registerKeyBinding(FLY_KEY_D);
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;

            if (FLY_KEY.isPressed()) {
                flyhackEnabled = true;
            }
            if (FLY_KEY_D.isPressed()) {
                flyhackEnabled = false;
            }

            if (player != null && flyhackEnabled) {
                player.getAbilities().flying = true;

                //bypass server floating check
                if (bypasscounter == 40) {
                    bypasscounter = 0;
                    double x = client.player.getX();
                    double y = client.player.getY();
                    double z = client.player.getZ();
                    player.setVelocity(player.getVelocity().x, -0.1, player.getVelocity().z);
                    y -= 0.1;
                    client.player.setPosition(x, y, z);

                } else {
                    bypasscounter += 1;
                }
            } else if (player != null) {
                player.getAbilities().flying = false;
            }
        });
    }
}
