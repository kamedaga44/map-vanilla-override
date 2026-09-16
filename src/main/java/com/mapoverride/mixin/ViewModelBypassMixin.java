package com.mapoverride.mixin;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.FilledMapItem;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
    targets = {
        "com.viewmodel.mixin.HeldItemTransformMixin",
        "com.viewmodel.mixin.HeldItemModelTransformMixin"
    },
    remap = false
)
public class ViewModelBypassMixin {

    @Inject(method = "onApplyTransforms", at = @At("HEAD"), cancellable = true, require = 0)
    private static void bypassMapTransforms(ItemStack stack, Hand hand, MatrixStack matrices, CallbackInfo ci) {
        if (stack != null && !stack.isEmpty()) {
            // 1. Vanillaの地図判定
            boolean isVanillaMap = stack.isOf(Items.FILLED_MAP) || stack.getItem() instanceof FilledMapItem;
            
            // 2. アイテムID（例: "modid:magical_map"）や表示名（"Magical map"）の文字列判定
            String itemId = stack.getItem().toString().toLowerCase();
            String name = stack.getName().getString().toLowerCase();
            boolean isCustomMap = itemId.contains("map") || name.contains("map");

            if (isVanillaMap || isCustomMap) {
                ci.cancel();
            }
        }
    }
}
