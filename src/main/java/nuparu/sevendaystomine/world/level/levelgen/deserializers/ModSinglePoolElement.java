package nuparu.sevendaystomine.world.level.levelgen.deserializers;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import nuparu.sevendaystomine.world.level.levelgen.processor.BookshelfProcessor;

public class ModSinglePoolElement extends SinglePoolElement {
    public static final Codec<ModSinglePoolElement> CODEC = RecordCodecBuilder.create((p_210429_) -> p_210429_.group(templateCodec(), processorsCodec(), projectionCodec()).apply(p_210429_, ModSinglePoolElement::new));
    protected ModSinglePoolElement(Either<ResourceLocation, StructureTemplate> p_210415_, Holder<StructureProcessorList> p_210416_, StructureTemplatePool.Projection p_210417_) {
        super(p_210415_, p_210416_, p_210417_);
    }
    @Override
    protected StructurePlaceSettings getSettings(Rotation p_210421_, BoundingBox p_210422_, boolean p_210423_) {
        StructurePlaceSettings structureplacesettings = new StructurePlaceSettings();
        structureplacesettings.setBoundingBox(p_210422_);
        structureplacesettings.setRotation(p_210421_);
        structureplacesettings.setKnownShape(true);
        structureplacesettings.setIgnoreEntities(false);
        structureplacesettings.addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        structureplacesettings.setFinalizeEntities(true);
        if (!p_210423_) {
            structureplacesettings.addProcessor(JigsawReplacementProcessor.INSTANCE);
        }
        structureplacesettings.addProcessor(BookshelfProcessor.INSTANCE);

        this.processors.value().list().forEach(structureplacesettings::addProcessor);
        this.getProjection().getProcessors().forEach(structureplacesettings::addProcessor);
        return structureplacesettings;
    }
}
