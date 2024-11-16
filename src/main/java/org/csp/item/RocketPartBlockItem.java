package org.csp.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentMapImpl;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.csp.component.RocketPart;
import org.csp.entity.RocketPartBlockEntity;
import org.csp.registry.ComponentRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RocketPartBlockItem extends BlockItem {
    public RocketPartBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        //updateBlockComponents(user.getStackInHand(hand));
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        RocketPart rocketPart = stack.get(ComponentRegistry.ROCKET_PART_COMPONENT_TYPE);

        if (rocketPart != null) {
            tooltip.add(Text.of("§7Mass: §2" + rocketPart.getMass() + "kg"));

//              if(rocketPart.getPayloadComponent() != null) {
//                  tooltip.add(Text.of("§7Max Payload: §2" + rocketPart.maxPayloadCapacity + "kg"));
//                  break;
            if (rocketPart.getFuelComponent() != null) {
                tooltip.add(Text.of("§7Burn Time: §2" + ((rocketPart.getFuelComponent().getCapactity() * rocketPart.getFuelComponent().getFillLevel()) * rocketPart.getFuelComponent().getBurnSpeed()) + "s"));
                tooltip.add(Text.of("§7Fuel Type: §2" + rocketPart.getFuelComponent().getFuelType().name()));
                if (rocketPart.getFuelComponent().getBurnPower() != 1f)
                    tooltip.add(Text.of("§7§o  Power Mod: §2x" + rocketPart.getFuelComponent().getBurnPower()));
                if (rocketPart.getFuelComponent().getBurnSpeed() != 1f)
                    tooltip.add(Text.of("§7§o  Speed Mod: §2x" + rocketPart.getFuelComponent().getBurnSpeed()));
            }
            if (rocketPart.getEngineComponent() != null) {
                tooltip.add(Text.of("§7Thrust: §2" + (rocketPart.getEngineComponent().getPower()) + "N"));
            }
        }
    }


    @Override
    protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (blockEntity instanceof RocketPartBlockEntity) {
            ((RocketPartBlockEntity) blockEntity).setRocketPart(stack.get(ComponentRegistry.ROCKET_PART_COMPONENT_TYPE).clone());
        }

        return super.postPlacement(pos, world, player, stack, state);
    }
}
