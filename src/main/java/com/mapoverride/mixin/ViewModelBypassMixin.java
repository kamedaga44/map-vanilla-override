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
            // 1. バニラ通常の地図判定
            boolean isVanillaMap = stack.isOf(Items.FILLED_MAP) || stack.getItem() instanceof FilledMapItem;

            // 2. アイテムの表示名判定（「Magical Map」や名前に「Map」が入っているもの）
            String displayName = stack.getName().getString().toLowerCase();
            boolean isMapName = displayName.contains("map");

            // 3. アイテムID文字列判定
            String itemId = stack.getItem().toString().toLowerCase();
            boolean isMapId = itemId.contains("map");

            if (isVanillaMap || isMapName || isMapId) {
                ci.cancel();
            }
        }
    }
}
