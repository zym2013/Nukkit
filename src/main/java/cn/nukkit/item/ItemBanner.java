package cn.nukkit.item;

import cn.nukkit.block.Block;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.IntTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.BannerPattern;
import cn.nukkit.utils.DyeColor;

/**
 * Created by PetteriM1
 */
public class ItemBanner extends Item {

    public ItemBanner() {
        this(0);
    }

    public ItemBanner(Integer meta) {
        this(meta, 1);
    }

    public ItemBanner(Integer meta, int count) {
        super(BANNER, meta, count, "Banner");
        this.block = Block.get(Block.STANDING_BANNER);
        //this.correctNBT(); // this was added by KCodeYT in pr #1019 but it seems to be causing crashes when crafting banners
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }

    public int getBaseColor() {
        return this.getNamedTag().getInt("Base");
    }

    public void setBaseColor(DyeColor color) {
        CompoundTag tag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        tag.putInt("Base", color.getDyeData() & 0x0f);
        this.setDamage(color.getDyeData() & 0x0f);
        this.setNamedTag(tag);
    }

    public int getType() {
        return this.getNamedTag().getInt("Type");
    }

    public void setType(int type) {
        CompoundTag tag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        tag.putInt("Type", type);
        this.setNamedTag(tag);
    }

    public void addPattern(BannerPattern pattern) {
        CompoundTag tag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        ListTag<CompoundTag> patterns = tag.getList("Patterns", CompoundTag.class);
        patterns.add(new CompoundTag("").
                putInt("Color", pattern.getColor().getDyeData() & 0x0f).
                putString("Pattern", pattern.getType().getName()));
        tag.putList(patterns);
        this.setNamedTag(tag);
    }

    public BannerPattern getPattern(int index) {
        CompoundTag tag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        return BannerPattern.fromCompoundTag(tag.getList("Patterns").size() > index && index >= 0 ? tag.getList("Patterns", CompoundTag.class).get(index) : new CompoundTag());
    }

    public void removePattern(int index) {
        CompoundTag tag = this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag();
        ListTag<CompoundTag> patterns = tag.getList("Patterns", CompoundTag.class);
        if(patterns.size() > index && index >= 0) {
            patterns.remove(index);
        }
        this.setNamedTag(tag);
    }

    public int getPatternsSize() {
        return (this.hasCompoundTag() ? this.getNamedTag() : new CompoundTag()).getList("Patterns").size();
    }

    public void correctNBT() {
        CompoundTag tag = this.getNamedTag() != null ? this.getNamedTag() : new CompoundTag();
        if (!tag.contains("Base") || !(tag.get("Base") instanceof IntTag)) {
            tag.putInt("Base", this.meta);
        }

        this.setNamedTag(tag);
    }
}
