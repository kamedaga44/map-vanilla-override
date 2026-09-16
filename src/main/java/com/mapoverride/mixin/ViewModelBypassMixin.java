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
            // 1. バニラ地図判定
            boolean isVanillaMap = stack.isOf(Items.FILLED_MAP) || stack.getItem() instanceof FilledMapItem;

            // 2. 名前・ID判定 ("map", "paper" 等を含むか)
            String displayName = stack.getName().getString().toLowerCase();
            String itemId = stack.getItem().toString().toLowerCase();
            
            boolean isMapLike = displayName.contains("map") || itemId.contains("map") || itemId.contains("paper");

            if (isVanillaMap || isMapLike) {
                // View Model Customizer の拡大・回転などの操作を完全にキャンセル
                ci.cancel();
            }
        }
    }
}
